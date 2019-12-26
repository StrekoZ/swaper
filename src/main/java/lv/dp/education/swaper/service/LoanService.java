package lv.dp.education.swaper.service;

import lv.dp.education.swaper.entities.LoanEntity;
import lv.dp.education.swaper.entities.LoanStatus;
import lv.dp.education.swaper.repository.LoanRepository;
import lv.dp.education.swaper.service.exception.EntityValidationException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class LoanService {

    @Autowired
    private LoanRepository repository;

    public List<LoanEntity> listLoans() {
        return repository.findAll();
    }

    public void createLoan(LoanEntity loan) throws EntityValidationException {
        validateLoan(loan);
        loan.setStatus(LoanStatus.FOUNDING);
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

    public LoanEntity getLoanByUuid(UUID loanUuid) {
        return repository.findByUuid(loanUuid);
    }
}
