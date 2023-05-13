package networking;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;


// The class that the SERVER uses to hold the list of clients and interact with them more easily
public class ClientCommunication {
	private Socket clientSocket;
	private Scanner in;
	private PrintWriter out;
	
	
	public ClientCommunication(Socket clientSocket) throws IOException {
		this.clientSocket = clientSocket;
		this.in = new Scanner(this.clientSocket.getInputStream(), "UTF-8");
		this.out = new PrintWriter(this.clientSocket.getOutputStream(), true);
	}
	
	public void sendMsgToClient(String data) {
		out.println(data);
	}
	
	public String recvMsgFromClient() {
		try {			
			return in.nextLine();
		} catch (Exception e) {return "";}
	}
	
	public void close() {
		try {
			this.clientSocket.close();
		} catch (IOException e) {}
	}
	
	@Override
	public String toString() {
		return this.clientSocket.toString();
	}
}
