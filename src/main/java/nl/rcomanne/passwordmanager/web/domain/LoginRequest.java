package nl.rcomanne.passwordmanager.web.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@Setter
@NoArgsConstructor
public class LoginRequest {

    @Email(message = "Not a valid email address")
    @NotBlank(message = "Mail is required")
    private String mail;

    @Size(min = 6, max = 256, message = "Password should be between 6 and 256 characters")
    @NotBlank(message = "Password is required")
    private String password;

    public String getMail() {
        return mail.toLowerCase();
    }
}
