package de.acwhadk.rz.filemyrun.gui;

import de.acwhadk.rz.filemyrun.core.setup.Const;
import de.acwhadk.rz.filemyrun.core.setup.Lang;
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
	
	private String[] comboBoxStrings;

	private Stage dialogStage;
	private FileFilter filter;
	
	@FXML
	private void initialize() {
		comboBoxStrings = new String[9];
		comboBoxStrings[0] = Const.EMPTY;
		comboBoxStrings[1] = Lang.get().text(Lang.OVERVIEW_CBX_COMPETITION);
		comboBoxStrings[2] = Lang.get().text(Lang.OVERVIEW_CBX_INTERVAL);
		comboBoxStrings[3] = Lang.get().text(Lang.OVERVIEW_CBX_TEMPO);
		comboBoxStrings[4] = Lang.get().text(Lang.OVERVIEW_CBX_RUN);
		comboBoxStrings[5] = Lang.get().text(Lang.OVERVIEW_CBX_ABC);
		comboBoxStrings[6] = Const.GARMIN_RUNNING;
		comboBoxStrings[7] = Const.GARMIN_BIKING;
		
		cbFileFilterTime.setText(Lang.get().text(Lang.FILEFILTER_LBL_TIME));
		cbFileFilterDist.setText(Lang.get().text(Lang.FILEFILTER_LBL_DISTANCE));
		cbFileFilterType.setText(Lang.get().text(Lang.FILEFILTER_LBL_TYPE));
		cbFileFilterText.setText(Lang.get().text(Lang.FILEFILTER_LBL_TEXT));
		
		btnFileFilterOk.setText(Lang.get().text(Lang.GENERAL_BTN_OK));
		btnFileFilterOk.setOnAction(e -> handleOk());
		btnFileFilterCancel.setText(Lang.get().text(Lang.GENERAL_BTN_CANCEL));
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
				String msg = Lang.get().text(Lang.FILEFILTER_INVALID_DATE);
				GuiControl.showException(new IllegalArgumentException(msg));
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
					String msg = Lang.get().text(Lang.FILEFILTER_INVALID_DISTANCE);
					GuiControl.showException(new IllegalArgumentException(msg));
					return null;
				}
			}
			filter.setFromDist(fromDist);
			filter.setToDist(toDist);
			
			filter.setTypeFilter(cbFileFilterType.isSelected());
			if (cbFileFilterType.isSelected() && cbxFileFilterType.getSelectionModel().getSelectedItem() == null) {
				String msg = Lang.get().text(Lang.FILEFILTER_INVALID_TYPE);
				GuiControl.showException(new IllegalArgumentException(msg));
				return null;
			}
			filter.setType(cbxFileFilterType.getSelectionModel().getSelectedItem());
			
			filter.setTextFilter(cbFileFilterText.isSelected());
			if (cbFileFilterText.isSelected() && (txtFileFilterText.getText() == null || txtFileFilterText.getText().isEmpty())) {
				String msg = Lang.get().text(Lang.FILEFILTER_INVALID_TEXT);
				GuiControl.showException(new IllegalArgumentException(msg));
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
