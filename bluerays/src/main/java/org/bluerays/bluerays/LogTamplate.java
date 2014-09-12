package org.bluerays.bluerays;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;

import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.lang.StringUtils;
import org.bluerays.config.BasicConfiguration;
import org.bluerays.config.Constants;
import org.bluerays.pattern.DynamicTamplate;
import org.bluerays.validater.Validate;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;

import com.mongodb.BasicDBObject;


public class LogTamplate {

	Date timestamp;

	String hostName,
	deviceName,
	deviceId,
	paths,
	message;

	String []fieldName;
	
	int projectionIndex;
	
	
	protected String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	protected Date getTimestamp() {
		return timestamp;
	}
	protected void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}
	protected String[] getFieldName() {
		return fieldName;
	}
	protected void setFieldName(String[] fieldName) {
		this.fieldName = fieldName;
	}
	protected int getProjectionIndex() {
		return projectionIndex;
	}
	protected void setProjectionIndex(int projectionIndex) {
		this.projectionIndex = projectionIndex;
	}
	protected String getHostName() {
		return hostName;
	}
	protected void setHostName(String hostName) {
		this.hostName = hostName;
	}
	protected String getDeviceName() {
		return deviceName;
	}
	protected void setDeviceName(String deviceName) {
		this.deviceName = deviceName;
	}
	protected String getDeviceId() {
		return deviceId;
	}
	protected void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}
	protected String getPaths() {
		return paths;
	}
	protected void setPaths(String paths) {
		this.paths = paths;
	}
	
	
	
	public BasicDBObject getParseLog(){
		
		if(!(getMessage().trim().length()>0))
			return null;
		
		BasicDBObject indexMessage=new BasicDBObject();
		XContentBuilder builder;

		try {
			builder = XContentFactory.jsonBuilder().startObject();
			builder.field(Constants.HOST, BasicConfiguration.HOST_NAME);
			builder.field(Constants.VERSION,1);
			
			Calendar defaultMessageDate=Calendar.getInstance();
			indexMessage.put(Constants.ES_INDEX_NAME, "logstash-"+(1900+defaultMessageDate.getTime().getYear())+".0"+(defaultMessageDate.getTime().getMonth()+1)+"."+defaultMessageDate.getTime().getDate());

			int length=DynamicTamplate.getDynamicTamplate().getFieldName().length;
			if(BasicConfiguration.pattern.get(BasicConfiguration.DYNAMIC_PATTERN_NAME[0])!=null){
				
				builder.field(Constants.message,getMessage());
				
				if(DynamicTamplate.getDynamicTamplate().fieldName!=null&&DynamicTamplate.getDynamicTamplate().fieldName.length>DynamicTamplate.getDynamicTamplate().getProjectionIndex()){
					
					CSVParser csvParser=CSVParser.parse(getMessage(), BasicConfiguration.headerPatternMap.get(BasicConfiguration.DYNAMIC_PATTERN_NAME[0]));
					for(CSVRecord record:csvParser){
						
						
						
						int  i=0;
						for(String r:record){
							
							if(DynamicTamplate.getDynamicTamplate().getProjectionIndex()==i){
								Date date=new Validate().date(r);
								if(date!=null){
									int yyyy=1900+date.getYear();
									int MM=1+date.getMonth();
									indexMessage.put(Constants.ES_INDEX_NAME, "logstash-"+yyyy+".0"+MM+"."+date.getDate());
									builder.field("@timestamp",new Timestamp(date.getTime()));
								}
							}
							else{
								if(r.trim().length()>0 && i<length){
									builder.field(DynamicTamplate.getDynamicTamplate().getFieldName()[i], r);
								}
								}

							i++;
						}
						
						builder.endObject();
						indexMessage.put(Constants.message, builder.string());
					}
//					String[] messages=StringUtils.splitPreserveAllTokens(message, DynamicTamplate.getDynamicTamplate().getDeleimeter());
//					if(DynamicTamplate.getDynamicTamplate().numberOfFiledInLog>(messages.length)){
//					
//					for( int i=0; i<(messages.length-1);i++){
//						
//						if(DynamicTamplate.getDynamicTamplate().getProjectionIndex()==i){
//					
//							Date date=new Validate().date(messages[i]);
//							if(date!=null){
//								int yyyy=1900+date.getYear();
//								int MM=1+date.getMonth();
//								indexMessage.put(Constants.ES_INDEX_NAME, "logstash-"+yyyy+".0"+MM+"."+date.getDate());
//								builder.field("@timestamp",new Timestamp(date.getTime()));
//							}
//						
//						}else {
//							if(messages[i].trim().length()>0)
//							builder.field(DynamicTamplate.getDynamicTamplate().getFieldName()[i], messages[i]);
//						}
//					}
//					}else{
//						for( int i=0; i<(DynamicTamplate.getDynamicTamplate().numberOfFiledInLog-1);i++){
//							
//							if(DynamicTamplate.getDynamicTamplate().getProjectionIndex()==i){
//								
//								Date date=new Validate().date(messages[i]);
//								if(date!=null){
//									int yyyy=1900+date.getYear();
//									int MM=1+date.getMonth();
//									indexMessage.put(Constants.ES_INDEX_NAME, "logstash-"+yyyy+".0"+MM+"."+date.getDate());
//									builder.field("@timestamp",new Timestamp(date.getTime()));
//								}
//							
//							}else {
//								if(messages[i].trim().length()>0)
//								builder.field(DynamicTamplate.getDynamicTamplate().getFieldName()[i], messages[i]);
//							}
//						}
//					}
					
				}
				
			}else {
				
				if(getMessage()!=null)
				builder.field(Constants.message,getMessage());
				builder.field("@timestamp",new Timestamp(new Date().getTime()));
			}

			
			indexMessage.put(Constants.message, builder.string());
			
			return indexMessage;			
		
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	
	}
	
	
	
	
}
