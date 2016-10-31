package de.acwhadk.rz.filemyrun.gui;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import de.acwhadk.rz.filemyrun.core.model.EquipmentMan;
import de.acwhadk.rz.filemyrun.core.setup.Const;
import de.acwhadk.rz.filemyrun.core.setup.Lang;

import java.util.Optional;

import javafx.beans.value.ChangeListener;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextInputDialog;
import javafx.stage.Stage;

/**
 * The controller class for the equipment definition dialog.
 * 
 * @author Ralf
 *
 */
public class EquipmentController {

    private static final int MAX_TYPES = 3;

    @FXML
    private Label lblTypes;

    @FXML
    private Label lblItems;

    @FXML
    private ComboBox<String> cbxEquipmentTypes;

    @FXML
    private ListView<String> listEquipmentItems;

    @FXML
    private Button btnEquipmentDeleteType;

    @FXML
    private Button btnEquipmentNewType;

    @FXML
    private Button btnEquipmentNewItem;

    @FXML
    private Button btnEquipmentDeleteItem;

    @FXML
    private Button btnEquipmentOk;

    @FXML
    private Button btnEquipmentCancel;

	private Stage dialogStage;

	private EquipmentMan equipmentManImplXml;

	private List<String> types;
	private Map<String, Map<Long, String>> items;
	
	/**
	 * Initializes the controller class. This method is automatically called
	 * after the fxml file has been loaded.
	 */
	@FXML
	private void initialize() {
		items = new HashMap<>();

		lblTypes.setText(Lang.get().text(Lang.EQUIPMENT_DLG_LBL_TYPES));
		lblItems.setText(Lang.get().text(Lang.EQUIPMENT_DLG_LBL_ITEMS));
		
		btnEquipmentOk.setText(Lang.get().text(Lang.GENERAL_BTN_OK));
		btnEquipmentCancel.setText(Lang.get().text(Lang.GENERAL_BTN_CANCEL));
		btnEquipmentNewType.setText(Lang.get().text(Lang.GENERAL_BTN_NEW));
		btnEquipmentDeleteType.setText(Lang.get().text(Lang.GENERAL_BTN_DELETE));
		btnEquipmentNewItem.setText(Lang.get().text(Lang.GENERAL_BTN_NEW));
		btnEquipmentDeleteItem.setText(Lang.get().text(Lang.GENERAL_BTN_DELETE));
		
		btnEquipmentOk.setOnAction(e -> handleOk());
		btnEquipmentCancel.setOnAction(e -> handleCancel());
		btnEquipmentNewType.setOnAction(e -> handleNewType());
		btnEquipmentDeleteType.setOnAction(e -> handleDeleteType());
		btnEquipmentNewItem.setOnAction(e -> handleNewItem());
		btnEquipmentDeleteItem.setOnAction(e -> handleDeleteItem());
		
		cbxEquipmentTypes.valueProperty().addListener((ChangeListener<String>) (ov, t, t1) -> typeChanged());
		listEquipmentItems.getSelectionModel().selectedItemProperty().addListener((ChangeListener<String>) (ov, t, t1) -> itemChanged());
	}

	/**
	 * Sets the stage of this dialog.
	 * @param dialogStage
	 */
	public void setDialogStage(Stage dialogStage) {
		this.dialogStage = dialogStage;
	}
	
	/**
	 * Called when the user clicks ok.
	 */
	private void handleOk() {
		updateEquipment();
		dialogStage.close();
	}

	private void updateEquipment() {
		Iterator<String> it = equipmentManImplXml.getEquipmentTypes().iterator();
		while(it.hasNext()) {
			String type = it.next();
			if (!types.contains(type)) {
				it.remove();
			}
		}
		for(String type : types) {
			equipmentManImplXml.addType(type);
			updateItems(type);
		}
		try {
			equipmentManImplXml.save();
		} catch (Exception e) {
			GuiControl.showException(e);
		}
	}

	private void updateItems(String type) {
		Map<Long, String> itemMap = getItemMap(type);
		equipmentManImplXml.setEquipmentItems(type, itemMap);
	}

	/**
	 * Called when the user clicks cancel.
	 */
	private void handleCancel() {
		dialogStage.close();
	}
	
	private void handleDeleteType() {
		String type = cbxEquipmentTypes.getSelectionModel().getSelectedItem();
		if (type == null || type.isEmpty()) {
			return;
		}
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle(Lang.get().text(Lang.ALERT_CONFIRM));
		alert.setHeaderText(Lang.get().text(Lang.ALERT_DELETE_EQUIPMENT_SHORT));
		String context = String.format(Lang.get().text(Lang.ALERT_DELETE_EQUIPMENT_LONG), type);
		alert.setContentText(context);

		Optional<ButtonType> result = alert.showAndWait();
		if (result.get() == ButtonType.OK){
		    cbxEquipmentTypes.getItems().remove(type);
		    types.remove(type);
		    typeChanged();
		}	
	}

