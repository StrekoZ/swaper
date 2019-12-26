package lv.dp.education.swaper.service;

import lv.dp.education.swaper.entities.InvestmentEntity;
import lv.dp.education.swaper.entities.LoanStatus;
import lv.dp.education.swaper.repository.InvestmentRepository;
import lv.dp.education.swaper.service.exception.EntityValidationException;
import lv.dp.education.swaper.service.exception.InsufficientAccountException;
import lv.dp.education.swaper.service.exception.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

@Service
@Transactional
public class InvestmentService {

    @Autowired
    private InvestorService investorService;
    @Autowired
    private InvestmentRepository repository;

    public List<InvestmentEntity> listInvestmentsForUser(String username) {
        return repository.findByInvestorUsername(username);
    }

    public void invest(InvestmentEntity investmentEntity) throws ServiceException {
        validateInvestment(investmentEntity);
        investmentEntity.setDate(new Date());
        investorService.reduceInvestorAccount(investmentEntity.getInvestor(), investmentEntity.getAmount());
        repository.save(investmentEntity);
    }

    private void validateInvestment(InvestmentEntity investment) throws EntityValidationException {
        if (investment == null) {
            throw new EntityValidationException(Collections.singletonList("Investment object is not provided"));
        }
        List<String> errors = new ArrayList<>();
        if (investment.getLoan() == null) {
            errors.add("Please specify valid Loan to invest in");
        }
        if (investment.getInvestor() == null) {
            errors.add("Investor is not specified");
        }
        if (investment.getAmount() == null || investment.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            errors.add("Investment amount must be positive");
        }

        if (!errors.isEmpty()) {
            throw new EntityValidationException(errors);
        }

        try {
            // validate investment sum doesn't exceed investor account
            investorService.validateInvestorHasEnoughFunds(investment.getInvestor(), investment.getAmount());
        } catch (InsufficientAccountException exception) {
            errors.add(exception.getMessage());
        }

        if (LoanStatus.REPAYMENT.equals(investment.getLoan().getStatus())) {
            // validate that there is no repayment process started for Loan
            errors.add("Loan is already in 'REPAYMENT' phase. Investments are not available");
        } else if (investment.getAmount().compareTo(investment.getLoan().remainingInvestmentAmount()) > 0) {
            // validate investment is not bigger than Loan target amount
            errors.add(String.format("Investment is bigger than it is required in a Loan. Required amount in a Loan is %s", investment.getLoan().remainingInvestmentAmount()));
        }

        if (!errors.isEmpty()) {
            throw new EntityValidationException(errors);
        }

    }
}
