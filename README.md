##SecureDatagramSocket

Extension of Java's `DatagramSocket` with confidentiality and data authentication/integrity support. These functionalities are completely transparent to the user and to the programmer.

###Confidentiality notes

Encryption and decryption are implemented using AES algorithm, in OFB mode, with a fixed initialization vector.

###Data authentication/integrity notes

Data authentication and data integrity are implemented adding a HMAC to a datagram. The HMAC is obtained from a SHA-1 hash and a password.

###Key exchange notes

In the proposed implementation (client/server sample) the key is obtained from a Diffie-Hellman key exchange scheme. This method allows a per-session key, but is vulnerable to MITM attacks. A more secure scheme could be implemented [having two RSA pair](https://en.wikipedia.org/wiki/Station-to-Station_protocol).

