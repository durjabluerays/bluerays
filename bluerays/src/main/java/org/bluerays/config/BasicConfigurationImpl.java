package org.bluerays.config;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.lang.StringUtils;

import com.mongodb.BasicDBObject;
import com.mongodb.util.JSON;

final class BasicConfigurationImpl {

	
	
	private static String hostName,
	IPV4,
	IPV6, 
	deviceName,
	deviceId,
	clusterName, 
	currentWorkingDir,
	logStashLogsPath;

	private static int  IPV4TCPPort=9300,
			IPV6TCPPort=9300,
			HttpPort=9200;
	
	private static BufferedReader reader;

	private static Map<String, BasicDBObject> basePatternMap;
	private static Map<String, CSVFormat> headerPatternMap=new HashMap<String, CSVFormat>();
	
	private static String [] dynamicPattern,
	paths;
	
	
	private static Set<String> fileExt;
	
	
	
	public static  Set<String> getFileExt() {
		return fileExt;
	}


	public static void setFileExt(Set<String> fileExt) {
		fileExt = fileExt;
	}


	protected static Map<String, CSVFormat> getHeaderPatternMap() {
		return headerPatternMap;
	}


	protected static String[] getDynamicPattern() {
		return dynamicPattern;
	}


	protected static void setDynamicPattern(String[] dynamicPattern) {
		BasicConfigurationImpl.dynamicPattern = dynamicPattern;
	}


	protected static Map<String, BasicDBObject> getBasePatternMap() {
		return basePatternMap;
	}


	protected static void setBasePatternMap(Map<String, BasicDBObject> basePatternMap) {
		BasicConfigurationImpl.basePatternMap = basePatternMap;
	}


	protected static int getIPV4TCPPort() {
		return IPV4TCPPort;
	}


	protected static void setIPV4TCPPort(int iPV4TCPPort) {
		IPV4TCPPort = iPV4TCPPort;
	}


	protected static int getIPV6TCPPort() {
		return IPV6TCPPort;
	}


	protected static void setIPV6TCPPort(int iPV6TCPPort) {
		IPV6TCPPort = iPV6TCPPort;
	}


	protected static int getHttpPort() {
		return HttpPort;
	}


	protected static void setHttpPort(int httpPort) {
		HttpPort = httpPort;
	}


	protected static String getLogStashLogsPath() {
		return logStashLogsPath;
	}


	protected static void setLogStashLogsPath(String logStashLogsPath) {
		BasicConfigurationImpl.logStashLogsPath = logStashLogsPath;
	}


	protected static boolean isEnableIPV4() {
		return enableIPV4;
	}


	protected static void setEnableIPV4(boolean enableIPV4) {
		BasicConfigurationImpl.enableIPV4 = enableIPV4;
	}


	protected static boolean isEnableIPV6() {
		return enableIPV6;
	}


	protected static void setEnableIPV6(boolean enableIPV6) {
		BasicConfigurationImpl.enableIPV6 = enableIPV6;
	}


	private static boolean enableIPV4;
	
	private static boolean enableIPV6;
	
	
	
	
	
	
	protected static String[] getPaths() {
		return paths;
	}


	protected static void setPaths(String[] paths) {
		BasicConfigurationImpl.paths = paths;
	}


	protected static String getCurrentWorkingDir() {
		return currentWorkingDir;
	}


	protected static void setCurrentWorkingDir(String currentWorkingDir) {
		BasicConfigurationImpl.currentWorkingDir = currentWorkingDir;
	}


	protected static String getDeviceName() {
		return deviceName;
	}


	protected static void setDeviceName(String deviceName) {
		BasicConfigurationImpl.deviceName = deviceName;
	}


	protected static String getDeviceId() {
		return deviceId;
	}


	protected static void setDeviceId(String deviceId) {
		BasicConfigurationImpl.deviceId = deviceId;
	}


	protected static String getClusterName() {
		return clusterName;
	}


	protected static void setClusterName(String clusterName) {
		BasicConfigurationImpl.clusterName = clusterName;
	}


	protected static String getIPV4() {
		return IPV4;
	}


	protected static void setIPV4(String iPV4) {
		IPV4 = iPV4;
	}


	protected static String getIPV6() {
		return IPV6;
	}


	protected static void setIPV6(String iPV6) {
		IPV6 = iPV6;
	}


	protected static String getHostName() {
		return hostName;
	}


	private static void setHostName(String host) {
		hostName = host;
	}


