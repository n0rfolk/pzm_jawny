package serial;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Map.Entry;
import java.util.Scanner;
import java.util.Vector;
import java.util.Enumeration;

import gnu.io.CommPort;
import gnu.io.CommPortIdentifier;
import gnu.io.NoSuchPortException;
import gnu.io.PortInUseException;
import gnu.io.SerialPort;
import gnu.io.UnsupportedCommOperationException;
import socket.Server;
import utilities.LogWriter;
import utilities.NoAvailableSerialPortException;

public class Communication {
	// serial comm streams
	private InputStream in; 
    private OutputStream out; 


	public Communication() {
		this.in = null;
		this.out = null;
		
		try {
			pickSerialPort();
		} catch (NoAvailableSerialPortException e) { e.printStackTrace();	}
	
	}
	
	/**
	 * Starts two threads that are handling whole incoming and outcoming
	 * packets and their corresponding signals.
	 */
	public void deploy() {
		(new Thread(new FpgaReader(in, out))).start();
        //(new Thread(new Server(in, out))).start();
	}
	
	/**
	 * Allows to pick serial port that will be used for further communication.
	 * It scans for available ports and waits for user to decide which one to open.
	 * @throws Exception 
	 */
	private void pickSerialPort() throws NoAvailableSerialPortException {
		Enumeration<?> portListInterface;
		ArrayList<CommPortIdentifier> portList = new ArrayList<>();
		CommPortIdentifier serialPortId;
		SerialPort port = null;
		
		log("Looking for available serial ports..");
		portListInterface = CommPortIdentifier.getPortIdentifiers();

		if (portListInterface.hasMoreElements()) {
			log("Found some serial ports:");
			// log what we've found
			while (portListInterface.hasMoreElements()) {
				serialPortId = (CommPortIdentifier) portListInterface.nextElement();
			
				if(serialPortId.getPortType() == CommPortIdentifier.PORT_SERIAL) {
					portList.add(serialPortId);
					log(serialPortId.getName());
				}
			}
			// ask user which one he wants to use
			Scanner reader = new Scanner(System.in); 
			System.out.println("Enter port name: ");
			String chosenName = reader.nextLine(); 
			reader.close();
			
			// try to open it
			try {
				serialPortId = CommPortIdentifier.getPortIdentifier(chosenName);
				 if (serialPortId.isCurrentlyOwned()) {
					 log("Port opening: ERR - other application is using this port");
				 }
				 port = (SerialPort) serialPortId.open(this.getClass().getName(),2000);
					log("Port opening: OK");
					port.setSerialPortParams(115200, SerialPort.DATABITS_8, SerialPort.STOPBITS_1, SerialPort.PARITY_NONE); 
					log("Port setting params: OK");
			} catch (Exception e1) { e1.printStackTrace(); }
			

            // assign streams      			                
            try {
				this.in = port.getInputStream();
				this.out = port.getOutputStream();
			} catch (IOException e) { e.printStackTrace(); }
            log("Assigning comm streams: OK");

		} else {
			log("There are no serial ports available. Terminating..");
			throw new NoAvailableSerialPortException();
		}
	}

	private void log(String s) {
		LogWriter.log(this.getClass().getName(), s);
	}
	
	public static void main(String[] args) {
		Communication c = new Communication();
		c.deploy();
	}
}
