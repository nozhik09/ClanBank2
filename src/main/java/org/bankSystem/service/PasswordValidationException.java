/*
 * created by max$
 */


package service;

public class PasswordValidationException extends Exception{
    public PasswordValidationException(String message) {
        super(message);
    }

    @Override
    public String getMessage() {
        return "Password validate exception: " + super.getMessage();
    }
}
