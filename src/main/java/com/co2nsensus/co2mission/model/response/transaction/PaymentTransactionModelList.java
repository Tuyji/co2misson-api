package com.co2nsensus.co2mission.model.response.transaction;

import com.co2nsensus.co2mission.model.dto.PaymentTransactionModel;
import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
public class PaymentTransactionModelList {

    List<PaymentTransactionModel> paymentTransactionModels = new ArrayList<>();
    private int totalPages;

}
