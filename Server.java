import java.io.*;
import java.net.*;
import java.util.*;

public class Server extends Thread{

	private DatagramSocket socket = null;
    private BufferedReader in = null;
    private boolean running = true;
	private int numOfPlayers;
	private int connectedPlayers = 0;

    public Server(int number) throws IOException {
        super("Server!");
        socket = new DatagramSocket(4445);		//socket ng server
		this.numOfPlayers = number;
    }

    public void run() {
		System.out.println("Waiting for players");
	
		DatagramPacket packet;
		byte[] buf;
		
        while (running) {		// infinite loop muna
            try {
				
                buf = new byte[256];
				packet = new DatagramPacket(buf, buf.length);
				socket.receive(packet);								//receive
				String received = new String(packet.getData(), 0, packet.getLength());
                buf = received.getBytes();

				InetAddress group = InetAddress.getByName("230.0.0.1");	//copy paste yung "230.0.0.1". group address ng mga client
                packet = new DatagramPacket(buf, buf.length, group, 4446);
                socket.send(packet);	//i-send sa group address yung nareceive

				// pang-check ng number of players
				if(received.startsWith("SERVER - ")){
					this.connectedPlayers++;
					if(this.connectedPlayers == this.numOfPlayers){
						buf = new byte[256];
						buf = ("---- CHAT STARTED ----").getBytes();
						packet = new DatagramPacket(buf, buf.length, group, 4446);
						socket.send(packet);
					}	
				}
				
		} catch (IOException e) {
                e.printStackTrace();
         }
        }
	socket.close();
    }
}