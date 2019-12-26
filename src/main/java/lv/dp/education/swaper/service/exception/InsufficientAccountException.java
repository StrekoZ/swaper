package lv.dp.education.swaper.service.exception;

import java.math.BigDecimal;

public class InsufficientAccountException extends ServiceException {
    public InsufficientAccountException(BigDecimal account) {
        super(String.format("There is no enough money in account. Account is %s", account));
    }
}
