package nl.rcomanne.passwordmanager.security.passwords;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.security.GeneralSecurityException;

@Slf4j
@Component
public class PasswordEncryption {

    public static final String AES = "AES";

    @Value("${password.key}")
    private String key;

    private Cipher encryptor;
    private Cipher decryptor;

    public String encryptPassword(String plainPassword) throws GeneralSecurityException {
        return byteArrayToHexString(encryptor.doFinal(plainPassword.getBytes()));
    }

    public String decryptPassword(String encryptedPassword) throws GeneralSecurityException {
        return new String(decryptor.doFinal(hexStringToByteArray(encryptedPassword)));
    }

    private String byteArrayToHexString(byte[] b) {
        StringBuilder sb = new StringBuilder(b.length * 2);
        for (byte value : b) {
            int v = value & 0xff;
            if (v < 16) {
                sb.append('0');
            }
            sb.append(Integer.toHexString(v));
        }
        return sb.toString().toUpperCase();
    }

    private byte[] hexStringToByteArray(String s) {
        byte[] b = new byte[s.length() / 2];
        for (int i = 0; i < b.length; i++) {
            int index = i * 2;
            int v = Integer.parseInt(s.substring(index, index + 2), 16);
            b[i] = (byte) v;
        }
        return b;
    }

    @PostConstruct
    private void initializeCipher() {
        try {
            // initialize the encryptor
            byte[] bytekey = hexStringToByteArray(key);
            SecretKeySpec sks = new SecretKeySpec(bytekey, AES);
            Cipher encryptCipher = Cipher.getInstance(AES);
            encryptCipher.init(Cipher.ENCRYPT_MODE, sks, encryptCipher.getParameters());
            this.encryptor = encryptCipher;

            // initialize the decryptor
            Cipher decryptCipher = Cipher.getInstance(AES);
            decryptCipher.init(Cipher.DECRYPT_MODE, sks);
            this.decryptor = decryptCipher;

        } catch (GeneralSecurityException ex) {
            log.error("Exception occured while creating cipher", ex);
            throw new RuntimeException("Error while starting the applicaton.");
        }
    }
}