	private void handleNewType() {
		TextInputDialog dialog = new TextInputDialog(Const.EMPTY);
		dialog.setTitle(Lang.get().text(Lang.EQUIPMENT_NEW_TYPE_TITLE));
		dialog.setHeaderText(Lang.get().text(Lang.EQUIPMENT_NEW_TYPE_HEADER));
		dialog.setContentText(Const.EMPTY);

		Optional<String> result = dialog.showAndWait();
		if (result.isPresent()) {
			String type = result.get();
			if (types.contains(type)) {
				return;
			}
			cbxEquipmentTypes.getItems().add(type);
			cbxEquipmentTypes.getSelectionModel().select(type);
			types.add(type);
			items.put(type, new HashMap<>());
			typeChanged();
		}
	}
	
	private void handleDeleteItem() {
		String item = listEquipmentItems.getSelectionModel().getSelectedItem();
		Map<Long, String> itemMap = getCurrentItemMap();
		if (item == null || itemMap == null) {
			return;
		}
		long key = 0L; 
		for(Entry<Long, String> entry : itemMap.entrySet()) {
			String value = entry.getValue();
			if (value.equals(item)) {
				key = entry.getKey();
			}
		}
		if (key > 0L) {
			itemMap.remove(key);
			listEquipmentItems.getItems().remove(item);
		}
	}

	private void handleNewItem() {
		TextInputDialog dialog = new TextInputDialog(Const.EMPTY);
		dialog.setTitle(Lang.get().text(Lang.EQUIPMENT_NEW_ITEM_TITLE));
		dialog.setHeaderText(Lang.get().text(Lang.EQUIPMENT_NEW_ITEM_HEADER));
		dialog.setContentText(Const.EMPTY);

		Optional<String> result = dialog.showAndWait();
		if (result.isPresent()) {
			String item = result.get();
			listEquipmentItems.getItems().add(item);
			Map<Long, String> itemMap = getCurrentItemMap();
			itemMap.put(equipmentManImplXml.getNextId(), item);
		}
	}

	public void setEquipmentMan(EquipmentMan equipmentManImplXml) {
		this.equipmentManImplXml = equipmentManImplXml;		
		
		types = equipmentManImplXml.getEquipmentTypes();
		cbxEquipmentTypes.getItems().clear();
		cbxEquipmentTypes.getItems().addAll(types);
		if (!types.isEmpty()) {
			cbxEquipmentTypes.getSelectionModel().selectFirst();
		}
		
		for(String t : types) {
			 Map<Long, String> itemMap = equipmentManImplXml.getEquipmentItems(t);
			 items.put(t, itemMap);
		}
		
		typeChanged();
	}

	private String getCurrentType() {
		String type = cbxEquipmentTypes.getSelectionModel().getSelectedItem();
		if (type == null || type.isEmpty()) {
			return null;
		}
		return type;
	}
	
	private Map<Long, String> getCurrentItemMap() {
		String type = getCurrentType();
		if (type == null) {
			return null;
		}
		return getItemMap(type);
	}
	
	private Map<Long, String> getItemMap(String type) {
		Map<Long, String> itemMap = items.get(type);
		if (itemMap == null) {
			itemMap = new HashMap<>();
			items.put(type, itemMap); 
		}
		return itemMap;
	}
	
	private void typeChanged() {
		String type = getCurrentType();
		btnEquipmentDeleteType.setDisable(type == null);
		btnEquipmentNewItem.setDisable(type == null);
		btnEquipmentNewType.setDisable(types.size() >= MAX_TYPES);
		fillItemList();
	}

	private void itemChanged() {
		String item = listEquipmentItems.getSelectionModel().getSelectedItem();
		btnEquipmentDeleteItem.setDisable(item == null);
	}
	
	private void fillItemList() {
		String currentType = cbxEquipmentTypes.getSelectionModel().getSelectedItem();
		listEquipmentItems.getItems().clear();
		
		if (currentType != null && !currentType.isEmpty()) {
			Map<Long, String> itemMap = getCurrentItemMap();
			listEquipmentItems.getItems().addAll(itemMap.values());
			btnEquipmentDeleteItem.setDisable(itemMap.isEmpty());
		} else {
			btnEquipmentDeleteItem.setDisable(true);
		}
	}
}
