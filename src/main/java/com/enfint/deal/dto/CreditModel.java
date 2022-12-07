package com.enfint.deal.dto;


import com.enfint.deal.model.PaymentScheduleElement;
import lombok.*;
import java.math.BigDecimal;
import java.util.List;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreditModel {

    private Long creditId;
    private BigDecimal amount;
    private Integer term;
    private BigDecimal monthlyPayment;
    private BigDecimal rate;
    private BigDecimal flc;
    private List<PaymentScheduleElement> paymentScheduleElement;
    private Boolean isInsuranceEnabled;
    private Boolean isSalaryClient;
}
