package master;

import serial.SerialComm;

public class Engine {
	boolean fpgaToJawny;
	boolean jawnyToFpga;
	
	// serial communication
	SerialComm sc;
	
	
	
	public Engine() {
		this.fpgaToJawny = false;
		this.jawnyToFpga = false;
		this.sc = new SerialComm();
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
		// create frame and set mode flags
		
	}



	public static void main(String[] args) {
		Engine e = new Engine();
	}
	
}
