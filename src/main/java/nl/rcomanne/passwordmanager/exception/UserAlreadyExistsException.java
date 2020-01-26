package nl.rcomanne.passwordmanager.exception;

public class UserAlreadyExistsException extends AlreadyExistsException {
    public UserAlreadyExistsException(String mail) {
        super(String.format("An user with mail address %s already exists", mail));
    }
}
