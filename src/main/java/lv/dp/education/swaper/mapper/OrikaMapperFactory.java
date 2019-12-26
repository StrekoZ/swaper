package lv.dp.education.swaper.mapper;

import lv.dp.education.swaper.model.InvestmentEntity;
import lv.dp.education.swaper.model.LoanEntity;
import lv.dp.education.swaper.rest.model.LoanRestGetModel;
import lv.dp.education.swaper.rest.model.LoanRestPutModel;
import ma.glasnost.orika.CustomMapper;
import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.MappingContext;
import ma.glasnost.orika.impl.DefaultMapperFactory;

import java.math.BigDecimal;

public class OrikaMapperFactory {

    private static OrikaMapperFactory MAPPER_FACTORY;

    final MapperFactory mapperFactory;

    public synchronized static OrikaMapperFactory getInstance() {
        if (MAPPER_FACTORY == null) {
            MAPPER_FACTORY = new OrikaMapperFactory();
        }
        return MAPPER_FACTORY;
    }

    private OrikaMapperFactory() {
        this.mapperFactory = new DefaultMapperFactory.Builder().build();
        registerBlueprintModelConverter();
    }

    private void registerBlueprintModelConverter() {
        this.mapperFactory.classMap(LoanEntity.class, LoanRestGetModel.class)
                .byDefault()
                .customize(new CustomMapper<LoanEntity, LoanRestGetModel>() {
                    @Override
                    public void mapAtoB(LoanEntity loanEntity, LoanRestGetModel loanRestGetModel, MappingContext context) {
                        // deafult field mapping
                        super.mapAtoB(loanEntity, loanRestGetModel, context);
                        // calculate sum of investments
                        loanRestGetModel.setInvestedAmount(
                                loanEntity.getInvestments().stream()
                                        .map(InvestmentEntity::getAmount)
                                        .reduce(BigDecimal.ZERO, BigDecimal::add)
                        );
                    }
                })
                .register();

        this.mapperFactory.classMap(LoanRestPutModel.class, LoanEntity.class)
                .byDefault()
                .register();
    }

    public static MapperFacade getMapperFacade() {
        return getInstance().mapperFactory.getMapperFacade();
    }
}