package edu.util;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

public class BaseDAO {
	private Connection conn;
	private PreparedStatement pstmt;
	private ResultSet rs;
	public BaseDAO() {
		try {
			conn = getConnection();
		} catch (IOException | SQLException e) {
			System.out.println("数据库获取链接失败！");
			e.printStackTrace();
		}
	}
	
	public static Connection getConnection() throws IOException, SQLException {
		Properties props = new Properties();
		try (InputStream in = Files.newInputStream(Paths.get("database.properties"))) {
			props.load(in);
		}
		
		String drivers = props.getProperty("jdbc.drivers");
		if (drivers != null) System.setProperty("jdbc.drivers", drivers);
		String url = props.getProperty("jdbc.url");
		String username = props.getProperty("jdbc.username");
		String password = props.getProperty("jdbc.password");
		
		return DriverManager.getConnection(url,username,password);
	}
	
	public ResultSet executeQuery(String sql,Object[] params) throws SQLException {
		 pstmt = conn.prepareStatement(sql);
		 if (params != null) {
			 for(int i = 1;i <= params.length;i++) {
				 pstmt.setObject(i, params[i-1]);
			 }
		 }
		 rs = pstmt.executeQuery();
		 return rs;
	}
	
	public int executeUpdate(String sql,Object[] params) throws SQLException {
		pstmt = conn.prepareStatement(sql);
		if (params != null) {
			for(int i = 0;i < params.length;i++) {
				pstmt.setObject(i+1, params[i]);
			}
		}
		int result = pstmt.executeUpdate();
		return result;
	}
	
	public void close() {
		if (conn != null) {
			try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		if (pstmt != null) {
			try {
				pstmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		if (rs != null) {
			try {
				rs.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
}
