package nl.rcomanne.passwordmanager.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.util.Map;

@Entity(name = "CustomUser")
@Table(name = "user")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
public class CustomUser {
    @Id
    private String mail;
    private String username;
    private String name;
    @JsonIgnore
    private String encodedPassword;

    @JsonIgnore
    @OneToMany(
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private Map<String, Password> passwords;
}
