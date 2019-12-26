package lv.dp.education.swaper.service;

import lv.dp.education.swaper.model.LoanEntity;
import lv.dp.education.swaper.repository.LoanRepository;
import lv.dp.education.swaper.service.exception.EntityValidationException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Service
public class LoanService {

    @Autowired
    private LoanRepository repository;

    public List<LoanEntity> listLoans() {
        return repository.findAll();
    }

    public void createLoan(LoanEntity loan) throws EntityValidationException {
        validateLoan(loan);
        repository.save(loan);
    }

    private void validateLoan(LoanEntity loan) throws EntityValidationException {
        if (loan == null) {
            throw new EntityValidationException(Collections.singletonList("Loan object is not provided"));
        }
        List<String> errors = new ArrayList<>();
        if (StringUtils.isBlank(loan.getDescription())) {
            errors.add("Loan Description is not provided");
        }
        if (loan.getTargetAmount() == null || loan.getTargetAmount().compareTo(BigDecimal.ZERO) <= 0) {
            errors.add("Loan Target Amount must be positive");
        }
        if (loan.getInterestPercent() == null || loan.getInterestPercent().compareTo(BigDecimal.ZERO) <= 0) {
            errors.add("Loan Interest Percent must be positive");
        }
        if (!errors.isEmpty()) {
            throw new EntityValidationException(errors);
        }
    }
}
