package it.fabiopizzati.secureudp;
import java.io.UnsupportedEncodingException;
import java.security.*;
import java.util.Arrays;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * Class that implements AES decryption/encryption algorithm given the bytes to encrypt/decrypt and a password.
 * @author Fabio Pizzati
 */
public class AES {
	 		
	/**
	 * Initialization vector needed for OFB encryption/decryption mode.
	 */
	private static final String IV = "DEFAULTINITVECTR"; // 16 byte
	
	/**
	 * Method used to encrypt a byte array. The encryption algorithm is AES, in OFB mode.
	 * @param plainTextBytes plaintext data array
	 * @param password password used to encrypt the data
	 * @return The bytes encrypted with the password.
	 * @throws NoSuchAlgorithmException
	 * @throws NoSuchPaddingException
	 * @throws UnsupportedEncodingException
	 * @throws InvalidKeyException
	 * @throws InvalidAlgorithmParameterException
	 * @throws IllegalBlockSizeException
	 * @throws BadPaddingException
	 */
	public static byte[] encrypt(byte[] plainTextBytes, byte[] password) throws 
		NoSuchAlgorithmException, 
		NoSuchPaddingException, 
		UnsupportedEncodingException, 
		InvalidKeyException, 
		InvalidAlgorithmParameterException, 
		IllegalBlockSizeException, 
		BadPaddingException
	{   

	    Cipher cipher = Cipher.getInstance("AES/OFB/Nopadding");
	    MessageDigest sha = MessageDigest.getInstance("SHA-1");
	    byte[] key = sha.digest(password);
	    key = Arrays.copyOf(key, 16);

	    SecretKeySpec spec = new SecretKeySpec(key, "AES");
	    cipher.init(Cipher.ENCRYPT_MODE, spec, new IvParameterSpec(IV.getBytes()));
	    return cipher.doFinal(plainTextBytes);
	}
	 
	/**
	 * Method used to decrypt a byte array. The decryption algorithm is AES, in OFB mode.
	 * @param encryptedTextBytes encrypted byte array
	 * @param password the password used to decrypt the data
	 * @return The decrypted byte array.
	 * @throws NoSuchAlgorithmException
	 * @throws NoSuchPaddingException
	 * @throws UnsupportedEncodingException
	 * @throws InvalidKeyException
	 * @throws InvalidAlgorithmParameterException
	 * @throws IllegalBlockSizeException
	 * @throws BadPaddingException
	 */
	public static byte[] decrypt(byte[] encryptedTextBytes, byte[] password) throws 
		NoSuchAlgorithmException, 
		NoSuchPaddingException, 
		UnsupportedEncodingException, 
		InvalidKeyException, 
		InvalidAlgorithmParameterException, 
		IllegalBlockSizeException, 
		BadPaddingException
	{
	    Cipher cipher = Cipher.getInstance("AES/OFB/Nopadding");
	    MessageDigest sha = MessageDigest.getInstance("SHA-1");
	    byte[] key = sha.digest(password);
	    key = Arrays.copyOf(key, 16); 

	    SecretKeySpec spec = new SecretKeySpec(key, "AES");
	    cipher.init(Cipher.DECRYPT_MODE, spec, new IvParameterSpec(IV.getBytes()));
	    byte[] decryptedTextBytes = cipher.doFinal(encryptedTextBytes);
	    
	    return decryptedTextBytes;
	}
}
