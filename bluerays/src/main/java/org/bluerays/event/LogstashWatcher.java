package org.bluerays.event;

import java.util.HashMap;
import java.util.Map;

public class LogstashWatcher {

	
	public static Map<String, Integer> fileMonitoringMap=new HashMap();
	
	public static void main(String[] args) {
		SendNotifications.getNotifications();
		for(Map.Entry<String, Integer> s: fileMonitoringMap.entrySet()){
		System.out.println(s.getKey()+"|"+s.getValue());
		}
		
		while(true){
			
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		}
	
	
}
