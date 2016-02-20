package de.acwhadk.rz.filemyrun.controller;


import de.acwhadk.rz.filemyrun.dialog.SplitLapDialog;
import de.acwhadk.rz.filemyrun.dialog.SplitLapDialog.SplitType;
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
		controller.getColSplitRound().setCellValueFactory(new PropertyValueFactory<>("round"));
		controller.getColSplitTime().setCellValueFactory(new PropertyValueFactory<>("time"));
		controller.getColSplitTotalDistance().setCellValueFactory(new PropertyValueFactory<>("totalDistance"));
		controller.getColSplitDistance().setCellValueFactory(new PropertyValueFactory<>("distance"));
		controller.getColSplitPace().setCellValueFactory(new PropertyValueFactory<>("pace"));
		controller.getColSplitTotalTime().setCellValueFactory(new PropertyValueFactory<>("totalTime"));
		controller.getColSplitAverageHeartRate().setCellValueFactory(new PropertyValueFactory<>("averageHeartRate"));
		controller.getColSplitMaximumHeartRate().setCellValueFactory(new PropertyValueFactory<>("maximumHeartRate"));
		controller.getColSplitAscent().setCellValueFactory(new PropertyValueFactory<>("ascent"));
		controller.getColSplitDescent().setCellValueFactory(new PropertyValueFactory<>("descent"));
		
		controller.getTableSplits().setRowFactory(
			    new Callback<TableView<Lap>, TableRow<Lap>>() {
			  @Override
			  public TableRow<Lap> call(TableView<Lap> tableView) {
			    final TableRow<Lap> row = new TableRow<>();
			    final ContextMenu rowMenu = new ContextMenu();
			    MenuItem editItem = new MenuItem("Split");
			    editItem.setOnAction(new EventHandler<ActionEvent>() {
			        @Override
			        public void handle(ActionEvent event) {
			        	splitLap(row.getItem());
			        }
			      });
			    MenuItem removeItem = new MenuItem("Join with Successor");
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
    	controller.getTxtBestTimeSplit().setText("");
    	controller.getTxtAverageTimeSplit().setText("");
    	controller.getTxtTotalTimeSplit().setText("");
    	controller.getTxtTotalDistanceSplit().setText("");
    	controller.getTxtBestPaceSplit().setText("");
    	controller.getTxtAveragePaceSplit().setText("");
	}
	
}
