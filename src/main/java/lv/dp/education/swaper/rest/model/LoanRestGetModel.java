/*
 * @(#)LoanRestGetModel.java
 *
 * Copyright Swiss Reinsurance Company, Mythenquai 50/60, CH 8022 Zurich. All rights reserved.
 */
package lv.dp.education.swaper.rest.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@AllArgsConstructor @NoArgsConstructor
public class LoanRestGetModel {
    private UUID uuid;

    private BigDecimal investedAmount;

    private BigDecimal targetAmount;

    private BigDecimal interestPercent;

    private String description;

}
