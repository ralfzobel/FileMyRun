package de.acwhadk.rz.filemyrun.dialog;

import java.io.IOException;
import java.net.URL;

import de.acwhadk.rz.filemyrun.controller.EquipmentMan;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * The equipment definition dialog.
 * 
 * @author Ralf
 *
 */
public class EquipmentDialog {

	private Stage primaryStage;
	private EquipmentMan equipmentMan;
	
	public EquipmentDialog(Stage primaryStage, EquipmentMan equipmentMan) {
		super();
		this.primaryStage = primaryStage;
		this.equipmentMan = equipmentMan;
	}
	
	public void showDialog() throws IOException {
		URL res = EquipmentDialog.class.getResource("equipment.fxml");
		// Load the fxml file and create a new stage for the popup
		FXMLLoader loader = new FXMLLoader(res );
		AnchorPane page = (AnchorPane) loader.load();
		Stage dialogStage = new Stage();
		dialogStage.setTitle("Ausrüstung bearbeiten");
		dialogStage.initModality(Modality.WINDOW_MODAL);
		dialogStage.initOwner(primaryStage);
		Scene scene = new Scene(page);
		dialogStage.setScene(scene);

		// Set the controller
		EquipmentController controller = loader.<EquipmentController>getController();
		controller.setDialogStage(dialogStage);
		controller.setEquipmentMan(equipmentMan);
		// Show the dialog and wait until the user closes it
		dialogStage.showAndWait();
	}
}
