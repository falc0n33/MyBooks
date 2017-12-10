import java.sql.*;

public class DbUtil {

	private static Connection connection = null;

	public static Connection getConnection() {
		if (connection != null)
			return connection;
		else {
			try {
				Class.forName("org.sqlite.JDBC");
				connection = DriverManager.getConnection("jdbc:sqlite::resource:BooksDB.s3db");
			} catch (ClassNotFoundException | SQLException e) {
				System.out.println(e);
			}
			return connection;
		}

	}
}
