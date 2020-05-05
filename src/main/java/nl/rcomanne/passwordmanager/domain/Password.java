package nl.rcomanne.passwordmanager.domain;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity(name = "Password")
@Table(name = "password")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
public class Password {
    @Id
    private String id;
    private String name;
    private String domain;
    private String password;
}
