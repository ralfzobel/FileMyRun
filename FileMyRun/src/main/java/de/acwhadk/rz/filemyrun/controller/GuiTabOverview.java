package de.acwhadk.rz.filemyrun.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import de.acwhadk.rz.filemyrun.dialog.EquipmentDialog;
import de.acwhadk.rz.filemyrun.file.TrainingFile;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TreeItem;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.paint.Color;
import javafx.util.Callback;

public class GuiTabOverview {

	private static final int MAX_EQUIPMENT_TYPES = 3;
	private ObservableList<String> comboBoxItems = FXCollections.observableArrayList();
	private String[] comboBoxStrings = { "", "Wettkampf", "Intervalltraining", "Tempolauf", 
			"Dauerlauf", "Lauf-ABC" };
	
	private Controller controller;
	private GuiControl guiControl; 
	private Activity activity;
	private boolean editable;
	
	public GuiTabOverview(Controller controller, GuiControl guiControl) {
		super();
		this.controller = controller;
		this.guiControl = guiControl;
		
		initialize();
	}
	public void setActivity(Activity activity) {
		this.activity = activity;
	}

	public void initActivity() {
		try {
			setEditable(false);
			if (activity == null) {
				initBlankActivity();
				return;
			}
			controller.getTxtActivityName().setText(activity.getName());
			controller.getTxtActivityDate().setText(Formatter.formatDate(activity.getDate()));
			controller.getCbxActivityType().getSelectionModel().select(activity.getType());
			controller.getTxtActivityDistance().setText(Formatter.formatDistance(activity.getDistance())+" km");
			controller.getTxtActivityTime().setText(Formatter.formatSeconds(activity.getTotalTime()));
			controller.getTxtActivityPace().setText(Formatter.formatPace(activity.getPace()));
			if (activity.getMaximumAltitude() != null) {
				controller.getTxtActivityAscent().setText(Long.toString(activity.getAscent()) + " m");
				controller.getTxtActivityDescent().setText(Long.toString(activity.getDescent()) + " m");
				controller.getTxtActivityMinAltitude().setText(Long.toString(activity.getMinimumAltitude()) + " m");
				controller.getTxtActivityMaxAltitude().setText(Long.toString(activity.getMaximumAltitude()) + " m");
			} else {
				controller.getTxtActivityAscent().setText("");
				controller.getTxtActivityDescent().setText("");
				controller.getTxtActivityMinAltitude().setText("");
				controller.getTxtActivityMaxAltitude().setText("");
			}
			controller.getTxtAreaDescription().setText("");
			controller.getTxtAreaDescription().setText(activity.getDescription());
			if (activity.getAverageHeartRate() != null && activity.getMaximumHeartRate() != null) {
				controller.getTxtActivityAvgHeartRate().setText(Long.toString(activity.getAverageHeartRate())+" bpm");
				controller.getTxtActivityMaxHeartRate().setText(Long.toString(activity.getMaximumHeartRate())+" bpm");
				controller.getTxtActivityCalories().setText(Integer.toString(activity.getCalories())+" kcal");
			} else {
				controller.getTxtActivityAvgHeartRate().setText("");
				controller.getTxtActivityMaxHeartRate().setText("");
				controller.getTxtActivityCalories().setText("");
			}		
			
			initActivityEquipment();
		} catch (Exception e) {
			GuiControl.showException(e);
		}
	}
	
	private void initActivityEquipment() {
		EquipmentMan eqman = guiControl.getEquipmentMan();
		List<String> types = eqman.getEquipmentTypes();
		for(int i=0; i<3; ++i) {
			ComboBox<String> cbx = getEquipmentComboBox(i);
			String type = getEquipmentType(i, types);
			if (type != null) {
				String item = eqman.getEquipmentUsedName(activity.getDate(), type);
				if (item.isEmpty()) {
					cbx.getSelectionModel().select(null);
					cbx.setPromptText(type);
				} else {
					cbx.getSelectionModel().select(item);
				}
			} else {
				cbx.getSelectionModel().select(null);
				cbx.setPromptText("");
			}
		}
	}
	
	private void initBlankActivity() {
		controller.getTxtActivityName().setText("");
		controller.getTxtActivityDate().setText("");
		controller.getCbxActivityType().getSelectionModel().select("");
		controller.getTxtActivityDistance().setText("");
		controller.getTxtActivityTime().setText("");
		controller.getTxtActivityPace().setText("");
		controller.getTxtActivityAvgHeartRate().setText("");
		controller.getTxtActivityMaxHeartRate().setText("");
		controller.getTxtActivityCalories().setText("");
		controller.getTxtActivityAscent().setText("");
		controller.getTxtActivityDescent().setText("");
		controller.getTxtActivityMinAltitude().setText("");
		controller.getTxtActivityMaxAltitude().setText("");
		controller.getTxtAreaDescription().setText("");
	}
	
	private void initialize() {
		setEditable(false);
		controller.getBtnActivityEdit().setOnAction( (ActionEvent) -> onEdit());
		controller.getBtnActivityCancel().setOnAction( (ActionEvent) -> onCancel());
		controller.getBtnActivityDelete().setOnAction( (ActionEvent) -> onDelete());
		controller.getBtnEditEquipment().setOnAction( (ActionEvent) -> onEditEquipment());
		
		controller.getCbxActivityType().setItems(comboBoxItems);
		controller.getCbxActivityType().setEditable(false);
		controller.getCbxActivityType().setCellFactory(new CbxCellFactory());
		
		controller.getTxtAreaDescription().setWrapText(true);

		initBlankActivity();	
		initEquipment();
	}
	
