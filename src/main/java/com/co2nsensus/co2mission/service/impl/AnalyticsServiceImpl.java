package com.co2nsensus.co2mission.service.impl;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.security.GeneralSecurityException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javax.annotation.PostConstruct;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;

import com.co2nsensus.co2mission.exception.AffiliateErrorCodes;
import com.co2nsensus.co2mission.exception.AnalyticsException;
import com.co2nsensus.co2mission.model.dto.MonthlyChannelData;
import com.co2nsensus.co2mission.model.entity.Affiliate;
import com.co2nsensus.co2mission.model.enums.AnalyticsBreakdownType;
import com.co2nsensus.co2mission.model.enums.AnalyticsDateInterval;
import com.co2nsensus.co2mission.model.request.AnalyticsRequest;
import com.co2nsensus.co2mission.model.response.analytics.AnalyticsModel;
import com.co2nsensus.co2mission.service.AffiliateService;
import com.co2nsensus.co2mission.service.AnalyticsService;
import com.co2nsensus.co2mission.service.RedisService;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.analyticsreporting.v4.AnalyticsReporting;
import com.google.api.services.analyticsreporting.v4.AnalyticsReportingScopes;
import com.google.api.services.analyticsreporting.v4.model.ColumnHeader;
import com.google.api.services.analyticsreporting.v4.model.DateRange;
import com.google.api.services.analyticsreporting.v4.model.DateRangeValues;
import com.google.api.services.analyticsreporting.v4.model.Dimension;
import com.google.api.services.analyticsreporting.v4.model.DimensionFilter;
import com.google.api.services.analyticsreporting.v4.model.DimensionFilterClause;
import com.google.api.services.analyticsreporting.v4.model.GetReportsRequest;
import com.google.api.services.analyticsreporting.v4.model.GetReportsResponse;
import com.google.api.services.analyticsreporting.v4.model.Metric;
import com.google.api.services.analyticsreporting.v4.model.MetricHeaderEntry;
import com.google.api.services.analyticsreporting.v4.model.Report;
import com.google.api.services.analyticsreporting.v4.model.ReportRequest;
import com.google.api.services.analyticsreporting.v4.model.ReportRow;
import com.google.auth.http.HttpCredentialsAdapter;
import com.google.auth.oauth2.GoogleCredentials;

@Service
public class AnalyticsServiceImpl implements AnalyticsService {

	private static final String CLIENT_SECRET_JSON_RESOURCE = "co2mission-a8f8d7b995dd.json";
	private static final String VIEW_ID = "192925153";
	private static final String APPLICATION_NAME = "Hello Analytics Reporting";
	private static final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();
	private AnalyticsReporting analyticsReporting;
	private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("YYYY-MM-dd");
	private static final String CHANNEL_DIMENSION_NAME = "ga:dimension3";
	private static final String SOURCE_DIMENSION_NAME = "ga:source";
	private static final String MONTH_DIMENSION_NAME = "ga:month";
	private static final String YEAR_DIMENSION_NAME = "ga:year";
	private static final String CONVERSION_METRICS_NAME = "ga:goal8Completions";
	private static final String CONVERSION_METRICS_ALIAS = "conversion-count";

	private final RedisService redisService;
	private final AffiliateService affiliateService;

	public AnalyticsServiceImpl(RedisService redisService, AffiliateService affiliateService) {
		this.redisService = redisService;
		this.affiliateService = affiliateService;
	}

	@PostConstruct
	public void initialize() throws GeneralSecurityException, IOException {
		analyticsReporting = initializeAnalyticsReporting();

	}

	/**
	 * Initializes an authorized Analytics Reporting service object.
	 *
	 * @return The analytics reporting service object.
	 * @throws IOException
	 * @throws GeneralSecurityException
	 */
	private AnalyticsReporting initializeAnalyticsReporting() throws GeneralSecurityException, IOException {
		GoogleCredentials googleCredentials = GoogleCredentials
				.fromStream(
						AnalyticsServiceImpl.class.getClassLoader().getResourceAsStream(CLIENT_SECRET_JSON_RESOURCE))
				.createScoped(AnalyticsReportingScopes.all());
		NetHttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();
		HttpRequestInitializer requestInitializer = new HttpCredentialsAdapter(googleCredentials);
		return new AnalyticsReporting.Builder(httpTransport, JSON_FACTORY, requestInitializer)
				.setApplicationName(APPLICATION_NAME).build();
	}

