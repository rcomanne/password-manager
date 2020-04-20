package nl.rcomanne.passwordmanager.web;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nl.rcomanne.passwordmanager.domain.CustomUser;
import nl.rcomanne.passwordmanager.domain.Password;
import nl.rcomanne.passwordmanager.service.UserService;
import nl.rcomanne.passwordmanager.web.domain.AddPasswordRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/pw")
@RequiredArgsConstructor
public class PasswordController {

    private final UserService userService;

    @PostMapping("/add")
    public ResponseEntity<Void> addPassword(@RequestBody AddPasswordRequest request, Principal principal) {
        final String mail = principal.getName();
        log.debug("adding password(s) for user {}", mail);
        CustomUser user = userService.addPasswords(mail, request.getPasswords());
        return ResponseEntity.ok().build();
    }

    @GetMapping("/all")
    public ResponseEntity<Map<String, Password>> getAllPasswords(Principal principal) {
        final String mail = principal.getName();
        log.debug("get all passwords for user {}", mail);
        return ResponseEntity.ok(userService.getAllPasswords(mail));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Password> getPassword(@PathVariable String id, Principal principal) {
        final String mail = principal.getName();
        log.debug("get unencrypted password for user {}", mail);
        return ResponseEntity.ok(userService.getPassword(mail, id));
    }

    @GetMapping("/test")
    public ResponseEntity<List<Password>> getTestPasswords() {
        log.debug("received request for test passwords");
        List<Password> passwords = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            passwords.add(Password.builder()
                    .name("username" + i)
                    .password("password" + i)
                    .domain("domain" + i)
                    .build()
            );
        }
        return ResponseEntity.ok(passwords);
    }
}
