package org.bluerays.config;

import java.util.Map;
import java.util.Set;

import org.apache.commons.csv.CSVFormat;

import com.mongodb.BasicDBObject;


public interface BasicConfiguration {

	
	
	String HOST_NAME=BasicConfigurationImpl.getHostName(),
			IPV4=BasicConfigurationImpl.getIPV4(),
			IPV6=BasicConfigurationImpl.getIPV6(),
			DEVICE_NAME=BasicConfigurationImpl.getDeviceName(),
			DEVICE_ID=BasicConfigurationImpl.getDeviceId(),
			CLUSTER_NAME=BasicConfigurationImpl.getClusterName(),
			WORKING_DIR=BasicConfigurationImpl.getCurrentWorkingDir();
	
	
	int IPV4PORT=BasicConfigurationImpl.getIPV4TCPPort(),
			HTTP_PORT=BasicConfigurationImpl.getHttpPort(),
			IPV6PORT=BasicConfigurationImpl.getIPV6TCPPort();
	
	Map<String, BasicDBObject> pattern=BasicConfigurationImpl.getBasePatternMap();
	Map<String, CSVFormat>headerPatternMap=BasicConfigurationImpl.getHeaderPatternMap();
	
	String[] DYNAMIC_PATTERN_NAME=BasicConfigurationImpl.getDynamicPattern(),
			paths=BasicConfigurationImpl.getPaths();
	Set<String> FILE_EXT =BasicConfigurationImpl.getFileExt();
	
}
