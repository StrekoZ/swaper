/*
 * @(#)UserAlreadyExistException.java
 *
 * Copyright Swiss Reinsurance Company, Mythenquai 50/60, CH 8022 Zurich. All rights reserved.
 */
package lv.dp.education.swaper.service.exception;

public class UserAlreadyExistException extends ServiceException {
    public UserAlreadyExistException(String username) {
        super(String.format("User '%s' already exists", username));
    }
}
