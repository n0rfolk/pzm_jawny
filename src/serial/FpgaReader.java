package serial;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import socket.Client;
import utilities.FileLoader;
import utilities.Frame;
import utilities.LogWriter;

/**
 * This class handles frames coming from fpga
 */
public class FpgaReader implements Runnable  {
	
    private Frame currentFrame; // temporary frame variable
    private int retransmissionRequestCounter;
	
	// serial comm streams
    InputStream in;
    OutputStream out;
    
    // socket comm misc
    Client c;
    ArrayList<String> ipTable; // maps ids to ip adresses
    
    
    // constructor
    public FpgaReader (InputStream in, OutputStream out) {
        this.in = in;
        this.out = out;
        this.retransmissionRequestCounter = 0;
        this.currentFrame = null;
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
    	//byte[] buffer = new byte[Frame.SIZE * 2];
    	ArrayList<Byte> buffer = new ArrayList<Byte>();
    	
    	//int i = 0;
    	byte currentByte;
    	boolean startFrameOccured = false;
    	
    	try {
			while ((currentByte = (byte) in.read()) != Frame.STOP) {
				//log("currentByte: " + String.format("0x%02X", currentByte));
				if (startFrameOccured) {
					buffer.add(currentByte);
					//buffer[i] = currentByte;
					//i++;
				}
				if (currentByte == Frame.START) {
					startFrameOccured = true;
				}
			}
			// in this place buffer has whole frame without START (0x06) and without STOP (0x07)
			
			//convert ArrayList<Byte> to byte[]
			byte[] bufferBytes = new byte[buffer.size()];
			for (int i = 0; i < buffer.size(); i++) {
				bufferBytes[i] = buffer.get(i);
			}
			
			// 2 - create new Frame object 
			currentFrame = new Frame(Frame.deescapeBytes(bufferBytes));
			
			log("Frame with CRC");
			Frame.printBytes(currentFrame.getBytesWithCRC());
			
			// 3 - calculate crc
			//log("calcCrc32: " + String.format("0x%02X", currentlyProcessedFrame.calcCrc32()));
			if (currentFrame.getcrc32() == currentFrame.calcCrc32()) {
				// 3.1
				// CRC: OK
				
				if (currentFrame.getType() == Frame.FIRST_FRAME || currentFrame.getType() == Frame.SINGLE_FRAME) { 
					c = new Client(ipTable.get(currentFrame.getIdO())); 
				}
				
				c.sendFrame(currentFrame);
				
				if (currentFrame.getType() == Frame.LAST_FRAME || currentFrame.getType() == Frame.SINGLE_FRAME) {
					c.closeSession();
				}
				
				out.write((byte) Frame.CONFIRM); 
			} else {
				// 3.2
				// CRC: ERR
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
    	currentFrame = null;
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