	@Override
	public List<AnalyticsModel> getAnalytics(AnalyticsRequest request) {
		GetReportsResponse gaResponse;
		String channelId = affiliateService.getAffiliateEntityById(request.getAffiliateId()).getChannelId();
		try {
			gaResponse = getReport(request, channelId);
			List<AnalyticsModel> analyticsModels = convertToAnalyticsResponse(gaResponse, request, channelId);
			return calculateAnalyticsEarnings(analyticsModels, request, request.getAffiliateId());
		} catch (IOException e) {
			throw new AnalyticsException(AffiliateErrorCodes.ANALYTICS_EXCEPTION.getCode(),
					AffiliateErrorCodes.ANALYTICS_EXCEPTION.getMessage(), e);
		}
	}

	private List<AnalyticsModel> calculateAnalyticsEarnings(List<AnalyticsModel> analyticsModelList,
			AnalyticsRequest analyticsRequest, String affiliateId) {
		Pair<LocalDate, LocalDate> dateRangePair = getDateRangeLocalDates(analyticsRequest);
		LocalDate startDate = dateRangePair.getFirst();
		LocalDate endDate = dateRangePair.getSecond();
		Map<String, List<MonthlyChannelData>> channelDataYearMap = getChannelData(affiliateId, startDate, endDate);
		List<AnalyticsModel> consolidatedList = new ArrayList<>();
		for (AnalyticsModel analyticsModel : analyticsModelList) {
			AnalyticsModel model = getModel(consolidatedList, analyticsModel.getSourceName(),
					analyticsModel.getBreakdownValue());
			model.setAddToCartCount(model.getAddToCartCount() + analyticsModel.getAddToCartCount());
			model.setBreakdownValue(analyticsModel.getBreakdownValue());
			model.setChannelId(analyticsModel.getChannelId());
			model.setClickCount(model.getClickCount() + analyticsModel.getClickCount());
			model.setRevenue(model.getRevenue().add(analyticsModel.getRevenue()));
			model.setSourceName(analyticsModel.getSourceName());
			model.setPurchaseCount(model.getPurchaseCount() + analyticsModel.getPurchaseCount());
			Optional<MonthlyChannelData> channelDataOptional = channelDataYearMap
					.get(String.valueOf(analyticsModel.getYear())).stream()
					.filter(c -> c.getMonthIndex() == analyticsModel.getMonth()).findFirst();
			if (channelDataOptional.isPresent())
				model.setConversion(
						model.getRevenue().multiply(channelDataOptional.get().getAffiliateStatus().getComissionRate()));
			consolidatedList.add(model);
		}
		for (AnalyticsModel analytics : consolidatedList) {
			analytics.setConversion(analytics.getConversion().setScale(2, RoundingMode.DOWN));
			analytics.setRevenue(analytics.getRevenue().setScale(2, RoundingMode.DOWN));
		}
		return consolidatedList;
	}

	private Map<String, List<MonthlyChannelData>> getChannelData(String affiliateId, LocalDate startDate,
			LocalDate endDate) {
		Map<String, List<MonthlyChannelData>> yearChannelDataMap = new HashMap<>();

		for (LocalDate date = startDate; date.equals(endDate) || date.isBefore(endDate); date = date.plusMonths(1)) {
			String year = String.valueOf(date.getYear());
			MonthlyChannelData channelData = redisService.getMonthlySales(affiliateId, String.valueOf(date.getYear()),
					date.getMonthValue());
			if (channelData != null) {
				if (yearChannelDataMap.containsKey(year)) {
					yearChannelDataMap.get(year).add(channelData);
				} else {
					List<MonthlyChannelData> channelDataList = new ArrayList<>();
					channelDataList.add(channelData);
					yearChannelDataMap.put(year, channelDataList);
				}
			}

		}
		return yearChannelDataMap;
	}

