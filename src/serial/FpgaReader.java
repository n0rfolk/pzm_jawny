package serial;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import socket.Client;
import utilities.Frame;
import utilities.LogWriter;

/**
 * This class handles frames comming from fpga
 */
public class FpgaReader implements Runnable  {
    InputStream in;
    OutputStream out;
    
    Frame currentlyProcessedFrame;
    
    Client c;
    
    
    public FpgaReader (InputStream in, OutputStream out) {
        this.in = in;
        this.out = out;
        this.currentlyProcessedFrame = new Frame();
        this.c = new Client("192.168.0.112");
    }
    
    /**
     * Method that processes single frame 
     * 
     * Steps:
     * 1. reads frame from input stream
     * 2. creates Frame object from read frame
     * 3. calculate CRC
     *    3.1 crc OK -> 
     *    
     *    3.2 crc ERR ->	
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
				if (currentByte == 0x06) {
					startFrameOccured = true;
				}
				if (startFrameOccured) {
					buffer[i++] = currentByte;
				}
			}
			buffer[i] = currentByte;
			
			
			// 2 - create new Frame object 
			currentlyProcessedFrame = new Frame(Frame.deescapeBytes(buffer));
			Frame.printBytes(currentlyProcessedFrame.getBytesWithCRC());
			
			// 3 - calculate crc
			if (currentlyProcessedFrame.getcrc32() == currentlyProcessedFrame.calcCrc32()) {
				// 3.1
				c.sendFrame(currentlyProcessedFrame);
			} else {
				//TODO: retransmission
			}
			
		} catch (IOException e) { e.printStackTrace(); }
    	
    	currentlyProcessedFrame = new Frame();
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
