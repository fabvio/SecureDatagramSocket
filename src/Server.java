import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.security.SecureRandom;
import java.util.Arrays;

public class Server {
	
	private static final int dhport = 8777;
	private static final int dataport = 12398;
	private static ServerSocket dhSocket;

	private static final int g = 2;
	private static final Long p = new Long("6974133661755618893");
	
	public static void main(String[] args) throws IOException{
		
		// Diffie-Hellman key exchange (needed public/private key pair
		// to implement more secure key exchange schemes)
		
		// Server's random
		SecureRandom sRand = new SecureRandom();
		Long xb = Math.abs(sRand.nextLong());

		System.out.println(xb);
		
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
			
			byte[] buf = new byte[256];
			DatagramPacket p = new DatagramPacket(buf, 256);
			SecureDatagramSocket socket = new SecureDatagramSocket(pwbuffer.array(), dataport);
			System.out.println("Datagram server started on port " + dataport);
			
			boolean end = false;
			
			while(!end){
				socket.receive(p);

				p.setData(Arrays.copyOfRange(p.getData(), 0, p.getLength()));
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
