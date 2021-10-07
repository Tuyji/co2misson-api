package com.co2nsensus.co2mission.service.impl;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import com.co2nsensus.co2mission.model.dto.AffiliateAmountType;
import com.co2nsensus.co2mission.model.dto.PaymentTransactionModel;
import com.co2nsensus.co2mission.model.entity.Affiliate;
import com.co2nsensus.co2mission.model.entity.AffiliateSubscription;
import com.co2nsensus.co2mission.model.entity.OffsetProject;
import com.co2nsensus.co2mission.model.entity.PaymentTransaction;
import com.co2nsensus.co2mission.model.enums.AffiliateSubscriptionStatus;
import com.co2nsensus.co2mission.model.enums.TransactionType;
import com.co2nsensus.co2mission.repo.AffiliateSubscriptionRepository;
import com.co2nsensus.co2mission.repo.PaymentTransactionRepository;
import com.co2nsensus.co2mission.service.AffiliateService;
import com.co2nsensus.co2mission.service.OffsetProjectService;
import com.co2nsensus.co2mission.service.PaymentTransactionService;
import com.co2nsensus.co2mission.service.client.CurrencyConversionClient;

import lombok.extern.slf4j.Slf4j;

@Service
@Lazy
@Slf4j
public class PaymentTransactionServiceImpl implements PaymentTransactionService {

	private final PaymentTransactionRepository paymentTransactionRepository;

	private final AffiliateService affiliateService;

	private final CurrencyConversionClient currencyConversionClient;

	private final AffiliateSubscriptionRepository affiliateSubscriptionRepository;

	private final OffsetProjectService offsetProjectService;

	public PaymentTransactionServiceImpl(PaymentTransactionRepository paymentTransactionRepository,
			AffiliateService affiliateService, CurrencyConversionClient currencyConversionClient,
			AffiliateSubscriptionRepository affiliateSubscriptionRepository,
			OffsetProjectService offsetProjectService) {
		this.paymentTransactionRepository = paymentTransactionRepository;
		this.affiliateService = affiliateService;
		this.currencyConversionClient = currencyConversionClient;
		this.affiliateSubscriptionRepository = affiliateSubscriptionRepository;
		this.offsetProjectService = offsetProjectService;
	}

	@Override
	public PaymentTransaction createPaymentTransaction(PaymentTransactionModel paymentTransactionModel) {
		log.info("PaymentTransactionServiceImpl.createPaymentTransaction started");
		Affiliate affiliate = affiliateService.getAffiliateByChannelId(paymentTransactionModel.getChannelId());
		LocalDateTime createdAt = LocalDateTime.parse(paymentTransactionModel.getCreatedAt(),
				DateTimeFormatter.ISO_OFFSET_DATE_TIME);
//		AffiliateAmountType amountInDollars = currencyConversionClient.convertCurrency(AffiliateAmountType.builder()
//				.amount(paymentTransactionModel.getAmount()).currency(paymentTransactionModel.getCurrency()).build());
//		
		paymentTransactionModel.setAmountInDollars(paymentTransactionModel.getAmount());
		AffiliateSubscription newSubsription = getNewSubscription(paymentTransactionModel, affiliate);
		if (!Objects.isNull(newSubsription))
			newSubsription = affiliateSubscriptionRepository.save(newSubsription);

		PaymentTransaction paymentTransaction = PaymentTransaction.builder().affiliate(affiliate)
				.amount(paymentTransactionModel.getAmount())
				.amountInDollars(paymentTransactionModel.getAmountInDollars()).createdAt(createdAt)
				.messageTime(LocalDateTime.now()).currency(paymentTransactionModel.getCurrency())
				.name(paymentTransactionModel.getName()).surName(paymentTransactionModel.getSurName())
				.offsetProject(getOffsetProject(paymentTransactionModel))
				.offsetAmount(paymentTransactionModel.getOffsetAmount()).orderId(paymentTransactionModel.getOrderId())
				.transactionType(paymentTransactionModel.getSubscription() == null ? TransactionType.OFFSET
						: TransactionType.SUBSCRIPTION)
				.trees(paymentTransactionModel.getSubscription() == null ? 0
						: paymentTransactionModel.getSubscription().getTrees())
				.transactionSource(paymentTransactionModel.getSource()).build();

		if (!Objects.isNull(newSubsription)) {
			paymentTransaction.setAffiliateSubscription(newSubsription);
		} else {
			Optional<AffiliateSubscription> subcriptionOptional = getExistingSubscription(paymentTransactionModel);
			if (subcriptionOptional.isPresent())
				paymentTransaction.setAffiliateSubscription(subcriptionOptional.get());
		}

		log.info("PaymentTransactionServiceImpl.createPaymentTransaction finished");
		return paymentTransactionRepository.save(paymentTransaction);
	}

