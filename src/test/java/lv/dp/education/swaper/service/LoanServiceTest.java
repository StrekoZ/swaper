package lv.dp.education.swaper.service;


import lv.dp.education.swaper.entities.LoanEntity;
import lv.dp.education.swaper.repository.LoanRepository;
import lv.dp.education.swaper.service.exception.EntityValidationException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.math.BigDecimal;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsIterableContaining.hasItems;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyObject;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class LoanServiceTest {

    @Mock
    private LoanRepository loanRepository;
    @InjectMocks
    private LoanService loanService;

    @Test
    public void createLoan_NULL() {
        // validate behaviour on NULL object
        EntityValidationException exception = assertThrows(EntityValidationException.class, () -> loanService.createLoan(null));
        assertEquals(1, exception.getErrors().size());

        verify(loanRepository, times(0)).save(anyObject());
        verify(loanRepository, times(0)).saveAndFlush(anyObject());
    }

    @Test
    public void createLoan_incorrect_fields() {
        // validate behaviour when incorrect Loan fields are passed
        LoanEntity entity = new LoanEntity();
        entity.setDescription(null);
        entity.setTargetAmount(new BigDecimal(-1));
        entity.setInterestPercent(null);

        EntityValidationException exception = assertThrows(EntityValidationException.class, () -> loanService.createLoan(entity));
        assertEquals(3, exception.getErrors().size());
        assertThat(exception.getErrors(), hasItems("Loan Description is not provided"));
        assertThat(exception.getErrors(), hasItems("Loan Target Amount must be positive"));
        assertThat(exception.getErrors(), hasItems("Loan Interest Percent must be positive"));

        verify(loanRepository, times(0)).save(anyObject());
        verify(loanRepository, times(0)).saveAndFlush(anyObject());
    }

    @Test
    public void createLoan_OK() throws EntityValidationException {
        // validate behaviour when incorrect Loan fields are passed
        LoanEntity entity = new LoanEntity();
        entity.setDescription("Test Loan");
        entity.setTargetAmount(new BigDecimal(100));
        entity.setInterestPercent(new BigDecimal(10));

        loanService.createLoan(entity);

        verify(loanRepository, times(1)).save(anyObject());
        verify(loanRepository, times(0)).saveAndFlush(anyObject());
    }
}