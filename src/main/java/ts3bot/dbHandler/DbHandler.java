package main.java.ts3bot.dbHandler;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


public class DbHandler {
	
	public static Connection connect() {
		Connection con = null;
		
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			con = DriverManager.getConnection("jdbc:mysql://localhost:3306/ts3bot?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC", "root", "12ght3pA");
		}
		catch(Exception e) {
			System.out.println(e);
		}
		
		return con;
	}
	
	public static void close(ResultSet result, PreparedStatement query, Connection con) {
		if (result != null) {
	        try {
	            result.close();
	        } catch (SQLException e) { /* ignored */}
	    }
	    if (query != null) {
	        try {
	            query.close();
	        } catch (SQLException e) { /* ignored */}
	    }
	    if (con != null) {
	        try {
	            con.close();
	        } catch (SQLException e) { /* ignored */}
	    }
	}
	
	public static void addClientOnline(int id, String nickname, String channel, String status) {
		Connection con = connect();
		
		PreparedStatement query = null;
		ResultSet result = null;
		String sql;
		
		try {
			sql = "INSERT INTO online_user_list (id, nickname, channel, status) VALUES (?, ?, ?, ?)";
			query = con.prepareStatement(sql);
			
			query.setInt(1, id);
			query.setString(2, nickname);
			query.setString(3, channel);
			query.setString(4, status);
			
			query.executeUpdate();
		}
		catch(SQLException e) {
			System.out.println(e);
		}
		
		finally {
		    close(result, query, con);
		}	
	}
	
	public static void deleteClientOnline(int id) {
		Connection con = connect();
		
		PreparedStatement query = null;
		ResultSet result = null;
		String sql;
		
		try {
			sql = "DELETE FROM online_user_list WHERE id=?";
			query = con.prepareStatement(sql);
			
			query.setInt(1, id);
			
			query.executeUpdate();
		}
		catch(SQLException e) {
			System.out.println(e);
		}
		
		finally {
		    close(result, query, con);
		}
	}
	
	public static void updateClientChannel(int id, String channel) {
		Connection con = connect();
		
		PreparedStatement query = null;
		ResultSet result = null;
		String sql;
		
		try {
			sql = "UPDATE online_user_list SET channel=? WHERE id=?";
			query = con.prepareStatement(sql);
			
			query.setString(1, channel);
			query.setInt(2, id);
			
			query.executeUpdate();
		}
		catch(SQLException e) {
			System.out.println(e);
		}
		
		finally {
		    close(result, query, con);
		}
		
	}
	
	public static void updateClientStatus(int id, String status) {
		Connection con = connect();
		
		PreparedStatement query = null;
		ResultSet result = null;
		String sql;
		
		try {
			sql = "UPDATE online_user_list SET status=? WHERE id=?";
			query = con.prepareStatement(sql);
			
			query.setString(1, status);
			query.setInt(2, id);
			
			query.executeUpdate();
		}
		catch(SQLException e) {
			System.out.println(e);
		}
		
		finally {
		    close(result, query, con);
		}
		
	}
	
	public static String getClientStatus(int id) {
		Connection con = connect();
		
		PreparedStatement query = null;
		ResultSet result = null;
		
		String sql;
		
		String status = "online";
		
		try {
			sql = "SELECT * FROM online_user_list WHERE id=?";
			query = con.prepareStatement(sql);
			
			query.setInt(1, id);
			
			result = query.executeQuery();
			
			if(result.next()) {
				status = result.getString(4);
			}
		}
		catch(SQLException e) {
			System.out.println(e);
		}
		
		finally {
		    close(result, query, con);
		}
		
		return status;
	}
	
	public static void emptyClientOnlineList() {
		Connection con = connect();
		
		PreparedStatement query = null;
		ResultSet result = null;
		String sql;
		
		try {
			sql = "TRUNCATE TABLE online_user_list";
			query = con.prepareStatement(sql);
			
			query.executeUpdate();
		}
		catch(SQLException e) {
			System.out.println(e);
		}
		
		finally {
		    close(result, query, con);
		}
	}

}
