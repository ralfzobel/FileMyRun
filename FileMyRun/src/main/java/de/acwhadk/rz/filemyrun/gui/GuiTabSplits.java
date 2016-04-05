package de.acwhadk.rz.filemyrun.gui;


import de.acwhadk.rz.filemyrun.dialog.SplitLapDialog;
import de.acwhadk.rz.filemyrun.dialog.SplitLapDialog.SplitType;
import de.acwhadk.rz.filemyrun.setup.Const;
import de.acwhadk.rz.filemyrun.setup.Lang;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;

/**
 * This class controls the contents of the split times tab.
 * 
 * @author Ralf
 *
 */
public class GuiTabSplits {

	private Controller controller;
	private GuiControl guiControl; 
	private Activity activity;
	
	private ObservableList<Lap> splitTable = FXCollections.observableArrayList();
	
	public GuiTabSplits(Controller controller, GuiControl guiControl) {
		super();
		this.controller = controller;
		this.guiControl = guiControl;
		
		initialize();
	}

	public void setActivity(Activity activity) {
		this.activity = activity;
	}
	
	public void initActivity() {
		splitTable.clear();
		if (activity != null) {
			splitTable.addAll(activity.getLaps(controller.getChkBoxSplitSnapIn().isSelected()));
		}
		calculateLaps();
	}

	private void initialize() {
		controller.getColSplitRound().setText(Lang.get().text(Lang.SPLIT_COL_ROUND));
		controller.getColSplitTime().setText(Lang.get().text(Lang.SPLIT_COL_TIME));
		controller.getColSplitTotalDistance().setText(Lang.get().text(Lang.SPLIT_COL_TOTAL_DISTANCE));
		controller.getColSplitDistance().setText(Lang.get().text(Lang.SPLIT_COL_DISTANCE));
		controller.getColSplitPace().setText(Lang.get().text(Lang.SPLIT_COL_PACE));
		controller.getColSplitTotalTime().setText(Lang.get().text(Lang.SPLIT_COL_TOTAL_TIME));
		controller.getColSplitAverageHeartRate().setText(Lang.get().text(Lang.SPLIT_COL_AVERAGE_HEARTRATE));
		controller.getColSplitMaximumHeartRate().setText(Lang.get().text(Lang.SPLIT_COL_MAXIMUM_HEARTRATE));
		controller.getColSplitAscent().setText(Lang.get().text(Lang.SPLIT_COL_ASCENT));
		controller.getColSplitDescent().setText(Lang.get().text(Lang.SPLIT_COL_DESCENT));
		
		controller.getColSplitRound().setCellValueFactory(new PropertyValueFactory<>(Const.SPLIT_ROUND));
		controller.getColSplitTime().setCellValueFactory(new PropertyValueFactory<>(Const.SPLIT_TIME));
		controller.getColSplitTotalDistance().setCellValueFactory(new PropertyValueFactory<>(Const.SPLIT_TOTAL_DISTANCE));
		controller.getColSplitDistance().setCellValueFactory(new PropertyValueFactory<>(Const.SPLIT_DISTANCE));
		controller.getColSplitPace().setCellValueFactory(new PropertyValueFactory<>(Const.SPLIT_PACE));
		controller.getColSplitTotalTime().setCellValueFactory(new PropertyValueFactory<>(Const.SPLIT_TOTAL_TIME));
		controller.getColSplitAverageHeartRate().setCellValueFactory(new PropertyValueFactory<>(Const.SPLIT_AVERAGE_HEARTRATE));
		controller.getColSplitMaximumHeartRate().setCellValueFactory(new PropertyValueFactory<>(Const.SPLIT_MAXIMUM_HEARTRATE));
		controller.getColSplitAscent().setCellValueFactory(new PropertyValueFactory<>(Const.SPLIT_ASCENT));
		controller.getColSplitDescent().setCellValueFactory(new PropertyValueFactory<>(Const.SPLIT_DESCENT));
		
		controller.getLblSplitBestTime().setText(Lang.get().text(Lang.SPLIT_LBL_BEST_TIME));
		controller.getLblSplitAvgTime().setText(Lang.get().text(Lang.SPLIT_LBL_AVG_TIME));
		controller.getLblSplitTotalTime().setText(Lang.get().text(Lang.SPLIT_LBL_TOTAL_TIME));
		controller.getLblSplitTotalDist().setText(Lang.get().text(Lang.SPLIT_LBL_TOTAL_DISTANCE));
		controller.getLblSplitBestPace().setText(Lang.get().text(Lang.SPLIT_LBL_BEST_PACE));
		controller.getLblSplitAvgPace().setText(Lang.get().text(Lang.SPLIT_LBL_AVG_PACE));
		
		controller.getTableSplits().setRowFactory(
			    new Callback<TableView<Lap>, TableRow<Lap>>() {
			  @Override
			  public TableRow<Lap> call(TableView<Lap> tableView) {
			    final TableRow<Lap> row = new TableRow<>();
			    final ContextMenu rowMenu = new ContextMenu();
			    MenuItem editItem = new MenuItem(Lang.get().text(Lang.SPLIT_MENU_SPLIT));
			    editItem.setOnAction(new EventHandler<ActionEvent>() {
			        @Override
			        public void handle(ActionEvent event) {
			        	splitLap(row.getItem());
			        }
			      });
			    MenuItem removeItem = new MenuItem(Lang.get().text(Lang.SPLIT_MENU_JOIN_SUCCESSOR));
			    removeItem.setOnAction(new EventHandler<ActionEvent>() {
			        @Override
			        public void handle(ActionEvent event) {
			        	joinLap(row.getItem());
			        }
			      });
			    rowMenu.getItems().addAll(editItem, removeItem);

			    // only display context menu for non-null items:
			    row.contextMenuProperty().bind(
			      Bindings.when(Bindings.isNotNull(row.itemProperty()))
			      .then(rowMenu)
			      .otherwise((ContextMenu)null));
			    return row;
			  }
			});
		controller.getTableSplits().setItems(splitTable);
		controller.getTableSplits().getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
		controller.getTableSplits().getSelectionModel().getSelectedItems().addListener(new ListChangeListener<Object>() {
			@Override
			public void onChanged(Change<?> c) {
				calculateLaps();
			}
		});
		
		controller.getChkBoxSplitSnapIn().setText(Lang.get().text(Lang.SPLIT_SNAPIN));
		controller.getChkBoxSplitSnapIn().selectedProperty().addListener(( observable, old_val, new_val) -> initActivity());
		
		setEmptySplitAverages();
	}
	
