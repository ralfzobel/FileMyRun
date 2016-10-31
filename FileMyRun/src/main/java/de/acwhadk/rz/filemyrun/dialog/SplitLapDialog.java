package de.acwhadk.rz.filemyrun.dialog;

import java.io.IOException;

import de.acwhadk.rz.filemyrun.core.setup.Const;
import de.acwhadk.rz.filemyrun.core.setup.Lang;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * The split lap dialog.
 * 
 * @author Ralf
 *
 */
public class SplitLapDialog {

	public enum SplitType { None, HalfTime, HalfDist, AtDist }; 
	
	private Stage primaryStage;
	private SplitType splitType;
	private Double splitAtDist;
	
	public SplitLapDialog(Stage primaryStage) {
		super();
		this.primaryStage = primaryStage;
	}

	public SplitType showDialog() throws IOException {
		// Load the fxml file and create a new stage for the popup
		FXMLLoader loader = new FXMLLoader(SplitLapDialog.class.getResource(Const.FXML_SPLITLAP));
		AnchorPane page = (AnchorPane) loader.load();
		Stage dialogStage = new Stage();
		dialogStage.setTitle(Lang.get().text(Lang.SPLITLAP_DLG_TITLE));
		dialogStage.initModality(Modality.WINDOW_MODAL);
		dialogStage.initOwner(primaryStage);
		dialogStage.initStyle(StageStyle.UTILITY);
		Scene scene = new Scene(page);
		dialogStage.setScene(scene);

		// Set the controller
		SplitLapController controller = loader.<SplitLapController>getController();
		controller.setDialogStage(dialogStage);

		// Show the dialog and wait until the user closes it
		dialogStage.showAndWait();

		if (controller.isOkClicked()) {
			if (controller.isSplitHalfTime()) {
				return SplitType.HalfTime;
			}
			if (controller.isSplitHalfDist()) {
				return SplitType.HalfDist;
			}
			if (controller.isSplitAtDist()) {
				splitAtDist = controller.getSplitAtDist();
				return SplitType.AtDist;
			}
		}
		return SplitType.None;
	}

	public SplitType getSplitType() {
		return splitType;
	}

	public Double getSplitAtDist() {
		return splitAtDist;
	}
	
	
}
