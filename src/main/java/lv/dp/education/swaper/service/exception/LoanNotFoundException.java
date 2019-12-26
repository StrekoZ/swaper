package lv.dp.education.swaper.service.exception;

import java.util.UUID;

public class LoanNotFoundException extends ServiceException {
    public LoanNotFoundException(UUID loanUuid) {
        super(String.format("Loan is not found by UUID = %s", loanUuid));
    }
}