	private void joinLap(Lap lap) {
		try {
			activity.joinLap(Integer.parseInt(lap.getRound())-1);
			guiControl.save();
			initActivity();
		} catch (Exception e) {
			GuiControl.showException(e);
		}
	}

	private void splitLap(Lap lap) {
		SplitLapDialog dialog = new SplitLapDialog(guiControl.getPrimaryStage());
		try {
			SplitType type = dialog.showDialog();
			switch(type) {
			case HalfTime:
				activity.splitLapHalfTime(Integer.parseInt(lap.getRound())-1);
				break;
			case HalfDist:	
				activity.splitLapHalfDist(Integer.parseInt(lap.getRound())-1);
				break;
			case AtDist:
				activity.splitLapAtDist(Integer.parseInt(lap.getRound())-1, dialog.getSplitAtDist());
				break;
			default:
				return;
			}
			guiControl.save();
			initActivity();
		} catch (Exception e) {
			GuiControl.showException(e);
		}
	}
	
    private void calculateLaps() {
    	try {
    		ObservableList<Lap> selectedLaps = controller.getTableSplits().getSelectionModel().getSelectedItems();
    		double totalTime = 0.0;
    		double bestTime = 999999.0;
    		double totalDistance = 0.0;
    		double bestPace = 999999.0;
    		int cnt = 0;
    		ObservableList<Lap> laps = selectedLaps == null || selectedLaps.isEmpty() ? splitTable : selectedLaps;
    		for(Lap lap : laps) {
    			if (lap == null) {
    				continue;
    			}
    			totalTime += lap.getTimeInSeconds();
    			Double dist = lap.getDistanceInMeters();
    			totalDistance += dist;
    			if (lap.getTimeInSeconds() < bestTime) {
    				bestTime = lap.getTimeInSeconds();
    			}
    			if (lap.getPaceInSeconds() < bestPace) {
    				bestPace = lap.getPaceInSeconds();
    			}
    			++cnt;
    		}
    		if (cnt == 0) {
    			setEmptySplitAverages();
    		} else {
    			double averageTime = totalTime / cnt;
    			double averagePace = totalTime / totalDistance;

    			controller.getTxtBestTimeSplit().setText(Formatter.formatSeconds(bestTime));
    			controller.getTxtAverageTimeSplit().setText(Formatter.formatSeconds(averageTime));
    			controller.getTxtTotalTimeSplit().setText(Formatter.formatSeconds(totalTime));
    			controller.getTxtTotalDistanceSplit().setText(Formatter.formatDistance(totalDistance));
    			controller.getTxtBestPaceSplit().setText(Formatter.formatPace(bestPace));
    			controller.getTxtAveragePaceSplit().setText(Formatter.formatPace(averagePace));
    		}
    	} catch (Exception e) {
    		GuiControl.showException(e);
    	}
    }
    
	private void setEmptySplitAverages() {
    	controller.getTxtBestTimeSplit().setText(Const.EMPTY);
    	controller.getTxtAverageTimeSplit().setText(Const.EMPTY);
    	controller.getTxtTotalTimeSplit().setText(Const.EMPTY);
    	controller.getTxtTotalDistanceSplit().setText(Const.EMPTY);
    	controller.getTxtBestPaceSplit().setText(Const.EMPTY);
    	controller.getTxtAveragePaceSplit().setText(Const.EMPTY);
	}
	
}
