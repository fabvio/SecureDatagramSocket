import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.security.SecureRandom;

public class Client {
	
	private static final int dhport = 8777;
	private static final int dataport = 12398;

	private static final int g = 2;
	private static final Long p = new Long("6974133661755618893");
	
	public static void main(String[] args) throws IOException{
		
		String message = null;
		
		// Client's random
		SecureRandom sRand = new SecureRandom();
		Long xa = Math.abs(sRand.nextLong());
		System.out.println(xa);
		
		// Send random to server and wait for server to send its
		Socket clientSocket = new Socket("localhost", dhport);
		System.out.println("Connected");
		DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
		System.out.println("Sent data");
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
