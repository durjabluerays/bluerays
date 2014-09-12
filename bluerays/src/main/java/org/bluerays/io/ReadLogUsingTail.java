package org.bluerays.io;

import java.io.BufferedInputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class ReadLogUsingTail {

	
	public static void main(String[] args) {
		
		
		
		try {
			Process tail=new ProcessBuilder("tail", "-100000f", "/home/introp/testfile/acct-2014-07-10-0192.csv").start();
			
			
			Scanner output=new Scanner(tail.getInputStream());
			BufferedWriter writer=new BufferedWriter(new FileWriter(new File("/home/introp/testfile/acct-2014-07-10-0192.csv"),true));
			while(output.hasNextLine()){
				String line=output.nextLine();
				System.out.println(output.nextLine());
				writer.write(line);
				writer.flush();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	
}
