import java.io.*;
import java.net.*;
import java.util.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Client extends JFrame{
	private static int game_state = 1;
	
	private JTextField userText;
	private JTextArea chatWindow;
	
	private String playerName = "";
	private String serverIP;
	
	private DatagramPacket packet;
	private MulticastSocket socket;
	private InetAddress clientAddress;
	private InetAddress serverAddress;
	private byte[] buf;
	
    public Client(String name, String host){
		super("Chat Box Client :3 ");
		
		this.playerName = name;
		this.serverIP = host;
		
		userText = new JTextField();
		userText.setEditable(false);
		userText.addActionListener(
			new ActionListener(){
				public void actionPerformed(ActionEvent event){
					sendMessage(event.getActionCommand());
					userText.setText("");
				}
			}
		);
		//add chatArea and chatWindow
		add(userText, BorderLayout.SOUTH);
		chatWindow =  new JTextArea();
		chatWindow.setEditable(false);
		add(new JScrollPane(chatWindow), BorderLayout.CENTER);
		setSize(300, 150);
		setVisible(true);
        
    }
	
	public void startRunning(){
		buf = new byte[256];
		buf = ("SERVER - " + this.playerName + " is now connected!").getBytes();	// unang string na isesend sa server
		
		try{
			MulticastSocket socket = new MulticastSocket(4446); //4446 port ng mga client
			InetAddress clientAddress = InetAddress.getByName("230.0.0.1"); // copy paste lang yung "230.0.0.1" haha. group address ng mga clients
			socket.joinGroup(clientAddress);
			
			serverAddress = InetAddress.getByName(serverIP);
			packet = new DatagramPacket(buf, buf.length, serverAddress, 4445); //4445 port the server
			socket.send(packet);					//send unang string sa server
		
		
			while(game_state == 1){ // infinite loop muna.
			
				buf = new byte[256];
				packet = new DatagramPacket(buf, buf.length);
				
				socket.receive(packet);		//receive ng packet kung meron

				String received = new String(packet.getData(), 0, packet.getLength());
				chatWindow.append(received + "\n");		//i-append sa chatWindow yung nareceive
				
				if(received.startsWith("---- CHAT STARTED ----")){
					ableToType(true);
				}
			}
			
			
			socket.leaveGroup(clientAddress);
		}catch(Exception e){}
		socket.close();
    }
	
	public void sendMessage(String message){
		buf = new byte[256];
		buf = (this.playerName+" - "+message).getBytes();
		
		try{
			socket = new MulticastSocket(4446);
			serverAddress = InetAddress.getByName(serverIP);
			packet = new DatagramPacket(buf, buf.length, serverAddress, 4445);
			socket.send(packet);
		}catch(Exception e){}		
	}
	
	private void ableToType(final boolean tof){
		SwingUtilities.invokeLater(
			new Runnable(){
				public void run(){
					userText.setEditable(tof);
				}
			}
		);
	}
}