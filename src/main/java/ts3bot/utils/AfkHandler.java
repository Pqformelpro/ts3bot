package main.java.ts3bot.utils;

import java.util.HashMap;
import java.util.Map;

import com.github.theholywaffle.teamspeak3.api.wrapper.ClientInfo;

import main.java.ts3bot.init.Main;

public class AfkHandler {
	
	Map<String, Integer> mutedClients;
	
	public AfkHandler() {
		mutedClients = new HashMap<String, Integer>();
	}
	
	public void addClientAfk(String uid) {
		if(!clientExists(uid)) {
			mutedClients.put(uid, 0);
			System.out.println("Client added: " + uid + " " + Main.cu.cc.getClientByUid(uid).getNickname());
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
		 System.out.println("Client incremented: " + uid + " " + Main.cu.cc.getClientByUid(uid).getNickname() + " " + mutedClients.get(uid));
	 }
	 
	 private void removeClientAfk(String uid) {
		 mutedClients.remove(uid);
		 System.out.println("Client removed: " + uid + " " + Main.cu.cc.getClientByUid(uid).getNickname());
	 }
     
     public void moveClientsToAfk() {
     	for(String uid : mutedClients.keySet()) {
     		if(mutedClients.get(uid) >= 5) {
     			Main.api.moveClient(Main.api.getClientByUId(uid).getId(), globals.AFK_CHANNEL_ID);
     			removeClientAfk(uid);
     		}
     		if(isClientNoLongerAfk(uid)) {
     			removeClientAfk(uid);
     		}
     	}
     }
     
     private boolean isClientNoLongerAfk(String uid) {
    	 ClientInfo client = Main.api.getClientByUId(uid);
    	 
    	 return client.getChannelId() != globals.AFK_CHANNEL_ID && !client.isOutputMuted(); 
     }
}
