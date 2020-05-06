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
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository repository;

    private final AuthenticationManager authenticationManager;
    private final JwtTokenUtil jwtTokenUtil;
    private final JwtUserDetailsService userDetailsService;

    private final PasswordEncryption encryptor;

    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    public CustomUser findUser(String mail) {
        return repository.findByMail(mail).orElseThrow(() -> new UserNotFoundException(mail));
    }

    public String registerUser(RegisterRequest registerRequest) {
        // try to find user - if user cannot be found we get an exception and we can continue - else mail is already registered
        try {
            findUser(registerRequest.getMail());
            throw new UserAlreadyExistsException(registerRequest.getMail());
        } catch (UserNotFoundException ex) {
            // no user found for mail address - create a new one
            CustomUser user = registerRequest.toUser();
            user.setEncodedPassword(encoder.encode(registerRequest.getPassword()));
            repository.save(user);
            authenticate(registerRequest.getMail(), registerRequest.getPassword());
            final UserDetails userDetails = userDetailsService.loadUserByUsername(user.getMail());
            return jwtTokenUtil.generateToken(userDetails);
        }
    }

    public CustomUser addPasswords(String mail, List<PasswordToAdd> passwords) {
        // get the user
        CustomUser user = findUser(mail);
        // get all passwords and add the new ones
        List<Password> existingPasswords = user.getPasswords();
        if (existingPasswords == null) {
            existingPasswords = new ArrayList<>();
        }
        List<Password> passwordsToAdd = new ArrayList<>();
        // loop over the received list and put the new passwords
        for (PasswordToAdd passwordToAdd : passwords) {
            try {
                Password password = passwordToAdd.toPassword();
                password.setPassword(encryptor.encryptPassword(passwordToAdd.getPassword()));

                if (existingPasswords.contains(password)) {
                    log.debug("password already exists - skipping");
                } else {
                    passwordsToAdd.add(password);
                }
            } catch (GeneralSecurityException ex) {
                log.error("Exception occured while encrypting passwordToAdd.", ex);
            }
        }
        // add the passwords to and save the User
        existingPasswords.addAll(passwordsToAdd);
        user.setPasswords(existingPasswords);
        return repository.save(user);
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
            // TODO use better exception than generic runtime
            throw new RuntimeException("Something went wrong");
        }
    }

    public String login(LoginRequest request) {
        // authenticate the user with spring security
        authenticate(request.getMail(), request.getPassword());
        final UserDetails userDetails = userDetailsService.loadUserByUsername(request.getMail());
        return jwtTokenUtil.generateToken(userDetails);
    }

    private void authenticate(String mail, String password) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(mail, password));
    }
}
