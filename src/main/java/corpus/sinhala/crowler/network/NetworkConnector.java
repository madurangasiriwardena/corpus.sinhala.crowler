package corpus.sinhala.crowler.network;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class NetworkConnector {
	private static NetworkConnector instance = null;
	Socket socket;
	OutputStreamWriter output;

	protected NetworkConnector() {
		// Exists only to defeat instantiation.
	}

	public static NetworkConnector getInstance() {
		if (instance == null) {
			instance = new NetworkConnector();
		}
		return instance;
	}
	
	public void connect(String host, int port) throws UnknownHostException, IOException{
		socket = new Socket(host, port);
		output =  new OutputStreamWriter(socket.getOutputStream());
	}
	
	public void send(String s) throws IOException{
		try {

		    output.write("check\n");
		    output.flush();
		    
		    output.write(s);
		    output.write("\n");
		    output.flush();
		} catch (IOException e) {
			throw e;
		}

	}
	
	public void close() throws IOException{
		
	    output.close();
	    socket.close();
	}
	
	public static void main(String args[]) throws IOException{
		NetworkConnector nc = NetworkConnector.getInstance();
		nc.connect("127.0.0.1", 12345);
		Scanner keyboard = new Scanner(System.in);
		System.out.println("enter an integer");
		String myint;
		while(!(myint = keyboard.next()).equals("exit")){
			nc.send(myint);
		}
		keyboard.close();
		nc.close();
	}
}
