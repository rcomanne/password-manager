package nl.rcomanne.passwordmanager;

import nl.rcomanne.passwordmanager.service.PasswordService;
import nl.rcomanne.passwordmanager.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@ActiveProfiles(value = {"test"})
class PasswordManagerApplicationTests {

    @Autowired
    private PasswordService passwordService;

    @Autowired
    private UserService userService;


    @Test
    void contextLoads() {
        assertNotNull(passwordService);
        assertNotNull(userService);
    }

}
