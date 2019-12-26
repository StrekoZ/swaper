/*
 * @(#)LoanIsFoundedException.java
 *
 * Copyright Swiss Reinsurance Company, Mythenquai 50/60, CH 8022 Zurich. All rights reserved.
 */
package lv.dp.education.swaper.service.exception;

public class LoanIsFoundedException extends ServiceException {
    public LoanIsFoundedException() {
        super("Loan is gathered necessary amount of money");
    }
}
