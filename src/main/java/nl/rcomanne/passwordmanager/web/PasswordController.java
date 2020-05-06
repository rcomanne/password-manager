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
        final String username = principal.getName();
        log.debug("adding password(s) for user {}", username);
        CustomUser user = userService.addPasswords(username, request.getPasswords());
        return ResponseEntity.ok().build();
    }

    @GetMapping("/all")
    public ResponseEntity<List<Password>> getAllPasswords(Principal principal) {
        final String username = principal.getName();
        log.debug("get all passwords for user {}", username);
        return ResponseEntity.ok(userService.getAllPasswordsCleaned(username));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Password> getPassword(@PathVariable Long id, Principal principal) {
        final String mail = principal.getName();
        log.debug("get unencrypted password for user {}", mail);
        return ResponseEntity.ok(userService.getPassword(mail, id));
    }
}
