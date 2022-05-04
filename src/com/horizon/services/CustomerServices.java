package com.horizon.services;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import com.horizon.MySQLConnector;

public class CustomerServices {
	static Connection connection;
	
	static int isBookAvailable(String bookID) throws SQLException {
		String query = "SELECT Quantity FROM Books WHERE BookID="+bookID;
		Statement st = connection.createStatement();
		ResultSet rs = st.executeQuery(query);
		if(!rs.next())
			return 0;
		return rs.getInt(1);
	}
	
	public static ResultSet getBookDetails(String bookID) throws SQLException {
		String query = "SELECT Title, Price, Quantity FROM Books WHERE BookID="+bookID;
		Statement st = connection.createStatement();
		ResultSet rs = st.executeQuery(query);
		return rs;
	}
	
	static void checkout(HashMap<String, Integer> cart) throws SQLException {
		if(cart.isEmpty()) {
			System.out.println("Cart is Empty!");
			return;
		}
		String query = "UPDATE Books SET Quantity = ? WHERE BookID = ?";
		PreparedStatement st = connection.prepareStatement(query);
		for(Map.Entry<String, Integer> book: cart.entrySet()) {
			ResultSet rs = getBookDetails(book.getKey());
			rs.next();
			st.setInt(1, rs.getInt(3)-book.getValue());
			st.setInt(2, Integer.parseInt(book.getKey()));
			st.executeUpdate();
		}
		System.out.println("Order Placed.");
		cart.clear();
	}
	
	static void viewCart(HashMap<String, Integer> cart) throws SQLException {
		if(isCartEmpty(cart)) {
			System.out.println("Cart is Empty!");
			return;
		}
		int cartPrice = 0; 
		for(Map.Entry<String, Integer> book: cart.entrySet()) {
			ResultSet rs = getBookDetails(book.getKey());
//			while(rs.next())
//				System.out.println(rs.getString(1)+" "+rs.getInt(2));
			rs.next();
			String title = rs.getString(1);
			int price = rs.getInt(2);
			System.out.println(book.getKey()+". Title: "+title+" |Quantity: "+book.getValue()+" | Price: "+price+" | Amount: "+book.getValue()*price);
			cartPrice += book.getValue()*price;
		}
		System.out.println("Total Amount: "+cartPrice);
	}
	
	static boolean isCartEmpty(HashMap<String, Integer> cart) {
		int count = 0;
		for(int value: cart.values())
			count += value;
		return count==0;
	}
	
	static void removeFromCart(HashMap<String, Integer> cart) {
		
		if(isCartEmpty(cart)) {
			System.out.println("Cart is Empty!");
			return;
		}
		
		Scanner sc = new Scanner(System.in);
		while(true) {
			System.out.print("Enter the BookID (Enter 0 to go back): ");
			String bookID = sc.next();
			try {
				Integer.parseInt(bookID);
			}	
			catch(NumberFormatException e) {
				System.out.println("Enter valid BookID.");
				continue;
			}
			
			if(bookID.equals("0"))
				break;
			
			
			if(cart.containsKey(bookID)) {
				while(true) {
					System.out.print("Quantity: ");
					String quantityToRemove = sc.next();
					try {
						Integer.parseInt(quantityToRemove);
					}
					catch(Exception e) {
						System.out.println("Enter Valid Quantity!");
						continue;
					}
					
					int updateQuantity = cart.get(bookID)<Integer.parseInt(quantityToRemove)?0:cart.get(bookID)-Integer.parseInt(quantityToRemove);
					cart.put(bookID, updateQuantity);
					break;
				}
 			}
			else
				System.out.println("Not in Cart!");
		}
	}
	
	static void addToCart(HashMap<String, Integer> cart) throws SQLException {
		Scanner sc = new Scanner(System.in);
		while(true) {
			System.out.print("Enter the BookID (Enter 0 to go back): ");
			String bookID = sc.next();
			try {
				Integer.parseInt(bookID);
			}	
			catch(NumberFormatException e) {
				System.out.println("Enter valid BookID.");
				continue;
			}
			
			if(bookID.equals("0"))
				break;
			
			int booksAvailable = isBookAvailable(bookID);
			
			if(booksAvailable > 0) {
				while(true) {
					System.out.print("Quantity: ");
					int quantity = sc.nextInt();
					if(cart.containsKey(bookID)) {
						int booksInCart = cart.get(bookID);
						if(quantity > booksAvailable-booksInCart) {
							System.out.println("Only "+(booksAvailable-booksInCart)+" books avialable.");
						}
						else {
							cart.put(bookID, cart.get(bookID)+quantity);
							break;
						}
					}
					else {
						cart.put(bookID, quantity);
						break;
					}
				}
			}
			else {
				System.out.println("Book Unavailable.");
			}
		}
	}
	
	static void buyBooks() throws SQLException {
//		System.out.println("We are closed! Come back later.");
		Scanner sc = new Scanner(System.in);
		String choice = "";
		HashMap<String, Integer> cart = new HashMap<>();
		while(!choice.equals("6")) {
			System.out.println("------------------------------------------------------------------------------------------------------------------------------");
			System.out.println("1. View Books.");
			System.out.println("2. Add Books to Cart.");
			System.out.println("3. Remove Books from Cart.");
			System.out.println("4. View Cart");
			System.out.println("5. Checkout.");
			System.out.println("6. Exit.");
			System.out.println("------------------------------------------------------------------------------------------------------------------------------");
			choice =sc.next();
			
			switch(choice) {
				case "1":
					UserServices.viewBooks();
					break;
				case "2":
					addToCart(cart);
					break;
				case "3":
					removeFromCart(cart);
					break;
				case "4":
					viewCart(cart);
					break;
				case "5":
					checkout(cart);
					break;
				case "6":
					System.out.println("Exiting Buying Window.");
					break;					
			}
		}		
	}

	
	public static void menu(String username) throws SQLException {
		Scanner sc = new Scanner(System.in);
		connection = MySQLConnector.getConnection();
		String choice = "";
		
		while(!choice.equals("3")) {
			System.out.println("----------------------------------------------------------------------------------------------------------------------------------------------");
			System.out.println("1. View Books");
			System.out.println("2. Buy Books");
			System.out.println("3. Log Out");
			System.out.println("----------------------------------------------------------------------------------------------------------------------------------------------");
			choice = sc.next();
			switch(choice) {
				case "1":
					UserServices.viewBooks();
					break;
				case "2":
//					System.out.println("We are closed! Come back later.");
					buyBooks();
					break;
				case "3":
					System.out.println("Logged Out Successfully.");
					break;
				default:
					System.out.println("Wrong Input!");
			}
		}
	}
}
