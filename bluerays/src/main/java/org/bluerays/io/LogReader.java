package org.bluerays.io;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;

import org.bluerays.bluerays.FileHistory;
import org.bluerays.bluerays.LogTamplate;
import org.bluerays.config.Constants;
import org.bluerays.output.PostLogs;

import com.mongodb.BasicDBObject;


public class LogReader  extends Thread{
	
	String path;
	
	private LogReader() {
	}

	public LogReader(String currentPath){
		this.path=currentPath;
	}
	@Override
	public void run() {
		readLog(this.path);
	}
	
	public  static void readLog(String path){
		
		FileHistory fileHistory = null;
		System.out.println(path);
		LogTamplate logTamplate;
		
		RandomAccessFile accessFile = null;
		ArrayList<BasicDBObject> newMessages=new ArrayList<BasicDBObject>();
		
		long lastCommit=0;
//		while(Constants.reading){
//
//			try {
//				
//				System.out.println("waiting ");
//				Thread.sleep(100);
//			} catch (InterruptedException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}			
//		}
		
		try {
			
			String line;
			accessFile=new RandomAccessFile(new File(path), "r");	
			long count=0;

			if(Constants.fileHistoryMap.containsKey(path)){
				fileHistory=Constants.fileHistoryMap.get(path);
				
				while(fileHistory.isLineReading()){
					try {
						Thread.sleep(500);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					System.out.println("Currently File is proccessing");
				}
				
			}else {
				fileHistory=new FileHistory();
				System.out.println(path);
				fileHistory.setLineReading(true);
				Constants.fileHistoryMap.put(path, fileHistory);
			}

		lastCommit=fileHistory.getLineRead();
		
		System.out.println(lastCommit+ "|"+count);
		while((line=accessFile.readLine())!=null){

			if(count>=lastCommit){
				logTamplate=new LogTamplate();
				logTamplate.setMessage(line);
				if(logTamplate.getParseLog()!=null){
				
//					System.out.println(line);
					newMessages.add(logTamplate.getParseLog());
				
				}
				
			}

			count++;
		}

		fileHistory.setLineRead(count);
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			fileHistory.setLineReading(false);
			Constants.fileHistoryMap.put(path, fileHistory);
			Constants.reading=false;
			logTamplate=null;
		
			if(newMessages!=null)
				if(newMessages.size()>0){
					new PostLogs().postLogInESHttp(newMessages);
				}
		
		newMessages=null;
		
		if(accessFile!=null)
				try {
					accessFile.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
		}
		
	}
	
	public static boolean addFileForReading(String path){
		
		return false;
	}
	
	
	
}
