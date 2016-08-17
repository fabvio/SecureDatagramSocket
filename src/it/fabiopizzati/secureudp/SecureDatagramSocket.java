package it.fabiopizzati.secureudp;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.util.Arrays;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;


/**
 * Class that adds confidentiality and data integrity features to {@code java.net.DatagramSocket}.
 * The class works as a upper layer that, in the {@code send} method, encrypts the data, calculate the HMAC for data authentication
 * and then calls the original DatagramSocket {@code send} method. 
 * In the {@code receive} method, the original {@code receive} method is called, the data are decrypted and then, finally, 
 * the HMAC check is performed.
 * @author Fabio Pizzati
 * @version 0.1
 * @since 08-17-2016
 */
public class SecureDatagramSocket extends DatagramSocket {

	/**
	 * Size of the hex encoded HMAC .
	 */
	public static final int MACSIZE = 40;
	/**
	 * Password used to encrypt/decrypt and sign data.
	 */
	private byte[] password;
	
	/**
	 * Constructor
	 * @param _password password used to initialize the Object
	 * @throws SocketException
	 */
	public SecureDatagramSocket(byte[] _password) throws SocketException {
		super();
		this.password = _password;
	}
	
	/**
	 * Constructor with communication port already set
	 * @param _password password used to initialize the Object
	 * @param PORT desired communication port
	 * @throws SocketException
	 */
	public SecureDatagramSocket(byte[] _password, int PORT) throws SocketException{
		super(PORT);
		this.password = _password;
	}
	
	/**
	 * Method used to encrypt data, calculate HMAC, and send data + HMAC
	 * @param p the packet to send
	 * @throws IOException
	 */
	@Override
	public void send(DatagramPacket p) throws IOException  {
		
		byte[] encryptedMessage = null;
		String hmac = null;
		
		try {
			hmac = HMAC.calculate(p.getData(), this.password);
		} catch (InvalidKeyException | SignatureException | NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		
		System.out.println(hmac);
		String message = hmac + new String(p.getData());
		
		try {
			encryptedMessage = AES.encrypt(message.getBytes(), this.password);
		} catch (InvalidKeyException | NoSuchAlgorithmException | NoSuchPaddingException
				| InvalidAlgorithmParameterException | IllegalBlockSizeException | BadPaddingException e) {
			e.printStackTrace();
		}
		
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		outputStream.write(encryptedMessage);
		p.setData(outputStream.toByteArray());
			
		super.send(p);
		
	}
	
	/**
	 * Method used to receive data, decrypt them, calculate HMAC, and check data integrity
	 * @param p the received packet
	 * @throws IOException
	 */
	@Override
	public void receive(DatagramPacket p) throws IOException {
		super.receive(p);
		
		// The size changes if the message is decrypted
		System.out.println(p.getLength());
		byte[] decryptedMessage = null;
		try {
			decryptedMessage = AES.decrypt(p.getData(), this.password);
		} catch (InvalidKeyException | NoSuchAlgorithmException | NoSuchPaddingException
				| InvalidAlgorithmParameterException | IllegalBlockSizeException | BadPaddingException e) {
			e.printStackTrace();
		}
		
		byte[] hmac = Arrays.copyOfRange(decryptedMessage, 0, MACSIZE);
		byte[] msg = Arrays.copyOfRange(decryptedMessage, MACSIZE, p.getLength());

		try {
			if(!HMAC.calculate(msg, this.password).equals(new String(hmac))){
				System.out.println("Message corruption detected!");
				System.out.println(HMAC.calculate(msg, this.password) + " <> " + new String(hmac));
			}
			System.out.println(p.getLength());
			p.setData(msg);
			
		} catch (InvalidKeyException | SignatureException | NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
	}
	
}
