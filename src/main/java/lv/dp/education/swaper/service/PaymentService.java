package lv.dp.education.swaper.service;

import lv.dp.education.swaper.entities.LoanEntity;
import lv.dp.education.swaper.entities.LoanStatus;
import lv.dp.education.swaper.entities.PaymentEntity;
import lv.dp.education.swaper.repository.PaymentRepository;
import lv.dp.education.swaper.service.exception.LoanNotFoundException;
import lv.dp.education.swaper.service.exception.RepaymentIsTooBigException;
import lv.dp.education.swaper.service.exception.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;
import java.util.UUID;

@Service
public class PaymentService {

    @Autowired
    private LoanService loanService;
    @Autowired
    private InvestorService investorService;

    @Autowired
    private PaymentRepository paymentRepository;

    public void makePayment(UUID loanUuid, BigDecimal payment) throws ServiceException {
        LoanEntity loan = loanService.getLoanByUuid(loanUuid);
        if (loan == null) {
            throw new LoanNotFoundException(loanUuid);
        }
        if (loan.remainingRepaymentAmount().compareTo(payment) < 0) {
            throw new RepaymentIsTooBigException(loan, payment);
        }
        loan.setStatus(LoanStatus.REPAYMENT);

        addPaymentToLoan(loan, payment);
        addFundsToInvestors(loan, payment);
    }

    protected void addFundsToInvestors(LoanEntity loan, BigDecimal payment) {
        BigDecimal totalInvestmentAmount = loan.totalInvestmentAmount(); // saving to local variable to avoid duplicating calculations
        loan.getInvestments().forEach(investment -> {
            investorService.addFundsToAccount(
                    investment.getInvestor(),
                    investment.getAmount().divide(totalInvestmentAmount).multiply(payment).setScale(2, RoundingMode.HALF_UP)
            );
        });
    }

    protected void addPaymentToLoan(LoanEntity loan, BigDecimal payment) {
        paymentRepository.save(PaymentEntity.builder()
                .loan(loan)
                .amount(payment)
                .date(new Date())
                .build()
        );
    }
}
