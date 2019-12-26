package lv.dp.education.swaper.mapper;

import com.google.common.collect.Sets;
import lv.dp.education.swaper.model.InvestmentEntity;
import lv.dp.education.swaper.model.LoanEntity;
import lv.dp.education.swaper.rest.model.LoanRestGetModel;
import lv.dp.education.swaper.rest.model.LoanRestPutModel;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class ModelConverterTest {

    @Test
    public void testMapper_LoanEntity_to_LoanRestGetModel() {
        LoanRestGetModel restGetModel = ModelConverter.map(
                new LoanEntity(
                        UUID.fromString("04c185c0-ea5b-4e9a-b310-6fd7651f49da"),
                        new BigDecimal("100"),
                        new BigDecimal("11.5"),
                        "Test Loan",
                        Sets.newHashSet(
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
                ),
                LoanRestGetModel.class);

        assertEquals(UUID.fromString("04c185c0-ea5b-4e9a-b310-6fd7651f49da"), restGetModel.getUuid());
        assertEquals(new BigDecimal("100"), restGetModel.getTargetAmount());
        assertEquals(new BigDecimal("11.5"), restGetModel.getInterestPercent());
        assertEquals("Test Loan", restGetModel.getDescription());
        assertEquals(new BigDecimal(15), restGetModel.getInvestedAmount());
    }

    @Test
    public void testMapper_LoanPutRestModel_to_LoanEntity() {
        LoanEntity entity = ModelConverter.map(new LoanRestPutModel(
                new BigDecimal(100),
                new BigDecimal(11),
                "new Test Loan"
        ), LoanEntity.class);

        assertEquals(new BigDecimal(100), entity.getTargetAmount());
        assertEquals(new BigDecimal(11), entity.getInterestPercent());
        assertEquals("new Test Loan", entity.getDescription());
        assertNull(entity.getUuid());
        assertNull(entity.getInvestments());
    }
}