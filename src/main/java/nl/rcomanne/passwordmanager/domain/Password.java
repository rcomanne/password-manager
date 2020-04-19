package nl.rcomanne.passwordmanager.domain;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;

@Data
@Builder
public class Password {
    @Id
    private String id;
    private String name;
    private String domain;
    private String password;
}
