import java.sql.SQLException;
import java.util.Scanner;

import com.horizon.services.AdminServices;
import com.horizon.services.CustomerServices;
import com.horizon.services.UserServices;

public class Main {
	
	/**
	 * @param args
	 * @throws SQLException
	 */
	public static void main(String args[]) throws SQLException {
		System.out.println("Welcome to Horizon Book Store");
		Scanner sc = new Scanner(System.in);
		String choice = "", username, password;
		while(!choice.equals("3")) {
			System.out.println("----------------------------------------------------------------------------------------------------------------------------------------------");
			System.out.println("1. Login.");
			System.out.println("2. Sign Up");
			System.out.println("3. Exit");
			System.out.println("----------------------------------------------------------------------------------------------------------------------------------------------");
			choice = sc.next();
			switch(choice) {
				case "1":
					username = UserServices.login();
					if(!username.equals("")) {
						if(username.equals("Admin"))
							AdminServices.menu();
						else {
							CustomerServices.menu(username);
						}
					}
					break;
				case "2":
					if(UserServices.addUser())
						System.out.println("Registerd Successfully");
					break;
				case "3":
					break;
				default:
					System.out.println("Wrong input");
			}
		}
		System.out.println("Program Terminated.");
	}
}
	
	
//	public static void main(String[] args) throws ClassNotFoundException, SQLException{
////		load and register Driver
//		Class.forName("com.mysql.jdbc.Driver");
////		Create connection
//		String url = "jdbc:mysql://localhost:3306/book_store";
//		String username = "root";
//		String password = "MySQL@Root10";
//		Connection con = DriverManager.getConnection(url, username, password);
//		Statement st = con.createStatement();
//		
//		int id=7;
//		String bookTable = "SELECT * FROM Books";
//		ResultSet rs = st.executeQuery(bookTable);
//		while(rs.next()) {
//			System.out.println(rs.getInt(1)+" "+rs.getString(2));
//		}
//		
//		
//		String title = "'Introduction to Algorithms'";
//		String author = "'Corman'";
//		String publisher = "'PHI'";
//		String genre = "'Horror'";
//		int quantity = 10;
//		
//		String insertBookInfo = "INSERT INTO Books (Title, Author, Publisher, Genre, Quantity)VALUES("
//		+title+","+author+","+publisher+","+genre+","+quantity+");";
////		String insertB2Info = "INSERT INTO BOOKS (Title, Author, Publisher, Genre, Quantity)VALUES("+title+","+author+","+ author+","+genre+","+30+")";
////		int rowsAffected = st.executeUpdate(insertBookInfo); 
////		System.out.println(rowsAffected+" rows affected.");
////		
//		String delete = "DELETE FROM Books where BookId IN (10, 11)";
//		int rowsAffectedDelete = st.executeUpdate(delete);
//		System.out.println(rowsAffectedDelete);
//		
//		st.close();
//		con.close();
//	}
//
//}
