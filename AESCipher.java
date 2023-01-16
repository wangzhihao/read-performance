import javax.crypto.*;
import java.security.*;
import java.nio.file.*;
import javax.crypto.spec.IvParameterSpec;
import java.text.DecimalFormat;



// https://github.com/eugenp/tutorials/blob/master/core-java-modules/core-java-security-algorithms/src/main/java/com/baeldung/aes/AESUtil.java
public class AESCipher {
    public static void main(String [] args) throws Exception {
        Cipher c = Cipher.getInstance("AES/CBC/PKCS5Padding");
        SecretKey key = generateKey(256);
        IvParameterSpec iv = generateIv();
        c.init(Cipher.ENCRYPT_MODE, key, iv);

        Path path = Paths.get("file.txt");
        long bytes = Files.size(path);

        long start = System.currentTimeMillis();
        byte[] input = Files.readAllBytes(path);
        byte[] cipherText = c.doFinal(input);
        long finish = System.currentTimeMillis();

        System.out.print("Encrypted " + formatFileSize(bytes) + " in " +  1.0 * (finish - start) / 1000 + " seconds from file, ");
        System.out.println("Throughtput is " + formatFileSize(1.0 * bytes / (finish - start) * 1000) + " per second");

        start = System.currentTimeMillis();
        c.init(Cipher.DECRYPT_MODE, key, iv);
        c.doFinal(cipherText);
        finish = System.currentTimeMillis();

        System.out.print("Decrypted " + formatFileSize(bytes) + " in " +  1.0 * (finish - start) / 1000 + " seconds from memory, ");
        System.out.println("Throughtput is " + formatFileSize(1.0 * bytes / (finish - start) * 1000) + " per second");

    }
    
    public static SecretKey generateKey(int n) throws NoSuchAlgorithmException {
        KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
        keyGenerator.init(n);
        SecretKey key = keyGenerator.generateKey();
        return key;
    }

    public static IvParameterSpec generateIv() {
        byte[] iv = new byte[16];
        new SecureRandom().nextBytes(iv);
        return new IvParameterSpec(iv);
    }

        // https://stackoverflow.com/a/20556766
    private static String formatFileSize(double size) {
        String hrSize = null;

        double b = size;
        double k = size/1024.0;
        double m = ((size/1024.0)/1024.0);
        double g = (((size/1024.0)/1024.0)/1024.0);
        double t = ((((size/1024.0)/1024.0)/1024.0)/1024.0);

        DecimalFormat dec = new DecimalFormat("0.00");

        if ( t>1 ) {
            hrSize = dec.format(t).concat(" TB");
        } else if ( g>1 ) {
            hrSize = dec.format(g).concat(" GB");
        } else if ( m>1 ) {
            hrSize = dec.format(m).concat(" MB");
        } else if ( k>1 ) {
            hrSize = dec.format(k).concat(" KB");
        } else {
            hrSize = dec.format(b).concat(" Bytes");
        }

        return hrSize;
    }
}
