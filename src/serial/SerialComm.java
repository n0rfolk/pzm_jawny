package serial;

import gnu.io.CommPort;
import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;
import utilities.LogWriter;

public class SerialComm {
	
	public SerialComm() {
		try {
			connect("COM6");
		} catch (Exception e) { e.printStackTrace(); }
	}

	void connect (String portName) throws Exception {
		log("Setting up..");
        CommPortIdentifier portIdentifier = CommPortIdentifier.getPortIdentifier(portName);
        if (portIdentifier.isCurrentlyOwned()) {
            log("Port opening: X - Port is currently in use");
        }
        else {
            CommPort commPort = portIdentifier.open(this.getClass().getName(),2000);
            
            if (commPort instanceof SerialPort) {
                SerialPort serialPort = (SerialPort) commPort;
                serialPort.setSerialPortParams(115200, SerialPort.DATABITS_8, SerialPort.STOPBITS_1, SerialPort.PARITY_NONE); 
                log("Port opening: OK");
                
                //InputStream in = serialPort.getInputStream();
                //OutputStream out = serialPort.getOutputStream();
                
                //(new Thread(new SerialReader(in))).start();
                //(new Thread(new SerialWriter(out))).start();

            }
            else {
            	 log("Port opening: X - Only serial ports can be used");
            }
        }
        log("Setting up succeeded!");
    }
	
	private void log(String s) {
		LogWriter.log(this.getClass().getName(), s);
	}
}
