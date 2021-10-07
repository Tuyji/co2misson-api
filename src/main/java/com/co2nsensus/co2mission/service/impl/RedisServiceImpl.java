package com.co2nsensus.co2mission.service.impl;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.co2nsensus.co2mission.exception.AffiliateErrorCodes;
import com.co2nsensus.co2mission.exception.AffiliateRuntimeException;
import com.co2nsensus.co2mission.model.dto.MonthlyChannelData;
import com.co2nsensus.co2mission.model.dto.MonthlySubscriptionData;
import com.co2nsensus.co2mission.model.entity.AffiliateStatus;
import com.co2nsensus.co2mission.model.entity.PaymentTransaction;
import com.co2nsensus.co2mission.model.enums.AffiliateStatusType;
import com.co2nsensus.co2mission.model.enums.TransactionType;
import com.co2nsensus.co2mission.model.response.location.CityModel;
import com.co2nsensus.co2mission.model.response.location.CountryModel;
import com.co2nsensus.co2mission.redis.RedisKeyGenerator;
import com.co2nsensus.co2mission.repo.CityRepository;
import com.co2nsensus.co2mission.repo.CountryRepository;
import com.co2nsensus.co2mission.repo.PaymentTransactionRepository;
import com.co2nsensus.co2mission.repo.RedisRepository;
import com.co2nsensus.co2mission.service.AffiliateStatusService;
import com.co2nsensus.co2mission.service.RedisService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class RedisServiceImpl implements RedisService {

	private final RedisRepository redisRepository;
	private final ObjectMapper objectMapper;
	private final AffiliateStatusService affiliateStatusService;
	private final PaymentTransactionRepository paymentTransactionRepository;
	private final CountryRepository countryRepository;
	private final CityRepository cityRepository;

	public RedisServiceImpl(RedisRepository redisRepository, ObjectMapper objectMapper,
			AffiliateStatusService affiliateStatusService, PaymentTransactionRepository paymentTransactionRepository,
			CountryRepository countryRepository, CityRepository cityRepository) {
		this.redisRepository = redisRepository;
		this.objectMapper = objectMapper;
		this.affiliateStatusService = affiliateStatusService;
		this.paymentTransactionRepository = paymentTransactionRepository;
		this.countryRepository = countryRepository;
		this.cityRepository = cityRepository;
	}

	@Override
	public List<MonthlyChannelData> getMonthlySales(String affiliateId, String year) {
		String monthlySalesKey = RedisKeyGenerator.getKey(affiliateId, year);
		Object monthlySalesObj = redisRepository.getAllKeysFromRedis(monthlySalesKey);
		Map<Integer, String> monthlyChannelMap = (Map<Integer, String>) monthlySalesObj;
		List<MonthlyChannelData> dataList = new ArrayList<>();
		try {
			for (Map.Entry<Integer, String> entry : monthlyChannelMap.entrySet()) {
				dataList.add(objectMapper.readValue(entry.getValue(), MonthlyChannelData.class));
			}
		} catch (IOException e) {
			throw new AffiliateRuntimeException(AffiliateErrorCodes.RUN_TIME_EXCEPTION.getCode(), "", e);
		}

		return dataList;
	}

	@Override
	public MonthlyChannelData getMonthlySales(String affiliateId, String year, int month) {
		String monthlySalesKey = RedisKeyGenerator.getKey(affiliateId, year);
		Object monthlySalesObj = redisRepository.getDataFromRedis(monthlySalesKey, String.valueOf(month));
		MonthlyChannelData channelData = MonthlyChannelData.builder().earnings(new BigDecimal(0)).monthIndex(month)
				.totalSales(new BigDecimal(0)).subscriptionData(MonthlySubscriptionData.builder().build())
				.affiliateStatus(affiliateStatusService.getAffiliateStatus(AffiliateStatusType.BRONZE)).build();
		if (!Objects.isNull(monthlySalesObj)) {
			try {
				channelData = objectMapper.readValue(monthlySalesObj.toString(), MonthlyChannelData.class);
			} catch (JsonMappingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (JsonProcessingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		return channelData;
	}

	@Override
	public void createOrUpdateTotalSales(PaymentTransaction paymentTransaction) throws IOException {
		String year = String.valueOf(paymentTransaction.getCreatedAt().getYear());
		String month = String.valueOf(paymentTransaction.getCreatedAt().getMonth().getValue());
		String totalSalesKey = RedisKeyGenerator.getKey(paymentTransaction.getAffiliate().getId().toString(), year);
		Object monthlyChannelDataObject = redisRepository.getDataFromRedis(totalSalesKey, month);

		if (Objects.isNull(monthlyChannelDataObject)) {
			MonthlyChannelData monthlyChannelData = getMonthlyChannelData(
					paymentTransaction.getAffiliate().getId().toString(), paymentTransaction.getCreatedAt());
			redisRepository.putDataToRedis(totalSalesKey, month, monthlyChannelData);
		} else {
			MonthlyChannelData channelData = null;
			channelData = objectMapper.readValue(monthlyChannelDataObject.toString(), MonthlyChannelData.class);
			MonthlyChannelData monthlyChannelData = updateMonthlyChannelData(channelData, paymentTransaction);
			redisRepository.putDataToRedis(totalSalesKey, month, monthlyChannelData);
		}
		incrementUnseenTransactionCount(paymentTransaction.getAffiliate().getId().toString());
	}

	private MonthlyChannelData updateMonthlyChannelData(MonthlyChannelData monthlyChannelData,
			PaymentTransaction paymentTransaction) {
		monthlyChannelData
				.setTotalSales(monthlyChannelData.getTotalSales().add(paymentTransaction.getAmountInDollars()));
		if (paymentTransaction.getTransactionType() == TransactionType.SUBSCRIPTION)
			monthlyChannelData.setSubscriptionData(
					updateSubscriptionData(monthlyChannelData.getSubscriptionData(), paymentTransaction));
		monthlyChannelData
				.setAffiliateStatus(affiliateStatusService.getAffiliateStatus(monthlyChannelData.getTotalSales()));
		monthlyChannelData.setEarnings(calculateAffiliateEarnings(monthlyChannelData));
		// notify channel
		return monthlyChannelData;
	}

	private BigDecimal calculateAffiliateEarnings(MonthlyChannelData monthlyChannelData) {
		return monthlyChannelData.getAffiliateStatus().getComissionRate().multiply(monthlyChannelData.getTotalSales());
	}

	@Override
	public int getUnseenTransactionCount(String affiliateId) {
		Object transactionCountObject = redisRepository.getDataFromRedis("TRANSACTION_COUNT", affiliateId);
		if (Objects.isNull(transactionCountObject))
			return 0;
		try {
			return objectMapper.readValue(transactionCountObject.toString(), Integer.class);
		} catch (JsonProcessingException e) {
			return 0;
		}
	}

	@Override
	public void resetUnseenTransactionCount(String affiliateId) {
		redisRepository.putDataToRedis("TRANSACTION_COUNT", affiliateId, 0);
	}

	@Override
	public void incrementUnseenTransactionCount(String affiliateId) {
		int transactionCount = getUnseenTransactionCount(affiliateId);
		transactionCount++;
		redisRepository.putDataToRedis("TRANSACTION_COUNT", affiliateId, transactionCount);
	}

	private MonthlyChannelData getMonthlyChannelData(String affiliateId, LocalDateTime transactionTime) {
		LocalDateTime start = transactionTime.truncatedTo(ChronoUnit.DAYS).withDayOfMonth(1);
		LocalDateTime end = transactionTime.plusMonths(1).truncatedTo(ChronoUnit.DAYS).withDayOfMonth(1);
		List<PaymentTransaction> paymentTransactionList = paymentTransactionRepository
				.findAllByAffiliateIdAndCreatedAtBetween(UUID.fromString(affiliateId), start, end);

		BigDecimal totalSales = new BigDecimal(0);
		MonthlySubscriptionData subscriptionData = MonthlySubscriptionData.builder().build();
		for (PaymentTransaction paymentTransaction : paymentTransactionList) {
			totalSales = totalSales.add(paymentTransaction.getAmountInDollars());
			if (paymentTransaction.getTransactionType() == TransactionType.SUBSCRIPTION) {
				subscriptionData
						.setTotalOffset(subscriptionData.getTotalOffset().add(paymentTransaction.getOffsetAmount()));
				subscriptionData
						.setTotalSales(subscriptionData.getTotalSales().add(paymentTransaction.getAmountInDollars()));
				subscriptionData.setTreesPlanted(subscriptionData.getTreesPlanted() + paymentTransaction.getTrees());
			}
		}

		AffiliateStatus status = affiliateStatusService.getAffiliateStatus(totalSales);

		return MonthlyChannelData.builder().affiliateStatus(status)
				.earnings(totalSales.multiply(status.getComissionRate())).totalSales(totalSales)
				.subscriptionData(subscriptionData).monthIndex(transactionTime.getMonthValue()).build();
	}

	private MonthlySubscriptionData updateSubscriptionData(MonthlySubscriptionData subscriptionData,
			PaymentTransaction paymentTransaction) {
		subscriptionData.setTotalOffset(subscriptionData.getTotalOffset().add(paymentTransaction.getOffsetAmount()));
		subscriptionData.setTotalSales(subscriptionData.getTotalSales().add(paymentTransaction.getAmountInDollars()));
		subscriptionData.setTreesPlanted(subscriptionData.getTreesPlanted() + paymentTransaction.getTrees());
		return subscriptionData;
	}

	@Override
	public List<CountryModel> getCountries() {
		Object countryList = redisRepository.getDataFromRedis("COUNTRIES", "COUNTRIES");
		List<CountryModel> countryModels = new ArrayList<>();
		if (Objects.isNull(countryList)) {
			countryModels = countryRepository.findAll().stream().map(c -> new CountryModel(c.getId(), c.getName()))
					.collect(Collectors.toList());
			redisRepository.putDataToRedis("COUNTRIES", "COUNTRIES", countryModels);
			return countryModels;
		} else {
			try {
				countryModels = objectMapper.readValue(countryList.toString(),
						objectMapper.getTypeFactory().constructCollectionType(List.class, CountryModel.class));
			} catch (JsonProcessingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		return countryModels;
	}

	@Override
	public List<CityModel> getCityListByCountry(Long countryId) {
		Object cityList = redisRepository.getDataFromRedis("CITIES", countryId.toString());
		List<CityModel> cityModels = new ArrayList<>();
		if (Objects.isNull(cityList)) {
			cityModels = cityRepository.findByCountryId(countryId).stream()
					.map(c -> new CityModel(c.getId(), c.getName(), c.getCode())).collect(Collectors.toList());
			redisRepository.putDataToRedis("CITIES", countryId.toString(), cityModels);
			return cityModels;
		} else {
			try {
				cityModels = objectMapper.readValue(cityList.toString(),
						objectMapper.getTypeFactory().constructCollectionType(List.class, CityModel.class));
			} catch (JsonProcessingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		return cityModels;
	}

}
