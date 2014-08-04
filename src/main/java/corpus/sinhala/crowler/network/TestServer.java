package corpus.sinhala.crowler.network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

public class TestServer {
	private static TestServer instance = null;

	protected TestServer() {
		// Exists only to defeat instantiation.
	}

	public static TestServer getInstance() {
		if (instance == null) {
			instance = new TestServer();
		}
		return instance;
	}
	
	public void receive() throws IOException{
		ServerSocket serverSocket = new ServerSocket(12345);
	    System.out.println("Server socket created");
	    Socket socket = serverSocket.accept();
	    System.out.println("Socket accepted");
	    BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
	    String date;
	    while((date = input.readLine())!=null && !date.equalsIgnoreCase("close")){
	    	System.out.println("Input: " + date);

	    }
	    System.out.println("close");
	    socket.close();
	    serverSocket.close();
	}
	
	public static void main(String args[]) throws IOException{
		TestServer nc = TestServer.getInstance();
		nc.receive();
	}
}
