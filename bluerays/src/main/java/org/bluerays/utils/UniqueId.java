package org.bluerays.utils;

import java.util.UUID;

public class UniqueId {

	
	public static synchronized String getId(){
		return System.nanoTime()+""+UUID.randomUUID();
	}
	
}
