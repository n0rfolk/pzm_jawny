package utilities;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

public class FileLoader {
	
	public static ArrayList<String> getIpTable() {
		String fileName = "ipTable";
		ArrayList<String> ipTable = new ArrayList<String>();
		try {
			Path filePath = Paths.get("./" + fileName);
			FileReader fileReader;
			fileReader = new FileReader(filePath.toString());
			BufferedReader bufferedReader = new BufferedReader(fileReader); 
			String textLine = bufferedReader.readLine();
			do {
			  ipTable.add(textLine);
			  System.out.println(textLine);
			  textLine = bufferedReader.readLine();
			} while(textLine != null);
			bufferedReader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return ipTable;	
	}
}
