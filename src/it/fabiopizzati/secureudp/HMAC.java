package it.fabiopizzati.secureudp;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.util.Formatter;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

/**
 * Class used to check data integrity and data authentication using a HMAC based on the SHA-1 hashing algorithm.
 * @author Fabio Pizzati
 *
 */
public class HMAC {
	
	/**
	 * String used to initialize Mac object.
	 * @see javax.crypto.Mac
	 */
	private static final String HMAC_SHA1_ALGORITHM = "HmacSHA1";

	/**
	 * Static method used to calculate the HMAC of a byte array.
	 * @param data data to authenticate
	 * @param key password to use to get the HMAC
	 * @return the HMAC, in HEX string
	 * @throws SignatureException
	 * @throws NoSuchAlgorithmException
	 * @throws InvalidKeyException
	 */
	public static String calculate(byte[] data, byte[] key) throws SignatureException, NoSuchAlgorithmException, InvalidKeyException {
		SecretKeySpec signingKey = new SecretKeySpec(key, HMAC_SHA1_ALGORITHM);
		Mac mac = Mac.getInstance(HMAC_SHA1_ALGORITHM);
		mac.init(signingKey);
		return toHexString(mac.doFinal(data));
	}
	
	/**
	 * Static method to convert a byte array into a Hex String
	 * @param bytes to convert to Hex String
	 * @return A string containing the converted byte array.
	 */
	private static String toHexString(byte[] bytes) {
		Formatter formatter = new Formatter();
		
		for (byte b : bytes) {
			formatter.format("%02x", b);
		}

		String result = formatter.toString();
		formatter.close();
		return result;
	}
	
}
