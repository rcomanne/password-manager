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
    private static final String APP_NAME = "PasswordManager";
    private static final String SITE_HOST = "https://pw.rcomanne.nl";

    private final JavaMailSender mailSender;

    public void sendActivationMail(CustomUser user) {
        final String subject = String.format("Activate your account for %s", APP_NAME);
        final String message = String.format("To activate your account, please go to %s/user/activate\n and use %s to activate your account", SITE_HOST, user.getActivationToken());
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
