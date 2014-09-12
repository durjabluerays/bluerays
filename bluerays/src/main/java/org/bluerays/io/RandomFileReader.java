package org.bluerays.io;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;

import org.bluerays.bluerays.FileHistory;
import org.bluerays.bluerays.LogTamplate;
import org.bluerays.config.Constants;
import org.bluerays.output.PostLogs;

import com.mongodb.BasicDBObject;

public class RandomFileReader extends Thread{

	String path;
	
	private RandomFileReader(){}
	public RandomFileReader(String filePath){
	this.path=filePath;
	}
	
	@Override
	public void run() {
		if(path!=null)
		readLog(path);
	}
	
	public static void main(String[] args) {
		
		
		RandomFileReader fileReader=new RandomFileReader();
		
		fileReader.readLog("/home/introp/testfile/1acct-2014-07-10-0192.csv");
//		fileReader.readFile("/home/introp/testfile/test72");
	}
	
	
	public void readLog(String path){
		
		FileHistory fileHistory=null;
		ArrayList<BasicDBObject> newMessages=new ArrayList<BasicDBObject>();
		LogTamplate logTamplate;		
		RandomAccessFile file=null;
		
		try {
			System.out.println(path);
			
			if(Constants.fileHistoryMap.containsKey(path)){
				fileHistory=Constants.fileHistoryMap.get(path);
				
				while(fileHistory.isLineReading()){
					try {
						System.out.println("Currently File is proccessing");
						Thread.sleep(500);
					
					} catch (InterruptedException e) {
					
						e.printStackTrace();
					
					}
					fileHistory=Constants.fileHistoryMap.get(path);
					fileHistory.setLineReading(true);
					
				}
			}else {
				
				fileHistory=new FileHistory();
				System.out.println(path);
				fileHistory.setLineReading(true);
				Constants.fileHistoryMap.put(path, fileHistory);
			
			}

			file=new RandomAccessFile(path, "r");
			file.seek(fileHistory.getOffsetRead());

			String line=null;
			long length=file.length();
			fileHistory.setLineReading(true);
			while((line=file.readLine())!=null){

				fileHistory.setOffsetRead(file.getFilePointer());
				logTamplate=new LogTamplate();
				logTamplate.setMessage(line);
				
				if(logTamplate.getParseLog()!=null){
					newMessages.add(logTamplate.getParseLog());
				}
				
				if(file.getFilePointer()==length){
					fileHistory.setLineReading(false);
					break;
				}
				
			}
		
			file.close();

		} catch (FileNotFoundException e) {
			
			e.printStackTrace();
		
		} catch (IOException e) {
		
			e.printStackTrace();

		}finally{
			
			fileHistory.setLineReading(false);
			Constants.fileHistoryMap.put(path, fileHistory);
			logTamplate=null;
		
			if(newMessages!=null)
				if(newMessages.size()>0){
					new PostLogs().postLogInESHttp(newMessages);
				}
		
			if(file!=null){

				try {
					file.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
				
			}
		}
		
		
		
	}
	
}
