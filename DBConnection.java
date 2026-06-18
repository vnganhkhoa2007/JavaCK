package Java;

import java.sql.Connection;
import java.sql.DriverManager;

public class DBConnection {

	private static final String URL = "jdbc:sqlserver://localhost:1433;" + "databaseName=QLCAPTHUOC;" + "encrypt=false;"
			+ "integratedSecurity=true;";

	public static Connection getConnection() throws Exception {

		return DriverManager.getConnection(URL);

	}

}