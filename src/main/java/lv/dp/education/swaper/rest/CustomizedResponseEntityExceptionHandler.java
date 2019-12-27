package lv.dp.education.swaper.rest;

import lv.dp.education.swaper.rest.model.ErrorRestModel;
import lv.dp.education.swaper.service.exception.*;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import static org.springframework.http.HttpStatus.*;

@ControllerAdvice
@RestController
public class CustomizedResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    /**
     * Method processes exceptions caused by executing REST endpoints and builds a correct ErrorRestModel with HTTP Code
     */
    @ExceptionHandler(Exception.class)
    public final ResponseEntity<ErrorRestModel> handleAllExceptions(Exception e, WebRequest request) {
        logger.error("Exception while executing request", e);

        if (e instanceof ServiceException) {
            handleServiceExceptions((ServiceException) e, request);
        } else if (e instanceof AccessDeniedException) {
            handleAccessExceptions((AccessDeniedException) e, request);
        }

        return new ResponseEntity<>(
                new ErrorRestModel("Unrecognized application exception: " + e.getMessage(), null),
                INTERNAL_SERVER_ERROR);
    }


    private final ResponseEntity<ErrorRestModel> handleServiceExceptions(ServiceException e, WebRequest request) {
        if (e instanceof EntityValidationException) {
            return new ResponseEntity<>(
                    new ErrorRestModel(e.getMessage(), ((EntityValidationException) e).getErrors()),
                    BAD_REQUEST);
        } else if (e instanceof UserAlreadyExistException) {
            return new ResponseEntity<>(
                    new ErrorRestModel(e.getMessage(), null),
                    BAD_REQUEST);
        } else if (e instanceof InsufficientAccountException) {
            return new ResponseEntity<>(
                    new ErrorRestModel(e.getMessage(), null),
                    BAD_REQUEST);
        } else if (e instanceof RepaymentIsTooBigException) {
            return new ResponseEntity<>(
                    new ErrorRestModel(e.getMessage(), null),
                    BAD_REQUEST);
        } else if (e instanceof LoanNotFoundException) {
            return new ResponseEntity<>(
                    new ErrorRestModel(e.getMessage(), null),
                    NOT_FOUND);
        }

        return new ResponseEntity<>(
                new ErrorRestModel(e.getMessage(), null),
                INTERNAL_SERVER_ERROR);

    }

    private final ResponseEntity<ErrorRestModel> handleAccessExceptions(AccessDeniedException e, WebRequest request) {
        if (request.getUserPrincipal() == null) {
            return new ResponseEntity<>(
                    new ErrorRestModel("You must authenticate first (please use '/auth/login' endpoint)", null),
                    UNAUTHORIZED);
        } else {
            return new ResponseEntity<>(
                    new ErrorRestModel("You don't have necessary access", null),
                    FORBIDDEN);
        }
    }
}