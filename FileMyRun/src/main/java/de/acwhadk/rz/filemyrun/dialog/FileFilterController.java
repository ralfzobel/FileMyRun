package de.acwhadk.rz.filemyrun.dialog;

import de.acwhadk.rz.filemyrun.controller.GuiControl;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * The controller for the file filter dialog.
 * 
 * @author Ralf
 *
 */
public class FileFilterController {

	@FXML
	private CheckBox cbFileFilterTime;

	@FXML
	private DatePicker dpFileFilterFrom;

	@FXML
	private DatePicker dpFileFilterTo;

	@FXML
	private CheckBox cbFileFilterType;

	@FXML
	private ComboBox<String> cbxFileFilterType;

    @FXML
    private CheckBox cbFileFilterDist;

    @FXML
    private TextField txtFileFilterDistFrom;

    @FXML
    private TextField txtFileFilterDistTo;
	
	@FXML
	private CheckBox cbFileFilterText;

	@FXML
	private TextField txtFileFilterText;

	@FXML
	private Button btnFileFilterOk;

	@FXML
	private Button btnFileFilterCancel;
	
	private String[] comboBoxStrings = { 
			"", "Wettkampf", "Intervalltraining", "Tempolauf", 
			"Dauerlauf", "Lauf-ABC", "RUNNING", "BIKING" };

	private Stage dialogStage;
	private FileFilter filter;
	
	@FXML
	private void initialize() {
		btnFileFilterOk.setOnAction(e -> handleOk());
		btnFileFilterCancel.setOnAction(e -> handleCancel());
		ObservableList<String> types = FXCollections.observableArrayList();
		types.addAll(comboBoxStrings);
		cbxFileFilterType.setItems(types);
	}

	private Object handleCancel() {
		filter = null;
		dialogStage.close();
		return null;
	}

	private Object handleOk() {
		if (cbFileFilterTime.isSelected() || 
				cbFileFilterDist.isSelected() || 
				cbFileFilterType.isSelected() || 
				cbFileFilterText.isSelected()) {
			filter = new FileFilter();
			filter.setTimeFilter(cbFileFilterTime.isSelected());
			if (cbFileFilterTime.isSelected() && (dpFileFilterFrom.getValue() == null || dpFileFilterTo.getValue() == null)) {
				GuiControl.showException(new IllegalArgumentException("Ungültige Eingabe in Datum-Feldern"));
				return null;
			}
			filter.setFromTime(dpFileFilterFrom.getValue());
			filter.setToTime(dpFileFilterTo.getValue());
			
			filter.setDistFilter(cbFileFilterDist.isSelected());
			double fromDist=0.;
			double toDist=0.;
			if (cbFileFilterDist.isSelected()) {
				try {
					fromDist = Double.parseDouble(txtFileFilterDistFrom.getText());
					toDist = Double.parseDouble(txtFileFilterDistTo.getText());
				} catch(NumberFormatException e) {
					GuiControl.showException(new IllegalArgumentException("Ungültige Eingabe in Distanz-Feldern"));
					return null;
				}
			}
			filter.setFromDist(fromDist);
			filter.setToDist(toDist);
			
			filter.setTypeFilter(cbFileFilterType.isSelected());
			if (cbFileFilterType.isSelected() && cbxFileFilterType.getSelectionModel().getSelectedItem() == null) {
				GuiControl.showException(new IllegalArgumentException("Ungültige Eingabe in Typ-Feld"));
				return null;
			}
			filter.setType(cbxFileFilterType.getSelectionModel().getSelectedItem());
			
			filter.setTextFilter(cbFileFilterText.isSelected());
			if (cbFileFilterText.isSelected() && (txtFileFilterText.getText() == null || txtFileFilterText.getText().isEmpty())) {
				GuiControl.showException(new IllegalArgumentException("Ungültige Eingabe in Text-Feld"));
				return null;
			}
			filter.setText(txtFileFilterText.getText());
		} else {
			filter = null;
		}
		dialogStage.close();
		return null;
	}
	
	/**
	 * Sets the stage of this dialog.
	 * @param dialogStage
	 */
	public void setDialogStage(Stage dialogStage) {
		this.dialogStage = dialogStage;
	}

	public FileFilter getFilter() {
		return filter;
	}	
}
