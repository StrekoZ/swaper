package lv.dp.education.swaper.rest;

import lv.dp.education.swaper.rest.model.ErrorRestModel;
import lv.dp.education.swaper.service.exception.EntityValidationException;
import lv.dp.education.swaper.service.exception.InsufficientAccountException;
import lv.dp.education.swaper.service.exception.ServiceException;
import lv.dp.education.swaper.service.exception.UserAlreadyExistException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
@RestController
public class CustomizedResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    private Logger logger = LoggerFactory.getLogger(this.getClass());


    /**
     * Method processes exceptions caused by executing REST endpoints and builds a correct ErrorRestModel with HTTP Code
     * */
    @ExceptionHandler(Exception.class)
    // todo refactor method improve readability
    public final ResponseEntity<ErrorRestModel> handleAllExceptions(Exception e, WebRequest request) {
        logger.error("Exception while executing request", e);

        ErrorRestModel errorModel;
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        if (e instanceof ServiceException) {
            errorModel = new ErrorRestModel(e.getMessage(), null);
            if (e instanceof EntityValidationException) {
                status = HttpStatus.BAD_REQUEST;
                errorModel.setErrorDetails(((EntityValidationException) e).getErrors());
            } else if (e instanceof UserAlreadyExistException) {
                status = HttpStatus.BAD_REQUEST;
            } else if (e instanceof InsufficientAccountException) {
                status = HttpStatus.BAD_REQUEST;
            }
        } else if (e instanceof AccessDeniedException) {
            if (request.getUserPrincipal() == null) {
                status = HttpStatus.UNAUTHORIZED;
                errorModel = new ErrorRestModel("You must authenticate first (please use '/auth/login' endpoint)", null);
            } else {
                status = HttpStatus.FORBIDDEN;
                errorModel = new ErrorRestModel("You don't have necessary access", null);
            }
        } else {
            errorModel = new ErrorRestModel("Unrecognized application exception: " + e.getMessage(), null);
        }
        return new ResponseEntity<>(errorModel, status);
    }
}