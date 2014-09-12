package org.bluerays.client;

import org.bluerays.config.BasicConfiguration;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.ImmutableSettings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;


public class TCPClient {

	private static Client client;

	public Client getClient() {
		if(client==null){
		TransportClient tc=new TransportClient();
		client=tc.addTransportAddress(new InetSocketTransportAddress(BasicConfiguration.IPV4, BasicConfiguration.IPV4PORT));
		setClient(client);
		}
		return client;
	}

	private void setClient(Client client) {
		this.client = client;
	}
	
}
