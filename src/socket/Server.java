package socket;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import serial.FpgaWriter;
import utilities.Frame;
import utilities.LogWriter;

public class Server implements Runnable {
	//Server attributes
	ServerSocket serverSocket;
	Socket socket;
	InputStream inSocket;
	InputStream inComm;
	OutputStream outComm;
	Frame currentFrame;
	FpgaWriter fpgaWriter;
	private boolean serverRestarted;
	private byte signal;

	// default Server constructor, init Server and init FpgaWriter
	public Server(InputStream inComm, OutputStream outComm) {
		this.serverRestarted = false;
		this.signal = Frame.CONFIRM;
		this.inComm = inComm;
		this.outComm = outComm;
		this.fpgaWriter = new FpgaWriter(inComm, outComm);
		initServer();
	}
	
	//Server restarts after Client closed session or entire file sent
	private void initServer() {
		try {
			if (serverRestarted) {
				log("server restarting");
			}
			serverRestarted = true;
			this.serverSocket = new ServerSocket(9999);
			log("server initialization...");
			log("waiting for client...");
			this.socket = serverSocket.accept();
			log("client confirmed, connection established");
			this.inSocket = socket.getInputStream();	
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	//Server start
	public void run(){
		while(true) {
			try {
				//Reading frame after FPGA confirmed last frame
				if (signal == Frame.CONFIRM) {
					readFrame();
				}
				fpgaWriter.sendFrame(currentFrame);
				//Waiting for CONFIRM signal from FPGA
				while ((signal = (byte) inComm.read()) == 0xFF) {}
				//Log confirmation from FPGA
				if (signal == Frame.CONFIRM) {
					log("frame " + currentFrame.getNr() + " confirmed from FPGA");
				}
				//close session after last frame and restart
				if(currentFrame.getType() == 0x01 && signal == Frame.CONFIRM) {
					closeSession();
					initServer();
				}
			} catch (IOException e) { e.printStackTrace(); }
		}
	}	
	
	//Read frame
	private void readFrame() {
		byte[] buffer = new byte[Frame.SIZE];
		try {
			this.inSocket.read(buffer);
			Frame.printBytes(buffer);
			this.currentFrame = new Frame(buffer);
		} catch (IOException e) { e.printStackTrace(); }
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
