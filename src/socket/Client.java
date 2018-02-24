package socket;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

import utilities.Frame;
import utilities.LogWriter;

public class Client {
	Socket socket;
	InputStream in;
	OutputStream out;
	
	// default Client constructor
	public Client() throws UnknownHostException, IOException {
		this.socket = new Socket("172.23.180.64", 9999);
		this.in = socket.getInputStream();
		this.out = socket.getOutputStream();
	}
	
	//closing session
	public void closeSession() throws IOException {
		this.socket.close();
	}
	
	//sending frame
	private void sendFrame(Frame f) throws IOException {
		this.out.write(f.getBytesWithCRC());
		closeSession();
	}
	
}
