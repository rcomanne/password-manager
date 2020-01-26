package nl.rcomanne;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.security.NoSuchAlgorithmException;
import java.util.stream.IntStream;

public class GenerateKey {
    public static final String AES = "AES";
    private static String byteArrayToHexString(byte[] b) {
        StringBuilder sb = new StringBuilder(b.length * 2);
        IntStream.range(0, b.length).map(i -> b[i] & 0xff).forEach(v -> {
            if (v < 16) {
                sb.append('0');
            }
            sb.append(Integer.toHexString(v));
        });
        return sb.toString().toUpperCase();
    }

    public static void main(String[] args) throws NoSuchAlgorithmException {
        KeyGenerator keyGen = KeyGenerator.getInstance(GenerateKey.AES);
        keyGen.init(128);
        SecretKey sk = keyGen.generateKey();
        String key = byteArrayToHexString(sk.getEncoded());
        System.out.println("key:" + key);
    }
}
