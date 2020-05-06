package nl.rcomanne.passwordmanager.web.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import nl.rcomanne.passwordmanager.domain.CustomUser;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class RegisterRequest {

    @Email(message = "Not a valid email address")
    @NotBlank(message = "Mail is required")
    private String mail;


    @Size(min = 6, max = 256, message = "Password should be between 6 and 256 characters")
    @NotBlank(message = "Password is required")
    private String password;

    public String getMail() {
        // ensure emails are ALWAYS processed in lower case
        return mail.toLowerCase();
    }

    public CustomUser toUser() {
        return CustomUser.builder()
                .mail(this.getMail())
                .passwords(new ArrayList<>())
                .activated(false)
                .activationToken(UUID.randomUUID().toString())
                .build();
    }

}
