import java.io.IOException;
import java.util.Comparator;

import com.jfoenix.controls.*;

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
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.beans.value.ObservableValue;

public class MyBooksController {

	@FXML
	private JFXListView<Book> booksView;

	private Comparator<Book> ascComp = new Comparator<Book>() {
		@Override
		public int compare(Book arg0, Book arg1) {
			return arg0.compareTo(arg1);
		}
	};

	private Comparator<Book> descComp = new Comparator<Book>() {
		@Override
		public int compare(Book arg0, Book arg1) {
			return arg1.compareTo(arg0);
		}
	};

	private String mode = "ASC";

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
				if (newValue != null)
					MyBooksLabel.setText("   " + newValue.getTitle());
			}
		});

		initContextMenu();

		searchField.textProperty().addListener((observable, oldValue, newValue) -> {
			if (newValue.length() == 0)
				searchBook("");
			else
				searchBook(newValue);
		});
		/*
		searchField.focusedProperty().addListener((obs, oldVal, newVal) -> { 
	    	if (newVal)
	    		searchIcon.setVisible(false); 
	    	else
	    		searchIcon.setVisible(true);
		});
		*/
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
		MenuItem m1 = new MenuItem("ASC");
		m1.setOnAction(action);
		MenuItem m2 = new MenuItem("DESC");
		m2.setOnAction(action);
		sortContext.getItems().setAll(m1, m2);
	}

	@FXML
	private void showMenu(MouseEvent event) {
		JFXButton b = (JFXButton) event.getSource();
		sortContext.show(b, event.getScreenX(), event.getScreenY());
	}

	private EventHandler<ActionEvent> getAction() {
		return new EventHandler<ActionEvent>() {

			public void handle(ActionEvent event) {
				MenuItem mItem = (MenuItem) event.getSource();
				String button = mItem.getText();
				if ("ASC".equals(button)) {
					mode = "ASC";
					sort();
				}
				if ("DESC".equals(button)) {
					mode = "DESC";
					sort();
				}
			}
		};
	}

	private void sort() {
		Comparator<Book> comp = mode.equals("ASC") ? ascComp : descComp;
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
		}
	}

	@FXML
	private void laterClick(MouseEvent event) {
		if (booksView.getItems() != laterList) {
			// laterList = dao.getAllBooks("later");
			booksView.setItems(laterList);
			state = State.LATER;
		}
	}

	@FXML
	private void readClick(MouseEvent event) {
		if (booksView.getItems() != readList) {
			// readList = dao.getAllBooks("read");
			booksView.setItems(readList);
			state = State.READ;
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

}