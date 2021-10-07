package com.co2nsensus.co2mission.redis;

import java.io.IOException;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Service;

import com.co2nsensus.co2mission.exception.AffiliateRuntimeException;
import com.co2nsensus.co2mission.model.dto.PaymentTransactionModel;
import com.co2nsensus.co2mission.model.entity.PaymentTransaction;
import com.co2nsensus.co2mission.service.PaymentTransactionService;
import com.co2nsensus.co2mission.service.RedisService;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.micrometer.core.annotation.Timed;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class TransactionMessageSubscriber implements MessageListener {

	private PaymentTransactionService paymentTransactionService;
	private ObjectMapper objectMapper;
	private RedisService redisService;

	@Timed(value = "redis.subscriber.time", description = "Time taken to return greeting")
	public void onMessage(final Message message, final byte[] pattern) {
		String messageBody = new String(message.getBody());
		log.info("Message received: " + messageBody);

		PaymentTransactionModel paymentTransactionModel = null;
		try {

			// Serialize message to PaymentTransactionModel
			paymentTransactionModel = objectMapper.readValue(messageBody, PaymentTransactionModel.class);

			PaymentTransaction paymentTransaction = paymentTransactionService
					.createPaymentTransaction(paymentTransactionModel);

			redisService.createOrUpdateTotalSales(paymentTransaction);

		} catch (IOException e) {
			log.error("REDIS ERR: \n Message: {}, \n Transaction message subscriber error on message read : {} ",
					messageBody, e);
		} catch (DataIntegrityViolationException e) {
			log.error("REDIS ERR: Message: {}, Transaction message subscriber error, orderId exists.", messageBody);
			throw e;
		}catch (AffiliateRuntimeException ex) {
			log.error("ASDASD REDIS ERR: \n Message: {}, Transaction message subscriber error: {}", messageBody, ex);
			throw ex;
		}
		catch (Exception ex) {
			log.error("REDIS ERR: \n Message: {}, Transaction message subscriber error: {}", messageBody, ex);
			throw ex;
		}
	}

	@Autowired
	public void setPaymentTransactionService(PaymentTransactionService paymentTransactionService) {
		this.paymentTransactionService = paymentTransactionService;
	}

	@Autowired
	public void setObjectMapper(ObjectMapper objectMapper) {
		this.objectMapper = objectMapper;
	}

	@Autowired
	public void setRedisService(RedisService redisService) {
		this.redisService = redisService;
	}

}