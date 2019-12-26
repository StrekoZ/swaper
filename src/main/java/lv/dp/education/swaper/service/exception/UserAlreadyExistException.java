package lv.dp.education.swaper.service.exception;

public class UserAlreadyExistException extends ServiceException {
    public UserAlreadyExistException(String username) {
        super(String.format("User '%s' already exists", username));
    }
}
