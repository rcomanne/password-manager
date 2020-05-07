package nl.rcomanne.passwordmanager.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nl.rcomanne.passwordmanager.domain.CustomUser;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class MailService {
    private static final String appName = "PasswordManager";
    private static final String apiHost = "https://api.rcomanne.nl";
    private static final String frontEndHost = "https://pw.rcomanne.nl";

    private final JavaMailSender mailSender;

    public void sendActivationMail(CustomUser user) {
        final String subject = String.format("Activate your account for %s", appName);
        final String message = String.format("To activate your account, please go to %s/activate", frontEndHost);
        sendMail(user.getMail(), "Activate your account for " + subject, message);
    }

    public void sendMail(String to, String subject, String text) {
        SimpleMailMessage mail = new SimpleMailMessage();
        mail.setTo(to);
        mail.setSubject(subject);
        mail.setText(text);

        mailSender.send(mail);
    }

}
