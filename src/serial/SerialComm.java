package serial;

import gnu.io.CommPort;
import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;
import utilities.LogWriter;

//(new TwoWaySerialComm()).connect("COM3");

public class SerialComm {
	
	public SerialComm() {
		try {
			connect("COM6");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	void connect (String portName) throws Exception {
        CommPortIdentifier portIdentifier = CommPortIdentifier.getPortIdentifier(portName);
        if (portIdentifier.isCurrentlyOwned()) {
            LogWriter.log("Error: Port is currently in use");
        }
        else {
            CommPort commPort = portIdentifier.open(this.getClass().getName(),2000);
            
            if ( commPort instanceof SerialPort ) {
                SerialPort serialPort = (SerialPort) commPort;
                serialPort.setSerialPortParams(57600,SerialPort.DATABITS_8,SerialPort.STOPBITS_1,SerialPort.PARITY_NONE);
                LogWriter.log("Port opening succeded!");
                //InputStream in = serialPort.getInputStream();
                //OutputStream out = serialPort.getOutputStream();
                
                //(new Thread(new SerialReader(in))).start();
                //(new Thread(new SerialWriter(out))).start();

            }
            else {
            	 LogWriter.log("Error: Only serial ports are handled by this example.");
            }
        }     
    }
}
