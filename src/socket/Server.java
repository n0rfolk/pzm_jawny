package socket;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;

import serial.FpgaWriter;
import utilities.Frame;
import utilities.LogWriter;

public class Server implements Runnable {
	//Server attributes
	ServerSocket serverSocket;
	Socket socket;
	InputStream in;
	Frame currentFrame;
	FpgaWriter fpgaWriter;

	// default Server constructor
	public Server() {
		initServer();
	}
	
	//Server restarts after Client closed session or entire file sent
	private void initServer() {
		try {
			this.serverSocket = new ServerSocket(9999);
			log("server initialization...");
			log("waiting for client...");
			this.socket = serverSocket.accept();
			log("client confirmed, connection established");
			this.in = socket.getInputStream();	
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	//Server start
	public void run(){
		while(true) {
			readFrame();
			// TODO: FpgaWriter
			//closeSession();
		}	
	}
	
	//Read frame
	private void readFrame() {
		byte[] buffer = new byte[Frame.SIZE];
		try {
			this.in.read(buffer);
			Frame.printBytes(buffer);
			this.currentFrame = new Frame(buffer);
		} catch (IOException e) {
			e.printStackTrace();
		}
		log("frame read");
	}
	
	//closing server session
	@SuppressWarnings("unused")
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
