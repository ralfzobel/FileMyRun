package de.acwhadk.rz.filemyrun.dialog;

import de.acwhadk.rz.filemyrun.core.setup.Lang;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.stage.Stage;

/**
 * The controller for the split lap dialog.
 * 
 * @author Ralf
 *
 */
public class SplitLapController {

    @FXML
    private Label lblQuestion;

	@FXML
	private RadioButton rbSplitHalfTime;

	@FXML
	private RadioButton rbSplitHalfDist;

	@FXML
	private RadioButton rbSplitAtDist;

	@FXML
	private Button btnSplitOk;

	@FXML
	private Button btnSplitCancel;

	@FXML
	private TextField tfSplitAtDist;

	private Stage dialogStage;

	private boolean okClicked;

	private Double splitAtDist;

	/**
	 * Initializes the controller class. This method is automatically called
	 * after the fxml file has been loaded.
	 */
	@FXML
	private void initialize() {
		okClicked = false;
		
		lblQuestion.setText(Lang.get().text(Lang.SPLITLAP_LBL_QUESTION));
		rbSplitHalfTime.setText(Lang.get().text(Lang.SPLITLAP_LBL_HALF_TIME));
		rbSplitHalfDist.setText(Lang.get().text(Lang.SPLITLAP_LBL_HALF_DIST));
		rbSplitAtDist.setText(Lang.get().text(Lang.SPLITLAP_LBL_DIST));
		
		btnSplitOk.setText(Lang.get().text(Lang.GENERAL_BTN_OK));
		btnSplitOk.setOnAction(e -> handleOk());
		btnSplitCancel.setText(Lang.get().text(Lang.GENERAL_BTN_CANCEL));
		btnSplitCancel.setOnAction(e -> handleCancel());
		
		final ToggleGroup group = new ToggleGroup();
		rbSplitHalfTime.setToggleGroup(group);
		rbSplitHalfDist.setToggleGroup(group);
		rbSplitAtDist.setToggleGroup(group);
	}

	/**
	 * Sets the stage of this dialog.
	 * @param dialogStage
	 */
	public void setDialogStage(Stage dialogStage) {
		this.dialogStage = dialogStage;
	}
	
	/**
	 * Returns true if the user clicked OK, false otherwise.
	 * @return
	 */
	public boolean isOkClicked() {
		return okClicked;
	}

	public boolean isSplitHalfTime() {
		return rbSplitHalfTime.isSelected();
	}
	
	public boolean isSplitHalfDist() {
		return rbSplitHalfDist.isSelected();
	}
	
	public boolean isSplitAtDist() {
		return rbSplitAtDist.isSelected();
	}
	
	public Double getSplitAtDist() {
		return splitAtDist;
	}
	
	/**
	 * Called when the user clicks ok.
	 */
	private void handleOk() {
		if (isInputValid()) {
			okClicked = true;
			dialogStage.close();
		}
	}

	private boolean isInputValid() {
		if (rbSplitAtDist.isSelected()) {
			try {
				splitAtDist = Double.parseDouble(tfSplitAtDist.getText());
			} catch(NumberFormatException e) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Called when the user clicks cancel.
	 */
	private void handleCancel() {
		dialogStage.close();
	}

}