	private Pair<LocalDate, LocalDate> getDateRangeLocalDates(AnalyticsRequest request) {
		LocalDateTime now = LocalDateTime.now();
		LocalDate startDate = null;
		LocalDate endDate = null;
		if (request.getInterval() == AnalyticsDateInterval.CUSTOM) {
			startDate = LocalDate.parse(request.getCustomIntervalFrom(), DATE_FORMAT);
			endDate = LocalDate.parse(request.getCustomIntervalTo(), DATE_FORMAT);
		} else if (request.getInterval() == AnalyticsDateInterval.YESTERDAY) {
			startDate = now.minusDays(1).toLocalDate();
			endDate = now.minusDays(1).toLocalDate();
		} else if (request.getInterval() == AnalyticsDateInterval.TODAY) {
			startDate = now.toLocalDate();
			endDate = now.toLocalDate();
		} else if (request.getInterval() == AnalyticsDateInterval.LAST_WEEK) {
			LocalDateTime weekStart = now.minusDays(7 + now.getDayOfWeek().getValue() - 1);
			LocalDateTime weekEnd = now.minusDays(now.getDayOfWeek().getValue());
			startDate = weekStart.toLocalDate();
			endDate = weekEnd.toLocalDate();
		} else if (request.getInterval() == AnalyticsDateInterval.LAST_MONTH) {
			LocalDateTime previousMonth = now.minusMonths(1);
			LocalDateTime monthStart = previousMonth.withDayOfMonth(1);
			LocalDateTime monthEnd = previousMonth.withDayOfMonth(previousMonth.getMonth().maxLength());
			startDate = monthStart.toLocalDate();
			endDate = monthEnd.toLocalDate();
		} else if (request.getInterval() == AnalyticsDateInterval.LAST_7_DAYS) {
			startDate = now.minusDays(8).toLocalDate();
			endDate = now.minusDays(1).toLocalDate();
		} else if (request.getInterval() == AnalyticsDateInterval.LAST_30_DAYS) {
			startDate = now.minusDays(31).toLocalDate();
			endDate = now.minusDays(1).toLocalDate();
		}
		return Pair.of(startDate, endDate);
	}

	private DateRange getDateRange(AnalyticsRequest request) {
		DateRange dateRange = new DateRange();
		Pair<LocalDate, LocalDate> localDatePair = getDateRangeLocalDates(request);
		dateRange.setStartDate(localDatePair.getFirst().format(DATE_FORMAT));
		dateRange.setEndDate(localDatePair.getSecond().format(DATE_FORMAT));
		return dateRange;
	}

	private List<DimensionFilterClause> getDimensionFilterClauseList(List<String> channelIds) {
		DimensionFilterClause filterClause = new DimensionFilterClause();
		DimensionFilter dimensionFilter = new DimensionFilter();
		dimensionFilter.setDimensionName(CHANNEL_DIMENSION_NAME);
		dimensionFilter.setOperator("EXACT");
		dimensionFilter.setExpressions(channelIds);
		filterClause.setFilters(Arrays.asList(dimensionFilter));
		List<DimensionFilterClause> filterClauseList = new ArrayList<>();
		filterClauseList.add(filterClause);
		return filterClauseList;
	}

	private DimensionFilterClause getSourceDimensionFilterClause(List<String> sourceList) {
		DimensionFilterClause filterClause = new DimensionFilterClause();
		DimensionFilter dimensionFilter = new DimensionFilter();
		dimensionFilter.setDimensionName(SOURCE_DIMENSION_NAME);
		dimensionFilter.setOperator("EXACT");
		dimensionFilter.setExpressions(sourceList);
		filterClause.setFilters(Arrays.asList(dimensionFilter));
		return filterClause;
	}

