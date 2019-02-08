package main.java.ts3bot.lvlSystem;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.github.theholywaffle.teamspeak3.api.wrapper.ClientInfo;

import main.java.ts3bot.dbHandler.DbHandler;
import main.java.ts3bot.init.Main;

public class LvlSystem {
	
	public final int expScaling = 45;
	public final int expLvlOne = 30;
		
	/*
	 * clientData index values
	 * [0] = lvl
	 * [1] = exp_current
	 * [2] = exp_next_lvl
	 * [3] = exp_total
	 * [4] = time_connected
	 * [5] = time_connected_old
	 */
	private int[] clientData;

	public LvlSystem() {
		fillLvlSystemOnInit();
	}
	
	public void update() {
		for (ClientInfo ci : Main.clientUpdater.clientCache.getClientList().values()) {
			if(clientExists(ci.getUniqueIdentifier())) {
				loadClientData(ci.getUniqueIdentifier());
				
				long tmpTimeConnected = ci.getTimeConnected();
				
				long rest = ci.getTimeConnected() % 1000;
				tmpTimeConnected = (tmpTimeConnected - rest) / 1000;
				
				rest = tmpTimeConnected % 60;
				tmpTimeConnected = (tmpTimeConnected - rest) / 60;
				
				int timeConnected = (int)tmpTimeConnected;

				
				if(timeConnected - clientData[5]  + clientData[1] >= clientData[2]) {
					updateClientData(
							ci.getUniqueIdentifier(),
							clientData[0] + 1,
							clientData[1] + timeConnected - clientData[5] - clientData[2],
							clientData[2] + expScaling,
							clientData[3] + timeConnected - clientData[5] - 1,
							timeConnected,
							clientData[4]);
				}
				else {
					updateClientData(
							ci.getUniqueIdentifier(),
							clientData[0],
							clientData[1] + timeConnected - clientData[5],
							clientData[2],
							clientData[3] + timeConnected - clientData[5] - 1,
							timeConnected,
							clientData[4]);
				}
			}
			else {
				if(!ci.getNickname().equals("ZsBot")) {
					addClient(ci.getUniqueIdentifier());
				}
			}
		}
	}
	
	
	public void loadClientData(String uID) {
		Connection con = DbHandler.connect();
		
		PreparedStatement query = null;
		ResultSet result = null;
		
		String sql;
		 clientData = new int[6];
		
		try {
			sql = "SELECT * FROM lvl_system WHERE uid=?";
			
			query = con.prepareStatement(sql);
			
			query.setString(1, uID);
			
			result = query.executeQuery();
			
			if (result.next()) {
				clientData[0] = result.getInt(3);
				clientData[1] = result.getInt(4);
				clientData[2] = result.getInt(5);
				clientData[3] = result.getInt(6);
				clientData[4] = result.getInt(7);
				clientData[5] = result.getInt(8);
			}
			
		}
		catch(SQLException e) {
			System.out.println(e);
		}
		
		finally {
		    DbHandler.close(null, query, con);
		}
	}
	
	public void fillLvlSystemOnInit() {
		
		for (ClientInfo ci : Main.clientUpdater.clientCache.getClientList().values()) {
			if(!clientExists(ci.getUniqueIdentifier())) {
				addClient(ci.getUniqueIdentifier());
			}
			else {
				resetBothTimeConnected(ci.getUniqueIdentifier());
			}
		}

	}
	
	public void resetBothTimeConnected(String uid) {
		Connection con = DbHandler.connect();		
		
		String sql = "UPDATE lvl_system SET time_connected=?, time_connected_old=? WHERE uid=?";
		
		PreparedStatement query = null;
		
		ClientInfo ci = Main.clientUpdater.clientCache.getClientByUid(uid);
		long tmpTimeConnected = ci.getTimeConnected();
		
		long rest = ci.getTimeConnected() % 1000;
		tmpTimeConnected = (tmpTimeConnected - rest) / 1000;
		
		rest = tmpTimeConnected % 60;
		tmpTimeConnected = (tmpTimeConnected - rest) / 60;
		
		int timeConnected = (int)tmpTimeConnected;
		
		try {
			query = con.prepareStatement(sql);
			
			query.setInt(1, timeConnected);
			query.setInt(2, 0);
			query.setString(3, uid);
			
			query.executeUpdate();
		}
		catch(SQLException e) {
			System.out.println(e);
		}
		
		finally {
		    DbHandler.close(null, query, con);
		}
	}
	
	public void addClient(String uID) {
		Connection con = DbHandler.connect();
		
		String sql = "INSERT INTO lvl_system (uid, nickname, lvl, exp_current, exp_next_lvl, exp_total, time_connected, time_connected_old) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
		
		PreparedStatement query = null;
		
		ClientInfo ci = Main.clientUpdater.clientCache.getClientByUid(uID);
		long tmpTimeConnected = ci.getTimeConnected();
		
		long rest = ci.getTimeConnected() % 1000;
		tmpTimeConnected = (tmpTimeConnected - rest) / 1000;
		
		rest = tmpTimeConnected % 60;
		tmpTimeConnected = (tmpTimeConnected - rest) / 60;
		
		int timeConnected = (int)tmpTimeConnected;
		
		try {
			query = con.prepareStatement(sql);
			query.setString(1, uID);
			query.setString(2, Main.clientUpdater.clientCache.getClientByUid(uID).getNickname());
			query.setInt(3, 1);
			query.setInt(4, 0);
			query.setInt(5, expLvlOne);
			query.setInt(6, 0);
			query.setInt(7, timeConnected);
			query.setInt(8, 0);
			query.executeUpdate();
		}
		catch(SQLException e) {
			System.out.println(e);
		}
		
		finally {
		    DbHandler.close(null, query, con);
		}
	}
	
	public boolean clientExists(String uID) {
		Connection con = DbHandler.connect();
		
		PreparedStatement query = null;
		ResultSet result = null;
		
		String sql;
		
		boolean exists = false;
		
		try {
			sql = "SELECT * FROM lvl_system WHERE uid=?";
			
			query = con.prepareStatement(sql);
			
			query.setString(1, uID);
			
			result = query.executeQuery();
			
			if(result.next()) {
				exists = true;
			}
		}
		catch(SQLException e) {
			System.out.println(e);
		}
		
		finally {
		    DbHandler.close(result, query, con);
		}
		
		return exists;
	}
	
	public void updateClientData(String uID, int lvl, int expCurrent, int expNextLvl, int expTotal, int timeConnected, int timeConnectedOld) {
		Connection con = DbHandler.connect();
		
		PreparedStatement query = null;
		
		String sql;
		
		try {
			sql = "UPDATE lvl_system SET nickname=?, lvl=?, exp_current=?, exp_next_lvl=?, exp_total=?, time_connected=?, time_connected_old=? WHERE uid=?";
			query = con.prepareStatement(sql);
			
			if(expCurrent == 0) {
				expCurrent = clientData[1];
			}
			if(expTotal == 0) {
				expTotal = clientData[3];
			}
			
			query.setString(1, Main.clientUpdater.clientCache.getClientByUid(uID).getNickname());
			query.setInt(2, lvl);
			query.setInt(3, expCurrent);
			query.setInt(4, expNextLvl);
			query.setInt(5, expTotal);
			query.setInt(6, timeConnected);
			query.setInt(7, timeConnectedOld);
			query.setString(8, uID);
			
			query.executeUpdate();
		}
		catch(SQLException e) {
			System.out.println(e);
		}
		
		finally {
		    DbHandler.close(null, query, con);
		}
	}
	
}