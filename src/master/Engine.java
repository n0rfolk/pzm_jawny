package master;

import java.util.Enumeration;

import gnu.io.CommPortIdentifier;
import utilities.Frame;

public class Engine {
	boolean fpgaToJawny;
	boolean jawnyToFpga;
	
	
	public Engine() {
		this.fpgaToJawny = false;
		this.jawnyToFpga = false;
		
		// for debugging purposes
		printAvailableComPorts();
		
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
	
	private void printAvailableComPorts() {
	        Enumeration<?> portList;
	        CommPortIdentifier serialPortId;
	
	        portList = CommPortIdentifier.getPortIdentifiers();
	        log("Available ports: ");
			while (portList.hasMoreElements()) {
				serialPortId = (CommPortIdentifier) portList.nextElement();
				if(serialPortId.getPortType() == CommPortIdentifier.PORT_SERIAL) {
					log(serialPortId.getName());
				}
			}
			
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
	
	private void log(String s) {
		System.out.println("Engine: " + s);
	
	}

}
