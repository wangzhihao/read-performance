import javax.crypto.*;
import java.security.*;
import java.nio.file.*;
import javax.crypto.spec.IvParameterSpec;
import java.util.zip.*;
import java.nio.file.*;
import java.io.*;
import java.text.DecimalFormat;

public class AESAndGZIP{

    public static void main(String [] args) throws Exception {

        Path path = Paths.get("file.txt");
        long bytes = Files.size(path);

        byte[] input = Files.readAllBytes(path);
        AESUtils aes = new AESUtils();
        GZIPUtils gzip = new GZIPUtils();

        long start = System.currentTimeMillis();
        byte[] output = aes.encrypt(gzip.compress(input));
        long finish = System.currentTimeMillis();

        System.out.print("Wrap data " + formatFileSize(bytes) + " in " +  1.0 * (finish - start) / 1000 + " seconds from memory, ");
        System.out.println("Throughtput is " + formatFileSize(1.0 * bytes / (finish - start) * 1000) + " per second");

        start = System.currentTimeMillis();
        gzip.decompress(aes.decrypt(output));
        finish = System.currentTimeMillis();

        System.out.print("UnWrap data " + formatFileSize(output.length) + " in " +  1.0 * (finish - start) / 1000 + " seconds from memory, ");
        System.out.println("Throughtput is " + formatFileSize(1.0 * output.length / (finish - start) * 1000) + " per second");

        start = System.currentTimeMillis();
        byte[] buffer = new byte[1024*1024]; 
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try(InputStream in = new GZIPInputStream(new CipherInputStream(new ByteArrayInputStream(output), aes.decryptC))) {
            int len;
            while ((len = in.read(buffer)) > 0) {
                baos.write(buffer, 0, len);
            }
        }
        baos.toByteArray();
        finish = System.currentTimeMillis();

        System.out.print("UnWrap data as stream " + formatFileSize(output.length) + " in " +  1.0 * (finish - start) / 1000 + " seconds from memory, ");
        System.out.println("Throughtput is " + formatFileSize(1.0 * output.length/ (finish - start) * 1000) + " per second");

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

class AESUtils {
    public Cipher encryptC, decryptC;

   public AESUtils() throws Exception {
        this.encryptC = Cipher.getInstance("AES/CBC/PKCS5Padding");
        this.decryptC = Cipher.getInstance("AES/CBC/PKCS5Padding");
        SecretKey key = generateKey(256);
        IvParameterSpec iv = generateIv();
        this.encryptC.init(Cipher.ENCRYPT_MODE, key, iv);
        this.decryptC.init(Cipher.DECRYPT_MODE, key, iv);
   }

   public byte[] encrypt(byte[] plain) throws Exception { return this.encryptC.doFinal(plain);}
   public byte[] decrypt(byte[] encoded) throws Exception { return this.decryptC.doFinal(encoded);}
    
   private SecretKey generateKey(int n) throws NoSuchAlgorithmException {
        KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
        keyGenerator.init(n);
        SecretKey key = keyGenerator.generateKey();
        return key;
    }

    private IvParameterSpec generateIv() {
        byte[] iv = new byte[16];
        new SecureRandom().nextBytes(iv);
        return new IvParameterSpec(iv);
    }
}

class GZIPUtils {
   public byte[] compress(byte[] plain) throws Exception { 
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try( GZIPOutputStream out = new GZIPOutputStream(baos)) {
            out.write(plain, 0, plain.length);
        }
        return baos.toByteArray();
   }
   public byte[] decompress(byte[] encoded) throws Exception { 
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024*1024]; 
        try(GZIPInputStream in = new GZIPInputStream(new ByteArrayInputStream(encoded))) {
            int len;
            while ((len = in.read(buffer)) > 0) {
                baos.write(buffer, 0, len);
            }
        }
        return baos.toByteArray();
   }
}

