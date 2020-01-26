package nl.rcomanne.passwordmanager.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;

import java.util.List;

@Data
@Builder
public class User {
    @Id
    private String mail;
    private String username;
    private String name;
    @JsonIgnore
    private String encodedPassword;
    @JsonIgnore
    private List<Password> passwords;
}
