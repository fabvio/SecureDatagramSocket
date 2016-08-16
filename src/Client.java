import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;

public class Client {
	public static void main(String[] args) throws IOException{
		SecureDatagramSocket socket = new SecureDatagramSocket("CULO");
	    InetAddress hostAddress = InetAddress.getByName("localhost");
	    DatagramPacket out = new DatagramPacket(new String("ciaone").getBytes(), 
	    										"ciaone".length(), hostAddress, 12398);
	    socket.send(out);
		socket.close();
	}
}
