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
public class ActivationRequest {

    @Email(message = "Not a valid email address")
    @NotBlank(message = "Mail is required")
    private String mail;


    @Size(min = 36, max = 36, message = "Activation token should be 36 characters")
    @NotBlank(message = "Activation token is required")
    private String activationToken;

    public String getMail() {
        // ensure emails are ALWAYS processed in lower case
        return mail.toLowerCase();
    }
}
