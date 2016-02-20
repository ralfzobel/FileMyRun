package de.acwhadk.rz.filemyrun.dialog;

import java.io.IOException;
import java.net.URL;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * The file filter dialog.
 * 
 * @author Ralf
 *
 */
public class FileFilterDialog {

	private Stage primaryStage;
	
	public FileFilterDialog(Stage primaryStage) {
		super();
		this.primaryStage = primaryStage;
	}
	
	public FileFilter showDialog() throws IOException {
		URL res = FileFilterDialog.class.getResource("filefilter.fxml");
		// Load the fxml file and create a new stage for the popup
		FXMLLoader loader = new FXMLLoader(res );
		AnchorPane page = (AnchorPane) loader.load();
		Stage dialogStage = new Stage();
		dialogStage.setTitle("Aktivitäten filtern");
		dialogStage.initModality(Modality.WINDOW_MODAL);
		dialogStage.initOwner(primaryStage);
		Scene scene = new Scene(page);
		dialogStage.setScene(scene);

		// Set the controller
		FileFilterController controller = loader.<FileFilterController>getController();
		controller.setDialogStage(dialogStage);
		// Show the dialog and wait until the user closes it
		dialogStage.showAndWait();
		
		return controller.getFilter();
	}
}
