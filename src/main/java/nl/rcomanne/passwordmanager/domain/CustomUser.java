package nl.rcomanne.passwordmanager.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity(name = "CustomUser")
@Table(name = "custom_user")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
public class CustomUser implements Serializable {
    @Transient
    private static final long serialVersionUID = -1180421230845189543L;

    @Id
    @GeneratedValue
    private Long id;
    @Column(name = "mail") private String mail;
    @JsonIgnore
    @Column(name = "encoded_password") private String encodedPassword;
    @JsonIgnore
    @Column(name = "activation_token") private String activationToken;
    @Column(name = "activated") private boolean activated;

    @JsonIgnore
    @OneToMany(
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    @Column(name = "passwords") private List<Password> passwords;
}
