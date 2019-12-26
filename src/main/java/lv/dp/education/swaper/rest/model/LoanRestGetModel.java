package lv.dp.education.swaper.rest.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@Data @Builder
@AllArgsConstructor @NoArgsConstructor
public class LoanRestGetModel {
    private UUID uuid;

    private BigDecimal investedAmount;

    private BigDecimal remainingRepaymentAmount;

    private BigDecimal targetAmount;

    private BigDecimal interestPercent;

    private String description;

}
