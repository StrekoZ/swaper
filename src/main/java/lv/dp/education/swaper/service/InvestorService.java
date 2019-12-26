package lv.dp.education.swaper.service;

import lv.dp.education.swaper.entities.InvestorEntity;
import lv.dp.education.swaper.repository.InvestorRepository;
import lv.dp.education.swaper.service.exception.InsufficientAccountException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;


@Service
@Transactional
public class InvestorService {

    @Autowired
    private InvestorRepository repository;

    public InvestorEntity findInvestorByUsername(String name) {
        return repository.findByUsername(name);
    }

    public void reduceInvestorAccount(InvestorEntity investor, BigDecimal amount) throws InsufficientAccountException {
        validateInvestorHasEnoughFunds(investor, amount);
        investor.setAccount(investor.getAccount().subtract(amount));
    }

    public void validateInvestorHasEnoughFunds(InvestorEntity investor, BigDecimal amount) throws InsufficientAccountException {
        if (investor.getAccount().compareTo(amount) < 0) {
            throw new InsufficientAccountException(investor.getAccount());
        }
    }

    public void addFundsToAccount(InvestorEntity investor, BigDecimal amount) {
        investor.setAccount(investor.getAccount().add(amount));
        repository.save(investor);
    }

    public void createInvestor(InvestorEntity investor) {
        // todo validation of input parameters
        repository.save(investor);
    }
}
