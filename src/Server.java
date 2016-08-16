import java.io.IOException;
import java.net.DatagramPacket;
import java.util.Arrays;

public class Server {
	
	public static void main(String[] args) throws IOException{
		byte[] buf = new byte[256];
	    DatagramPacket p = new DatagramPacket(buf, 256);
	    SecureDatagramSocket socket = new SecureDatagramSocket("CULO", 12398);
	    System.out.println("Server started");
	    while(true){
	    	socket.receive(p);
	    	
	    	if(new String(p.getData()).equals("bye")){
	    		break;
	    	}
	    	p.setData(Arrays.copyOfRange(p.getData(), 0, p.getLength()));
	    	System.out.println("Received:" + new String(p.getData(), "UTF8"));
	    }
	    
	    socket.close();
	    
	}
	
}
