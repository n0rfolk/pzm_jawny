package master;

import utilities.Frame;

public class Engine {
	boolean fpgaToJawny;
	boolean jawnyToFpga;
	
	
	public Engine() {
		this.fpgaToJawny = false;
		this.jawnyToFpga = false;
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
		// TODO Auto-generated method stub

	}
	
	private void log(String s) {
		System.out.println("Engine: " + s);
	
	}

}
