package nl.rcomanne.passwordmanager.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nl.rcomanne.passwordmanager.domain.User;
import nl.rcomanne.passwordmanager.exception.AlreadyExistsException;
import nl.rcomanne.passwordmanager.exception.UserAlreadyExistsException;
import nl.rcomanne.passwordmanager.exception.UserNotFoundException;
import nl.rcomanne.passwordmanager.repository.UserRepository;
import nl.rcomanne.passwordmanager.web.domain.LoginRequest;
import nl.rcomanne.passwordmanager.web.domain.RegisterRequest;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository repository;

    public User findUser(String mail) {
        return repository.findByMail(mail).orElseThrow(() -> new UserNotFoundException(mail));
    }

    public User registerUser(RegisterRequest registerRequest) {
        // try to find user - if user cannot be found we get an exception and we can continue - else mail is already registered
        try {
            findUser(registerRequest.getMail());
            throw new UserAlreadyExistsException(registerRequest.getMail());
        } catch (UserNotFoundException ex) {
            // no user found for mail address - create a new one
            User user = registerRequest.toUser();
            return repository.save(user);
        }
    }

    public User login(LoginRequest loginRequest) {
        // try to find user - if exception occurs user does not exist and exception will be handled by controller advice
        User user = findUser(loginRequest.getMail());
        if (loginRequest.getPassword().equals(user.getEncodedPassword())) {
            // passwords match - login succeeded
            return user;
        } else {
            // passwords do NOT match - throw exception
            throw new BadCredentialsException("Invalid mail and/or password");
        }
    }

}
