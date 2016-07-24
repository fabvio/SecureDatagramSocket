import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

public class Main {

	public static void main(String[] args) throws InvalidKeyException, NoSuchAlgorithmException, InvalidKeySpecException, NoSuchPaddingException, InvalidAlgorithmParameterException, UnsupportedEncodingException, IllegalBlockSizeException, BadPaddingException {
		
		AesCipher AES = new AesCipher("culo");
		AES.setIv("BBBBBBBBBBBBBBBB");
		AesCipher AESD = new AesCipher("culo");
		AESD.setIv("BBBBBBBBBBBBBBBB");

		byte[] encr = AES.encrypt(new String("ciaone").getBytes("UTF8"));
		byte[] decr = AESD.decrypt(encr);
	
		System.out.println(new String(encr, "UTF8"));
		System.out.println(new String(decr, "UTF8"));
	}

}
