package de.acwhadk.rz.filemyrun.gui;

import de.acwhadk.rz.filemyrun.core.model.Activity;
import de.acwhadk.rz.filemyrun.core.model.Position;
import de.acwhadk.rz.filemyrun.core.model.TrackPoint;
import de.acwhadk.rz.filemyrun.core.setup.Lang;
import de.acwhadk.rz.filemyrun.webmap.WebMap;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.TreeCell;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.util.Callback;

/**
 * This class controls the contents of the map tab.
 * 
 * @author Ralf
 *
 */
public class GuiTabMap {

	private Controller controller;
	private GuiControl guiControl;
	private WebMap webMap;
	private Activity activity;
	private LapTree lapTree;

	public GuiTabMap(Controller controller, GuiControl guiControl) {
		super();
		this.controller = controller;
		this.guiControl = guiControl;
		initialize();
	}

	private void initialize() {
		controller.getTabMap().setDisable(true);
		webMap = new WebMap(controller.getWebMapView(), controller.getTabMap());

		controller.getBtnRoadMap().setText(Lang.get().text(Lang.MAP_BUTTON_ROADMAP));
		controller.getBtnSatellite().setText(Lang.get().text(Lang.MAP_BUTTON_SATELLITE));
		controller.getBtnHybrid().setText(Lang.get().text(Lang.MAP_BUTTON_HYBRID));
		controller.getBtnLandscape().setText(Lang.get().text(Lang.MAP_BUTTON_LANDSCAPE));
		
		TreeView<String> treeView = controller.getLapTreeView();
		treeView.getSelectionModel().selectedItemProperty().addListener( 
				(ObservableValue<? extends TreeItem<String>> observable, TreeItem<String> old_val, TreeItem<String> new_val)
				-> treeSelectionChanged(new_val));
		treeView.setShowRoot(false);

		final ToggleGroup mapTypeGroup = new ToggleGroup();
		controller.getBtnRoadMap().setSelected(true);
		controller.getBtnRoadMap().setToggleGroup(mapTypeGroup);
		controller.getBtnSatellite().setToggleGroup(mapTypeGroup);
		controller.getBtnHybrid().setToggleGroup(mapTypeGroup);
		controller.getBtnLandscape().setToggleGroup(mapTypeGroup);
		mapTypeGroup.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
			public void changed(ObservableValue<? extends Toggle> observableValue, Toggle toggle, Toggle toggle1) {
				if (controller.getBtnRoadMap().isSelected()) {
					webMap.setMapTypeRoad();
				} else if (controller.getBtnSatellite().isSelected()) {
					webMap.setMapTypeSatellite();
				} else if (controller.getBtnHybrid().isSelected()) {
					webMap.setMapTypeHybrid();
				} else if (controller.getBtnLandscape().isSelected()) {
					webMap.webMapsetMapTypeTerrain();
				}
			}
		});

		treeView.setCellFactory(new Callback<TreeView<String>,TreeCell<String>>(){
			@Override
			public TreeCell<String> call(TreeView<String> p) {
				return new TreeCellImpl();
			}
		});
    }

	private class TreeCellImpl extends TreeCell<String> {
	    @Override
	    public void updateItem(String item, boolean empty) {
	        super.updateItem(item, empty);
	        if (empty) {
	        	setText(null);
	        	setContextMenu(null);
	        } else {
	        	setText(getItem().toString());
		        if (getTreeItem() != null && getTreeItem().isLeaf()) {
		    		ContextMenu menu = new ContextMenu();
		    		
		    		MenuItem deleteToHereItem = new MenuItem(Lang.get().text(Lang.MAP_MENU_DELETE_TO_HERE));
		    		deleteToHereItem.setOnAction(t -> deleteToHere(getItem()));
		    		menu.getItems().add(deleteToHereItem);
	
		    		MenuItem deleteToEndItem = new MenuItem(Lang.get().text(Lang.MAP_MENU_DELETE_TO_END));
		    		deleteToEndItem.setOnAction(t -> deleteToEnd(getItem()));
		    		menu.getItems().add(deleteToEndItem);
		    		
		    		setContextMenu(menu);
		        } else {
		        	setContextMenu(null);
		        }
	        }
	    }		
	}
	
	private void deleteToHere(String tpName) {
		TrackPoint tp = lapTree.getTrackPoint(tpName);
		try {
			activity.deleteToHere(tp);
			guiControl.save();
			initActivity();
		} catch (Exception e) {
			GuiControl.showException(e);
		}
	}
	
	private void deleteToEnd(String tpName) {
		TrackPoint tp = lapTree.getTrackPoint(tpName);
		try {
			activity.deleteToEnd(tp);
			guiControl.save();
			initActivity();
		} catch (Exception e) {
			GuiControl.showException(e);
		}
	}
	
	private void treeSelectionChanged(TreeItem<String> new_val) {
		if (new_val == null || lapTree == null) {
			return;
		}
		TrackPoint tp = lapTree.getTrackPoint(new_val.getValue());
		if (tp == null || tp.getPosition() == null) {
			webMap.clearTrackPoint();
			return;
		}
		Position pos = tp.getPosition();
		webMap.setTrackPoint(pos.getLatitude(), pos.getLongitude());
	}

	public void initActivity() {
		if (activity == null) {
			return;
		}
		lapTree = new LapTree(activity);
		controller.getLapTreeView().setRoot(lapTree.getRoot());
		controller.getLapTreeView().toFront();
		
		webMap.setTrack(activity);
	}

	public void setActivity(Activity activity) {
		this.activity = activity;		
	}
}
