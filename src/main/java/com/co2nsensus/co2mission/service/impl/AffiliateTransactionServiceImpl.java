package com.co2nsensus.co2mission.service.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.support.PagedListHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import com.co2nsensus.co2mission.mapper.AffiliateMapper;
import com.co2nsensus.co2mission.model.dto.AffiliateEntityListPagedModel;
import com.co2nsensus.co2mission.model.dto.MonthlyChannelData;
import com.co2nsensus.co2mission.model.dto.PaymentTransactionModel;
import com.co2nsensus.co2mission.model.dto.PaymentTransactionWrapper;
import com.co2nsensus.co2mission.model.entity.Affiliate;
import com.co2nsensus.co2mission.model.entity.PaymentTransaction;
import com.co2nsensus.co2mission.model.enums.AffiliateVerificationStatus;
import com.co2nsensus.co2mission.model.enums.PaymentVerificationStatus;
import com.co2nsensus.co2mission.model.enums.SourceType;
import com.co2nsensus.co2mission.model.enums.TransactionType;
import com.co2nsensus.co2mission.model.response.analytics.AnalyticsModel;
import com.co2nsensus.co2mission.model.response.source.AffiliateSourceModel;
import com.co2nsensus.co2mission.model.response.source.AffiliateSourceModelList;
import com.co2nsensus.co2mission.model.response.transaction.AffiliateTransactionMetrics;
import com.co2nsensus.co2mission.model.response.transaction.AffiliateTransactionMetricsList;
import com.co2nsensus.co2mission.model.response.transaction.PaymentTransactionMetrics;
import com.co2nsensus.co2mission.model.response.transaction.PaymentTransactionModelList;
import com.co2nsensus.co2mission.model.response.transaction.TransactionHistoryResponseModel;
import com.co2nsensus.co2mission.repo.PaymentTransactionRepository;
import com.co2nsensus.co2mission.service.AffiliateService;
import com.co2nsensus.co2mission.service.AffiliateSourceService;
import com.co2nsensus.co2mission.service.AffiliateTransactionService;
import com.co2nsensus.co2mission.service.AnalyticsService;
import com.co2nsensus.co2mission.service.RedisService;
import com.co2nsensus.co2mission.utils.DateUtil;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class AffiliateTransactionServiceImpl implements AffiliateTransactionService {

	private final PaymentTransactionRepository paymentTransactionRepository;

	private final RedisService redisService;

	private final AnalyticsService analyticsService;

	private final AffiliateSourceService affiliateSourceService;

	private final AffiliateService affiliateService;

	private final AffiliateMapper affiliateMapper;

	private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("dd MMM YYYY")
			.withLocale(Locale.ENGLISH);

	public AffiliateTransactionServiceImpl(PaymentTransactionRepository paymentTransactionRepository,
			RedisService redisService, AnalyticsService analyticsService, AffiliateSourceService affiliateSourceService,
			AffiliateService affiliateService, AffiliateMapper affiliateMapper) {
		this.paymentTransactionRepository = paymentTransactionRepository;
		this.redisService = redisService;
		this.analyticsService = analyticsService;
		this.affiliateSourceService = affiliateSourceService;
		this.affiliateService = affiliateService;
		this.affiliateMapper = affiliateMapper;
	}

	@Override
	public PaymentTransactionModelList getLastNTransactions(String affiliateId, Pageable pageable) {
		log.info("AffiliateTransactionServiceImpl.getLastNTransactions started");

		Page<PaymentTransaction> paymentTransactionsPaged = paymentTransactionRepository
				.findAllByAffiliateId(UUID.fromString(affiliateId), pageable);
		List<PaymentTransaction> paymentTransactions = paymentTransactionsPaged.getContent();
		List<PaymentTransactionModel> paymentTransactionModels = paymentTransactions.stream().map(p -> convert(p))
				.collect(Collectors.toList());

		log.info("AffiliateTransactionServiceImpl.getLastNTransactions finished");

		return PaymentTransactionModelList.builder().paymentTransactionModels(paymentTransactionModels).build();
	}

	@Override
	public TransactionHistoryResponseModel getTransactionHistory(String affiliateId, String source, LocalDate startDate,
			LocalDate endDate, int page) {
		log.info("AffiliateTransactionServiceImpl.getTransactionHistory started");
		List<PaymentTransaction> paymentTransactionsAll;
		if (StringUtils.isAllBlank(source)) {
			paymentTransactionsAll = paymentTransactionRepository.findAllByAffiliateIdAndCreatedAtBetween(
					UUID.fromString(affiliateId), startDate.atTime(0, 0), endDate.atTime(0, 0).plusDays(1));
		} else {
			paymentTransactionsAll = paymentTransactionRepository
					.findAllByAffiliateIdAndTransactionSourceAndCreatedAtBetweenAndTransactionType(
							UUID.fromString(affiliateId), source, startDate.atTime(0, 0),
							endDate.atTime(0, 0).plusDays(1), TransactionType.OFFSET);
		}
		PagedListHolder<PaymentTransaction> pagedList = new PagedListHolder<PaymentTransaction>(paymentTransactionsAll);
		pagedList.setPageSize(10);
		pagedList.setPage(page);

		List<PaymentTransactionWrapper> transactionsWithEarnings = calculateMyEarnings(affiliateId,
				paymentTransactionsAll);

		PaymentTransactionMetrics paymentTransactionMetrics = getPaymentTransactionMetrics(affiliateId, null, startDate,
				endDate, paymentTransactionsAll, transactionsWithEarnings, source);

		List<PaymentTransactionModel> paymentTransactionModels = transactionsWithEarnings.stream()
				.filter(t -> pagedList.getPageList().contains(t.getPaymentTransaction())).map(t -> convert(t))
				.collect(Collectors.toList());

		PaymentTransactionModelList paymentTransactionModelList = PaymentTransactionModelList.builder()
				.paymentTransactionModels(paymentTransactionModels).totalPages(pagedList.getPageCount()).build();

		TransactionHistoryResponseModel responseModel = new TransactionHistoryResponseModel();
		responseModel.setPaymentTransactionModels(paymentTransactionModelList);
		responseModel.setPaymentTransactionMetrics(paymentTransactionMetrics);
		redisService.resetUnseenTransactionCount(affiliateId);
		log.info("AffiliateTransactionServiceImpl.getTransactionHistory finished");

		return responseModel;
	}

	@Override
	public TransactionHistoryResponseModel getTransactionHistory(String affiliateId, SourceType sourceType,
			LocalDate startDate, LocalDate endDate, String projectId) {
		List<PaymentTransaction> paymentTransactionsAll = paymentTransactionRepository
				.findAllByAffiliateIdAndCreatedAtBetween(UUID.fromString(affiliateId), startDate.atTime(0, 0),
						endDate.atTime(0, 0));
		List<PaymentTransaction> paymentTransactionsFiltered = new ArrayList<>();
		AffiliateSourceModelList affiliateSources = new AffiliateSourceModelList();
		if (sourceType != null) {
			affiliateSources = affiliateSourceService.getAffiliateSources(affiliateId, sourceType);
		}
		for (PaymentTransaction paymentTransaction : paymentTransactionsAll) {
			if (StringUtils.isNoneBlank(projectId)
					&& !projectId.equals(paymentTransaction.getOffsetProject().getId().toString()))
				continue;
			if (sourceType != null) {
				boolean sourceExists = affiliateSources.getSourceList().stream()
						.anyMatch(s -> s.getAnalyticsName().equals(paymentTransaction.getTransactionSource()));
				if (!sourceExists)
					continue;
			}
			paymentTransactionsFiltered.add(paymentTransaction);
		}

		List<PaymentTransactionWrapper> transactionsWithEarnings = calculateMyEarnings(affiliateId,
				paymentTransactionsAll);

		PaymentTransactionMetrics paymentTransactionMetrics = getPaymentTransactionMetrics(affiliateId, null, startDate,
				endDate, paymentTransactionsAll, transactionsWithEarnings, "");

		List<PaymentTransactionModel> paymentTransactionModels = paymentTransactionsFiltered.stream()
				.map(t -> convert(t)).collect(Collectors.toList());

		PaymentTransactionModelList paymentTransactionModelList = PaymentTransactionModelList.builder()
				.paymentTransactionModels(paymentTransactionModels).build();

		TransactionHistoryResponseModel responseModel = new TransactionHistoryResponseModel();
		responseModel.setPaymentTransactionModels(paymentTransactionModelList);
		responseModel.setPaymentTransactionMetrics(paymentTransactionMetrics);
		return responseModel;

	}

	public TransactionHistoryResponseModel getTransactionHistory(Affiliate affiliate, AnalyticsModel analyticsModel,
			SourceType sourceType, LocalDate startDate, LocalDate endDate, String projectId) {
		List<PaymentTransaction> paymentTransactionsAll = paymentTransactionRepository
				.findAllByAffiliateIdAndCreatedAtBetween(affiliate.getId(), startDate.atTime(0, 0),
						endDate.atTime(0, 0));
		List<PaymentTransaction> paymentTransactionsFiltered = new ArrayList<>();
		AffiliateSourceModelList affiliateSources = new AffiliateSourceModelList();
		if (sourceType != null) {
			affiliateSources = affiliateSourceService.getAffiliateSources(affiliate.getId().toString(), sourceType);
		}
		for (PaymentTransaction paymentTransaction : paymentTransactionsAll) {
			if (StringUtils.isNoneBlank(projectId)
					&& !projectId.equals(paymentTransaction.getOffsetProject().getId().toString()))
				continue;
			if (sourceType != null) {
				boolean sourceExists = affiliateSources.getSourceList().stream()
						.anyMatch(s -> s.getAnalyticsName().equals(paymentTransaction.getTransactionSource()));
				if (!sourceExists)
					continue;
			}
			paymentTransactionsFiltered.add(paymentTransaction);
		}

		List<PaymentTransactionWrapper> transactionsWithEarnings = calculateMyEarnings(affiliate.getId().toString(),
				paymentTransactionsAll);

		PaymentTransactionMetrics paymentTransactionMetrics = getPaymentTransactionMetrics(affiliate.getId().toString(),
				analyticsModel, startDate, endDate, paymentTransactionsAll, transactionsWithEarnings, "");

		List<PaymentTransactionModel> paymentTransactionModels = paymentTransactionsFiltered.stream()
				.map(t -> convert(t)).collect(Collectors.toList());

		PaymentTransactionModelList paymentTransactionModelList = PaymentTransactionModelList.builder()
				.paymentTransactionModels(paymentTransactionModels).build();

		TransactionHistoryResponseModel responseModel = new TransactionHistoryResponseModel();
		responseModel.setPaymentTransactionModels(paymentTransactionModelList);
		responseModel.setPaymentTransactionMetrics(paymentTransactionMetrics);
		return responseModel;

	}

	private PaymentTransactionMetrics getPaymentTransactionMetrics(AnalyticsModel analyticsModel, LocalDate startDate,
			LocalDate endDate, List<PaymentTransaction> paymentTransactionsAll,
			List<PaymentTransactionWrapper> transactionsWithEarnings) {
		int totalClicks = analyticsModel == null ? 0 : analyticsModel.getClickCount();

		// Get Min Sales
		BigDecimal minSalesAmount = paymentTransactionsAll.stream()
				.min(Comparator.comparing(PaymentTransaction::getAmountInDollars))
				.orElse(PaymentTransaction.zeroPaymentTransaction()).getAmountInDollars();

		// Get Max Sales
		BigDecimal maxSalesAmount = paymentTransactionsAll.stream()
				.max(Comparator.comparing(PaymentTransaction::getAmountInDollars))
				.orElse(PaymentTransaction.zeroPaymentTransaction()).getAmountInDollars();

		// Get Avg Sales
		BigDecimal avgSalesAmount;
		if (paymentTransactionsAll.size() > 0) {
			avgSalesAmount = paymentTransactionsAll.stream().map(x -> x.getAmountInDollars())
					.reduce(BigDecimal.ZERO, BigDecimal::add)
					.divide(BigDecimal.valueOf(paymentTransactionsAll.size()), 2, RoundingMode.HALF_UP);
		} else {
			avgSalesAmount = new BigDecimal(0);
		}

		// Number Of Offset
		int numberOfOffset = paymentTransactionsAll.size();

		// Get totalOffsetAmount
		BigDecimal totalOffsetAmount = paymentTransactionsAll.stream().map(x -> x.getOffsetAmount())
				.reduce(BigDecimal.ZERO, BigDecimal::add);

		// Get totalSalesAmount
		BigDecimal totalSalesAmount = paymentTransactionsAll.stream().map(x -> x.getAmountInDollars())
				.reduce(BigDecimal.ZERO, BigDecimal::add);

		// Get total earnings
		BigDecimal myEarnings = transactionsWithEarnings.stream().map(t -> t.getEarning()).reduce(BigDecimal.ZERO,
				BigDecimal::add);

		return PaymentTransactionMetrics.builder().totalClicks(totalClicks).minSalesAmount(minSalesAmount)
				.maxSalesAmount(maxSalesAmount).averageSalesAmount(avgSalesAmount).totalSalesAmount(totalSalesAmount)
				.numberOfOffsets(numberOfOffset).totalOffsetAmount(totalOffsetAmount).myEarnings(myEarnings).build();
	}

	private PaymentTransactionMetrics getPaymentTransactionMetrics(String affiliateId, AnalyticsModel analyticsModel,
			LocalDate startDate, LocalDate endDate, List<PaymentTransaction> paymentTransactionsAll,
			List<PaymentTransactionWrapper> transactionsWithEarnings, String source) {
		if (analyticsModel == null)
			analyticsModel = analyticsService
					.getClickAndPurchaseCounts(Arrays.asList(affiliateService.getAffiliateEntityById(affiliateId)),
							startDate.atStartOfDay(), endDate.atStartOfDay(), source)
					.get(affiliateId);
		return getPaymentTransactionMetrics(analyticsModel, startDate, endDate, paymentTransactionsAll,
				transactionsWithEarnings);
	}

	private List<PaymentTransactionWrapper> calculateMyEarnings(String affiliateId,
			List<PaymentTransaction> paymentTransactions) {
		AffiliateSourceModelList affiliateSourceModels = affiliateSourceService.getAllAffiliateSources(affiliateId);
		List<PaymentTransactionWrapper> transactionsWithEarning = new ArrayList<>();
		for (PaymentTransaction paymentTransaction : paymentTransactions) {

			LocalDateTime createdAt = paymentTransaction.getCreatedAt();
			int year = DateUtil.getYearOfLocalDate(createdAt);
			int month = DateUtil.getMonthOfLocalDate(createdAt);

			MonthlyChannelData monthlyChannelData = redisService
					.getMonthlySales(paymentTransaction.getAffiliate().getId().toString(), String.valueOf(year), month);

			BigDecimal myEarning = paymentTransaction.getAmountInDollars()
					.multiply(monthlyChannelData.getAffiliateStatus().getComissionRate());

			String sourceName = "";
			Optional<AffiliateSourceModel> sourceOptional = affiliateSourceModels.getSourceList().stream()
					.filter(s -> s.getAnalyticsName().equals(paymentTransaction.getTransactionSource())).findFirst();
			if (sourceOptional.isPresent())
				sourceName = sourceOptional.get().getName();
			transactionsWithEarning.add(PaymentTransactionWrapper.builder().earning(myEarning)
					.paymentTransaction(paymentTransaction).sourceName(sourceName).build());
		}

		return transactionsWithEarning;
	}

	private PaymentTransactionModel convert(PaymentTransactionWrapper paymentTransactionWrapper) {
		return PaymentTransactionModel.builder().amount(paymentTransactionWrapper.getPaymentTransaction().getAmount())
				.amountInDollars(paymentTransactionWrapper.getPaymentTransaction().getAmountInDollars())
				.source(paymentTransactionWrapper.getSourceName())
				.projectName(paymentTransactionWrapper.getPaymentTransaction().getOffsetProject().getProjectName())
				.createdAt(DATE_FORMAT.format(paymentTransactionWrapper.getPaymentTransaction().getCreatedAt()))
				.earning(paymentTransactionWrapper.getEarning().setScale(2, RoundingMode.DOWN))
				.name(paymentTransactionWrapper.getPaymentTransaction().getName())
				.offsetAmount(paymentTransactionWrapper.getPaymentTransaction().getOffsetAmount())
				.surName(paymentTransactionWrapper.getPaymentTransaction().getSurName()).build();
	}

	private PaymentTransactionModel convert(PaymentTransaction paymentTransaction) {
		return PaymentTransactionModel.builder().amount(paymentTransaction.getAmount())
				.amountInDollars(paymentTransaction.getAmountInDollars())
				.source(paymentTransaction.getTransactionSource())
				.projectName(paymentTransaction.getOffsetProject().getProjectName())
				.createdAt(DATE_FORMAT.format(paymentTransaction.getCreatedAt())).name(paymentTransaction.getName())
				.offsetAmount(paymentTransaction.getOffsetAmount()).surName(paymentTransaction.getSurName()).build();
	}

	@Override
	public AffiliateTransactionMetrics getTransactionMetrics(String affiliateId, SourceType sourceType,
			LocalDate startDate, LocalDate endDate, int page, String projectId, String sort, int size, String filter,
			Direction direction, AffiliateVerificationStatus verificationStatus,
			PaymentVerificationStatus paymentVerificationStatus, String platformId) {
		Pageable requestedPage = PageRequest.of(page, size, Sort.by(direction, sort));
		AffiliateEntityListPagedModel affiliates = affiliateService.getAffiliateEntitiesPaged(filter, sourceType,
				verificationStatus, paymentVerificationStatus, platformId, requestedPage);
		List<AffiliateTransactionMetrics> affiliateList = new ArrayList<>();
		Map<String, AnalyticsModel> affiliateAnalyticsMap = analyticsService.getClickAndPurchaseCounts(
				affiliates.getAffiliateList(), startDate.atStartOfDay(), endDate.atStartOfDay(), "");
		Affiliate affiliate = affiliateService.getAffiliateEntityById(affiliateId);
		TransactionHistoryResponseModel transactionResponse = getTransactionHistory(affiliate,
				affiliateAnalyticsMap.get(affiliate.getId().toString()), sourceType, startDate, endDate, projectId);
		affiliateList.add(AffiliateTransactionMetrics.builder().affiliate(affiliateMapper.convert(affiliate))
				.transactionHistory(transactionResponse).build());

		return AffiliateTransactionMetrics.builder().affiliate(affiliateMapper.convert(affiliate))
				.transactionHistory(transactionResponse).build();
	}

	@Override
	public AffiliateTransactionMetricsList getAllTransactionMetrics(SourceType sourceType, LocalDate startDate,
			LocalDate endDate, int page, String projectId, String sort, int size, String filter, Direction direction,
			AffiliateVerificationStatus verificationStatus, PaymentVerificationStatus paymentVerificationStatus,
			String platformId) {
		Pageable requestedPage = PageRequest.of(page, size, Sort.by(direction, sort));
		AffiliateEntityListPagedModel affiliates = affiliateService.getAffiliateEntitiesPaged(filter, sourceType,
				verificationStatus, paymentVerificationStatus, platformId, requestedPage);
		List<AffiliateTransactionMetrics> affiliateList = new ArrayList<>();
		Map<String, AnalyticsModel> affiliateAnalyticsMap = analyticsService.getClickAndPurchaseCounts(
				affiliates.getAffiliateList(), startDate.atStartOfDay(), endDate.atStartOfDay(), "");
		for (Affiliate affiliate : affiliates.getAffiliateList()) {
			TransactionHistoryResponseModel transactionResponse = getTransactionHistory(affiliate,
					affiliateAnalyticsMap.get(affiliate.getId().toString()), sourceType, startDate, endDate, projectId);
			affiliateList.add(AffiliateTransactionMetrics.builder().affiliate(affiliateMapper.convert(affiliate))
					.transactionHistory(transactionResponse).build());

		}
		return AffiliateTransactionMetricsList.builder().affiliateList(affiliateList)
				.totalPages(affiliates.getTotalPages()).build();
	}

}
