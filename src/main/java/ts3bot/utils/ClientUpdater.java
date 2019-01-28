package main.java.ts3bot.utils;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import com.github.theholywaffle.teamspeak3.api.wrapper.Client;

import main.java.ts3bot.init.Main;

/**
 * Simple demo that uses java.util.Timer to schedule a task 
 * to execute once 5 seconds have passed.
 */

public class ClientUpdater {
    Timer timer;
    public ClientCache cc;
    public AfkHandler ah;

    public ClientUpdater(int seconds) {
    	this.cc = new ClientCache();
    	this.ah = new AfkHandler();
    	
    	fillClientCacheOnInit();
    	
        timer = new Timer();
        timer.scheduleAtFixedRate(new RemindTask(), seconds*1000, seconds*1000);
	}
    
    public void fillClientCacheOnInit() {
    	List<Client> clients =  Main.api.getClients();
        
        for (Client client : clients) {
        	if (!client.getNickname().equals("ZsBot")) {
        		this.cc.setClient(client.getId(), Main.api.getClientInfo(client.getId()));
        	}
		}
    }

    class RemindTask extends TimerTask {
        public void run() {
        	
        	Main.ls.update();
        	
        	cc = new ClientCache();
        	
            List<Client> clients =  Main.api.getClients();
            
            String uid;
            
            for (Client client : clients) {
            	if (!client.getNickname().equals("ZsBot")) {
            		cc.setClient(client.getId(), Main.api.getClientInfo(client.getId()));
            		
            		uid = client.getUniqueIdentifier();
            		
            		if(client.getChannelId() != globals.AFK_CHANNEL_ID) {
	            		if(client.isOutputMuted()) {
	            			if(!ah.clientExists(uid)) {
	            				ah.addClientAfk(uid);
	            			}
	            			else {
	            				ah.incrementClientAfk(uid);
	            			}
	            		}
            		}
            		ah.moveClientsToAfk();
            	}
			}
            
            //System.out.println("updated!");
        }
    }
}