	static{
		
		
		try {
			
			InetAddress localhost=InetAddress.getLocalHost();
			
			setHostName(localhost.getHostName());
			
			setCurrentWorkingDir(System.getProperty("user.dir"));
			
			init();
			initPattern();
			
		} catch (UnknownHostException e) {

			e.printStackTrace();
		} catch (Exception e) {
			
			e.printStackTrace();
		}
		
		
	}
	
	
	private static void init() throws Exception {
		
		File file =new File("../config/logstash.config");
		file.createNewFile();
		
		reader=new BufferedReader(new FileReader(file));
		
		String line=null;
		
		int count=0;
		
		while((line=reader.readLine())!=null){
			
			count++;

			if(line.trim().length()>0)

				if(!StringUtils.contains(line, '#')){

					String [] data=StringUtils.split(line,':');

					if(data.length==2){

						if("cluster.name".equalsIgnoreCase(data[0]))
							setClusterName(data[1].trim());
						else
							if("host.name".equalsIgnoreCase(data[0]))
								setHostName(data[1].trim());
							else
								if("device.name".equals(data[0]))
									setDeviceName(data[1].trim());
								else 
									if("device.id".equals(data[0]))
										setDeviceId(data[1].trim());
									else
										if("IPV4".equalsIgnoreCase(data[0])){

											if(Boolean.getBoolean(data[1].trim()))
												setEnableIPV4(true);
										}
										else 
											if("IPV6".equalsIgnoreCase(data[0])){

												if(Boolean.getBoolean(data[1].trim()))													
													setEnableIPV6(true);
											}
											else

												if("paths".equalsIgnoreCase(data[0]))
													setPaths(StringUtils.split(data[1].trim(), Constants.comma));
												else 
													if("elasticsearch.tcp.IPV4.port".equals(data[0])){

														String [] ipPort=StringUtils.split(data[1],Constants.semiColon);
														
														if(ipPort.length==2){
															
															setIPV4(ipPort[0].trim());
															
															setIPV4TCPPort(Integer.parseInt(ipPort[1].trim()));
														}
														else {
															reader.close();
															throw new Exception("Invalide port  in "+file.getCanonicalPath()+" At line " +count);
														}
													}
													else
														if("elasticsearch.http.IPV6.port".equalsIgnoreCase(data[0])){

															String [] ipPort=StringUtils.split(data[1],Constants.semiColon);
															if(ipPort.length==2){
																
																setIPV6(ipPort[0]);
																
																setIPV6TCPPort(Integer.parseInt(ipPort[1].trim()));
															
															}
															else {
																reader.close();
																throw new Exception("Invalide port  in "+file.getCanonicalPath()+" At line " +count);
															}
															
														}
														else 
															if("log".equalsIgnoreCase(data[0]))

																setLogStashLogsPath(data[1].trim());
															else if("file.ext".equalsIgnoreCase(data[0])){
																
																
																String []fileExt=StringUtils.split(data[1],',');
																Set<String> extSet=new HashSet<String>();
																
																for(String s: fileExt){
																	extSet.add(s.trim());
																}
																
																setFileExt(extSet);
															}
															else	
															if(Constants.patternConfigName.equalsIgnoreCase(data[0])){
																	
																	setDynamicPattern(StringUtils.split(data[1].trim(),Constants.comma));
																}
																else
																	if("elasticsearch.http.IPV4.port".equalsIgnoreCase(data[0])){

																		String [] ipPort=StringUtils.split(data[1],Constants.semiColon);
																		if(ipPort.length==2){
																			
																			setIPV4(ipPort[0]);
																			setHttpPort(Integer.parseInt(ipPort[1].trim()));
																		
																		}
																		else {
																			reader.close();
																			throw new Exception("Invalide port  in "+file.getCanonicalPath()+" At line " +count);
																		}			
																		
																	}else
																	{
																		reader.close();
																		throw new Exception("Invalide property defination in "+file.getCanonicalPath()+" At line " +count);
																	}
						

			}else {
				reader.close();
				throw new Exception("Invalide property define in "+file.getCanonicalPath()+" At line " +count);
			}
				}
		}

		reader.close();
			
	}

	
	private static void initPattern() throws IOException{
		
		File pattern=new File("../patterns/");
		
		basePatternMap=new HashMap<String, BasicDBObject>();
		
		for(File file: pattern.listFiles()){
		try {
			
				if(file.getName().toString().contains("~"))
				continue;
			
			reader=new BufferedReader(new FileReader(file));
			
			
			String line; 
			int count=0;
			String fileName=file.getName();
			while((line=reader.readLine())!=null){
				
				count++;
				BasicDBObject validJsonProperty=validJson(line,count,fileName);
				if(validJsonProperty!=null){
					
				basePatternMap.put(validJsonProperty.getString(Constants.patternName), validJsonProperty);
				 char[] deli=validJsonProperty.getString(Constants.DELIMITER).trim().toCharArray();
				 CSVFormat csvFormat;
				 
				 if(deli.length>0){
					 csvFormat=CSVFormat.DEFAULT.withDelimiter(deli[0]).withHeader(validJsonProperty.getString(Constants.MESSAGE_HEADER)).withQuote('"');
				 }else {
					 csvFormat=CSVFormat.DEFAULT.withDelimiter(',').withHeader(validJsonProperty.getString(Constants.MESSAGE_HEADER)).withQuote('"');
				 }
				 
				 headerPatternMap.put(validJsonProperty.getString(Constants.patternName), csvFormat);
				 
				}
				
			}
			
			reader.close();
			
		} catch (FileNotFoundException e) {

			throw new IOException("Unable to  load pattern file name "+ file.getCanonicalPath());
			
		}
		}
		
	}


	@SuppressWarnings("finally")
	private static BasicDBObject validJson(String line, int count,String fileName) {
		
		String msg=null;
		
		try{
			
			msg="In valid JSON format at line "+count+" in file '"+fileName+"'";

		BasicDBObject pattern=(BasicDBObject) JSON.parse(line);

		if(pattern.getString(Constants.patternName)!=null&&StringUtils.isNotBlank(pattern.getString(Constants.patternName))&&pattern.getString(Constants.patternName).matches("[a-zA-Z0-9]+")){
			return pattern;
		}
		else{
			
			msg="In valid patternName or not define at line "+count+" in file '"+fileName+"'";
			
			throw new Exception();
		}

		}catch(Exception ex){
			
			try {
				throw new Exception(msg);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally{
				return null;
			}
		}
		
	}
	
	
}
