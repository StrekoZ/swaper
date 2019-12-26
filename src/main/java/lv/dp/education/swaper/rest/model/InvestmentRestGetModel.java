package lv.dp.education.swaper.rest.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;

@Data @Builder
@AllArgsConstructor @NoArgsConstructor
public class InvestmentRestGetModel {
    private UUID uuid;

    private BigDecimal amount;

    private Date date;

    private UUID loanUuid;
}
