package corpus.sinhala.crowler.network;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class NetworkConnector {
	private static NetworkConnector instance = null;
	Socket socket;
	PrintWriter output;

	protected NetworkConnector() {
		// Exists only to defeat instantiation.
	}

	public static NetworkConnector getInstance() {
		if (instance == null) {
			instance = new NetworkConnector();
		}
		return instance;
	}
	
	public void connect() throws UnknownHostException, IOException{
		socket = new Socket("127.0.0.1", 1111);
		output =  new PrintWriter(socket.getOutputStream(), true);
	}
	
	public void send(String s) throws IOException{
	    output.println(s);
	    output.flush();
	}
	
	public void close() throws IOException{
		
	    output.close();
	    socket.close();
	}
	
	public static void main(String args[]) throws IOException{
		NetworkConnector nc = NetworkConnector.getInstance();
//		nc.send();
	}
}
