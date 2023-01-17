import java.util.zip.*;
import java.nio.file.*;
import java.io.*;
import java.text.DecimalFormat;

public class GZIP {

    public static void main(String [] args) throws Exception {

        Path path = Paths.get("file.txt");
        int bytes = (int)Files.size(path);

        byte[] input = Files.readAllBytes(path);
        byte[] buffer = new byte[1024*1024]; 

        long start = System.currentTimeMillis();

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try( GZIPOutputStream out = new GZIPOutputStream(baos)) {
            out.write(input, 0, bytes);
        }
        byte[] output = baos.toByteArray();
        long finish = System.currentTimeMillis();

        System.out.print("Compress " + formatFileSize(bytes) + " in " +  1.0 * (finish - start) / 1000 + " seconds from memory, ");
        System.out.println("Throughtput is " + formatFileSize(1.0 * bytes / (finish - start) * 1000) + " per second");

        start = System.currentTimeMillis();
        try(GZIPInputStream in = new GZIPInputStream(new ByteArrayInputStream(output))) {
            while (in.read(buffer) > 0) ;
        }
        finish = System.currentTimeMillis();

        System.out.print("Decompress " + formatFileSize(bytes) + " in " +  1.0 * (finish - start) / 1000 + " seconds from memory, ");
        System.out.println("Throughtput is " + formatFileSize(1.0 * bytes / (finish - start) * 1000) + " per second");

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
