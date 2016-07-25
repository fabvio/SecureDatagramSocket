import java.io.IOException;
import java.net.DatagramPacket;

public class Server {

	public Server() throws IOException{
		super();
		byte[] buf = new byte[1000];
	    DatagramPacket p = new DatagramPacket(buf, buf.length);
	    SecureDatagramSocket socket = new SecureDatagramSocket("CULO", 4567);
	    System.out.println("Server started");
	    while(true){
	    	socket.receive(p);
	    	if(new String(p.getData()).equals("byte")){
	    		break;
	    	}
	    }
	    
	    socket.close();
	    
	}
	
}
