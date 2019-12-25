package lv.dp.education.swaper.mapper;

import ma.glasnost.orika.MapperFacade;

public class ModelConverter {

    private static final MapperFacade mapper = OrikaMapperFactory.getMapperFacade();

    public static <T> T map(Object object, Class<T> targetClass) {
        return mapper.map(object, targetClass);
    }
}