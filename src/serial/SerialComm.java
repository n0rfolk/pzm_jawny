package serial;

import java.io.InputStream;
import java.io.OutputStream;

import gnu.io.CommPort;
import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;
import utilities.LogWriter;

public class SerialComm {
	private InputStream in; 
    private OutputStream out; 
    
	public SerialComm(String portName) throws Exception {
		log("Setting up..");
        CommPortIdentifier portIdentifier = CommPortIdentifier.getPortIdentifier(portName);
        if (portIdentifier.isCurrentlyOwned()) {
            log("Port opening: ERR - Port is currently in use");
        }
        else {
            CommPort commPort = portIdentifier.open(this.getClass().getName(),2000);
            
            if (commPort instanceof SerialPort) {
                SerialPort serialPort = (SerialPort) commPort;
                serialPort.setSerialPortParams(115200, SerialPort.DATABITS_8, SerialPort.STOPBITS_1, SerialPort.PARITY_NONE); 
                log("Port opening: OK");
                
                this.in = serialPort.getInputStream();
                this.out = serialPort.getOutputStream();
            }
            else {
            	 log("Port opening: ERR - Only serial ports can be used");
            }
            log("Setting up succeeded!");
        }
        
	}
	
	public InputStream getInputStream() {
		return in;
	}

	public OutputStream getOutputStream() {
		return out;
	}
	
	private void log(String s) {
		LogWriter.log(this.getClass().getName(), s);
	}
}
