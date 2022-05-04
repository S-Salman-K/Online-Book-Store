package com.horizon;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MySQLConnector {
	private static final String url = "jdbc:mysql://localhost:3306/book_store";
	private static final String username = "root";
	private static final String password = "MySQL@Root10";
	public static Connection connection;
	
	
	public static Connection getConnection() throws SQLException {
		if(connection==null) {
			try {
				Class.forName("com.mysql.cj.jdbc.Driver");
			}
			catch(ClassNotFoundException ex){
				System.out.println("Could not register server: "+ex.getMessage());
				ex.printStackTrace();
			}
			connection = DriverManager.getConnection(url, username, password);
		}
		
		if(connection.isClosed()) {
			connection = DriverManager.getConnection(url, username, password);
		}
		return connection;
	}
}
