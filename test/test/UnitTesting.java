package test;

import static org.junit.Assert.assertEquals;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.junit.jupiter.api.Test;

import com.horizon.MySQLConnector;
import com.horizon.services.CustomerServices;

class UnitTesting {

	@Test
	void test() throws SQLException {
		ResultSet st = CustomerServices.getBookDetails("1");
		String bookName = st.getString(1);
		assertEquals("The Da Vinci Code", bookName);
	}

}
