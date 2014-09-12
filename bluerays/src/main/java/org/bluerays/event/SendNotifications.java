package org.bluerays.event;

import org.apache.commons.vfs2.FileChangeEvent;
import org.apache.commons.vfs2.FileListener;
import org.apache.commons.vfs2.FileName;
import org.apache.commons.vfs2.FileObject;
import org.apache.commons.vfs2.FileSystemException;
import org.apache.commons.vfs2.FileSystemManager;
import org.apache.commons.vfs2.VFS;
import org.apache.commons.vfs2.impl.DefaultFileMonitor;
import org.bluerays.config.BasicConfiguration;
import org.bluerays.utils.FileModificationQueue;


public class SendNotifications {

	public static SendNotifications notifications;
	
	
	public static SendNotifications getNotifications() {
		
		if(notifications==null){
		
			notifications=new SendNotifications();
		
		try {
			
			FileSystemManager  manager=VFS.getManager();
			FileObject fileObject=manager.resolveFile(BasicConfiguration.paths[0]);
			
			DefaultFileMonitor fileMonitor=new DefaultFileMonitor(new  FileEventTracker());
			fileMonitor.addFile(fileObject);
			fileMonitor.setDelay(100);
			fileMonitor.setRecursive(true);
			fileMonitor.start();
			
			
			
		} catch (FileSystemException e) {
			e.printStackTrace();
		} 
		}
		return notifications;
	}
	
	
	private static class FileEventTracker implements FileListener{

		public void fileCreated(FileChangeEvent event) throws Exception {	

		String path=event.getFile().getURL().getPath();
		FileName fileName=event.getFile().getName();
		String fileBaseName=fileName.getBaseName();
		FileModificationQueue.addToQueue(path, fileBaseName);

		path=null;
		fileBaseName=null;
		fileName=null;			
		
		}


		public void fileDeleted(FileChangeEvent event) throws Exception {
			
			String path=event.getFile().getURL().getPath();
			FileModificationQueue.removeFromQueue(path);
		}

		public void fileChanged(FileChangeEvent event) throws Exception {


			
			String path=event.getFile().getURL().getPath();
			FileName fileName=event.getFile().getName();
			String fileBaseName=fileName.getBaseName();
			System.out.println("File modified");
			FileModificationQueue.addToQueue(path, fileBaseName);
			
			path=null;
			fileBaseName=null;
			fileName=null;
			
			
			
			
		}
		
	}
	
	
	
}
