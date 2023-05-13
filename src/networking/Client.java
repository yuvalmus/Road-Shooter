package networking;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class Client {
	private Socket socket;
	private Scanner in;
	private PrintWriter out;
	
	public Client() {
		try {
			this.socket = new Socket(NetworkingConstants.SERVER_IP, NetworkingConstants.PORT);
			this.in = new Scanner(this.socket.getInputStream(), "UTF-8");
			this.out = new PrintWriter(this.socket.getOutputStream(), true);
			
		} catch (IOException e) {}
	}
	
	public void sendMsgToServer(String data) {
		out.println(data);
	}
	
	public String recvMsgFromServer() {
		return in.nextLine();
	}
	
	public void close() {
		try {
			this.socket.close();
		} catch (IOException e) {}
	}
}
