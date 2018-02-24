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
	public Server() throws UnknownHostException, IOException {
		this.serverSocket = new ServerSocket(9999);
		this.socket = serverSocket.accept();
		this.in = socket.getInputStream();
		log("created new instance");
	}
	
	//Server start
	void run() throws IOException {
		readFrame();
		// TODO: FpgaWriter
		closeSession();
	}
	
	//Read frame
	private void readFrame() throws IOException {
		byte[] buffer = new byte[Frame.SIZE];
		this.in.read(buffer);
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
}
