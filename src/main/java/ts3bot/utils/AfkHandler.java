package main.java.ts3bot.utils;

import java.util.HashMap;
import java.util.Map;

import com.github.theholywaffle.teamspeak3.TS3Api;
import com.github.theholywaffle.teamspeak3.api.wrapper.ClientInfo;

public class AfkHandler {
	
	Map<String, Integer> mutedClients;
	
	public TS3Api api;
	public ClientUpdater clientUpdater;
	
	public AfkHandler(TS3Api api, ClientUpdater clientUpdater) {
		mutedClients = new HashMap<String, Integer>();
		
		this.api = api;
		this.clientUpdater = clientUpdater;
	}
	
	public void addClientAfk(String uid) {
		if(!clientExists(uid)) {
			mutedClients.put(uid, 0);
		}
	}
	
	 public Map<String, Integer> getClientsAfk() {
     	return mutedClients;
     }
	 
	 public boolean clientExists(String uid) {
		 return mutedClients.containsKey(uid);
	 }
	 
	 public void incrementClientAfk(String uid) {
		 mutedClients.replace(uid, mutedClients.get(uid) + 1);
	 }
	 
	 private void removeClientAfk(String uid) {
		 mutedClients.remove(uid);
	 }
     
     public void moveClientsToAfk() {
     	for(String uid : mutedClients.keySet()) {
     		if(mutedClients.get(uid) >= 5) {
     			api.moveClient(clientUpdater.clientCache.getClientByUid(uid).getId(), globals.AFK_CHANNEL_ID);
     			removeClientAfk(uid);
     		}
     		if(isClientNoLongerAfk(uid)) {
     			removeClientAfk(uid);
     		}
     	}
     }
     
     private boolean isClientNoLongerAfk(String uid) {
    	 ClientInfo client = clientUpdater.clientCache.getClientByUid(uid);
    	 
    	 return client.getChannelId() != globals.AFK_CHANNEL_ID && !client.isOutputMuted(); 
     }
}
