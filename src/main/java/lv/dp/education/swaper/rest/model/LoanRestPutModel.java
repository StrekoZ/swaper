package lv.dp.education.swaper.rest.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data @Builder
@AllArgsConstructor @NoArgsConstructor
public class LoanRestPutModel {
    private BigDecimal targetAmount;

    private BigDecimal interestPercent;

    private String description;
}
