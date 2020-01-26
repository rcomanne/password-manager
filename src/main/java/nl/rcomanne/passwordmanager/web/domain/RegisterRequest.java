package nl.rcomanne.passwordmanager.web.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import nl.rcomanne.passwordmanager.domain.User;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@Setter
@NoArgsConstructor
public class RegisterRequest {

    @Email(message = "Not a valid email address")
    @NotBlank(message = "Mail is required")
    private String mail;

    @Size(min = 1, max = 256, message = "Username should be between 1 and 256 characters")
    @NotBlank(message = "Username is required")
    private String username;

    private String name;

    @Size(min = 6, max = 256, message = "Password should be between 6 and 256 characters")
    @NotBlank(message = "Password is required")
    private String password;

    public String getMail() {
        // ensure emails are ALWAYS processed in lower case
        return mail.toLowerCase();
    }

    public User toUser() {
        return User.builder()
                .mail(mail)
                .username(username)
                // TODO: Encrypt password
                .encodedPassword(password)
                .build();
    }

}
