import java.awt.Desktop;
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

	//private Desktop desktop = Desktop.getDesktop();

	@FXML
	private JFXTextField titleField;

	@FXML
	private JFXButton linkButton;

	@FXML
	private JFXButton imageButton;

	@FXML
	private JFXButton addButton2;

	public void initialize() {
		contr = MyBooks.getMyContr();
	}
	
	@FXML
	void getLink(MouseEvent event) {
		FileChooser chooser = new FileChooser();
		chooser.setTitle("Select Book");
		chooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("All Files", "*.*"),
				new FileChooser.ExtensionFilter("PDF", "*.PDF"), new FileChooser.ExtensionFilter("TXT", "*.txt"));
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
	void getImage(MouseEvent event) throws IOException {
		FileChooser chooser = new FileChooser();
		chooser.setTitle("Select Image");
		chooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("All Files", "*.*"),
				new FileChooser.ExtensionFilter("JPG", "*.jpg"), new FileChooser.ExtensionFilter("PNG", "*.png"));
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
	void addBook(MouseEvent event) {
		title = titleField.getText();
		if (title != null && !"".equals(title) && link != null) {
			image = (image == null) ? "" : image;
			
			imageButton.setDisable(true);
			titleField.setEditable(false);
			titleField.setFocusTraversable(false);
			JFXButton b = (JFXButton) event.getSource();
			b.setDisable(true);
			
			BookDao dao = new BookDao();
			State state = contr.getState();
			
			switch (state) {
			case READING:
				dao.addBook(new Book(title, link, image, 0), "reading");
				break;
			case LATER:
				dao.addBook(new Book(title, link, image, 0), "later");
				break;
			case READ:
				dao.addBook(new Book(title, link, image, 0), "read");
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
