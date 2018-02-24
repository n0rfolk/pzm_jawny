package serial;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import utilities.LogWriter;

public class FpgaWriter implements Runnable 
{
	InputStream in;
    OutputStream out;
    
    public FpgaWriter (InputStream in, OutputStream out) {
    	this.in = in;
        this.out = out;
    }
    
    public void run () {
        try {                
            int c = 0;
            while (( c = System.in.read()) > -1) {
                this.out.write(c);
            }                
        }
        catch (IOException e) { e.printStackTrace(); }            
    }
    
	private void log(String s) {
		LogWriter.log(this.getClass().getName(), s);
	}
}