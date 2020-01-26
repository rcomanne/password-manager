package nl.rcomanne.passwordmanager.security.jwt;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "jwt")
public class JwtConfigurationProperties {
    @NotBlank
    @Size(min = 8)
    private String secret;
}
