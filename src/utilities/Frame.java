package utilities;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.zip.CRC32;
import java.util.zip.Checksum;

public class Frame {
	//Frame structure
	public static final int SIZE = 87; // in bytes
	public static final int dataSize = 64;
	public static final int nonceSize = 12;
	byte type;
	byte idO;
	byte idN;
	int nr;
	byte[] nonce;
	byte[] data;
	int crc32;
	
	//flags
	public final static byte START = 0x06;
	public final static byte STOP = 0x07;
	public final static byte ESCAPE = 0x14;
	public final static byte XORVALUE = 0x20;
	public final static byte CONFIRM = 0x05;
	public final static byte ERROR = 0x04;
	public final static byte FATAL_ERROR = 0x08;
	public final static byte FIRST_FRAME = 0x10;
	public final static byte LAST_FRAME = 0x01;
	public final static byte SINGLE_FRAME = 0x03;
	
	//Frame constructor filling data with 1
	public Frame() {
		this.nonce = new byte[nonceSize];
		Arrays.fill(this.nonce, (byte) 1);
		this.data = new byte[dataSize];
		Arrays.fill(this.data, (byte) 1);
	}
	
	//Frame unpacking from byte[]
	public Frame(byte[] bytes) {
		this.type = bytes[0];
		this.idO = bytes[1];
		this.idN = bytes[2];
		this.nr = bytesToInt(new byte[] {bytes[3], bytes[4], bytes[5], bytes[6]});
		
		//filling nonce
		byte[] nonceBytes = new byte[nonceSize];
		Arrays.fill(nonceBytes, (byte) 0);
		for (int i = 0; i < nonceSize; i++) {
			nonceBytes[i] = bytes[7 + i];
		}
		this.nonce = nonceBytes;
		
		//filling data
		byte[] dataBytes = new byte[dataSize];
		Arrays.fill(dataBytes, (byte) 0);
		for (int i = 0; i < dataSize; i++) {
			dataBytes[i] = bytes[nonceSize + 7 + i];
		}
		this.data = dataBytes;
		
		this.crc32 = bytesToInt(new byte[] {bytes[SIZE-4], bytes[SIZE-3], bytes[SIZE-2], bytes[SIZE-1]});			
	}
	
	//Returns byte[] of Frame without CRC
	public byte[] getBytes() {
		byte[] typeTab = new byte[1]; 
		typeTab[0] = this.getType();
		byte[] IdOTab = new byte[1];
		IdOTab[0] = this.getIdO();
		byte[] IdNTab = new byte[1];
		IdNTab[0] = this.getIdN();
		byte[] bytes = bytesConcatenation(typeTab, IdOTab, IdNTab, this.intToBytes(this.getNr()), this.getNonce(), this.getData());
		return bytes;
	}

	//Returns calculated crc of a Frame
	public int calcCrc32() {
		Checksum checksum = new CRC32();
		byte[] bytes = this.getBytes();
		checksum.update(bytes, 0, bytes.length);
		return (int) checksum.getValue();
	}
	
	//returns concatenated bytes from Frame. Frame constructing without crc, used in getBytes()
	public byte[] bytesConcatenation(byte[] type, byte[] idO, byte[] idN, byte[] nr, byte[] nonce, byte[] data) {
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream( );
		try {
			outputStream.write(type);
			outputStream.write(idO);
			outputStream.write(idN);
			outputStream.write(nr);
			outputStream.write(nonce);
			outputStream.write(data);
		} catch (IOException e) {
			e.printStackTrace();
		}

		byte result[] = outputStream.toByteArray( );
		return result;
	}
	
	//returns byte[] of frame with crc of this frame
	public byte[] getBytesWithCRC() {
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream( );
		try {
			outputStream.write( this.getBytes() );
			outputStream.write( ByteBuffer.allocate(4).putInt(this.getcrc32()).array());
		} catch (IOException e) {
			e.printStackTrace();
		}
		byte result[] = outputStream.toByteArray( );
		return result;
	}
	
	// escape
	public static byte[] escapeBytes(byte[] bytes) {
		List<Byte> bytesList = new ArrayList<Byte>();
		for (int i = 0; i < bytes.length; i++) {
			if (bytes[i] == (byte) START || bytes[i] == (byte) STOP || bytes[i] == (byte) ESCAPE) {
				bytesList.add((byte) ESCAPE);
				bytesList.add((byte) (bytes[i] ^ (byte) XORVALUE));
			}
			else {
				bytesList.add(bytes[i]);
			}
		}
		
		byte[] trueBytes = new byte[bytesList.size()];
		for (int i = 0; i < bytesList.size(); i++) {
			trueBytes[i] = bytesList.get(i);
		}
		return trueBytes;
	}
	
	// de-escape
	public static byte[] deescapeBytes(byte[] bytes) {
		List<Byte> bytesList = new ArrayList<Byte>();
		boolean escaped = false;
		for (int i = 0; i < bytes.length; i++) {
			if (escaped == false && (bytes[i] == (byte) START || bytes[i] == (byte) STOP || bytes[i] == (byte) ESCAPE)) { 
				bytes[i + 1] ^= XORVALUE;
				escaped = true;
			}
			else {
				bytesList.add(bytes[i]);
				escaped = false;
			}
		}
		
		byte[] trueBytes = new byte[bytesList.size()];
		for (int i = 0; i < bytesList.size(); i++) {
			trueBytes[i] = bytesList.get(i);
		}
		return trueBytes;
	}
	
	//prints byte[]
	public static void printBytes(byte[] bytes) {
		for (int i = 0; i < bytes.length; i++) {
			System.out.print(String.format("%02X ",  bytes[i]));
		}
		System.out.println("");
	}
	
	//returns conversion from short to byte[]
	public byte[] shortToBytes(short value) {
		ByteBuffer buffer = ByteBuffer.allocate(2);
		buffer.putShort(value);
		return buffer.array();
	}
	
	//returns conversion from int to byte[]
	public byte[] intToBytes(int value) {
		ByteBuffer buffer = ByteBuffer.allocate(4);
		buffer.putInt(value);
		return buffer.array();
	}
	
	//returns conversion from byte[] to short
	public short bytesToShort(byte[] bytes) {
		short s = (short) (bytes[0]<<8 | bytes[1] & 0xFF);
		return s;
	}
	
	//returns conversion from byte[] to int
	int bytesToInt(byte[] bytes) {
	     return ByteBuffer.wrap(bytes).getInt();
	}
	
	//getters section
	public byte getType() {
		return type;
	}
	
	private byte[] getNonce() {
		return nonce;
	}
	
	public byte[] getData() {
		return data;
	}

	public int getcrc32() {
		return crc32;
	}

	public byte getIdO() {
		return idO;
	}


	public byte getIdN() {
		return idN;
	}

	public int getNr() {
		return nr;
	}	
}
