package nl.rcomanne.passwordmanager.security.jwt;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nl.rcomanne.passwordmanager.domain.CustomUser;
import nl.rcomanne.passwordmanager.exception.UserNotFoundException;
import nl.rcomanne.passwordmanager.repository.UserRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Slf4j
@Service
@RequiredArgsConstructor
public class JwtUserDetailsService implements UserDetailsService {

    private final UserRepository repository;

    @Override
    public UserDetails loadUserByUsername(String mail) {
        CustomUser user = findUser(mail);
        if (user != null) {
            return new User(user.getMail(), user.getEncodedPassword(), Collections.singletonList(new SimpleGrantedAuthority("USER")));
        } else {
            throw new UsernameNotFoundException("User not found for mail: " + mail);
        }

    }

    public CustomUser findUser(String mail) {
        return repository.findByMail(mail).orElseThrow(() -> new UserNotFoundException(mail));
    }
}
