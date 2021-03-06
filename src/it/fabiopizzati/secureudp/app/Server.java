package it.fabiopizzati.secureudp.app;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.security.SecureRandom;

import it.fabiopizzati.secureudp.SecureDatagramSocket;

/**
 * Class that implements a simple server that accept a client connection, performs a Diffie-Hellman key exchange,
 * then open a {@code SecureDatagramSocket} to safely receive UDP packet from the client. When the client sends a certain message,
 * the server closes the connection and waits for a new client.
 * @author Fabio Pizzati
 */
public class Server {
	
	/**
	 * Diffie-Hellman TCP socket port.
	 */
	private static final int dhport = 8777;
	
	/**
	 * {@code SecureDatagramSocket} port.
	 */
	private static final int dataport = 12398;
	
	/**
	 * Socket used to accept client connections.
	 */
	private static ServerSocket dhSocket;
	
	/**
	 * Recieved packet maximum size.
	 */
	private static final int bufsize = 512;
	
	/**
	 * Primitive root of {@code p}
	 */
	private static final int g = 2;
	
	/**
	 * Large prime number used as DH secret
	 */
	private static final Long p = new Long("6974133661755618893");
	
	/**
	 * Main method. Creates a {@code ServerSocket} on a certain port and wait for a client connection.
	 * Then, it performs a Diffie-Hellman exchange, then creates a {@code SecureDatagramSocket}.
	 * When a message is received, it is also printed on the standard output. When a certain string ("bye") is received,
	 * the server closes the connection and waits for a new client to connect.
	 * The DH values are obtained randomly from a {@code SecureRandom} object.
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException{
		
		// Diffie-Hellman key exchange (needed public/private key pair
		// to implement more secure key exchange schemes)
		
		// Server's random
		SecureRandom sRand = new SecureRandom();
		Long xb = Math.abs(sRand.nextLong());
		
		dhSocket = new ServerSocket(dhport);
		
		while(true){
			System.out.println("Waiting for connection...");
			
			// Tcp socket for password data exchange
			Socket connection = dhSocket.accept();	
			System.out.println("Connection from client accepted");
            BufferedReader inFromClient = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            DataOutputStream outToClient = new DataOutputStream(connection.getOutputStream());

            // Parsing client random and sending back server's
            Long xa = new Long(inFromClient.readLine().trim());
            outToClient.writeUTF(xb.toString() + "\n");
            
            // Calculating password...
            Long password = (g^(xa*xb)) % p;
            
            // Conversion to byte array
            ByteBuffer pwbuffer = ByteBuffer.allocate(Long.BYTES);
            pwbuffer.putLong(password);
			
			byte[] buf = new byte[bufsize];
			SecureDatagramSocket socket = new SecureDatagramSocket(pwbuffer.array(), dataport);
			System.out.println("Datagram server started on port " + dataport);
			
			boolean end = false;
			
			while(!end){
				
				DatagramPacket p = new DatagramPacket(buf, bufsize);
				socket.receive(p);

				String received = new String(p.getData());
				System.out.println("Received:" + received);
				
				if(received.equals("bye")){
					end = true;
				}
			}
			
			System.out.println("Connection closed");
			socket.close();
		}
	}
	
}
