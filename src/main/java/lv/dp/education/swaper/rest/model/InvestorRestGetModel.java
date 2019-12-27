package lv.dp.education.swaper.rest.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class InvestorRestGetModel {
    private String username;

    private BigDecimal account = BigDecimal.ZERO;

    private Set<InvestmentRestGetModel> investments;
}
