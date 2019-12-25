package lv.dp.education.swaper.mapper;

import lv.dp.education.swaper.model.LoanApplication;
import lv.dp.education.swaper.rest.model.LoanApplicationRegistrationRestAdminModel;
import lv.dp.education.swaper.rest.model.LoanApplicationRestAdminModel;
import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.impl.DefaultMapperFactory;

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
        // add custom transformation if any. Currently fields are matching
        this.mapperFactory.classMap(LoanApplication.class, LoanApplicationRestAdminModel.class)
                .byDefault()
                .register();
        this.mapperFactory.classMap(LoanApplication.class, LoanApplicationRegistrationRestAdminModel.class)
                .byDefault()
                .register();

    }

    public static MapperFacade getMapperFacade() {
        return getInstance().mapperFactory.getMapperFacade();
    }
}