	/**
	 * Query the Analytics Reporting API V4. Constructs a request for the sessions
	 * for the past seven days. Returns the API response.
	 *
	 * @param service
	 * @return GetReportResponse
	 * @throws IOException
	 */
	private GetReportsResponse getReport(AnalyticsRequest analyticsRequest, String channelId) throws IOException {

		List<Metric> metrics = new ArrayList<>();

		Metric transactionValues = new Metric().setExpression("ga:transactionRevenue").setAlias("transaction-value");
		Metric goalAddToCartCompletions = new Metric().setExpression("ga:goal9Completions")
				.setAlias("add-to-cart-count");
		Metric goalConversionCompletions = new Metric().setExpression(CONVERSION_METRICS_NAME)
				.setAlias(CONVERSION_METRICS_ALIAS);
		Metric sessions = new Metric().setExpression("ga:sessions").setAlias("sessions");
		metrics.add(transactionValues);
		metrics.add(goalConversionCompletions);
		metrics.add(goalAddToCartCompletions);
		metrics.add(sessions);

		ReportRequest request = new ReportRequest().setViewId(VIEW_ID)
				.setDateRanges(Arrays.asList(getDateRange(analyticsRequest)))
				.setDimensions(getDimensions(analyticsRequest.getBreakDown()))
				.setDimensionFilterClauses(getDimensionFilterClauseList(Arrays.asList(channelId))).setMetrics(metrics);

		ArrayList<ReportRequest> requests = new ArrayList<ReportRequest>();
		requests.add(request);

		GetReportsRequest getReport = new GetReportsRequest().setReportRequests(requests);

		return analyticsReporting.reports().batchGet(getReport).execute();
	}

	private List<Dimension> getDimensions(AnalyticsBreakdownType breakdown) {
		List<Dimension> dimensionList = new ArrayList<>();
		Dimension channel = new Dimension().setName(CHANNEL_DIMENSION_NAME);
		Dimension source = new Dimension().setName(SOURCE_DIMENSION_NAME);
		Dimension month = new Dimension().setName(MONTH_DIMENSION_NAME);
		Dimension year = new Dimension().setName(YEAR_DIMENSION_NAME);
		Dimension breakdownDimension = new Dimension().setName(breakdown.gaName());
		dimensionList.add(source);
		dimensionList.add(channel);
		dimensionList.add(month);
		dimensionList.add(year);
		dimensionList.add(breakdownDimension);
		return dimensionList;
	}

	/**
	 * Parses and prints the Analytics Reporting API V4 response.
	 *
	 * @param response the Analytics Reporting API V4 response.
	 */
	private List<AnalyticsModel> convertToAnalyticsResponse(GetReportsResponse response, AnalyticsRequest request,
			String channelId) {
		List<AnalyticsModel> responseList = new ArrayList<>();
		for (Report report : response.getReports()) {
			ColumnHeader header = report.getColumnHeader();
			List<String> dimensionHeaders = header.getDimensions();
			List<MetricHeaderEntry> metricHeaders = header.getMetricHeader().getMetricHeaderEntries();
			List<ReportRow> rows = report.getData().getRows();
			if (rows == null || rows.size() == 0)
				return responseList;
			for (ReportRow row : rows) {
				List<String> dimensions = row.getDimensions();
				List<DateRangeValues> metrics = row.getMetrics();
				String sourceName = "";
				String breakdownValue = "";
				String month = "";
				String year = "";
				for (int i = 0; i < dimensionHeaders.size() && i < dimensions.size(); i++) {
					if (dimensionHeaders.get(i).equals(AnalyticsBreakdownType.AGE.gaName())
							|| dimensionHeaders.get(i).equals(AnalyticsBreakdownType.DEVICE.gaName())
							|| dimensionHeaders.get(i).equals(AnalyticsBreakdownType.GENDER.gaName())
							|| dimensionHeaders.get(i).equals(AnalyticsBreakdownType.LOCATION.gaName())) {
						breakdownValue = dimensions.get(i);
					}
					if (dimensionHeaders.get(i).equals(SOURCE_DIMENSION_NAME)) {
						sourceName = dimensions.get(i);
					}
					if (dimensionHeaders.get(i).equals(YEAR_DIMENSION_NAME)) {
						year = dimensions.get(i);
					}
					if (dimensionHeaders.get(i).equals(MONTH_DIMENSION_NAME)) {
						month = dimensions.get(i);
					}
				}
				AnalyticsModel model = getModel(responseList, sourceName, breakdownValue);
				model.setMonth(Integer.valueOf(month));
				model.setYear(Integer.valueOf(year));
				for (int j = 0; j < metrics.size(); j++) {
					DateRangeValues values = metrics.get(j);
					for (int k = 0; k < values.getValues().size() && k < metricHeaders.size(); k++) {
						if (metricHeaders.get(k).getName().equals("add-to-cart-count")) {
							model.setAddToCartCount(Integer.valueOf(values.getValues().get(k)));
						}
						if (metricHeaders.get(k).getName().equals("sessions")) {
							model.setClickCount(Integer.valueOf(values.getValues().get(k)));
						}
						if (metricHeaders.get(k).getName().equals("transaction-value")) {
							model.setRevenue(new BigDecimal(values.getValues().get(k)));
						}
						if (metricHeaders.get(k).getName().equals(CONVERSION_METRICS_ALIAS)) {
							model.setPurchaseCount(Integer.valueOf(values.getValues().get(k)));
						}

					}
				}
				responseList.add(model);
			}
		}
		return responseList;
	}

