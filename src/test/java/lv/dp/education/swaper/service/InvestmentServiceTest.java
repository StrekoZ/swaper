package lv.dp.education.swaper.service;

import lv.dp.education.swaper.entities.InvestmentEntity;
import lv.dp.education.swaper.entities.InvestorEntity;
import lv.dp.education.swaper.entities.LoanEntity;
import lv.dp.education.swaper.repository.InvestmentRepository;
import lv.dp.education.swaper.service.exception.EntityValidationException;
import lv.dp.education.swaper.service.exception.InsufficientAccountException;
import lv.dp.education.swaper.service.exception.ServiceException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;

import java.math.BigDecimal;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsIterableContaining.hasItems;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyObject;
import static org.mockito.Mockito.*;


@RunWith(MockitoJUnitRunner.class)
public class InvestmentServiceTest {

    @Mock
    private InvestmentRepository investmentRepository;
    @Spy
    private InvestorService investorService;
    @InjectMocks
    private InvestmentService investmentService;

    @Before
    public void before() throws InsufficientAccountException {
        doNothing().when(investorService).reduceInvestorAccount(any(), any());
    }

    @Test
    public void invest_NULL() throws InsufficientAccountException {
        // validate behaviour on NULL object
        EntityValidationException exception = assertThrows(EntityValidationException.class, () -> investmentService.invest(null));
        assertEquals(1, exception.getErrors().size());

        verify(investorService, times(0)).reduceInvestorAccount(anyObject(), anyObject());
        verify(investmentRepository, times(0)).save(anyObject());
        verify(investmentRepository, times(0)).saveAndFlush(anyObject());
    }

    @Test
    public void invest_incorrectFields() throws InsufficientAccountException {
        InvestmentEntity investment = new InvestmentEntity();
        investment.setAmount(new BigDecimal(-1));

        EntityValidationException exception = assertThrows(EntityValidationException.class, () -> investmentService.invest(investment));
        assertEquals(3, exception.getErrors().size());

        verify(investorService, times(0)).reduceInvestorAccount(anyObject(), anyObject());
        verify(investmentRepository, times(0)).save(anyObject());
        verify(investmentRepository, times(0)).saveAndFlush(anyObject());
    }

    @Test
    public void invest_insufficientFunds() throws InsufficientAccountException {
        EntityValidationException exception = assertThrows(EntityValidationException.class, () -> investmentService.invest(
                InvestmentEntity.builder()
                        .amount(new BigDecimal(10))
                        .loan(LoanEntity.builder().targetAmount(new BigDecimal(100)).build())
                        .investor(InvestorEntity.builder().account(new BigDecimal(5)).build())
                        .build()
        ));
        assertThat(exception.getErrors(), hasItems(new InsufficientAccountException(new BigDecimal(5)).getMessage()));

        verify(investorService, times(0)).reduceInvestorAccount(anyObject(), anyObject());
        verify(investmentRepository, times(0)).save(anyObject());
        verify(investmentRepository, times(0)).saveAndFlush(anyObject());
    }

    @Test
    public void invest_investmentIsTooBig() throws InsufficientAccountException {
        EntityValidationException exception = assertThrows(EntityValidationException.class, () -> investmentService.invest(
                InvestmentEntity.builder()
                        .amount(new BigDecimal(10))
                        .loan(LoanEntity.builder().targetAmount(new BigDecimal(5)).build())
                        .investor(InvestorEntity.builder().account(new BigDecimal(100)).build())
                        .build()
        ));
        assertThat(exception.getErrors(), hasItems("Investment is bigger than it is required in a Loan. Required amount in a Loan is 5"));

        verify(investorService, times(0)).reduceInvestorAccount(anyObject(), anyObject());
        verify(investmentRepository, times(0)).save(anyObject());
        verify(investmentRepository, times(0)).saveAndFlush(anyObject());
    }

    @Test
    public void invest_OK() throws ServiceException {
        investmentService.invest(
                InvestmentEntity.builder()
                        .amount(new BigDecimal(10))
                        .loan(LoanEntity.builder().targetAmount(new BigDecimal(15)).build())
                        .investor(InvestorEntity.builder().account(new BigDecimal(100)).build())
                        .build()
        );

        verify(investorService, times(1)).reduceInvestorAccount(anyObject(), anyObject());
        verify(investmentRepository, times(1)).save(anyObject());
        verify(investmentRepository, times(0)).saveAndFlush(anyObject());
    }
}