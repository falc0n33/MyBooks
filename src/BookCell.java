import java.io.File;

import com.jfoenix.controls.JFXListCell;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.TextAlignment;

public class BookCell extends JFXListCell<Book> {
	private HBox box = new HBox(8);
	private ImageView thumbImage = new ImageView();
	private Label label = new Label();

	public BookCell() {
		box.setAlignment(Pos.CENTER_LEFT);

		thumbImage.setPreserveRatio(true);
		thumbImage.setFitHeight(100);
		box.getChildren().add(thumbImage);

		label.setWrapText(true);
		label.setTextAlignment(TextAlignment.CENTER);
		box.getChildren().add(label);

		setPrefWidth(USE_PREF_SIZE);
	}

	@Override
	public void updateItem(Book item, boolean empty) {
		super.updateItem(item, empty);

		if (empty || item == null) {
			setGraphic(null);
		} else {
			setText("");
			try {
				File file = new File(item.getImage().substring(6, item.getImage().length()));
				if (file.exists()) {
					Image image = new Image(item.getImage());
					thumbImage.setImage(image);
				} else
					thumbImage.setImage(new Image("images/default.jpg"));
			} catch (Exception e) {
				thumbImage.setImage(new Image("images/default.jpg"));
			}
			label.setText(item.getTitle());
			setStyle("-fx-background-color: #34495e;");
			label.setTextFill(Color.WHITE);
			setGraphic(box);
		}

	}

}
