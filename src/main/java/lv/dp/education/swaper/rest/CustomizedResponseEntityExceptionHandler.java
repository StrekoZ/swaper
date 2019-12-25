package lv.dp.education.swaper.rest;

import lv.dp.education.swaper.service.exception.ServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
@RestController
public class CustomizedResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @ExceptionHandler(Exception.class)
    public final ResponseEntity<String> handleAllExceptions(Exception e, WebRequest request) {
        logger.error("Exception while executing request", e);
        String errorMessage;
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        if (e instanceof ServiceException) {
            errorMessage = e.getMessage();
        } else {
            errorMessage = "Unrecognized application exception: " + e.getMessage();
        }
        return new ResponseEntity<>(errorMessage, status);
    }
}