	private AnalyticsModel getModel(List<AnalyticsModel> modelList, String sourceName, String breakdownValue) {
		AnalyticsModel model = modelList.stream().filter(m -> m.getSourceName() != null
				&& m.getSourceName().equals(sourceName) && m.getBreakdownValue().equals(breakdownValue)).findFirst()
				.orElse(new AnalyticsModel());
		model.setSourceName(sourceName);
		model.setBreakdownValue(breakdownValue);
		return model;
	}

	private BigDecimal getRevenue(String affiliateId, String month, String year, BigDecimal conversion) {
		MonthlyChannelData monthlyChannelData = redisService.getMonthlySales(affiliateId, year, Integer.valueOf(month));
		return conversion.multiply(monthlyChannelData.getAffiliateStatus().getComissionRate());
	}

	@Override
	public int getClickCount(String affiliateId, String source, LocalDateTime startDate, LocalDateTime endDate) {
		List<Metric> metrics = new ArrayList<>();
		String channelId = affiliateService.getAffiliateEntityById(affiliateId).getChannelId();
		metrics.add(new Metric().setExpression("ga:sessions").setAlias("sessions"));
		List<Dimension> dimensionList = new ArrayList<>();
		dimensionList.add(new Dimension().setName(CHANNEL_DIMENSION_NAME));
		if (StringUtils.isNoneBlank(source)) {
			dimensionList.add(new Dimension().setName(SOURCE_DIMENSION_NAME));
		}
		DateRange dateRange = new DateRange().setStartDate(DATE_FORMAT.format(startDate))
				.setEndDate(DATE_FORMAT.format(endDate));
		List<DimensionFilterClause> dimensionFilters = getDimensionFilterClauseList(Arrays.asList(channelId));
		if (StringUtils.isNoneBlank(source)) {
			DimensionFilter dimensionFilter = new DimensionFilter();
			dimensionFilter.setDimensionName(SOURCE_DIMENSION_NAME);
			dimensionFilter.setOperator("EXACT");
			dimensionFilter.setExpressions(Arrays.asList(source));
			dimensionFilters.get(0).getFilters().add(dimensionFilter);
		}
		ReportRequest request = new ReportRequest().setViewId(VIEW_ID).setDateRanges(Arrays.asList(dateRange))
				.setDimensionFilterClauses(dimensionFilters).setDimensions(dimensionList);

		ArrayList<ReportRequest> requests = new ArrayList<>();
		requests.add(request);

		GetReportsRequest getReport = new GetReportsRequest().setReportRequests(requests);
		try {
			GetReportsResponse gaResponse = analyticsReporting.reports().batchGet(getReport).execute();
			int clickCount = 0;
			for (Report report : gaResponse.getReports()) {
				List<ReportRow> rows = report.getData().getRows();
				for (ReportRow row : rows) {
					List<DateRangeValues> metricResponses = row.getMetrics();
					for (int j = 0; j < metricResponses.size(); j++) {
						DateRangeValues values = metricResponses.get(j);
						for (int k = 0; k < values.getValues().size(); k++) {
							clickCount++;
						}
					}
				}
			}
			return clickCount;
		} catch (IOException e) {
			e.printStackTrace();
			return 0;
		}

	}

