package com.co2nsensus.co2mission.service.impl;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.co2nsensus.co2mission.exception.AffiliateCustomNotFoundException;
import com.co2nsensus.co2mission.mapper.PayoutMapper;
import com.co2nsensus.co2mission.model.entity.Affiliate;
import com.co2nsensus.co2mission.model.entity.PayoutBatch;
import com.co2nsensus.co2mission.model.response.earnings.PayoutBatchModel;
import com.co2nsensus.co2mission.model.response.earnings.PayoutModel;
import com.co2nsensus.co2mission.repo.PayoutBatchRepository;
import com.co2nsensus.co2mission.service.AffiliateService;
import com.co2nsensus.co2mission.service.PayoutService;

@Service
public class PayoutServiceImpl implements PayoutService {

	private final AffiliateService affiliateService;
	private final PayoutMapper payoutMapper;
	private final PayoutBatchRepository payoutBatchRepository;

	public PayoutServiceImpl(AffiliateService affiliateService, PayoutMapper payoutMapper,
			PayoutBatchRepository payoutBatchRepository) {
		this.affiliateService = affiliateService;
		this.payoutMapper = payoutMapper;
		this.payoutBatchRepository = payoutBatchRepository;
	}

	@Override
	public List<PayoutModel> getAffiliatePayouts(String affiliateId) {
		Affiliate affiliate = affiliateService.getAffiliateEntityById(affiliateId);
		return affiliate.getPayoutTransactions().stream().map(p -> payoutMapper.convert(p))
				.collect(Collectors.toList());
	}

	@Override
	public List<PayoutModel> getBatchPayouts(String batchId) {
		PayoutBatch payoutBatch = payoutBatchRepository.findById(UUID.fromString(batchId))
				.orElseThrow(() -> new AffiliateCustomNotFoundException(""));
		return payoutBatch.getPayoutTransactions().stream().map(p -> payoutMapper.convert(p))
				.collect(Collectors.toList());
	}

	@Override
	public List<PayoutBatchModel> getBatches() {
		List<PayoutBatch> payoutBatchList = payoutBatchRepository.findAll();
		return payoutBatchList.stream().map(p -> payoutMapper.convert(p)).collect(Collectors.toList());
	}

}
