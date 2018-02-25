package serial;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Map.Entry;

import socket.Client;
import utilities.FileLoader;
import utilities.Frame;
import utilities.LogWriter;

/**
 * This class handles frames comming from fpga
 */
public class FpgaReader implements Runnable  {
	
    private Frame currentlyProcessedFrame; // temporary frame variable
    private int retransmissionRequestCounter;
	
	// serial comm streams
    InputStream in;
    OutputStream out;
    
    // socket comm minsc
    Client c;
    ArrayList<String> ipTable; // maps ids to ip adresses
    
    
    // constructor
    public FpgaReader (InputStream in, OutputStream out) {
        this.in = in;
        this.out = out;
        this.retransmissionRequestCounter = 0;
        this.currentlyProcessedFrame = null;
        this.ipTable = FileLoader.getIpTable(); // load ipTables from file
    }
    
    /**
     * Method that processes single frame 
     * 
     * Steps:
     * 1. reads frame from input stream
     * 2. creates Frame object from read frame
     * 3. calculate CRC
     *    3.1 CRC: OK  -> send to remote via Sockets and send confirmation to fpga
     *    3.2 CRC: ERR -> request retransmission from fpga
     *    
     * 		
     */
    public void processFrame() {
    	
    	// 1 - receive whole frame from fpga device 
    	byte[] buffer = new byte[Frame.SIZE * 2];
    	
    	int i = 0;
    	byte currentByte;
    	boolean startFrameOccured = false;
    	
    	try {
			while ((currentByte = (byte) in.read()) != 0x07) {
				//log("currentByte: " + String.format("0x%02X", currentByte));
				if (startFrameOccured) {
					buffer[i] = currentByte;
					i++;
				}
				if (currentByte == 0x06) {
					startFrameOccured = true;
				}
			}
			// in this place buffer has whole frame without START (0x06) and without END (0x07)
			
		
			
			// 2 - create new Frame object 
			currentlyProcessedFrame = new Frame(Frame.deescapeBytes(buffer));
			
			log("Frame with CRC");
			Frame.printBytes(currentlyProcessedFrame.getBytesWithCRC());
			
			// 3 - calculate crc
			if (currentlyProcessedFrame.getcrc32() == currentlyProcessedFrame.calcCrc32()) {
				// 3.1
				// CRC: OK
			
				
				
				if (currentlyProcessedFrame.getType() == 0x00) { // beginning of file
					c = new Client(ipTable.get(currentlyProcessedFrame.getIdO())); 
				}
				
				c.sendFrame(currentlyProcessedFrame);
				
				if (currentlyProcessedFrame.getType() == 0x01) { // end of file
					c.closeSession();
				}
				
				// announce frame OK
				out.write((byte) 0x05); 
			} else {
				// 3.2
				// CRC: ERR
				// retransmission needed
				retransmissionRequestCounter++;
				if (retransmissionRequestCounter < 50) {
					// announce error
					out.write((byte) 0x04);
				} else {
					// announce fatal error
					out.write((byte) 0x08);
					retransmissionRequestCounter = 0;
				}
			}
			
		} catch (IOException e) { e.printStackTrace(); }
    	
    	// clean up temporary variables
    	retransmissionRequestCounter = 0;
    	currentlyProcessedFrame = null;
    }

    
    private void log(String s) {
		LogWriter.log(this.getClass().getName(), s);
	}

	@Override
	public void run() {
		while (true) {
			processFrame();
		}	
	}
}
