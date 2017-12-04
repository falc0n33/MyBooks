import java.sql.*;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class BookDao {

    private Connection connection;

    public BookDao() {
        connection = DbUtil.getConnection();
    }

    public void addBook(Book book, String category) {
        try {
            PreparedStatement preparedStatement = connection
                    .prepareStatement("insert into " + category + "(title,link,image,rate) values (?, ?, ?, ?)");
            preparedStatement.setString(1, book.getTitle());
            preparedStatement.setString(2, book.getLink());
            preparedStatement.setString(3, book.getImage());
            preparedStatement.setInt(4, book.getRate());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void clear(String category) {
        try {
        	Statement statement = connection.createStatement();
            statement.executeUpdate("delete from " + category);
        } catch (SQLException e) {
            //e.printStackTrace();
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
                books.add(b);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return books;
    }
    /*
    public void move(Book book, String from, String to) {
        try {
            Statement statement = connection.createStatement();
            statement.executeUpdate("delete from " + from + " where title = '" + book.getTitle() + "'");
        	PreparedStatement preparedStatement = connection
                    .prepareStatement("insert into " + to + "(title,link,image,rate) values (?, ?, ?, ?)");
            preparedStatement.setString(1, book.getTitle());
            preparedStatement.setString(2, book.getLink());
            preparedStatement.setString(3, book.getImage());
            preparedStatement.setInt(4, book.getRate());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }*/
    
    public void move(Book book, String from, String to) {
    	delete(book, from);
    	addBook(book, to);
    }


    public void delete(Book book, String from) {
        try {
            Statement statement = connection.createStatement();
            statement.executeUpdate("delete from " + from + " where title = '" + book.getTitle() + "'");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
