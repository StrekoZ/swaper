package lv.dp.education.swaper.mapper;

import com.google.common.collect.Sets;
import lv.dp.education.swaper.entities.InvestmentEntity;
import lv.dp.education.swaper.entities.LoanEntity;
import lv.dp.education.swaper.rest.model.InvestmentRestGetModel;
import lv.dp.education.swaper.rest.model.InvestmentRestPutModel;
import lv.dp.education.swaper.rest.model.LoanRestGetModel;
import lv.dp.education.swaper.rest.model.LoanRestPutModel;
import lv.dp.education.swaper.service.LoanService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class OrikaMapperTest {
    @Mock
    private LoanService loanService;
    @InjectMocks
    private OrikaMapper orikaMapper;

    @Before
    public void before() {
        orikaMapper.init();
    }

    @Test
    public void testMapper_LoanEntity_to_LoanRestGetModel() {
        LoanRestGetModel restGetModel = orikaMapper.map(
                new LoanEntity().builder()
                        .uuid(UUID.fromString("12345678-1234-1234-1234-123456789abc"))
                        .targetAmount(new BigDecimal("100"))
                        .interestPercent(new BigDecimal("11.5"))
                        .description("Test Loan")
                        .investments(Sets.newHashSet(
                                new InvestmentEntity(
                                        UUID.randomUUID(),
                                        new BigDecimal(10),
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
                                )

                        ).build(),
                LoanRestGetModel.class);

        assertEquals(UUID.fromString("12345678-1234-1234-1234-123456789abc"), restGetModel.getUuid());
        assertEquals(new BigDecimal("100"), restGetModel.getTargetAmount());
        assertEquals(new BigDecimal("11.5"), restGetModel.getInterestPercent());
        assertEquals("Test Loan", restGetModel.getDescription());
        assertEquals(new BigDecimal(15), restGetModel.getInvestedAmount());
        assertEquals(new BigDecimal(15), restGetModel.getRemainingRepaymentAmount());
    }

    @Test
    public void testMapper_LoanPutRestModel_to_LoanEntity() {
        LoanEntity entity = orikaMapper.map(LoanRestPutModel.builder()
                .targetAmount(new BigDecimal(100))
                .interestPercent(new BigDecimal(11))
                .description("new Test Loan").build(), LoanEntity.class);

        assertEquals(new BigDecimal(100), entity.getTargetAmount());
        assertEquals(new BigDecimal(11), entity.getInterestPercent());
        assertEquals("new Test Loan", entity.getDescription());
        assertNull(entity.getUuid());
        assertNull(entity.getInvestments());
    }

    @Test
    public void testMapper_InvestmentEntity_to_InvestmentRestGetModel() {
        Date date = new Date();

        InvestmentRestGetModel model = orikaMapper.map(InvestmentEntity.builder()
                .uuid(UUID.fromString("12345678-1234-1234-1234-123456789abc"))
                .amount(BigDecimal.TEN)
                .date(date)
                .loan(LoanEntity.builder().uuid(UUID.fromString("12345678-aaaa-bbbb-cccc-123456789abc")).build())
                .build(), InvestmentRestGetModel.class);

        assertEquals(UUID.fromString("12345678-1234-1234-1234-123456789abc"), model.getUuid());
        assertEquals(BigDecimal.TEN, model.getAmount());
        assertEquals(date, model.getDate());
        assertEquals(UUID.fromString("12345678-aaaa-bbbb-cccc-123456789abc"), model.getLoanUuid());

    }

    @Test
    public void testMapper_InvestmentRestPutGetModel_to_InvestmentEntity() {
        when(loanService.getLoanByUuid(any(UUID.class))).thenAnswer(invocationOnMock -> LoanEntity.builder().uuid(invocationOnMock.getArgument(0)).build());

        InvestmentEntity entity = orikaMapper.map(InvestmentRestPutModel.builder()
                        .amount(new BigDecimal(10))
                        .loanUuid(UUID.fromString("12345678-1234-1234-1234-123456789abc"))
                        .build(),
                InvestmentEntity.class);

        assertEquals(new BigDecimal(10), entity.getAmount());
        assertEquals(UUID.fromString("12345678-1234-1234-1234-123456789abc"), entity.getLoan().getUuid());
    }
}