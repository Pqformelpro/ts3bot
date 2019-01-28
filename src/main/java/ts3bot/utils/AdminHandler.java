package main.java.ts3bot.utils;

import java.util.ArrayList;
import java.util.List;

public class AdminHandler {
	
	List<String> admins = new ArrayList<String>();
	
	public AdminHandler() {
		addNewAdmin(globals.OLE_UID);
	}
	
	public void addNewAdmin(String uid) {
		if(!isAdmin(uid)) {
			admins.add(uid);
		}
	}
	
	public boolean isAdmin(String uid) {
		return admins.contains(uid);
	}
	
	public void deleteAdmin(String uid) {
		if(isAdmin(uid)) {
			admins.remove(uid);
		}
	}
}
