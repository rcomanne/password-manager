package nl.rcomanne.passwordmanager.domain;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Password {
    private String name;
    private String domain;
    private String password;
}
