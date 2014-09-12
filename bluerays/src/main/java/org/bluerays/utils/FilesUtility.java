package org.bluerays.utils;

import java.io.FilenameFilter;

import org.apache.commons.lang.StringUtils;
import org.bluerays.config.BasicConfiguration;


public class FilesUtility {

	private String path;	
	private String fileName;
	
	private FilesUtility() {
	}

	
	public FilesUtility (String path, String fileName){
		this.path=path;
		this.fileName=fileName;
	}
	public boolean isValidFilePath(){
		
		return true;
		
	}
	
	public boolean isValidFileExtension(){
		
		if(fileName.contains("~")||fileName.contains(".swp")){
			return false;
		}
		
		if(fileName.indexOf('.')==0)
			return false;
		return true;
	}
	
	
	public boolean isFileRegister(){
		
		if(BasicConfiguration.FILE_EXT!=null &&BasicConfiguration.FILE_EXT.size()>0){
			
			
			if(BasicConfiguration.FILE_EXT.contains(fileName)){
			return true;	
			}else {
				
				String ext=StringUtils.substring(fileName, fileName.lastIndexOf('.'), (fileName.length()-1));
				if(BasicConfiguration.FILE_EXT.contains("*."+ext)){
					return true;
				}
				
			}
			
			
		}else {
			
			return true;
		}
		
		return false;
	}
	
}
