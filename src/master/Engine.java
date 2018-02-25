package master;

import serial.FpgaReader;
import serial.FpgaWriter;
import socket.Server;
import serial.Communication;
import utilities.Frame;
import utilities.LogWriter;

/**
 * 
 * This class is control center of application flow.
 *
 */
public class Engine {
	boolean fpgaToJawny;
	boolean jawnyToFpga;
	
	// serial communication module
	private Communication sc;
	private FpgaReader fr;
	private FpgaWriter fw;
	
	
	
	
	public Engine() {
		this.fpgaToJawny = false;
		this.jawnyToFpga = false;
		
		// initialize serial communication
		try {
			(new Thread(new Server())).start();
			//this.sc = new Communication("COM7");
		} catch (Exception e) { e.printStackTrace(); }
	}
	
	
	
	public void run() {
		while (true) {
			listenToStartFrame();
			
			if (fpgaToJawny) { 
				processTransactionFromFpga();
				fpgaToJawny = false;
			} 
			
			if (jawnyToFpga) {
				processTransactionFromJawny();			
				jawnyToFpga = false;
			}
		}
		
	}
	
	
	
	private void processTransactionFromJawny() {
		// TODO Auto-generated method stub
		
	}
	


	private void processTransactionFromFpga() {
		/*
		 *  while (frame.type != last frame) {
		 *  	calculateCrc(); // of current frame
		 *  	if (crcOK) {
					send this frame to jawny
					send confirmation to fpga
					listenToFrame();
				} else {
					send error
					requestRetransmission();
					listenToFrame();
				}
				
		 *  }
		 */
		
	}



	private void calculateCrc() {
		// TODO Auto-generated method stub
		
	}



	private void listenToStartFrame() {
		//log("Waiting for first frame..");
		//fr.readFrame();
		//fpgaToJawny = true;
		
		
		
	}



	public static void main(String[] args) {
		Engine e = new Engine();
		e.run();
	}
	
	private void log(String s) {
		LogWriter.log(this.getClass().getName(), s);
	}
	
}
