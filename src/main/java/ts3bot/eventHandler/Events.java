package main.java.ts3bot.eventHandler;

import com.github.theholywaffle.teamspeak3.api.event.ChannelCreateEvent;
import com.github.theholywaffle.teamspeak3.api.event.ChannelDeletedEvent;
import com.github.theholywaffle.teamspeak3.api.event.ChannelDescriptionEditedEvent;
import com.github.theholywaffle.teamspeak3.api.event.ChannelEditedEvent;
import com.github.theholywaffle.teamspeak3.api.event.ChannelMovedEvent;
import com.github.theholywaffle.teamspeak3.api.event.ChannelPasswordChangedEvent;
import com.github.theholywaffle.teamspeak3.api.event.ClientJoinEvent;
import com.github.theholywaffle.teamspeak3.api.event.ClientLeaveEvent;
import com.github.theholywaffle.teamspeak3.api.event.ClientMovedEvent;
import com.github.theholywaffle.teamspeak3.api.event.PrivilegeKeyUsedEvent;
import com.github.theholywaffle.teamspeak3.api.event.ServerEditedEvent;
import com.github.theholywaffle.teamspeak3.api.event.TS3Listener;
import com.github.theholywaffle.teamspeak3.api.event.TextMessageEvent;
import com.github.theholywaffle.teamspeak3.api.wrapper.ClientInfo;

import main.java.ts3bot.init.Main;
import main.java.ts3bot.logger.Logger;
import main.java.ts3bot.utils.globals;
import main.java.ts3bot.dbHandler.DbHandler;

public class Events {
	public static void loadEvents() {
		Main.api.registerAllEvents();
	}
	
	public static void addListener() {
		Main.api.addTS3Listeners(new TS3Listener() {
			
			@Override
			public void onTextMessage(TextMessageEvent e) {
				
				// log bot usage (uid, nickname, command, date, time)
				
				if (e.getMessage().startsWith("!zsbot")) {
					if (e.getMessage().length() == 4) {
						Main.api.sendPrivateMessage(e.getInvokerId(), "Here is a list of bot commands: ");
					}
					else {
						
						// lvl system
						if (e.getMessage().equals("!zsbot lvl")) {
							Main.api.sendPrivateMessage(e.getInvokerId(), "You are lvl 27!");
						}
						else if (e.getMessage().equals("!zsbot lvl showoff")) {
							Main.api.sendChannelMessage(Main.api.getClientInfo(e.getInvokerId()).getChannelId(), e.getInvokerName() + " is lvl 27!");
						}
						else {
							Main.api.sendPrivateMessage(e.getInvokerId(), "Type '!zsbot' for a list of commands"); //
						}
						
						
						// set param
						if (e.getInvokerUniqueId().equals(globals.OLE_UID)) {
							if(e.getMessage().equals("!zsbot setparam")) {
								
							}
						}
						
						// get param
						
						// add param
					}
				}
			}
			
			@Override
			public void onServerEdit(ServerEditedEvent e) {
				
			}
			
			@Override
			public void onPrivilegeKeyUsed(PrivilegeKeyUsedEvent e) {
				
			}
			
			@Override
			public void onClientMoved(ClientMovedEvent e) {		
				DbHandler.updateClientChannel(e.getClientId(), Main.channels.getChannel(e.getTargetChannelId()).getName());
				
				String status = "online";
				
				if (Main.channels.getChannel(e.getTargetChannelId()).getName().equals("AFK / Kurz Tür")) {
					status = "away";
				}
				
				if(DbHandler.getClientStatus(e.getClientId()).equals("away")) {
					status = "online";
				}
				
				DbHandler.updateClientStatus(e.getClientId(), status);
				
				ClientInfo ci = Main.cu.cc.getClient(e.getClientId());
				Logger.addUserLog(ci.getUniqueIdentifier(), ci.getNickname(), "ClientMoved", Main.channels.getChannel(e.getTargetChannelId()).getName());
			}
			
			@Override
			public void onClientLeave(ClientLeaveEvent e) {
				// mark client offline in db
				DbHandler.deleteClientOnline(e.getClientId());
				
				ClientInfo ci = Main.cu.cc.getClient(e.getClientId());
				Logger.addUserLog(ci.getUniqueIdentifier(), ci.getNickname(), "ClientLeave", "");
			}
			
			@Override
			public void onClientJoin(ClientJoinEvent e) {
				DbHandler.addClientOnline(e.getClientId(), e.getClientNickname(), "Wartestübchen", "online");
				
				if(Main.ls.clientExists(e.getUniqueClientIdentifier())) {
					Main.ls.resetBothTimeConnected(e.getUniqueClientIdentifier());
				}
				else {
					Main.ls.addClient(e.getUniqueClientIdentifier());
				}
				
				Logger.addUserLog(e.getUniqueClientIdentifier(), e.getClientNickname(), "ClientJoin", "");
			}
			
			@Override
			public void onChannelPasswordChanged(ChannelPasswordChangedEvent e) {
				Logger.addChannelLog(e.getInvokerUniqueId(), e.getInvokerName(), "ChannelPasswordChanged", Main.channels.getChannel(e.getChannelId()).getName());
			}
			
			@Override
			public void onChannelMoved(ChannelMovedEvent e) {		
				Logger.addChannelLog(e.getInvokerUniqueId(), e.getInvokerName(), "ChannelMoved", Main.channels.getChannel(e.getChannelId()).getName());
			}
			
			@Override
			public void onChannelEdit(ChannelEditedEvent e) {
				Logger.addChannelLog(e.getInvokerUniqueId(), e.getInvokerName(), "ChannelEdited", Main.channels.getChannel(e.getChannelId()).getName());
			}
			
			@Override
			public void onChannelDescriptionChanged(ChannelDescriptionEditedEvent e) {
				Logger.addChannelLog(e.getInvokerUniqueId(), e.getInvokerName(), "ChannelDescriptionEdited", Main.channels.getChannel(e.getChannelId()).getName());
			}
			
			@Override
			public void onChannelDeleted(ChannelDeletedEvent e) {
				Logger.addChannelLog(e.getInvokerUniqueId(), e.getInvokerName(), "ChannelDeleted", Main.channels.getChannel(e.getChannelId()).getName());
			}
			
			@Override
			public void onChannelCreate(ChannelCreateEvent e) {
				Logger.addChannelLog(e.getInvokerUniqueId(), e.getInvokerName(), "ChannelCreated", Main.channels.getChannel(e.getChannelId()).getName());
			}
		});
	}
}
