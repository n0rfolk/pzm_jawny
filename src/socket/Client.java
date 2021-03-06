package socket;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import utilities.Frame;
import utilities.LogWriter;

public class Client {
	//Client attributes
	Socket socket;
	OutputStream out;
	
	// default Client constructor
	public Client() {
		String defautIP = "172.23.180.64";
		try {
			this.socket = new Socket(defautIP, 9999);
			this.out = socket.getOutputStream();
		} catch (IOException e) { e.printStackTrace(); }
		log("created new client instance with default ip " + defautIP);
	}
	
	//Client constructor with given IP address
	public Client(String ipAddress) {
		try {
			this.socket = new Socket(ipAddress, 9999);
			this.out = socket.getOutputStream();
		} catch (IOException e) { e.printStackTrace(); }
		
		log("created new client instance with ip " + ipAddress);
	}
	
	//closing session
	public void closeSession() {
		try {
			this.socket.close();
			log("session closed");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	//sending frame
	public void sendFrame(Frame f){
		try {
			this.out.write(f.getBytesWithCRC());
			log("frame sent");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	//prints logs
	private void log(String s) {
		LogWriter.log(this.getClass().getName(), s);
	}
}
