package main.java.ts3bot.utils;

import java.util.HashMap;
import java.util.Map;

import com.github.theholywaffle.teamspeak3.api.wrapper.ClientInfo;

public class ClientCache {
	
	Map<Integer, ClientInfo> clients;
	
	public ClientCache() {
		clients = new HashMap<Integer, ClientInfo>();
	}
	
	public void setClient(int clientID, ClientInfo clientInfo) {
		clients.put(clientID, clientInfo);
	}
	
	public ClientInfo getClient(int clientID) {
		return clients.get(clientID);
	}
	
	public ClientInfo getClientByUid(String uid) {
		ClientInfo tmpClient = null;
		for (ClientInfo ci: clients.values()) {
			if(ci.getUniqueIdentifier().equals(uid)) {
				tmpClient = ci;
				break;
			}
		}
		
		return tmpClient;
	}
	
	public void deleteClient(int clientID) {
		clients.remove(clientID);
	}
	
	public Map<Integer, ClientInfo> getClientList() {
		return clients;
	}
}
