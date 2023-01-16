
``` sh
base64 /dev/urandom | head -c 512000000 > file.txt && java AESCipher.java

Encrypted 488.28 MB in 2.966 seconds from file, Throughtput is 164.63 MB per second
Decrypted 488.28 MB in 1.865 seconds from memory, Throughtput is 261.81 MB per second
```

