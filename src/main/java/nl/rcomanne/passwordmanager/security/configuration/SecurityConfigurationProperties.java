package nl.rcomanne.passwordmanager.security.configuration;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "password")
public class SecurityConfigurationProperties {
    @NotBlank
    private String key;
}
