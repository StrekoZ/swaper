/*
 * @(#)ErrorRestModel.java
 *
 * Copyright Swiss Reinsurance Company, Mythenquai 50/60, CH 8022 Zurich. All rights reserved.
 */
package lv.dp.education.swaper.rest.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class ErrorRestModel {
    private String errorTitle;
    private List<String> errorDetails;
}
