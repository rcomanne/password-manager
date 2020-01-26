package nl.rcomanne.passwordmanager.web.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import nl.rcomanne.passwordmanager.domain.Password;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@NoArgsConstructor
public class PasswordToAdd {

    @NotBlank(message = "Name is required")
    private String name;

    @NotBlank(message = "Domain is required")
    private String domain;

    @NotBlank(message = "Password is required")
    private String password;

    public Password toPassword() {
        return Password.builder()
                .name(this.name)
                .domain(this.domain)
                .password(this.password)
                .build();
    }
}
