package com.horizon.services;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

import com.horizon.MySQLConnector;

public class AdminServices {
	static Connection connection;
	
	
	static boolean isBookPresent(String title, String author, String publisher) throws SQLException {
		String query = "SELECT title, author, publisher FROM Books WHERE Title="+title;
		PreparedStatement st = connection.prepareStatement(query);
		ResultSet rs = st.executeQuery();
		if(rs.next()) {
			System.out.println(rs.getString(1));
			if(rs.getString(2).equals(author) && rs.getString(3).equals(publisher))
				return true;
		}
		return false;
	}
	
		
	static int isBookPresent(int bookID) throws SQLException {
		String query = "SELECT * FROM Books WHERE BookID="+bookID;
		Statement st = connection.createStatement();
		ResultSet rs = st.executeQuery(query);
		if(rs.next())
			return rs.getInt(7);
		return 0;
	}
	
	static void deleteBooks() throws SQLException {
		Scanner sc = new Scanner(System.in);
		UserServices.viewBooks();
		System.out.print("BookID: ");
		int bookID = sc.nextInt();
		System.out.print("Quantity: ");
		int quantityDelete = sc.nextInt();
		int quantityPresent = isBookPresent(bookID);
		int newQuantity = quantityPresent < quantityDelete ? 0 : quantityPresent-quantityDelete;
		String query = "UPDATE BOOKS SET QUANTITY=? WHERE BookID=?;";
		PreparedStatement st = connection.prepareStatement(query);
		st.setInt(1, newQuantity);
		st.setInt(2, bookID);
		int row = st.executeUpdate();
		if(row>0)
			System.out.println("Books Removed Successfully");
		else
			System.out.println("Books With "+bookID+" ID not present.");
	}
	
	static void addBooks() throws SQLException {
		Scanner sc = new Scanner(System.in);
		System.out.print("Title: ");
		String title = sc.nextLine();
		System.out.print("Author: ");
		String author = sc.nextLine();
		System.out.print("Publisher: ");
		String publisher = sc.nextLine();
		System.out.print("Genre: ");
		String genre = sc.nextLine();
		System.out.print("Price: ");
		int price = sc.nextInt();
		System.out.print("Quantity: ");
		int quantity = sc.nextInt();
		
		String query = "INSERT INTO BOOKS (Title, Author, Publisher, Genre, Price, Quantity)VALUES(?, ?, ?, ?, ?, ?);";
		PreparedStatement st = connection.prepareStatement(query);
		st.setString(1, title);
		st.setString(2, author);
		st.setString(3, publisher);
		st.setString(4, genre);
		st.setInt(5, price);
		st.setInt(6, quantity);
		
		int row = st.executeUpdate();
		if(row>0)
			System.out.println("Books Added Successfully.");
		else
			System.out.println("Failed to Add Books.");
	} 
	
	
	
	public static void menu() throws SQLException {
		Scanner sc = new Scanner(System.in);
		connection = MySQLConnector.getConnection();
		
		String choice = "";
		while(!choice.equals("4")) {
			System.out.println("----------------------------------------------------------------------------------------------------------------------------------------------");
			System.out.println("1. View Books");
			System.out.println("2. Add Books");
			System.out.println("3. Delete Books");
			System.out.println("4. Log Out");
			System.out.println("----------------------------------------------------------------------------------------------------------------------------------------------");
			choice = sc.next();
			switch(choice) {
				case "1":
					UserServices.viewBooks();
					break;
				case "2":
					addBooks();
					break;
				case "3":
					deleteBooks();
					break;
				case "4":
					System.out.println("Logged Out Successfully.");
					break;
				default:
					System.out.println("Wrong Input!");
			}
		}
	}
}
