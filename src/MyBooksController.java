import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.util.Comparator;

import com.jfoenix.controls.*;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.beans.value.ObservableValue;

public class MyBooksController {

	@FXML
	private JFXListView<Book> booksView;

	private Desktop desktop = Desktop.getDesktop();

	private Book currentBook;

	private Comparator<Book> ascTitleComp = new Comparator<Book>() {
		@Override
		public int compare(Book arg0, Book arg1) {
			return arg0.compareTo(arg1);
		}
	};

	private Comparator<Book> descTitleComp = new Comparator<Book>() {
		@Override
		public int compare(Book arg0, Book arg1) {
			return arg1.compareTo(arg0);
		}
	};

	private Comparator<Book> ascRateComp = new Comparator<Book>() {
		@Override
		public int compare(Book arg0, Book arg1) {
			Integer r0 = arg0.getRate();
			Integer r1 = arg1.getRate();
			return r0.compareTo(r1);
		}
	};

	private Comparator<Book> descRateComp = new Comparator<Book>() {
		@Override
		public int compare(Book arg0, Book arg1) {
			Integer r0 = arg0.getRate();
			Integer r1 = arg1.getRate();
			return r1.compareTo(r0);
		}
	};

	private String mode = "ascTitle";
	
	private ObservableList<Book> readingList = FXCollections.observableArrayList();
	private ObservableList<Book> laterList = FXCollections.observableArrayList();
	private ObservableList<Book> readList = FXCollections.observableArrayList();
	private BookDao dao;
	private static State state = State.READING;

	@FXML
	private JFXTextField searchField;
	private ContextMenu sortContext;
	@FXML
	private FontAwesomeIconView searchIcon;

	private EventHandler<ActionEvent> action = getAction();

	@FXML
	private Label MyBooksLabel;

	@FXML
	private ImageView imageView;
	@FXML
	private JFXBadge star;
	@FXML
	private JFXTextArea descriptionArea;
	@FXML
	private Label titleLabel;
	@FXML
	private FontAwesomeIconView openButton;
	@FXML
	private FontAwesomeIconView deleteIcon;

	public void initialize() {
		dao = new BookDao();
		readingList = dao.getAllBooks("reading");
		laterList = dao.getAllBooks("later");
		readList = dao.getAllBooks("read");
		state = State.READING;
		booksView.setItems(readingList);
		booksView.setExpanded(true);

		booksView.setCellFactory(new Callback<ListView<Book>, ListCell<Book>>() {
			@Override
			public ListCell<Book> call(ListView<Book> param) {
				return new BookCell();
			}
		});

		booksView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Book>() {
			@Override
			public void changed(ObservableValue<? extends Book> observable, Book oldValue, Book newValue) {
				if (newValue != null) {
					currentBook = newValue;
					showBook(newValue);
				}
			}
		});

		initContextMenu();

