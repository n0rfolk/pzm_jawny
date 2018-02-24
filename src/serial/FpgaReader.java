package serial;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import utilities.Frame;
import utilities.LogWriter;

/**
 * This class handles frame exchange with fpga device
 */
public class FpgaReader {
    InputStream in;
    OutputStream out;
    
    public FpgaReader (InputStream in, OutputStream out) {
        this.in = in;
        this.out = out;
    }
    
    /**
     * Method that reads single frame from input stream
     */
    public Frame readFrame() {
    	Frame currentFrame = new Frame();
    	byte[] buffer = new byte[Frame.SIZE * 2];
    	
    	int i = 0;
    	byte currentByte;
    	boolean startFrameOccured = false;
    	
    	try {
 
			while ((currentByte = (byte) in.read()) != 0x07) {
				//log("" + i);
				if (currentByte == 0x06) {
					startFrameOccured = true;
				}
				if (startFrameOccured) {
					buffer[i++] = currentByte;
				}
			}
			log("" + i);
			log("currentByte: " + currentByte);
			buffer[i] = currentByte;
			
			currentFrame = new Frame(buffer);
			Frame.printBytes(currentFrame.getBytesWithCRC());
			System.out.println("");
		} catch (IOException e) { e.printStackTrace(); }
    	
//    	try {
//			while ((len = this.in.read(buffer)) > -1) {
//				log("" + (i++) + "\n");
//				System.out.print(new String(buffer,0,len));
//			}
//			Frame.printBytes(buffer);
//		} catch (IOException e) { e.printStackTrace(); }
    	
    	
    	
    	return currentFrame;

    	
    	
    }
    
    private void log(String s) {
		LogWriter.log(this.getClass().getName(), s);
	}
}
