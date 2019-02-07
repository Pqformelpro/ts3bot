package main.java.ts3bot.utils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.github.theholywaffle.teamspeak3.api.wrapper.Channel;

import main.java.ts3bot.init.Main;

public class ChannelCache {

	Map<Integer, Channel> channels = new HashMap<Integer, Channel>();
	
	public ChannelCache() {
		List<Channel> tmpChannels = Main.api.getChannels();
		
		for(Channel channel : tmpChannels) {
			channels.put(channel.getId(), channel);
		}
	}
	
	public Channel getChannel(int id) {
		return channels.get(id);
	}
	
	public void addChannel(Channel channel) {
		channels.put(channel.getId(), channel);
	}
	
	public void deleteChannelByObject(int id) {
		channels.remove(id);
	}
	
	public void updateChannel(Channel channel) {
		channels.replace(channel.getId(), channel);
	}
}
