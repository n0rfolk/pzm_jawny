package utilities;

import java.util.Enumeration;

import gnu.io.CommPortIdentifier;

public class SerialComm {
	private Enumeration<?> portList;
	private CommPortIdentifier commPortIdentifier;
	
	
	public SerialComm() {
		
	}
	
	/**
	 * Functions scans for available serial communication ports
	 * and stores them in portList variable.
	 */
	private void scanAvailableCommPorts() {
		CommPortIdentifier tmp;
        portList = CommPortIdentifier.getPortIdentifiers();
        LogWriter.log("Printing available ports..");
		while (portList.hasMoreElements()) {
			tmp = (CommPortIdentifier) portList.nextElement();
			if(tmp.getPortType() == CommPortIdentifier.PORT_SERIAL) {
				LogWriter.log(tmp.getName());
			}
		}
	}
	
	private void openCommPort() {
		
	}
}
