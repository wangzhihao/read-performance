import javax.crypto.*;
import java.security.*;
import java.nio.file.*;
import java.util.concurrent.*;
import javax.crypto.spec.IvParameterSpec;
import java.text.DecimalFormat;

public class AESCipherMultiThreads {
    public static void main(String [] args) throws Exception {
        Cipher c = Cipher.getInstance("AES/CBC/PKCS5Padding");
        SecretKey key = generateKey(256);
        IvParameterSpec iv = generateIv();
        c.init(Cipher.ENCRYPT_MODE, key, iv);

        Path path = Paths.get("file.txt");
        long bytes = Files.size(path);

        byte[] input = Files.readAllBytes(path);
        byte[] cipherText = c.doFinal(input);

        int samples = 30;
        int threads = 16;

        for( int i = 1; i <= threads; i++) {
            ExecutorService executor = Executors.newFixedThreadPool(i);

            long start = System.currentTimeMillis();
            for (int j = 0; j < samples; j++) {
                final int index = j;
                executor.submit(() -> {
                    try {
                        Cipher cc = Cipher.getInstance("AES/CBC/PKCS5Padding");
                        cc.init(Cipher.DECRYPT_MODE, key, iv);
                        cc.doFinal(cipherText);
                        // System.out.println("Decrypted entry " + index);
                    } catch (Exception ex) {
                        ex.getCause().printStackTrace();
                    }
                });
            }
            executor.shutdown();
            executor.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
            long finish = System.currentTimeMillis();

            System.out.print("With " + i + " Threads, ");
            System.out.print("Decrypted " + formatFileSize(bytes * samples) + " in " +  1.0 * (finish - start) / 1000 + " seconds from memory, ");
            System.out.println("Throughtput is " + formatFileSize(1.0 * bytes * samples / (finish - start) * 1000) + " per second");
        }

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
