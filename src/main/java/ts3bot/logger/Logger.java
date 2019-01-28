package main.java.ts3bot.logger;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;

import main.java.ts3bot.dbHandler.DbHandler;


public class Logger {
	// log bot usage
	// log client (join/leave/move)
	// log channel (creation/move/edit)
	// log statistics (messages sent/links sent/channel created/total online time)
	
	public static void addUserLog(String uid, String nickname, String event, String channel) {
		Connection con = DbHandler.connect();
		
		PreparedStatement query = null;
		String sql;

		try {			
			sql = "INSERT INTO user_logs (uid, nickname, event, channel, date, time) VALUES (?, ?, ?, ?, ?, ?)";
			
			query = con.prepareStatement(sql);
			
			query.setString(1, uid);
			query.setString(2, nickname);
			query.setString(3, event);
			query.setString(4, channel);
			query.setDate(5, Date.valueOf(LocalDate.now()));
			query.setTime(6, Time.valueOf(LocalTime.now()));
			
			query.executeUpdate();
		}
		catch(SQLException e) {
			System.out.println(e);
		}
		
		finally {
		    DbHandler.close(null, query, con);
		}
	}
	
	public static void addChannelLog(String uid, String nickname, String event, String channel) {
		Connection con = DbHandler.connect();
		
		PreparedStatement query = null;
		String sql;

		try {
			sql = "INSERT INTO channel_logs (uid, nickname, event, channel, date, time) VALUES (?, ?, ?, ?, ?, ?)";
			
			query = con.prepareStatement(sql);
			
			query.setString(1, uid);
			query.setString(2, nickname);
			query.setString(3, event);
			query.setString(4, channel);
			query.setDate(5, Date.valueOf(LocalDate.now()));
			query.setTime(6, Time.valueOf(LocalTime.now()));
			
			query.executeUpdate();
		}
		catch(SQLException e) {
			System.out.println(e);
		}
		
		finally {
			DbHandler.close(null, query, con);
		}
	}
	
	public static void addServerLog(String uid, String nickname, String event) {
		Connection con = DbHandler.connect();
		
		PreparedStatement query = null;
		String sql;

		try {
			sql = "INSERT INTO server_logs (uid, nickname, event, date, time) VALUES (?, ?, ?, ?, ?)";
			
			query = con.prepareStatement(sql);
			
			query.setString(1, uid);
			query.setString(2, nickname);
			query.setString(3, event);
			query.setDate(4, Date.valueOf(LocalDate.now()));
			query.setTime(5, Time.valueOf(LocalTime.now()));
			
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