package serial;

import java.io.IOException;
import java.io.InputStream;

public class FpgaReader implements Runnable {
    InputStream in;
    
    public FpgaReader (InputStream in) {
        this.in = in;
    }
    
    /**
     * Function that reads single frame from fpga
     */
    public void run () {
    	// TODO: implement behavior
//        byte[] buffer = new byte[1024];
//        int len = -1;
//        try {
//            while ((len = this.in.read(buffer)) > -1) {
//                System.out.print(new String(buffer,0,len));
//            }
//        } catch (IOException e) { e.printStackTrace(); }            
    }
}
