package socket;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

import utilities.Frame;
import utilities.LogWriter;

public class Server {
	//Server attributes
	ServerSocket serverSocket;
	Socket socket;
	InputStream in;
	// TODO: FpgaWriter

	// default Server constructor
	public Server() {
		try {
			this.serverSocket = new ServerSocket(9999);
			log("created new instance");
			this.socket = serverSocket.accept();
			log("connection established");
			this.in = socket.getInputStream();	
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	//Server start
	void run(){
		readFrame();
		// TODO: FpgaWriter
		//closeSession();	
	}
	
	//Read frame
	private void readFrame() {
		byte[] buffer = new byte[Frame.SIZE];
		try {
			this.in.read(buffer);
			Frame.printBytes(buffer);
		} catch (IOException e) {
			e.printStackTrace();
		}
		log("frame read");
	}
	
	//closing server session
	private void closeSession() throws IOException {
		this.socket.close();
		this.serverSocket.close();
		log("session closed");
	}
	//prints logs
	private void log(String s) {
		LogWriter.log(this.getClass().getName(), s);
	}
	
	public static void main(String[] args) {
		Server server = new Server();
		while(true) {
			server.run();
		}
	}
}
