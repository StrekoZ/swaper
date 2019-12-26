package lv.dp.education.swaper.service.exception;

import lombok.Getter;

import java.util.List;


@Getter
public class EntityValidationException extends ServiceException {
    private List<String> errors;

    public EntityValidationException(List<String> errors) {
        super("Entity fields are incorrect");
        this.errors = errors;
    }
}
