package org.bluerays.config;

import java.util.HashMap;
import java.util.Map;

import org.bluerays.bluerays.FileHistory;


public class Constants {

	public static String patternName="patternName",
			patternConfigName="pattern.name",
			message="message",
			DELIMITER="delimiter",
			MESSAGE_HEADER="messageHeader",
			PROJECT = "project",
			INDEX = "index",
			HOST = "host",
			VERSION = "@version",
			ES_INDEX_NAME = "logstash-";
	
	
	/**
	 * date parsing constant 
	 */
	public static char plus='+',
			space=' ',
			tab='	',
			HIPEN = '-',
			comma=',',
			semiColon=';',
			colon=':';

	public static int BULK_BOX_SIZE = 10000;
	
	public static boolean reading=false;
	
	public static Map<String, Integer>lastCommitPoint=new HashMap();
	
	public static Map<String, FileHistory> fileHistoryMap=new HashMap<String, FileHistory>();
	
	
	
}
