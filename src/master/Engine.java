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
		// TODO Auto-generated method stub
		
	}



	private void listenToStartFrame() {
		
		// TODO Auto-generated method stub
		
	}



	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
