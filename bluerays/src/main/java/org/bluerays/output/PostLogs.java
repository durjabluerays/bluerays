package org.bluerays.output;
import java.io.IOException;
import java.util.List;

import org.bluerays.client.RestClient;
import org.bluerays.client.TCPClient;
import org.bluerays.config.BasicConfiguration;
import org.bluerays.config.Constants;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequestBuilder;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;

import com.mongodb.BasicDBObject;

public class PostLogs {

	
	
	
	public static void main(String[] args) throws IOException, InterruptedException {
		
		TCPClient tamplate=new TCPClient();
		Client client=tamplate.getClient();
		BulkRequestBuilder bulkBuilder=client.prepareBulk();
		
		String indexName="logstash-2014.08.22";
		
//		LogTamplate logTamplate=new LogTamplate();
		
//		logTamplate.setMessage("d,2014-08-24 16:00:14+0530,2014-07-10 16:00:02+0530,,Emailstatements.cards@hdfcbank.net,VJAYPRAKASH@YMAIL.COM,,relayed,2.0.0 (success),smtp;250 ok dirdel,mta7.am0.yahoodns.net (66.196.118.35),success,smtp,hbdpstmt3.hdfcbank.com (202.154.168.163),smtp,172.30.149.109,66.196.118.35,\"PIPELINING,8BITMIME,SIZE,STARTTLS\",155704,CAMPAIGN,,");

//		System.out.println(logTamplate.getParseLog());
		
		for(int i=0; i<100; i++){
			
//			BasicDBObject pattern=logTamplate.getParseLog();
			
			
//			System.out.println("*************"+pattern);
		
			IndexRequestBuilder  requestBuilder=client.prepareIndex(indexName, "logs");
			
			requestBuilder.setSource(getSampleLog(1));
		
		
		bulkBuilder.add(requestBuilder);
		
		BulkResponse bulkResponse=bulkBuilder.execute().actionGet();
		}
	}

	
	public boolean postLogInES(List<BasicDBObject> messages){
		
		TCPClient tamplate=new TCPClient();
		Client client=tamplate.getClient();
		BulkRequestBuilder bulkBuilder=client.prepareBulk();

		int bulkBoxSize=0;
		for(BasicDBObject message: messages){
			
			bulkBoxSize++;
			IndexRequestBuilder  requestBuilder=client.prepareIndex(message.getString(Constants.ES_INDEX_NAME), BasicConfiguration.DEVICE_NAME);
			requestBuilder.setSource(message.getString(Constants.message));
			bulkBuilder.add(requestBuilder);
			
			if((bulkBoxSize%Constants.BULK_BOX_SIZE)==0){

				BulkResponse bulkResponse=bulkBuilder.execute().actionGet();

				if(bulkResponse.hasFailures()){
					System.out.println("Log posted. Size:" +bulkBoxSize);
					bulkBuilder=client.prepareBulk();
				}
			}
		}

		if((bulkBoxSize%messages.size())!=0){

			if(bulkBuilder.execute().actionGet().hasFailures()){
				System.out.println("Log posted. Size:" +bulkBoxSize);
				if(bulkBoxSize==messages.size()){
				messages=null;
				return true;
				}
			}

		}

		
		
		return false;
	}
	
	
	
	

	
	
	public boolean postLogInESHttp(List<BasicDBObject> messages){
		
		RestClient restClient =new RestClient();
		
		int bulkBoxSize=0;
		for(BasicDBObject message: messages){
			
			bulkBoxSize++;
			restClient.postMessage(message.getString(Constants.ES_INDEX_NAME), BasicConfiguration.DEVICE_NAME, message.getString(Constants.message));
		
		}
				

		if(bulkBoxSize==messages.size()){
			System.out.println("Log posted. Size:" +bulkBoxSize);
			messages=null;
			return true;
		}

		
		return false;
	}
	
	
	
	
	
	
	
	
	
	private static String getSampleLog(int second) throws IOException, InterruptedException {
		
		XContentBuilder builder=XContentFactory.jsonBuilder().startObject();
		
		builder.field("message", " I am Log 20 20");
		builder.field("@version",1);
		builder.field("host", "admin");
		builder.field("path","/tmp/access_log");
		Thread.sleep(1000					);
		builder.field("@timestamp" , "2014-08-22T23:15:45.030Z");
		builder.endObject();
		
		System.out.println(builder.string());
		return builder.string();
	}
	
	
	
	
}
