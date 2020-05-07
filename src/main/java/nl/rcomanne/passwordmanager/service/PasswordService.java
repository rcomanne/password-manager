package nl.rcomanne.passwordmanager.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class PasswordService {

    private List<String> letters = new ArrayList<>();
    private List<String> numbers = new ArrayList<>();
    private List<String> specialCharacters = new ArrayList<>();
    private final Random r = new Random();

    @PostConstruct
    private void initCharacters() {
        log.info("initializing numbers");
        for (int i = 0; i < 10; i++) {
            log.debug("Adding number {} to possible numbers list", i);
            numbers.add(Integer.toString(i));
        }
        Collections.shuffle(numbers);

        log.info("initializing letters");
        for (int i = 65; i < 91; i++) {
            letters.add(Character.toString(i));
        }
        for (int i = 97; i < 123; i++) {
            letters.add(Character.toString(i));
        }
        Collections.shuffle(letters);

        log.info("initializing special characters");
        specialCharacters = new ArrayList<>();
        specialCharacters.addAll(List.of("?", "!", "@", "#", "$", "%", "&", "-", "_", "=", "+"));
        Collections.shuffle(specialCharacters);
    }

    public String generateLettersPassword(int length) {
        StringBuilder generated = new StringBuilder();
        for (int i = 0; i < length; i++) {
            generated.append(letters.get(r.nextInt(letters.size())));
        }
        return generated.toString();
    }

    public String generateLettersNumbersPassword(int length) {
        StringBuilder generated = new StringBuilder();
        for (int i = 0; i < length; i++) {
            if (r.nextInt(10) < 8) {
                generated.append(letters.get(r.nextInt(letters.size())));
            } else {
                generated.append(numbers.get(r.nextInt(numbers.size())));
            }
        }
        return generated.toString();
    }

    public String generatePasswordAllCharacters(int length) {
        StringBuilder generated = new StringBuilder();
        for (int i = 0; i < length; i++) {
            int random = r.nextInt(100);
            if (random <= 70) {
                generated.append(letters.get(r.nextInt(letters.size())));
            } else if (random <= 85) {
                generated.append(numbers.get(r.nextInt(numbers.size())));
            } else if (random <= 100) {
                generated.append(specialCharacters.get(r.nextInt(specialCharacters.size())));
            }
        }
        return generated.toString();
    }
}
