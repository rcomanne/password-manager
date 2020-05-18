package nl.rcomanne.passwordmanager.domain;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

@Entity(name = "Password")
@Table(name = "password")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
public class Password implements Serializable {

    @Transient
    private static final long serialVersionUID = 2402906733003560591L;

    @Id
    @GeneratedValue
    private Long id;
    @Column(name = "name") private String name;
    @Column(name = "domain") private String domain;
    @Column(name = "encodedPassword") private String encodedPassword;

}
