package lv.dp.education.swaper.service.exception;

import lv.dp.education.swaper.entities.LoanEntity;

import java.math.BigDecimal;

public class RepaymentIsTooBigException extends ServiceException {
    public RepaymentIsTooBigException(LoanEntity loan, BigDecimal payment) {
        super(String.format("Payment %s is bigger than Loan remaining repayment amount %s", payment, loan.remainingRepaymentAmount()));
    }
}
