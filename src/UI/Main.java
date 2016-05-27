package UI;


public class Main {

	static int socketPort = 5000;

	public static void main(String[] args) {
		EV3Server slave = new EV3Server(socketPort);
	}
}
