package nl.rcomanne.passwordmanager.web.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import nl.rcomanne.passwordmanager.domain.Password;

import javax.validation.constraints.Size;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class AddPasswordRequest {
    @Size(min = 1, message = "At least one password should be present when adding new passwords")
    private List<PasswordToAdd> passwords;
}
