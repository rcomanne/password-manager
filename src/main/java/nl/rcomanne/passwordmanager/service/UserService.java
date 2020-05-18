package nl.rcomanne.passwordmanager.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nl.rcomanne.passwordmanager.domain.CustomUser;
import nl.rcomanne.passwordmanager.domain.Password;
import nl.rcomanne.passwordmanager.exception.PasswordNotFoundException;
import nl.rcomanne.passwordmanager.exception.UserAlreadyExistsException;
import nl.rcomanne.passwordmanager.exception.UserNotFoundException;
import nl.rcomanne.passwordmanager.repository.UserRepository;
import nl.rcomanne.passwordmanager.security.jwt.JwtTokenUtil;
import nl.rcomanne.passwordmanager.security.jwt.JwtUserDetailsService;
import nl.rcomanne.passwordmanager.security.passwords.PasswordEncryption;
import nl.rcomanne.passwordmanager.web.domain.LoginRequest;
import nl.rcomanne.passwordmanager.web.domain.PasswordToAdd;
import nl.rcomanne.passwordmanager.web.domain.RegisterRequest;
import org.hibernate.cfg.NotYetImplementedException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository repository;

    private final MailService mailService;

    private final AuthenticationManager authenticationManager;
    private final JwtTokenUtil jwtTokenUtil;
    private final JwtUserDetailsService userDetailsService;

    private final PasswordEncryption encryptor;

    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    private CustomUser findUser(String mail) {
        return repository.findByMail(mail).orElseThrow(() -> new UserNotFoundException(mail));
    }

    public void registerUser(RegisterRequest registerRequest) {
        // try to find user - if user cannot be found we get an exception and we can continue - else mail is already registered
        try {
            findUser(registerRequest.getMail());
            throw new UserAlreadyExistsException(registerRequest.getMail());
        } catch (UserNotFoundException ex) {
            // no user found for mail address - create a new one
            CustomUser user = registerRequest.toUser();
            user.setEncodedPassword(encoder.encode(registerRequest.getPassword()));
            repository.save(user);

            mailService.sendActivationMail(user);
        }
    }

    public boolean activateUser(String activationToken) {
        Optional<CustomUser> optionalUser = repository.findByActivationToken(activationToken);

        if (optionalUser.isEmpty()) {
            return false;
        } else {
            CustomUser user = optionalUser.get();
            user.setActivated(true);
            repository.save(user);
            return true;
        }
    }

    public void resetPassword(String mail) {
        throw new NotYetImplementedException("Resetting password is not yet implemented");
    }

    @Transactional
    public void addPasswords(String mail, List<PasswordToAdd> newPasswords) {
        // get the user
        CustomUser user = findUser(mail);
        // get all passwords and add the new ones
        List<Password> passwords = user.getPasswords();
        if (passwords == null) {
            passwords = new ArrayList<>();
        }

        // loop over the received list and put the new passwords
        for (PasswordToAdd passwordToAdd : newPasswords) {
            try {
                // convert the object to a Password object and encrypt the password itself
                Password password = passwordToAdd.toPassword();
                password.setPassword(encryptor.encryptPassword(passwordToAdd.getPassword()));

                // remove existing password if it is for the same domain and username
                passwords.removeIf(currentPassword -> currentPassword.getDomain().equals(password.getDomain()) && currentPassword.getName().equals(password.getName()));

                // add the password to the list
                passwords.add(password);
            } catch (GeneralSecurityException ex) {
                log.error("Exception occured while encrypting passwordToAdd.", ex);
            }
        }
    }

    public List<Password> getAllPasswords(String username) {
        CustomUser user = findUser(username);
        List<Password> passwords = user.getPasswords();

        if (passwords == null) {
            return new ArrayList<>();
        }

        return passwords;
    }

    public List<Password> getAllPasswordsCleaned(String username) {
        List<Password> passwords = getAllPasswords(username);

        passwords.forEach(password -> password.setPassword("your_password_is_in_another_castle"));

        return passwords;
    }

    public Password getPassword(String mail, Long id) {
        // get the user
        CustomUser user = findUser(mail);
        try {
            // get the specific password object
            Password password = user.getPasswords().stream().filter(pw -> id.equals(pw.getId())).findFirst().orElseThrow(() -> new PasswordNotFoundException(id, mail));
            password.setPassword(encryptor.decryptPassword(password.getPassword()));
            return password;
        } catch (GeneralSecurityException ex) {
            log.error("Exception occurred while decrypting password", ex);
            throw new IllegalStateException("Something went wrong");
        }
    }

    public String login(LoginRequest request) {
        CustomUser user = findUser(request.getMail());
        if (user.isActivated()) {
            // authenticate the user with spring security
            authenticate(request.getMail(), request.getPassword());
            final UserDetails userDetails = userDetailsService.loadUserByUsername(request.getMail());
            return jwtTokenUtil.generateToken(userDetails);
        } else {
            throw new IllegalArgumentException("account has not yet been activated");
        }
    }

    private void authenticate(String mail, String password) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(mail, password));
    }
}
