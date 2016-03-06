package de.acwhadk.rz.filemyrun.controller;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import de.acwhadk.rz.filemyrun.equipment.EquipmentUsedEntry;

import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;

/**
 * This class controls the contents of the statistic tab.
 * 
 * @author Ralf
 *
 */
public class GuiTabEquipment {

	private Controller controller;
	private GuiControl guiControl; 

	private BarChart<String,Number> chart;
	
	public GuiTabEquipment(Controller controller, GuiControl guiControl) {
		super();
		this.controller = controller;
		this.guiControl = guiControl;

		initialize();
	}

	private void initialize() {
		chart = new BarChart<>(new CategoryAxis(), new NumberAxis());
		setDefaultChartProperties(chart);
		
		controller.getCbxTimeDistance().setEditable(false);

		EquipmentMan eqman = guiControl.getEquipmentMan();
		List<String> types = eqman.getEquipmentTypes();
		
		ObservableList<String> comboBoxItemsPredefined = FXCollections.observableArrayList();
		comboBoxItemsPredefined.addAll(types);
		controller.getCbxStatisticEquipment().setItems(comboBoxItemsPredefined);
		controller.getCbxStatisticEquipment().getSelectionModel().selectFirst();
		controller.getCbxStatisticEquipment().setEditable(false);
		controller.getCbxStatisticEquipment().valueProperty().addListener((ChangeListener<String>) (ov, t, t1) -> handleEquipmentType());

		handleEquipmentType();
		
		controller.getStackPaneEquipment().getChildren().addAll(chart);

	}

	public void initStatistic() {
		try {
			chart.getData().clear();

			XYChart.Series<String, Number> series = new XYChart.Series<>();
			EquipmentMan eqman = guiControl.getEquipmentMan();
			String type = controller.getCbxStatisticEquipment().getSelectionModel().getSelectedItem();
			Map<Long, String> items = eqman.getEquipmentItems(type);
			for(Entry<Long, String> entry : items.entrySet()) {
				Long id = entry.getKey();
				String name = entry.getValue();
				List<EquipmentUsedEntry> uses = eqman.getEquipmentUsedEntryList();
				Double y = 0.0;
				for(EquipmentUsedEntry use : uses) {
					if (use.getId() != id) {
						continue;
					}
					y += use.getUsedDistance();
				}
				series.getData().add(new XYChart.Data<String,Number>(name, y));
			}
			chart.getData().add(series);			
		} catch (Exception e) {
			GuiControl.showException(e);
		}
	}		

	private Object handleEquipmentType() {
		
		initStatistic();
		return null;
	}
	
	private void setDefaultChartProperties(final BarChart<String,Number> chart) {
		chart.setLegendVisible(false);
		chart.setAnimated(false);
	}

}
