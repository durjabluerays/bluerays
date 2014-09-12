package org.bluerays.pattern;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Arrays;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.lang.StringUtils;
import org.bluerays.config.BasicConfiguration;
import org.bluerays.config.Constants;

import com.mongodb.BasicDBObject;


public class DynamicTamplate{

	private static DynamicTamplate dynamicTamplate=null;
	
	public String hostName;
	public String deviceName;
	public String deviceId;
	public String paths;
	public String []fieldName;
	public String deleimeter="	";
	public int projectionIndex;
	public int numberOfFiledInLog=1; 
	public CSVFormat format;
	
	
	
	public int getNumberOfFiledInLog() {
		return numberOfFiledInLog;
	}

	public void setNumberOfFiledInLog(int numberOfFiledInLog) {
		this.numberOfFiledInLog = numberOfFiledInLog;
	}

	public String getDeleimeter() {
		return deleimeter;
	}

	public void setDeleimeter(String deleimeter) {
		this.deleimeter = deleimeter;
	}

	public String getHostName() {
		return hostName;
	}

	public void setHostName(String hostName) {
		this.hostName = hostName;
	}

	public String getDeviceName() {
		return deviceName;
	}

	public void setDeviceName(String deviceName) {
		this.deviceName = deviceName;
	}

	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	public String getPaths() {
		return paths;
	}

	public void setPaths(String paths) {
		this.paths = paths;
	}

	public String[] getFieldName() {
		return fieldName;
	}

	public void setFieldName(String[] fieldName) {
		this.fieldName = fieldName;
	}

	public int getProjectionIndex() {
		return projectionIndex;
	}

	public void setProjectionIndex(int projectionIndex) {
		this.projectionIndex = projectionIndex;
	}

	public static void setDynamicTamplate(DynamicTamplate dynamicTamplate) {
		DynamicTamplate.dynamicTamplate = dynamicTamplate;
	}

	protected DynamicTamplate(){
		
	}
	
	public static DynamicTamplate getDynamicTamplate(){

		if(dynamicTamplate==null){

			dynamicTamplate=new DynamicTamplate().loadDyanamicConfig();;
			System.out.println("Loading Configuration First Time");
			return dynamicTamplate;

		}
		
		return dynamicTamplate;		
	}

	private  DynamicTamplate loadDyanamicConfig() {

		if(BasicConfiguration.HOST_NAME!=null){
			setHostName(BasicConfiguration.HOST_NAME);

		}else {

			try {
				setHostName(InetAddress.getLocalHost().getHostName());
			} catch (UnknownHostException e) {
				e.printStackTrace();
			}

		}

		setDeviceName(BasicConfiguration.DEVICE_NAME);
		setDeviceId(BasicConfiguration.DEVICE_ID);
		BasicDBObject pattern=BasicConfiguration.pattern.get(BasicConfiguration.DYNAMIC_PATTERN_NAME[0]);

		if(pattern.getString(Constants.DELIMITER)!=null){

			setDeleimeter(pattern.getString(Constants.DELIMITER));
			
			if(pattern.get(Constants.MESSAGE_HEADER)!=null){

				String[] header=StringUtils.splitPreserveAllTokens(pattern.getString(Constants.MESSAGE_HEADER), pattern.getString(Constants.DELIMITER));

				if(header.length>0){
					setNumberOfFiledInLog(header.length);
				}
				if(header.length>0){

					if(pattern.get(Constants.PROJECT)!=null){

						BasicDBObject project=(BasicDBObject)pattern.get(Constants.PROJECT);
						setProjectionIndex(project.getInt(Constants.INDEX));
						setFieldName(header);
		
					}
				}


			}

		}

		if(BasicConfiguration.paths!=null){

			setPaths(BasicConfiguration.paths[0]);

		}


		return this;
		
	}

	@Override
	public String toString() {
		return "DynamicTamplate [hostName=" + hostName + ", deviceName="
				+ deviceName + ", deviceId=" + deviceId + ", paths=" + paths
				+ ", fieldName=" + Arrays.toString(fieldName) + ", deleimeter="
				+ deleimeter + ", projectionIndex=" + projectionIndex
				+ ", numberOfFiledInLog=" + numberOfFiledInLog + "]";
	}


	
	
	
}
