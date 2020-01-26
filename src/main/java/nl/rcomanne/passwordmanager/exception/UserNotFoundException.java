package nl.rcomanne.passwordmanager.exception;

public class UserNotFoundException extends NotFoundException {
    public UserNotFoundException() {
        super("User not found");
    }

    public UserNotFoundException(String mail) {
        super(String.format("User with mail %s not found", mail));
    }
}
