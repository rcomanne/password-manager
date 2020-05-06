package nl.rcomanne.passwordmanager.exception;

public class PasswordNotFoundException extends NotFoundException {
    public PasswordNotFoundException() {
        super("Password not found");
    }

    public PasswordNotFoundException(Long id, String mail) {
        super(String.format("Password with id %d not found for user %s", id, mail));
    }
}