		searchField.textProperty().addListener((observable, oldValue, newValue) -> {
			if (newValue.length() == 0)
				searchBook("");
			else
				searchBook(newValue);
		});
	}

	void searchBook(String s) {
		ObservableList<Book> searchList = FXCollections.observableArrayList();
		ObservableList<Book> oldList = FXCollections.observableArrayList();
		switch (state) {
		case READING:
			oldList = readingList;
			break;
		case LATER:
			oldList = laterList;
			break;
		case READ:
			oldList = readList;
			break;
		}
		if (s.length() != 0) {
			searchIcon.setVisible(false);
			for (Book b : oldList)
				if ((b.getTitle().toLowerCase()).contains(s.toLowerCase()))
					searchList.add(b);
			booksView.setItems(searchList);
		} else {
			searchIcon.setVisible(true);
			booksView.setItems(oldList);
		}
	}

	private void initContextMenu() {
		sortContext = new ContextMenu();
		MenuItem m1 = new MenuItem();
		m1.setGraphic(new FontAwesomeIconView(FontAwesomeIcon.SORT_ALPHA_ASC));
		m1.setId("ascTitle");
		m1.setOnAction(action);

		MenuItem m2 = new MenuItem();
		m2.setGraphic(new FontAwesomeIconView(FontAwesomeIcon.SORT_ALPHA_DESC));
		m2.setId("descTitle");
		m2.setOnAction(action);

		MenuItem m3 = new MenuItem();
		m3.setGraphic(new FontAwesomeIconView(FontAwesomeIcon.SORT_NUMERIC_ASC));
		m3.setId("ascRate");
		m3.setOnAction(action);

		MenuItem m4 = new MenuItem();
		m4.setGraphic(new FontAwesomeIconView(FontAwesomeIcon.SORT_NUMERIC_DESC));
		m4.setId("descRate");
		m4.setOnAction(action);

		sortContext.getItems().setAll(m1, m2, m3, m4);
	}

	@FXML
	private void showMenu(MouseEvent event) {
		JFXButton b = (JFXButton) event.getSource();
		sortContext.show(b, event.getScreenX(), event.getScreenY());
	}

	private EventHandler<ActionEvent> getAction() {
		return new EventHandler<ActionEvent>() {
			public void handle(ActionEvent event) {
				MenuItem m = (MenuItem) event.getSource();
				System.out.println(m.getId());
				mode = m.getId();
				sort();
			}
		};
	}

	private void sort() {
		Comparator<Book> comp = ascTitleComp;
		switch (mode) {
		case "ascTitle":
			comp = ascTitleComp;
			break;
		case "descTitle":
			comp = descTitleComp;
			break;
		case "ascRate":
			comp = ascRateComp;
			break;
		case "descRate":
			comp = descRateComp;
			break;
		}
		booksView.getItems().sort(comp);
	}

	public void refresh(State state) {
		switch (state) {
		case READING:
			readingList = dao.getAllBooks("reading");
			booksView.setItems(readingList);
			break;
		case LATER:
			laterList = dao.getAllBooks("later");
			booksView.setItems(laterList);
			break;
		case READ:
			readList = dao.getAllBooks("read");
			booksView.setItems(readList);
			break;
		}
	}

	@FXML
	private void readingClick(MouseEvent event) {
		if (booksView.getItems() != readingList) {
			// readingList = dao.getAllBooks("reading");
			booksView.setItems(readingList);
			state = State.READING;
			searchField.clear();
			hideBook();
		}
	}

	@FXML
	private void laterClick(MouseEvent event) {
		if (booksView.getItems() != laterList) {
			// laterList = dao.getAllBooks("later");
			booksView.setItems(laterList);
			state = State.LATER;
			searchField.clear();
			hideBook();
		}
	}

	@FXML
	private void readClick(MouseEvent event) {
		if (booksView.getItems() != readList) {
			// readList = dao.getAllBooks("read");
			booksView.setItems(readList);
			state = State.READ;
			searchField.clear();
			hideBook();
		}
	}

	public State getState() {
		return state;
	}

	@FXML
	private void openAddBook(MouseEvent event) throws IOException {
		Parent root = FXMLLoader.load(getClass().getResource("AddForm.fxml"));
		Stage stage = new Stage();
		Scene scene = new Scene(root);
		scene.getStylesheets().add("style.css");
		stage.setTitle("Add Book");
		stage.setScene(scene);
		stage.setWidth(356);
		stage.setHeight(295);
		stage.setResizable(false);
		stage.show();
	}

	@FXML
	void starClicked(MouseEvent event) {
		int val = Integer.parseInt(star.getText());
		if (val == 5) {
			star.setText("0");
			currentBook.setRate(0);
		} else {
			star.setText(Integer.toString(val + 1));
			if (currentBook != null)
				currentBook.setRate(val + 1);
		}
	}

	@FXML
	void deleteClicked(MouseEvent event) {
		deleteBook(currentBook);
		hideBook();
		refresh(state);
	}

	@FXML
	void mouseOver(MouseEvent event) {
		imageView.setOpacity(0.3);
		openButton.setVisible(true);
		star.setVisible(true);
	}

	@FXML
	void mouseOut(MouseEvent event) {
		imageView.setOpacity(1);
		openButton.setVisible(false);
		star.setVisible(false);
	}

	@FXML
	void openInOS(MouseEvent event) {
		try {
			if (currentBook.getLink().length() > 6) {
				File file = new File(currentBook.getLink().substring(6));
				if (file.exists())
					desktop.open(file);
			}
		} catch (IOException e) {
			System.out.println("error");
		}
	}

	public void showBook(Book book) {
		MyBooksLabel.setVisible(false);
		imageView.setVisible(true);
		// star.setVisible(true);
		deleteIcon.setVisible(true);
		descriptionArea.setVisible(true);
		titleLabel.setVisible(true);
		descriptionArea.setText(book.getLink());
		titleLabel.setText(book.getTitle());
		star.setText(Integer.toString(book.getRate()));
		try {
			imageView.setImage(new Image(book.getImage()));
		} catch (IllegalArgumentException e) {
			imageView.setImage(new Image("images/default.jpg"));
		}
	}

	public void hideBook() {
		MyBooksLabel.setVisible(true);
		imageView.setVisible(false);
		deleteIcon.setVisible(false);
		descriptionArea.setVisible(false);
		titleLabel.setVisible(false);
	}

	public void deleteBook(Book book) {
		if (book != null) {
			switch (state) {
			case READING:
				dao.delete(book, "reading");
				break;
			case LATER:
				dao.delete(book, "later");
				break;
			case READ:
				dao.delete(book, "read");
				break;
			}
		}
	}

}