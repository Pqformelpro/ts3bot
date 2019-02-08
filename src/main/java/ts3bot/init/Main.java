package main.java.ts3bot.init;

import main.java.ts3bot.dbHandler.DbHandler;
import main.java.ts3bot.eventHandler.Events;
import main.java.ts3bot.lvlSystem.LvlSystem;
import main.java.ts3bot.utils.ChannelCache;
import main.java.ts3bot.utils.ClientUpdater;
import main.java.ts3bot.utils.globals;

import com.github.theholywaffle.teamspeak3.TS3Api;
import com.github.theholywaffle.teamspeak3.TS3Config;
import com.github.theholywaffle.teamspeak3.TS3Query;
import com.github.theholywaffle.teamspeak3.api.reconnect.ConnectionHandler;
import com.github.theholywaffle.teamspeak3.api.reconnect.ReconnectStrategy;
import com.github.theholywaffle.teamspeak3.api.wrapper.ClientInfo;

public class Main {
	
	public static boolean isFirstConnect = true;
	public static LvlSystem lvlSystem;
	public static ChannelCache channels;
	public static ClientUpdater clientUpdater;
	
	public static void main(String[] args) {
		final TS3Config config = new TS3Config();
		// localhost
		//config.setHost("127.0.0.1");
		
		// zockerstuebchen
		config.setHost(globals.ZS_SERVER_IP);
		
		config.setEnableCommunicationsLogging(true);
		
		config.setReconnectStrategy(ReconnectStrategy.exponentialBackoff());
		
		config.setConnectionHandler(new ConnectionHandler() {

			@Override
			public void onConnect(TS3Query query) {
				TS3Api api = query.getApi();
				// localhost
				//api.login("bot", "i25PWd50");
				
				// login ole
				// api.login("bot", "ojFJQXL6");
				
				// login zsbot
				api.login("ZsBot", "bgxvAn2f");

				api.selectVirtualServerById(globals.ZS_SERVER_ID);
				
				api.setNickname("ZsBot");
				
				DbHandler.emptyClientOnlineList();
				
				channels = new ChannelCache(api);

				clientUpdater = new ClientUpdater(globals.UPDATE_RATE, api);
				
				for (ClientInfo ci : clientUpdater.clientCache.getClientList().values()) {
					if (channels.getChannel(ci.getChannelId()).getName().equals("AFK / Kurz Tür")) {
						DbHandler.addClientOnline(ci.getId(), ci.getNickname(), channels.getChannel(ci.getChannelId()).getName(), "away");
					}
					else {
						DbHandler.addClientOnline(ci.getId(), ci.getNickname(), channels.getChannel(ci.getChannelId()).getName(), "online");
					}
				}
				
				if (isFirstConnect) {
					lvlSystem = new LvlSystem();
					
					isFirstConnect = false;
				}
				else {
					lvlSystem.fillLvlSystemOnInit();
				}
				
				Events.loadEvents(api);
				
				System.out.println("connected!");
			}

			@Override
			public void onDisconnect(TS3Query ts3Query) {
				System.out.println("disconnected!");
			}
			
		});
		
		final TS3Query query = new TS3Query(config);
				
		query.connect();
		
		Events.addListener(query.getApi());
		
		System.out.println("Der Bot wurde gestartet!");
	}
}