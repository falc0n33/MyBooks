import java.sql.*;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class BookDao {

	private Connection connection;

	public BookDao() {
		connection = DbUtil.getConnection();
	}

	public boolean addBook(Book book, String category) {
		try {
			PreparedStatement preparedStatement = connection.prepareStatement(
					"insert into " + category + "(title,link,image,rate,comment) values (?, ?, ?, ?,?)");
			preparedStatement.setString(1, book.getTitle());
			preparedStatement.setString(2, book.getLink());
			preparedStatement.setString(3, book.getImage());
			preparedStatement.setInt(4, book.getRate());
			preparedStatement.setString(5, book.getComment());
			preparedStatement.executeUpdate();
			return true;
		} catch (SQLException e) {
			// e.printStackTrace();
			return false;
		}
	}

	public boolean update(Book book, String category) {
		try {
			PreparedStatement preparedStatement = connection.prepareStatement(
					"update " + category + " set title = ?, image = ?, rate = ?, comment = ? where link = ?");
			preparedStatement.setString(1, book.getTitle());
			preparedStatement.setString(2, book.getImage());
			preparedStatement.setInt(3, book.getRate());
			preparedStatement.setString(4, book.getComment());
			preparedStatement.setString(5, book.getLink());
			preparedStatement.executeUpdate();
			return true;
		} catch (SQLException e) {
			// e.printStackTrace();
			return false;
		}
	}

	public void clear(String category) {
		try {
			Statement statement = connection.createStatement();
			statement.executeUpdate("delete from " + category);
		} catch (SQLException e) {
			// e.printStackTrace();
		}
	}

	public ObservableList<Book> getAllBooks(String category) {
		ObservableList<Book> books = FXCollections.observableArrayList();
		try {
			Statement statement = connection.createStatement();
			ResultSet rs = statement.executeQuery("select * from " + category);
			while (rs.next()) {
				Book b = new Book();
				b.setTitle(rs.getString(2));
				b.setLink(rs.getString(3));
				b.setImage(rs.getString(4));
				b.setRate(rs.getInt(5));
				b.setComment(rs.getString(6));
				books.add(b);
			}
		} catch (SQLException e) {
			// e.printStackTrace();
		}

		return books;
	}

	public boolean move(Book book, String from, String to) {
		if (addBook(book, to)) {
			delete(book, from);
			return true;
		}
		return false;
	}

	public boolean delete(Book book, String from) {
		try {
			Statement statement = connection.createStatement();
			statement.executeUpdate("delete from " + from + " where title = '" + book.getTitle() + "'");
			return true;
		} catch (SQLException e) {
			// e.printStackTrace();
			return false;
		}
	}

}
