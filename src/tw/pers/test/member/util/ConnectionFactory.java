package tw.pers.test.member.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionFactory {

	public static Connection getConnection() throws SQLException {
		Connection conn = DriverManager.getConnection("jdbc:sqlite:MemberSystem.db");
		return conn;
	}
}
