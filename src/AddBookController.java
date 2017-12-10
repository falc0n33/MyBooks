import java.io.File;
import java.io.IOException;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class AddBookController {

	private MyBooksController contr;
	private String title;
	private String link;
	private String image;

	@FXML
	private JFXTextField titleField;

	@FXML
	private JFXButton linkButton;

	@FXML
	private JFXButton imageButton;

	@FXML
	private JFXButton addButton2;

	@FXML
	private void initialize() {
		contr = MyBooks.getMyContr();
	}

	@FXML
	private void getLink(MouseEvent event) {
		FileChooser chooser = new FileChooser();
		chooser.setTitle("Select Book");
		chooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Books", " *.pdf", " *.fb2", " *.doc",
				" *.docx", " *.txt", " *.epub", " *.mobi"));
		Node node = (Node) event.getSource();
		File file = chooser.showOpenDialog(node.getScene().getWindow());
		if (file != null) {
			link = file.toURI().toString();
			link = link.replaceAll("%20", " ");
			JFXButton b = (JFXButton) event.getSource();
			b.setDisable(true);
		}
	}

	@FXML
	private void getImage(MouseEvent event) throws IOException {
		FileChooser chooser = new FileChooser();
		chooser.setTitle("Select Image");
		chooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Images", " *.jpg", " *.png"));
		Node node = (Node) event.getSource();
		File file = chooser.showOpenDialog(node.getScene().getWindow());
		if (file != null) {
			image = file.toURI().toString();
			image = image.replaceAll("%20", " ");
			JFXButton b = (JFXButton) event.getSource();
			b.setDisable(true);
		}
	}

	@FXML
	private void addBook(MouseEvent event) {
		title = titleField.getText();
		if (title != null && !"".equals(title) && link != null) {
			image = (image == null) ? "file:/" : image;

			imageButton.setDisable(true);
			titleField.setEditable(false);
			titleField.setFocusTraversable(false);
			JFXButton b = (JFXButton) event.getSource();
			b.setDisable(true);

			BookDao dao = new BookDao();
			State state = contr.getCurrentState();

			switch (state) {
			case READING:
				dao.addBook(new Book(title, link, image, 0, "..."), "reading");
				break;
			case LATER:
				dao.addBook(new Book(title, link, image, 0, "..."), "later");
				break;
			case READ:
				dao.addBook(new Book(title, link, image, 0, "..."), "read");
				break;
			}

			contr.refresh(state);

			Stage stage = (Stage) addButton2.getScene().getWindow();
			stage.close();
		} else {
			titleField.requestFocus();
		}
	}
}
