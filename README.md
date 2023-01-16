
``` sh
base64 /dev/urandom | head -c 512000000 > file.txt && java AESCipher.java

Encrypted 488.28 MB in 2.219 seconds from memory, Throughtput is 220.05 MB per second
Decrypted 488.28 MB in 1.864 seconds from memory, Throughtput is 261.95 MB per second
```

