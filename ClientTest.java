import javax.swing.JFrame;

public class ClientTest{
	public static void main(String[] args){
		
		if (args.length != 2){
			System.out.println("Usage: java ClientTest <player name> <IPaddress>");
			System.exit(1);
		}
		
		Client cmsc137 =  new Client(args[0], args[1]);
		cmsc137.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		cmsc137.startRunning();
	}
}