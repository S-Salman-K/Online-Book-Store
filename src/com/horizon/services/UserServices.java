package com.horizon.services;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

import com.horizon.MySQLConnector;

public class UserServices {
	static Connection connection;
	
	static int viewBooks() throws SQLException {
		String query = "SELECT * FROM BOOKS";
		Statement st = connection.createStatement();
		ResultSet rs = st.executeQuery(query);
		
		System.out.println("Books: "+rs.getFetchSize());
		int bookCount = 0;
		while(rs.next()) {
			System.out.println(rs.getInt(1)+" "+rs.getString(2)+" "+rs.getString(3)+" "+rs.getString(4)+" "+rs.getString(5)+" "+rs.getInt(6)+" "+rs.getInt(7));
			bookCount++;
		}
		return bookCount;
	}

	
	public static String login() throws SQLException {
		Scanner sc = new Scanner(System.in);
		System.out.print("Username: ");
		String username = sc.next();
		System.out.print("Password: ");
		String password = sc.next();
		if(UserServices.areCredentialsValid(username, password)) {
			System.out.println("*** Welcome "+username+" ***");
			return username;
		}
		return "";
	}
	
	public static String inputUsername() throws SQLException {
		boolean isValidUsername = false;
		String username=null;
		Scanner sc = new Scanner(System.in);
		while(!isValidUsername) {
			System.out.print("Username: ");
			username = sc.next();
			if(username.length()<3)
				System.out.println("Username should be have atleast 3 characters.");
			else {
				isValidUsername = !UserServices.isUsernameAlreadyExist(username);
				if(!isValidUsername)
					System.out.println("Username already taken.");
			}
		}
//		sc.close();
		return username;
	}
	
	public static String inputPassword() throws SQLException {
		boolean isValidPassword = false;
		String password=null;
		Scanner sc = new Scanner(System.in);
		while(!isValidPassword) {
			System.out.print("Password: ");
			password = sc.next();
			if(password.length()<3) {
				System.out.println("Password should be have atleast 3 characters.");
			}
			isValidPassword = password.length()>=3;
		}
		return password;
	}
	
	public static boolean addUser() throws SQLException {
		String username = UserServices.inputUsername();
		String password = UserServices.inputPassword();
//		String query = "INSERT INTO USERS (Username, Password)VALUES(\""+username+"\",\""+password+"\");";
		Connection connection = MySQLConnector.getConnection();
		String query = "INSERT INTO USERS (Username, Password)VALUES(?,?)";
		PreparedStatement st = connection.prepareStatement(query);
		st.setString(1, username);
		st.setString(2, password);
		int row = st.executeUpdate();
		return row>0;
	}
	
	public static boolean isValidPassword(String password) {
		if(password.length()<3)
			return false;
		return true;
	}
	
	public static boolean isUsernameAlreadyExist(String username) throws SQLException {
		String getUser = "SELECT * FROM USERS WHERE USERNAME='"+username+"';";
		
		Connection connection = MySQLConnector.getConnection(); 
		Statement st = connection.createStatement();
		ResultSet rs = st.executeQuery(getUser);

		if(!rs.next()) {
			return false;
		}
		return true;
	}
	
	public static boolean areCredentialsValid(String username, String password) throws SQLException {
		String getUser = "SELECT * FROM USERS WHERE USERNAME='"+username+"';";
		
		connection = MySQLConnector.getConnection(); 
		Statement st = connection.createStatement();
		ResultSet rs = st.executeQuery(getUser);

		if(!rs.next()) {
			System.out.println("Not Registered!");
			return false;
		}
			
		String passwordDB = rs.getString(3);
		if(!passwordDB.equals(password)) {
			System.out.println("Invalid Password!");
			return false;
		}
		
		return true;		
	}
}
