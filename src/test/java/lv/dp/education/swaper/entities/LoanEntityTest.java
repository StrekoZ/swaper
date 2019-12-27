package lv.dp.education.swaper.entities;

import com.google.common.collect.Sets;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.Assert.assertEquals;

public class LoanEntityTest {

    private LoanEntity loan;

    @Before
    public void setupLoan() {
        loan = LoanEntity.builder()
                .targetAmount(new BigDecimal(100))
                .interestPercent(BigDecimal.TEN)
                .investments(Sets.newHashSet(
                        new InvestmentEntity(
                                UUID.randomUUID(),
                                BigDecimal.TEN,
                                null,
                                null,
                                null
                        ),
                        new InvestmentEntity(
                                UUID.randomUUID(),
                                new BigDecimal(5),
                                null,
                                null,
                                null
                        )

                ))
                .payments(Sets.newHashSet(
                        PaymentEntity.builder().amount(BigDecimal.TEN).build()
                ))
                .build();
    }

    @Test
    public void totalInvestmentAmount() {
        assertEquals(new BigDecimal(15), loan.totalInvestmentAmount());
    }

    @Test
    public void remainingInvestmentAmount() {
        assertEquals(new BigDecimal(85), loan.remainingInvestmentAmount());
    }

    @Test
    public void totalRepaymentAmount() {
        assertEquals(new BigDecimal("16.50"), loan.totalRepaymentAmount());
    }

    @Test
    public void remainingRepaymentAmount() {
        assertEquals(new BigDecimal("6.50"), loan.remainingRepaymentAmount());
    }
}