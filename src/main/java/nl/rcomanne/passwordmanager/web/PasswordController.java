package nl.rcomanne.passwordmanager.web;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nl.rcomanne.passwordmanager.domain.CustomUser;
import nl.rcomanne.passwordmanager.domain.Password;
import nl.rcomanne.passwordmanager.service.PasswordService;
import nl.rcomanne.passwordmanager.service.UserService;
import nl.rcomanne.passwordmanager.web.domain.AddPasswordRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/pw")
@RequiredArgsConstructor
public class PasswordController {

    private final UserService userService;
    private final PasswordService passwordService;

    @GetMapping("/generate/{length}/{type}")
    public ResponseEntity<Map<String, String>> generatePassword(@PathVariable int length, @PathVariable(required = false) String type) {
        String generated;
        switch (type) {
            case "ltrnum": generated = passwordService.generateLettersNumbersPassword(length); break;
            case "ltr": generated = passwordService.generateLettersPassword(length); break;
            default:
            case "all": generated = passwordService.generatePasswordAllCharacters(length); break;
        }
        return ResponseEntity
                .ok(Map.of("generated", generated));
    }

    @GetMapping("/generate/{length}")
    public ResponseEntity<Map<String, String>> generatePassword(@PathVariable int length) {
        return ResponseEntity
                .ok(Map.of("generated", passwordService.generatePasswordAllCharacters(length)));
    }

    @PostMapping("/add")
    public ResponseEntity<Void> addPassword(@RequestBody AddPasswordRequest request, Principal principal) {
        final String mail = principal.getName();
        log.debug("adding password(s) for user {}", mail);
        CustomUser user = userService.addPasswords(mail, request.getPasswords());
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePassword(@PathVariable long id, Principal principal) {
        final String mail = principal.getName();
        log.debug("deleting password {} for user {}", id, mail);
        userService.deletePassword(mail, id);
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
        log.debug("get unencrypted password {} for user {}", id, mail);
        return ResponseEntity.ok(userService.getPassword(mail, id));
    }
}
