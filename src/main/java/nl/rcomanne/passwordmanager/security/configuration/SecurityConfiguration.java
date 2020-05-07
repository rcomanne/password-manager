package nl.rcomanne.passwordmanager.security.configuration;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nl.rcomanne.passwordmanager.security.jwt.JwtAuthenticationEntryPoint;
import nl.rcomanne.passwordmanager.security.jwt.JwtRequestFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Slf4j
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    private final JwtAuthenticationEntryPoint authenticationEntryPoint;
    private final UserDetailsService userDetailsService;
    private final JwtRequestFilter requestFilter;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                // Ignore auth for these entry points
                .authorizeRequests()
                .antMatchers("/user/register", "/user/login", "/user/activate").permitAll()
                .antMatchers("/pw/generate/**").permitAll()
                // Ensure every other entry point is authenticated
                .anyRequest().authenticated()
                .and()
                // Define the authentication entry point
                .exceptionHandling().authenticationEntryPoint(authenticationEntryPoint)
                .and()
                // Ensure we have a stateless service to prevent credential storage in session
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                // Allow Cors requests
                .cors()
                .and()
                 // Disable CSRF
                .csrf().disable();

        // Add the filter to validate tokens with every request
        http
                .addFilterBefore(requestFilter, UsernamePasswordAuthenticationFilter.class);
    }

    // Bean for the global password encoder used in the service
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // Bean for the global AuthenticationManager
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
}
