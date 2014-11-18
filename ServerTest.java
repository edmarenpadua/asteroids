public class ServerTest {
    public static void main(String[] args) throws java.io.IOException {
	
		if (args.length != 1){
			System.out.println("Usage: java ClientTest <number of Players>");
			System.exit(1);
		}
		Server server = new Server(Integer.parseInt(args [0]));
        server.start();
    }
}