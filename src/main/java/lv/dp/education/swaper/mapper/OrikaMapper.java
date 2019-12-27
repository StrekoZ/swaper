package lv.dp.education.swaper.mapper;

import lv.dp.education.swaper.entities.InvestmentEntity;
import lv.dp.education.swaper.entities.InvestorEntity;
import lv.dp.education.swaper.entities.LoanEntity;
import lv.dp.education.swaper.rest.model.*;
import lv.dp.education.swaper.service.LoanService;
import ma.glasnost.orika.CustomMapper;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.MappingContext;
import ma.glasnost.orika.impl.DefaultMapperFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class OrikaMapper {
    private MapperFactory mapperFactory;

    @Autowired
    private LoanService loanService;

    public <T> T map(Object object, Class<T> targetClass) {
        return mapperFactory.getMapperFacade().map(object, targetClass);
    }

    @PostConstruct
    public void init() {
        mapperFactory = new DefaultMapperFactory.Builder().build();

        mapperFactory.classMap(LoanEntity.class, LoanRestGetModel.class)
                .byDefault()
                .customize(new CustomMapper<LoanEntity, LoanRestGetModel>() {
                    @Override
                    public void mapAtoB(LoanEntity loanEntity, LoanRestGetModel loanRestGetModel, MappingContext context) {
                        // deafult field mapping
                        super.mapAtoB(loanEntity, loanRestGetModel, context);
                        // calculate sum of investments
                        loanRestGetModel.setInvestedAmount(loanEntity.totalInvestmentAmount());
                        loanRestGetModel.setRemainingRepaymentAmount(loanEntity.remainingRepaymentAmount());
                    }
                })
                .register();

        mapperFactory.classMap(LoanRestPutModel.class, LoanEntity.class)
                .byDefault()
                .register();

        mapperFactory.classMap(InvestmentEntity.class, InvestmentRestGetModel.class)
                .byDefault()
                .customize(new CustomMapper<InvestmentEntity, InvestmentRestGetModel>() {
                    @Override
                    public void mapAtoB(InvestmentEntity investmentEntity, InvestmentRestGetModel investmentRestGetModel, MappingContext context) {
                        super.mapAtoB(investmentEntity, investmentRestGetModel, context);
                        investmentRestGetModel.setLoanUuid(investmentEntity.getLoan().getUuid());
                    }
                })
                .register();


        mapperFactory.classMap(InvestmentRestPutModel.class, InvestmentEntity.class)
                .byDefault()
                .customize(new CustomMapper<InvestmentRestPutModel, InvestmentEntity>() {
                    @Override
                    public void mapAtoB(InvestmentRestPutModel investmentRestPutModel, InvestmentEntity investmentEntity, MappingContext context) {
                        super.mapAtoB(investmentRestPutModel, investmentEntity, context);
                        if (investmentRestPutModel.getLoanUuid() != null) {
                            investmentEntity.setLoan(loanService.getLoanByUuid(investmentRestPutModel.getLoanUuid()));
                        }
                    }
                })
                .register();


        mapperFactory.classMap(InvestorEntity.class, InvestorRestGetModel.class)
                .byDefault()
                .register();
    }
}