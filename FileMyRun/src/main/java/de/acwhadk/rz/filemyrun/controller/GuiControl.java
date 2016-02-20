package de.acwhadk.rz.filemyrun.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import de.acwhadk.rz.filemyrun.dialog.FileFilter;
import de.acwhadk.rz.filemyrun.dialog.FileFilterDialog;
import de.acwhadk.rz.filemyrun.file.TrainingFile;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Tab;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.stage.Stage;

/**
 * This class controls the activity tree and the selected tab in the main window.
 * 
 * @author Ralf
 *
 */
public class GuiControl {

	private Stage primaryStage;
	private Controller controller;
	private Activity activity;
	private Map<String, Activity> activityMap; 
	private TrainingFile trainingFile;
	private FileTree fileTree;
	private TrainingFileMan trainingFileMan; 
	private EquipmentMan equipmentMan;
	
	private GuiTabOverview guiTabOverview;
	private GuiTabSplits guiTabSplits;	
	private GuiTabCharts guiTabCharts;	
	private GuiTabMap guiTabMap;
	private GuiTabStatistic guiTabStatistic;	

	public GuiControl(Controller controller, TrainingFileMan trainingFileMan, EquipmentMan equipmentMan, Stage primaryStage) {
		this.primaryStage = primaryStage;
		this.controller = controller;
		this.trainingFileMan = trainingFileMan;
		this.equipmentMan = equipmentMan;
		this.activityMap = new HashMap<>();
		
		this.guiTabOverview = new GuiTabOverview(controller, this);
		this.guiTabSplits = new GuiTabSplits(controller, this);
		this.guiTabCharts = new GuiTabCharts(controller, this);
		this.guiTabMap = new GuiTabMap(controller, this);
		this.guiTabStatistic = new GuiTabStatistic(controller, this);
		
		fileTree = new FileTree(trainingFileMan, null);
		
		initialize();
	}
	
	public FileTree getFileTree() {
		return fileTree;
	}

	public TrainingFileMan getTrainingFileMan() {
		return trainingFileMan;
	}

	public EquipmentMan getEquipmentMan() {
		return equipmentMan;
	}

	public void treeSelectionChanged(TreeItem<String> new_val) {
		if (new_val == null) {
			return;
		}
        String selectedItem = new_val.getValue();
        System.out.println("Selected Text : " + selectedItem);
        trainingFile = fileTree.getTrainingFile(selectedItem);
        setNewActivity();
       	setNewTab(controller.getTabPane().getSelectionModel().getSelectedItem());
    }

	public void deleteActivity() {
		trainingFileMan.deleteFile(trainingFile);
		TreeItem<String> c = controller.getActivityTreeView().getSelectionModel().getSelectedItem();
        c.getParent().getChildren().remove(c);
	}

	public void save() throws Exception {
		if (activity == null) {
			return;
		}
		activity.save();
			
		TrainingFile tf = trainingFileMan .getTrainingFile(activity.getDate());
   		tf.setType(activity.getType());
   		tf.setName(activity.getName());
   	    tf.setDistance(activity.getDistance());
   	    tf.setComment(activity.getDescription());
		trainingFileMan.save();
	}
	
	private void initialize() {
		controller.getActivityTreeView().toFront();
		TreeItem<String> root = fileTree.getRoot();
		
		TreeView<String> treeView = controller.getActivityTreeView();
		treeView.setRoot(root);
		treeView.getSelectionModel().selectedItemProperty().addListener( 
				(ObservableValue<? extends TreeItem<String>> observable, TreeItem<String> old_val, TreeItem<String> new_val)
				-> treeSelectionChanged(new_val));
		treeView.setShowRoot(false);
		treeView.getSelectionModel().select(0);
		if (treeView.getSelectionModel().getSelectedItem() != null) {
			treeView.getSelectionModel().getSelectedItem().setExpanded(true);
			treeView.getSelectionModel().selectNext();
		}		
		controller.getTabPane().getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Tab>() {
			@Override
			public void changed(ObservableValue<? extends Tab> observable, Tab oldValue, Tab newValue) {
				setNewTab(newValue);
			}
		});
		controller.getBtnFilterTreeView().setOnAction(e -> filterTree());
	}
	
	private void filterTree() {
		FileFilterDialog dialog = new FileFilterDialog(primaryStage);
		try {
			FileFilter filter = dialog.showDialog();
			fileTree = new FileTree(trainingFileMan, filter);
			initialize();
		} catch (IOException e) {
			GuiControl.showException(e);
		}
	}

	private void setNewTab(Tab newTab) {
		try {
			if (newTab == controller.getTabOverview()) {
				controller.getActivityTreeView().toFront();
				controller.getBtnFilterTreeView().setDisable(false);
				guiTabOverview.initActivity();
			}
			if (newTab == controller.getTabSplits()) {
				controller.getActivityTreeView().toFront();
				controller.getBtnFilterTreeView().setDisable(false);
				guiTabSplits.initActivity();
			}
			if (newTab == controller.getTabCharts()) {
				controller.getActivityTreeView().toFront();
				controller.getBtnFilterTreeView().setDisable(false);
				guiTabCharts.initActivity();
			}		
			if (newTab == controller.getTabMap()) {
				controller.getBtnFilterTreeView().setDisable(true);
				guiTabMap.initActivity();
			}		
			if (newTab == controller.getTabStatistic()) {
				controller.getActivityTreeView().toFront();
				controller.getBtnFilterTreeView().setDisable(true);
				guiTabStatistic.initStatistic();
			}		
		} catch (Exception e) {
			showException(e);
		}
	}
	
	private void setNewActivity() {
		try {
			if (trainingFile == null) {
				activity = null;		
			} else {
				activity = activityMap.get(trainingFile.getTrainingFile());
				if (activity == null) {
					activity = new Activity(trainingFile);
					activityMap.put(trainingFile.getTrainingFile(), activity);
				}
			}
			guiTabOverview.setActivity(activity);
			guiTabSplits.setActivity(activity);
			guiTabCharts.setActivity(activity);
			guiTabMap.setActivity(activity);
		} catch (Exception e) {
			showException(e);
		}
	}

	
	public Stage getPrimaryStage() {
		return primaryStage;
	}

	public static void showException(Exception e) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("Exception");
        alert.setHeaderText("Exception");
        alert.setContentText("" + e);
        alert.showAndWait();
	}
}
