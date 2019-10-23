package fvarrui.path.calculator;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class PathCalcApp extends Application {
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/MainView.fxml"));
		BorderPane root = loader.load();
	
		primaryStage.getIcons().add(new Image("/images/documents-icon-16x16.png"));
		primaryStage.getIcons().add(new Image("/images/documents-icon-24x24.png"));
		primaryStage.getIcons().add(new Image("/images/documents-icon-32x32.png"));
		primaryStage.setTitle("Path Calculator");
		primaryStage.setScene(new Scene(root));
		primaryStage.show();
	}

	public static void main(String[] args) {
		launch(args);
	}

}
