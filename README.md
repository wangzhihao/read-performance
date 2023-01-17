
``` sh
> base64 /dev/urandom | head -c 512000000 > file.txt && java AESCipher.java

Encrypted 488.28 MB in 2.219 seconds from memory, Throughtput is 220.05 MB per second
Decrypted 488.28 MB in 1.864 seconds from memory, Throughtput is 261.95 MB per second


> nproc
8

> base64 /dev/urandom | head -c 50000000 > file.txt && java AESCipherMultiThreads.java

With 1 Threads, Decrypted 1.40 GB in 4.835 seconds from memory, Throughtput is 295.87 MB per second
With 2 Threads, Decrypted 1.40 GB in 3.023 seconds from memory, Throughtput is 473.21 MB per second
With 3 Threads, Decrypted 1.40 GB in 2.03 seconds from memory, Throughtput is 704.69 MB per second
With 4 Threads, Decrypted 1.40 GB in 1.314 seconds from memory, Throughtput is 1.06 GB per second
With 5 Threads, Decrypted 1.40 GB in 1.126 seconds from memory, Throughtput is 1.24 GB per second
With 6 Threads, Decrypted 1.40 GB in 1.215 seconds from memory, Throughtput is 1.15 GB per second
With 7 Threads, Decrypted 1.40 GB in 0.991 seconds from memory, Throughtput is 1.41 GB per second
With 8 Threads, Decrypted 1.40 GB in 0.945 seconds from memory, Throughtput is 1.48 GB per second
With 9 Threads, Decrypted 1.40 GB in 0.919 seconds from memory, Throughtput is 1.52 GB per second
With 10 Threads, Decrypted 1.40 GB in 0.89 seconds from memory, Throughtput is 1.57 GB per second
With 11 Threads, Decrypted 1.40 GB in 0.891 seconds from memory, Throughtput is 1.57 GB per second
With 12 Threads, Decrypted 1.40 GB in 0.884 seconds from memory, Throughtput is 1.58 GB per second
With 13 Threads, Decrypted 1.40 GB in 0.89 seconds from memory, Throughtput is 1.57 GB per second
With 14 Threads, Decrypted 1.40 GB in 0.904 seconds from memory, Throughtput is 1.55 GB per second
With 15 Threads, Decrypted 1.40 GB in 0.864 seconds from memory, Throughtput is 1.62 GB per second
With 16 Threads, Decrypted 1.40 GB in 0.877 seconds from memory, Throughtput is 1.59 GB per second

> base64 /dev/urandom | head -c 512000000 > file.txt  && java GZIP.java
Compress 488.28 MB in 23.357 seconds from memory, Throughtput is 20.91 MB per second
Size after compress is 371.43 MB compress ratio: 0.76068234375
Decompress 371.43 MB in 2.487 seconds from memory, Throughtput is 149.35 MB per second

> base64 /dev/urandom | head -c 512000000 > file.txt  && java AESAndGZIP.java
Wrap data 488.28 MB in 24.831 seconds from memory, Throughtput is 19.66 MB per second
UnWrap data 488.28 MB in 4.609 seconds from memory, Throughtput is 105.94 MB per second
```

