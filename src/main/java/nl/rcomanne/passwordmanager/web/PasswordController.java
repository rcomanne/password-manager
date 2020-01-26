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

@Slf4j
@RestController
@RequestMapping("/pw")
@RequiredArgsConstructor
public class PasswordController {

    private final UserService userService;

    @PostMapping("/add")
    public ResponseEntity<CustomUser> addPassword(@RequestBody AddPasswordRequest request, Principal principal) {
        final String mail = principal.getName();
        CustomUser user = userService.addPasswords(mail, request.getPasswords());
        return ResponseEntity
                .ok(user);
    }

    @GetMapping("/{name}")
    public ResponseEntity<Password> getPassword(@PathVariable String name, Principal principal) {
        final String mail = principal.getName();
        return ResponseEntity.ok(userService.getPassword(mail, name));
    }

}
