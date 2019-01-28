package main.java.ts3bot.init;

import main.java.ts3bot.dbHandler.DbHandler;
import main.java.ts3bot.eventHandler.Events;
import main.java.ts3bot.lvlSystem.LvlSystem;
import main.java.ts3bot.utils.ClientUpdater;
import main.java.ts3bot.utils.globals;

import com.github.theholywaffle.teamspeak3.TS3Api;
import com.github.theholywaffle.teamspeak3.TS3Config;
import com.github.theholywaffle.teamspeak3.TS3Query;
import com.github.theholywaffle.teamspeak3.api.wrapper.ClientInfo;

public class Main {

	public static final TS3Config config = new TS3Config();
	public static final TS3Query query = new TS3Query(config);
	public static final TS3Api api = query.getApi();
	
	public static ClientUpdater cu;
	public static LvlSystem ls;
	
	public static void main(String[] args) {
		// localhost
		//config.setHost("127.0.0.1");
		
		// zockerstuebchen
		config.setHost("62.146.10.130");
		
		config.setEnableCommunicationsLogging(true);
		
		query.connect();
		
		// localhost
		//api.login("bot", "i25PWd50");
		
		// login ole
		// api.login("bot", "ojFJQXL6");
		
		// login zsbot
		api.login("ZsBot", "bgxvAn2f");

		api.selectVirtualServerById(globals.ZS_SERVER_ID);
		
		api.setNickname("ZsBot");
		
		DbHandler.emptyClientOnlineList();
		
		// Update every 15 minutes
		cu = new ClientUpdater(globals.UPDATE_RATE);
		
		for (ClientInfo ci : cu.cc.getClientList().values()) {
			if (api.getChannelInfo(ci.getChannelId()).getName().equals("AFK / Kurz Tür")) {
				DbHandler.addClientOnline(ci.getId(), ci.getNickname(), api.getChannelInfo(ci.getChannelId()).getName(), "away");
			}
			else {
				DbHandler.addClientOnline(ci.getId(), ci.getNickname(), api.getChannelInfo(ci.getChannelId()).getName(), "online");
			}
		}
		
		ls = new LvlSystem();
		
		Events.loadEvents();
		
		System.out.println("Der Bot wurde gestartet!");
	}

}