package org.bluerays.utils;

import java.util.Queue;
import java.util.concurrent.LinkedBlockingDeque;

import org.bluerays.config.Constants;
import org.bluerays.io.RandomFileReader;

public class FileModificationQueue {

	
	public static Queue< String> filesQueue=new LinkedBlockingDeque<String>();

	public static Queue<String> getFilesQueue() {
		return filesQueue;
	}

	
	
	public static boolean addToQueue(String path ,String fileName){
		
		FilesUtility files=new FilesUtility(path ,fileName);
		System.out.println(path);
		
		if(files.isValidFileExtension())
			if(files.isValidFilePath() && files.isFileRegister())
			{
					if(getFilesQueue().add(path)){
						System.out.println("File Added in  Queue : " + path);
//						new  LogReader(path).start();
						
						new RandomFileReader(path).start();;
						return true;
					}
			}
		
		return false;
	}



	public static void removeFromQueue(String path) {

		if(Constants.fileHistoryMap.containsKey(path)){
			Constants.fileHistoryMap.remove(path);
			System.out.println("File Removed "+ path);
		}
		
		
	}
	
	
	
}
