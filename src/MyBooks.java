import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.*;
import javafx.scene.image.Image;
import javafx.stage.*;

public class MyBooks extends Application {

	private static MyBooksController myContr;

	@Override
	public void start(Stage stage) throws Exception {
		// Parent root = FXMLLoader.load(getClass().getResource("MyBooks.fxml"));
		FXMLLoader loader = new FXMLLoader(getClass().getResource("MyBooks.fxml"));
		Parent root = loader.load();
		myContr = (MyBooksController) loader.getController();
		Scene scene = new Scene(root);
		scene.getStylesheets().add("style.css");
		stage.getIcons().add(new Image("images/book.png"));
		stage.setTitle("MyBooks");
		stage.setScene(scene);
		stage.setWidth(1200);
		stage.setHeight(825);
		stage.setResizable(false);
		stage.show();
	}

	public static MyBooksController getMyContr() {
		return myContr;
	}

	public static void main(String[] args) {
		launch(args);
	}

}
