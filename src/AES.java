import java.io.UnsupportedEncodingException;
import java.security.*;
import java.util.Arrays;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class AES {
	 		
	private static final String IV = "DEFAULTINITVECTR"; // 16 byte
	
	public static byte[] encrypt(byte[] plainTextBytes, String password) throws 
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
	    byte[] key = sha.digest(password.getBytes("UTF-8"));
	    key = Arrays.copyOf(key, 16);

	    SecretKeySpec spec = new SecretKeySpec(key, "AES");
	    cipher.init(Cipher.ENCRYPT_MODE, spec, new IvParameterSpec(IV.getBytes()));
	    return cipher.doFinal(plainTextBytes);
	}
	 
	public static byte[] decrypt(byte[] encryptedTextBytes, String password) throws 
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
	    byte[] key = sha.digest(password.getBytes("UTF-8"));
	    key = Arrays.copyOf(key, 16); 

	    SecretKeySpec spec = new SecretKeySpec(key, "AES");
	    cipher.init(Cipher.DECRYPT_MODE, spec, new IvParameterSpec(IV.getBytes()));
	    byte[] decryptedTextBytes = cipher.doFinal(encryptedTextBytes);
	    
	    return decryptedTextBytes;
	}
}
