package nl.rcomanne.passwordmanager.web.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.Serializable;

@Getter
@AllArgsConstructor
public class JwtResponse implements Serializable {

    private static final long serialVersionUID = 1512696666575385736L;

    private final String message;
    private final String jwtToken;
}