	@Override
	public Map<String, AnalyticsModel> getClickAndPurchaseCounts(List<Affiliate> affiliateList, LocalDateTime startDate,
			LocalDateTime endDate, String source) {
		Map<String, String> channelIdAffiliateIdMap = new HashMap<>();
		affiliateList.stream().forEach(a -> channelIdAffiliateIdMap.put(a.getChannelId(), a.getId().toString()));
		List<AnalyticsModel> analyticsList = sendClickCountRequest(new ArrayList<>(channelIdAffiliateIdMap.keySet()),
				startDate, endDate, source);
		return analyticsList.stream()
				.collect(Collectors.toMap(a -> channelIdAffiliateIdMap.get(a.getChannelId()), Function.identity()));
	}

	private List<AnalyticsModel> sendClickCountRequest(List<String> channelIds, LocalDateTime startDate,
			LocalDateTime endDate, String source) {
		List<AnalyticsModel> modelList = new ArrayList<>();
		channelIds.forEach(m -> modelList.add(AnalyticsModel.builder().channelId(m).build()));
		List<Metric> metrics = new ArrayList<>();
		metrics.add(new Metric().setExpression("ga:sessions").setAlias("sessions"));
		metrics.add(new Metric().setExpression("ga:goal8Completions").setAlias("conversion-count"));
		List<Dimension> dimensionList = new ArrayList<>();
		dimensionList.add(new Dimension().setName(CHANNEL_DIMENSION_NAME));
		List<DimensionFilterClause> dimensionFilters = getDimensionFilterClauseList(channelIds);
		if (StringUtils.isNoneBlank(source)) {
			dimensionList.add(new Dimension().setName(SOURCE_DIMENSION_NAME));
			dimensionFilters.add(getSourceDimensionFilterClause(Arrays.asList(source)));
		}
		DateRange dateRange = new DateRange().setStartDate(DATE_FORMAT.format(startDate))
				.setEndDate(DATE_FORMAT.format(endDate.plusDays(1)));

		ReportRequest request = new ReportRequest().setViewId(VIEW_ID).setDateRanges(Arrays.asList(dateRange))
				.setDimensionFilterClauses(dimensionFilters).setDimensions(dimensionList).setMetrics(metrics);

		ArrayList<ReportRequest> requests = new ArrayList<>();
		requests.add(request);

		GetReportsRequest getReport = new GetReportsRequest().setReportRequests(requests);

		try {
			GetReportsResponse gaResponse = analyticsReporting.reports().batchGet(getReport).execute();
			for (Report report : gaResponse.getReports()) {
				ColumnHeader header = report.getColumnHeader();
				List<String> dimensionHeaders = header.getDimensions();
				List<MetricHeaderEntry> metricHeaders = header.getMetricHeader().getMetricHeaderEntries();
				List<ReportRow> rows = report.getData().getRows();
				if (rows != null) {
					for (ReportRow row : rows) {
						AnalyticsModel model = new AnalyticsModel();
						List<DateRangeValues> metricResponses = row.getMetrics();
						List<String> dimensions = row.getDimensions();
						for (int j = 0; j < metricResponses.size(); j++) {
							DateRangeValues values = metricResponses.get(j);
							for (int k = 0; k < values.getValues().size() && k < metricHeaders.size(); k++) {
								if (metricHeaders.get(k).getName().equals("conversion-count")) {
									model.setPurchaseCount(
											model.getPurchaseCount() + Integer.valueOf(values.getValues().get(k)));
								}
								if (metricHeaders.get(k).getName().equals("sessions")) {
									model.setClickCount(
											model.getClickCount() + Integer.valueOf(values.getValues().get(k)));
								}
							}
						}
						for (int i = 0; i < dimensionHeaders.size() && i < dimensions.size(); i++) {
							if (dimensionHeaders.get(i).equals(CHANNEL_DIMENSION_NAME)) {
								model.setChannelId(dimensions.get(i));
							}
						}
						OptionalInt indexOpt = IntStream.range(0, modelList.size())
								.filter(i -> model.getChannelId().equals(modelList.get(i).getChannelId())).findFirst();

						modelList.set(indexOpt.getAsInt(), model);
					}
				}

			}
			return modelList;
		} catch (IOException e) {
			e.printStackTrace();
			return modelList;
		}

	}

}
