import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

/**
 * 
 */

/**
 * @author Fabio Pizzati
 *
 */

public class SecureDatagramSocket extends DatagramSocket {

	private String password;
	
	public SecureDatagramSocket(String _password) throws SocketException {
		super();
		this.password = _password;
	}
	
	public SecureDatagramSocket(String _password, int PORT) throws SocketException{
		super(PORT);
		this.password = _password;
	}
	
	@Override
	public void send(DatagramPacket p) throws IOException  {
		byte[] encryptedMessage = null;
		try {
			encryptedMessage = AES.encrypt(p.getData(), this.password);
		} catch (InvalidKeyException | NoSuchAlgorithmException | NoSuchPaddingException
				| InvalidAlgorithmParameterException | IllegalBlockSizeException | BadPaddingException e) {
			e.printStackTrace();
		}
		
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		outputStream.write(encryptedMessage);
		
		p.setData(outputStream.toByteArray());
			
		super.send(p);
		
	}
	
	@Override
	public void receive(DatagramPacket p) throws IOException {
		super.receive(p);
		
		// The size changes if the message is decrypted
		int size = p.getLength();
		
		byte[] decryptedMessage = null;
		try {
			decryptedMessage = AES.decrypt(p.getData(), this.password);
		} catch (InvalidKeyException | NoSuchAlgorithmException | NoSuchPaddingException
				| InvalidAlgorithmParameterException | IllegalBlockSizeException | BadPaddingException e) {
			e.printStackTrace();
		}
		p.setData(decryptedMessage);
		p.setLength(size);
	}
	
	
	
}