	private void initEquipment() {
		EquipmentMan eqman = guiControl.getEquipmentMan();
		List<String> types = eqman.getEquipmentTypes();
		for(int i=0; i<MAX_EQUIPMENT_TYPES; ++i) {
			ComboBox<String> cbx = getEquipmentComboBox(i);
			String type = getEquipmentType(i, types);
			if (cbx != null) {
				cbx.setPromptText(type);
				Map<Long, String> items = eqman.getEquipmentItems(type);
				cbx.getItems().clear();
				List<String> sortedList = new ArrayList<>();
				sortedList.addAll(items.values());
				Collections.sort(sortedList);
				cbx.getItems().addAll(sortedList);
				cbx.setCellFactory(new CbxCellFactory());
				cbx.setEditable(false);
				setCbxEquipmentEditable(cbx, false);
				if (activity != null) {
					String name = eqman.getEquipmentUsedName(activity.getDate(), type);
					if (!name.isEmpty()) {
						cbx.getSelectionModel().select(name);
					}
				}
			}
		}
	}
	
	private void setCbxEquipmentEditable(ComboBox<String> cbx, boolean editable) {
		String prompt = cbx.getPromptText();
		boolean selectable = prompt != null && !prompt.isEmpty();
		cbx.setDisable(editable && selectable);
	}
	
	private String getEquipmentType(int i, List<String> types) {
		if (i < types.size()) {
			return types.get(i);
		}
		return null;
	}
	
	private ComboBox<String> getEquipmentComboBox(int i) {
		switch(i) {
		case 0:
			return controller.getCbxEquipmentType1();
		case 1:
			return controller.getCbxEquipmentType2();
		case 2:
			return controller.getCbxEquipmentType3();
		}
		return null;
	}
	private void setEditable(boolean editable) {
		if (editable) {
			comboBoxItems.addAll(comboBoxStrings);
		} else {
			comboBoxItems.clear();
			String activityType = activity == null ? "" : activity.getType();
			controller.getCbxActivityType().getSelectionModel().select(activityType);
		}
		controller.getTxtActivityName().setEditable(editable);
		controller.getTxtAreaDescription().setEditable(editable);
		controller.getTxtActivityDistance().setEditable(editable);
		
		controller.getBtnActivityEdit().setText(editable ? "Save" : "Edit");
		controller.getBtnActivityEdit().setDisable(activity == null);
		controller.getBtnActivityCancel().setDisable(!editable);
		controller.getBtnActivityDelete().setDisable(activity == null);
		this.editable = editable;
	}

	private void onEdit() {
    	if (editable) {
       		try {
       			activity.setType(controller.getCbxActivityType().getSelectionModel().getSelectedItem());
       			activity.setName(controller.getTxtActivityName().getText());
       			activity.setDescription(controller.getTxtAreaDescription().getText());
       			activity.setDistance(controller.getTxtActivityDistance().getText());
       			
       			guiControl.save();
				
       			TrainingFile tf = guiControl.getTrainingFileMan().getTrainingFile(activity.getDate());
				String name = guiControl.getFileTree().getName(tf);
				TreeItem<String> item = controller.getActivityTreeView().getSelectionModel().getSelectedItem();
				item.setValue(name);
				guiControl.getFileTree().update(tf);
				
				saveEquipment();
			} catch (Exception e) {
				GuiControl.showException(e);
			}
    	}
    	setEditable(!editable);
    }
    
	private void onCancel() {
    	setEditable(false);
    }
    
    private void onDelete() {
    	Alert alert = new Alert(AlertType.CONFIRMATION);
    	alert.setTitle("Bitte bestätigen");
    	alert.setHeaderText("Aktivität löschen");
    	alert.setContentText("Soll die Aktivität wirklich gelöscht werden?");
    	Optional<ButtonType> result = alert.showAndWait();
    	 if (result.isPresent() && result.get() == ButtonType.OK) {
    	     guiControl.deleteActivity();
    	 }    	
    }
	
	private void onEditEquipment() {
		EquipmentDialog dialog = new EquipmentDialog(guiControl.getPrimaryStage(), guiControl.getEquipmentMan());
		try {
			dialog.showDialog();
			initEquipment();
		} catch (IOException e) {
			GuiControl.showException(e);
		}
	}

    private void saveEquipment() throws Exception {
		EquipmentMan eqman = guiControl.getEquipmentMan();
		List<String> types = eqman.getEquipmentTypes();
		for(int i=0; i<MAX_EQUIPMENT_TYPES; ++i) {
			ComboBox<String> cbx = getEquipmentComboBox(i);
			String type = getEquipmentType(i, types);
			if (cbx != null && type != null) {
				String usedEquipment = cbx.getSelectionModel().getSelectedItem();
				eqman.setEquipmentUsedEntry(activity.getDate(), type, usedEquipment, activity.getTotalTime(), activity.getDistance());
			}
		}
    }	
	
	private class CbxCellFactory implements Callback<ListView<String>, ListCell<String>> {
        @Override public ListCell<String> call(ListView<String> p) {
            return new ListCell<String>() {
                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                        if (item != null) {
                            setText(item);  
                            getStyleClass().add("combo-box");
                            setTextFill(Color.DARKBLUE);
                        }
                }
            };
        }
    }
}