	private OffsetProject getOffsetProject(PaymentTransactionModel paymentTransactionModel) {
		Optional<OffsetProject> offsetProjectOptional = offsetProjectService
				.getOffsetProjectByExternalId(paymentTransactionModel.getProjectId());
		if (offsetProjectOptional.isPresent()) {
			return offsetProjectOptional.get();
		} else {
			OffsetProject offsetProject = new OffsetProject();
			offsetProject.setExternalId(paymentTransactionModel.getProjectId());
			offsetProject.setProjectName(paymentTransactionModel.getProjectName());
			return offsetProjectService.saveOffsetProject(offsetProject);
		}
	}

	public AffiliateSubscription getNewSubscription(PaymentTransactionModel paymentTransactionModel,
			Affiliate affiliate) {
		if (paymentTransactionModel.getSubscription() == null
				|| paymentTransactionModel.getSubscription().getIntervalCount() != 1)
			return null;

		AffiliateSubscription affiliateSubscription = new AffiliateSubscription();
		affiliateSubscription.setActive(true);
		affiliateSubscription.setAffiliate(affiliate);
		affiliateSubscription.setSource(paymentTransactionModel.getSource());
		affiliateSubscription.setSubscriberName(paymentTransactionModel.getName());
		affiliateSubscription.setSubscriberSurname(paymentTransactionModel.getSurName());
		affiliateSubscription.setCreatedAt(LocalDateTime.parse(paymentTransactionModel.getCreatedAt(),
				DateTimeFormatter.ISO_OFFSET_DATE_TIME));
		affiliateSubscription.setMonthlyOffset(paymentTransactionModel.getSubscription().getCarbon());
		affiliateSubscription.setName(paymentTransactionModel.getSubscription().getPlanId());
		affiliateSubscription.setMonthlyPayment(paymentTransactionModel.getAmount());
		affiliateSubscription.setMonthlyPaymentCurrency(paymentTransactionModel.getCurrency());
		affiliateSubscription.setMonthlyPaymentDollar(paymentTransactionModel.getAmountInDollars());
		affiliateSubscription.setMonthlyTrees(paymentTransactionModel.getSubscription().getTrees());
		affiliateSubscription.setSubscriptionStatus(AffiliateSubscriptionStatus.ACTIVE);
		affiliateSubscription.setSubscriptionId(paymentTransactionModel.getSubscription().getSubscriptionId());
		return affiliateSubscription;
	}

	private Optional<AffiliateSubscription> getExistingSubscription(PaymentTransactionModel paymentTransactionModel) {
		if (paymentTransactionModel.getSubscription() == null)
			return Optional.empty();
		return affiliateSubscriptionRepository
				.findBySubscriptionId(paymentTransactionModel.getSubscription().getSubscriptionId());
	}

	public void handleSubscription(PaymentTransactionModel paymentTransactionModel,
			PaymentTransaction paymentTransaction) {
		if (paymentTransactionModel.getSubscription() == null)
			return;

		if (paymentTransactionModel.getSubscription().getIntervalCount() == 1) {
			createNewSubscription(paymentTransactionModel, paymentTransaction);
		} else {

		}
	}

	private void createNewSubscription(PaymentTransactionModel paymentTransactionModel,
			PaymentTransaction paymentTransaction) {
		AffiliateSubscription affiliateSubscription = new AffiliateSubscription();
		affiliateSubscription.setActive(true);
		affiliateSubscription.setAffiliate(paymentTransaction.getAffiliate());
		affiliateSubscription.setSource(paymentTransactionModel.getSource());
		affiliateSubscription.setCreatedAt(LocalDateTime.now());
		affiliateSubscription.setMonthlyOffset(paymentTransactionModel.getSubscription().getCarbon());
		affiliateSubscription.setMonthlyPayment(paymentTransactionModel.getAmount());
		affiliateSubscription.setMonthlyPaymentCurrency(paymentTransactionModel.getCurrency());
		affiliateSubscription.setMonthlyPaymentDollar(paymentTransactionModel.getAmountInDollars());
		affiliateSubscription.setMonthlyTrees(paymentTransactionModel.getSubscription().getTrees());
		affiliateSubscription.setSubscriptionStatus(AffiliateSubscriptionStatus.ACTIVE);
		List<PaymentTransaction> transactionList = new ArrayList<>();
		paymentTransaction.setAffiliateSubscription(affiliateSubscription);
		transactionList.add(paymentTransaction);
		affiliateSubscription.setPaymentTransactions(transactionList);
		affiliateSubscriptionRepository.save(affiliateSubscription);
	}
}
