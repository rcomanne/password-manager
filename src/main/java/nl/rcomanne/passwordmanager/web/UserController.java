package nl.rcomanne.passwordmanager.web;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nl.rcomanne.passwordmanager.service.UserService;
import nl.rcomanne.passwordmanager.web.domain.ActivationRequest;
import nl.rcomanne.passwordmanager.web.domain.JwtResponse;
import nl.rcomanne.passwordmanager.web.domain.LoginRequest;
import nl.rcomanne.passwordmanager.web.domain.RegisterRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Map;

@Slf4j
@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    public static final String MESSAGE = "message";
    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<Map<String, String>> register(@RequestBody @Valid RegisterRequest request) {
        log.debug("register request received: {}", request.toString());
        userService.registerUser(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(Map.of(MESSAGE, "Account has been registered and activation mail has been sent"));
    }

    @PostMapping("/activate")
    public ResponseEntity<JwtResponse> activate(@RequestBody @Valid ActivationRequest request) {
        log.debug("activation request received: {}", request.getActivationToken());
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new JwtResponse("Account has been activated", userService.activateUser(request.getMail(), request.getActivationToken())));
    }

    @PostMapping("/login")
    public ResponseEntity<JwtResponse> login(@RequestBody @Valid LoginRequest request) {
        log.debug("login request received: {}", request.toString());
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new JwtResponse("User has been logged in", userService.login(request)));
    }
}
