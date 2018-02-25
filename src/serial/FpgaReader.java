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
        log(".. is listening");
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
			while ((currentByte = (byte) in.read()) != Frame.STOP) {
				//log("currentByte: " + String.format("0x%02X", currentByte));
				if (startFrameOccured) {
					buffer[i] = currentByte;
					i++;
				}
				if (currentByte == Frame.START) {
					startFrameOccured = true;
				}
			}
			// in this place buffer has whole frame without START (0x06) and without STOP (0x07)
			
		
			
			// 2 - create new Frame object 
			currentlyProcessedFrame = new Frame(Frame.deescapeBytes(buffer));
			
			log("Frame with CRC");
			Frame.printBytes(currentlyProcessedFrame.getBytesWithCRC());
			
			// 3 - calculate crc
			//log("calcCrc32: " + String.format("0x%02X", currentlyProcessedFrame.calcCrc32()));
			if (currentlyProcessedFrame.getcrc32() == currentlyProcessedFrame.calcCrc32()) {
				// 3.1
				// CRC: OK
			
				
				
				if (currentlyProcessedFrame.getType() == Frame.FIRST_FRAME) { // beginning of file
					c = new Client(ipTable.get(currentlyProcessedFrame.getIdO())); 
				}
				
				c.sendFrame(currentlyProcessedFrame);
				
				if (currentlyProcessedFrame.getType() == Frame.LAST_FRAME) { // end of file
					c.closeSession();
				}
				
				// announce frame OK
				out.write((byte) Frame.CONFIRM); 
			} else {
				// 3.2
				// CRC: ERR
				// retransmission needed
				retransmissionRequestCounter++;
				if (retransmissionRequestCounter < 50) {
					// announce error
					out.write((byte) Frame.ERROR);
				} else {
					// announce fatal error
					out.write((byte) Frame.FATAL_ERROR);
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
