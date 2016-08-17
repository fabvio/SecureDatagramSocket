package it.fabiopizzati.secureudp.app;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.security.SecureRandom;

import it.fabiopizzati.secureudp.SecureDatagramSocket;

/**
 * Class that implements a simple client that established a TCP connection with a server, performs a Diffie-Hellman key exchange,
 * then open a {@code SecureDatagramSocket} to safely send UDP packet to a server.
 * @author Fabio Pizzati
 */
public class Client {
	
	/**
	 * Diffie-Hellman TCP socket port.
	 */
	private static final int dhport = 8777;
	
	/**
	 * {@code SecureDatagramSocket} port.
	 */
	private static final int dataport = 12398;

	/**
	 * Large prime number used as DH secret
	 */
	private static final Long p = new Long("6974133661755618893");
	
	/**
	 * Primitive root of {@code p}
	 */
	private static final int g = 2;
	
	/**
	 * Main method. Creates a TCP socket, performs a Diffie-Hellman exchange, then creates a {@code SecureDatagramSocket}.
	 * While the message sent is not a certain string, it prompts the user and send the data through the {@code SecureDatagramSocket}.
	 * The DH values are obtained randomly from a {@code SecureRandom} object.
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException{
		
		String message = null;
		
		// Client's random
		SecureRandom sRand = new SecureRandom();
		Long xa = Math.abs(sRand.nextLong());

		// Send random to server and wait for server to send its
		Socket clientSocket = new Socket("localhost", dhport);
		System.out.println("Connected to server");
		DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
		BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
		
		outToServer.writeUTF(xa.toString() + "\n");
		Long xb = new Long(inFromServer.readLine().trim());
		clientSocket.close();
		
		// Calculating password...
		Long password = (g^(xa*xb)) % p;
	
        // Conversion to byte array
        ByteBuffer pwbuffer = ByteBuffer.allocate(Long.BYTES);
        pwbuffer.putLong(password);

		SecureDatagramSocket ssocket = new SecureDatagramSocket(pwbuffer.array());
	    InetAddress hostAddress = InetAddress.getByName("localhost");
		
	    // Get the msg from standard input
	    do{
	    	System.out.println("Please insert message here(\"bye\" to close connection)");
	    	BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));		
	    	message = new String(reader.readLine());
	    	DatagramPacket out = new DatagramPacket(message.getBytes(), message.length(), hostAddress, dataport);
	    	ssocket.send(out);
	    } while(!message.equals("bye"));
	    
		ssocket.close();
	}
}
