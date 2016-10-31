package de.acwhadk.rz.filemyrun.dialog;

import java.io.IOException;
import java.net.URL;

import de.acwhadk.rz.filemyrun.core.setup.Const;
import de.acwhadk.rz.filemyrun.core.setup.Lang;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

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
		URL res = FileFilterDialog.class.getResource(Const.FXML_FILEFILTER);
		// Load the fxml file and create a new stage for the popup
		FXMLLoader loader = new FXMLLoader(res );
		AnchorPane page = (AnchorPane) loader.load();
		Stage dialogStage = new Stage();
		dialogStage.setTitle(Lang.get().text(Lang.FILEFILTER_DLG_TITLE));
		dialogStage.initModality(Modality.WINDOW_MODAL);
		dialogStage.initOwner(primaryStage);
		dialogStage.initStyle(StageStyle.UTILITY);
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
