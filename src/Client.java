import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;

public class Client {
	public Client() throws IOException{
		super();
		SecureDatagramSocket socket = new SecureDatagramSocket("CULO");
	    InetAddress hostAddress = InetAddress.getByName("localhost");
	    DatagramPacket out = new DatagramPacket(new String("ciaone").getBytes(), 
	    										"ciaone".length(), hostAddress, 4567);
	    socket.send(out);
		socket.close();
	}
}
