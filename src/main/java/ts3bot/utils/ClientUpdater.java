package main.java.ts3bot.utils;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import com.github.theholywaffle.teamspeak3.TS3Api;
import com.github.theholywaffle.teamspeak3.api.wrapper.Client;

import main.java.ts3bot.init.Main;

/**
 * Simple demo that uses java.util.Timer to schedule a task 
 * to execute once 5 seconds have passed.
 */

public class ClientUpdater {
    Timer timer;
    
    public ClientCache clientCache;
    public AfkHandler afkHandler;

    public TS3Api api;

    public ClientUpdater(int seconds, TS3Api api) {
    	this.clientCache = new ClientCache();
    	this.afkHandler = new AfkHandler(api);
    	
    	this.api = api;
    	
    	fillClientCacheOnInit();
    	
        timer = new Timer();
        timer.scheduleAtFixedRate(new RemindTask(), seconds*1000, seconds*1000);
	}
    
    public void fillClientCacheOnInit() {
    	List<Client> clients =  api.getClients();
        
        for (Client client : clients) {
        	if (client.isRegularClient()) {
        		this.clientCache.setClient(client.getId(), api.getClientInfo(client.getId()));
        	}
		}
    }

    class RemindTask extends TimerTask {
        public void run() {
        	
        	Main.lvlSystem.update();
        	
        	clientCache = new ClientCache();
        	
            List<Client> clients =  api.getClients();
            
            String uid;
            
            for (Client client : clients) {
            	if (!client.getNickname().equals("ZsBot")) {
            		clientCache.setClient(client.getId(), api.getClientInfo(client.getId()));
            		
            		uid = client.getUniqueIdentifier();
            		
            		if(client.getChannelId() != globals.AFK_CHANNEL_ID) {
	            		if(client.isOutputMuted()) {
	            			if(!afkHandler.clientExists(uid)) {
	            				afkHandler.addClientAfk(uid);
	            			}
	            			else {
	            				afkHandler.incrementClientAfk(uid);
	            			}
	            		}
            		}
            		afkHandler.moveClientsToAfk();
            	}
			}
            
            //System.out.println("updated!");
        }
    }
}