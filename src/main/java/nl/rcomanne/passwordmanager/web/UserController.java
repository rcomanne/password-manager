package nl.rcomanne.passwordmanager.web;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nl.rcomanne.passwordmanager.service.UserService;
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

    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<Map<String, String>> register(@RequestBody @Valid RegisterRequest request) {
        log.debug("register request received: {}", request.toString());
        userService.registerUser(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(Map.of("message", "Account has been registered and activation mail has been sent"));
    }

    @PostMapping("/activate/{token}")
    public ResponseEntity<JwtResponse> register(@PathVariable String token, @RequestBody String mail ) {
        log.debug("activation request received: {}", token);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new JwtResponse("Account has been activated", userService.activateUser(mail, token)));
    }

    @PostMapping("/login")
    public ResponseEntity<JwtResponse> login(@RequestBody @Valid LoginRequest request) {
        log.debug("login request received: {}", request.toString());
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new JwtResponse("Login successful", userService.login(request)));
    }
}
