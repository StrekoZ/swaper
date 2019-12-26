package lv.dp.education.swaper.service.exception;

public class LoanIsFoundedException extends ServiceException {
    public LoanIsFoundedException() {
        super("Loan is gathered necessary amount of money");
    }
}
