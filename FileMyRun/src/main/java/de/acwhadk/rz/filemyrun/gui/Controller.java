package de.acwhadk.rz.filemyrun.gui;

import de.acwhadk.rz.filemyrun.core.algo.SplitTime;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.SplitPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.TreeView;
import javafx.scene.layout.StackPane;
import javafx.scene.web.WebView;

/**
 * This class collects all FXML instanciated objects and provides getters for them.
 * 
 * @author Ralf
 *
 */
public class Controller {

    @FXML
    private SplitPane hSplitPane;

    @FXML
    private Button btnFilterTreeView;

    @FXML
    private Label lblFilterText;
    
    @FXML
    private StackPane stackPaneTree;

    @FXML
    private TreeView<String> activityTreeView;

    @FXML
    private TreeView<String> lapTreeView;

    @FXML
    private TabPane tabPane;
    
    @FXML
    private Tab tabOverview;

    @FXML
    private Tab tabSplits;
    
    @FXML
    private Tab tabCharts;
    
    @FXML
    private Label lblOverviewDistance;

    @FXML
    private Label lblOverviewTime;

    @FXML
    private Label lblOverviewPace;

    @FXML
    private Label lblOverviewAscent;

    @FXML
    private Label lblOverviewDescent;

    @FXML
    private Label lblOverviewHeartRate;

    @FXML
    private Label lblOverviewHeartRateMax;

    @FXML
    private Label lblOverviewCalories;

    @FXML
    private Label lblOverviewAltMin;

    @FXML
    private Label lblOverviewAltMax;

    @FXML
    private ComboBox<String> cbxActivityType;

    @FXML
    private TextField txtActivityDate;

    @FXML
    private TextField txtActivityName;

    @FXML
    private TextArea txtAreaDescription;

    @FXML
    private ComboBox<String> cbxEquipmentType1;

    @FXML
    private ComboBox<String> cbxEquipmentType2;

    @FXML
    private ComboBox<String> cbxEquipmentType3;

    @FXML
    private Button btnEditEquipment;

    @FXML
    private TextField txtActivityDistance;

    @FXML
    private TextField txtActivityTime;

    @FXML
    private TextField txtActivityPace;

    @FXML
    private TextField txtActivityAscent;

    @FXML
    private TextField txtActivityDescent;

    @FXML
    private TextField txtActivityAvgHeartRate;

    @FXML
    private TextField txtActivityMaxHeartRate;

    @FXML
    private TextField txtActivityCalories;

    @FXML
    private TextField txtActivityMinAltitude;

    @FXML
    private TextField txtActivityMaxAltitude;
    
    @FXML
    private Button btnActivityEdit;

    @FXML
    private Button btnActivityCancel;

    @FXML
    private Button btnActivityDelete;

    @FXML
    private TableView<SplitTime> tableSplits;
    
    @FXML
    private TableColumn<SplitTime, String> colSplitRound;

    @FXML
    private TableColumn<SplitTime, String> colSplitTime;

    @FXML
    private TableColumn<SplitTime, String> colSplitTotalDistance;

    @FXML
    private TableColumn<SplitTime, String> colSplitDistance;

    @FXML
    private TableColumn<SplitTime, String> colSplitPace;

    @FXML
    private TableColumn<SplitTime, String> colSplitTotalTime;

    @FXML
    private TableColumn<SplitTime, String> colSplitAverageHeartRate;

    @FXML
    private TableColumn<SplitTime, String> colSplitMaximumHeartRate;

    @FXML
    private TableColumn<SplitTime, String> colSplitAscent;

    @FXML
    private TableColumn<SplitTime, String> colSplitDescent;
    
    @FXML
    private Label lblSplitBestTime;

    @FXML
    private TextField txtBestTimeSplit;

    @FXML
    private Label lblSplitAvgTime;

    @FXML
    private TextField txtAverageTimeSplit;

    @FXML
    private Label lblSplitTotalTime;

    @FXML
    private TextField txtTotalTimeSplit;

    @FXML
    private Label lblSplitTotalDist;

    @FXML
    private TextField txtTotalDistanceSplit;

    @FXML
    private Label lblSplitBestPace;

    @FXML
    private TextField txtBestPaceSplit;

    @FXML
    private Label lblSplitAvgPace;

    @FXML
    private TextField txtAveragePaceSplit;

    @FXML
    private CheckBox chkBoxSplitSnapIn;

    @FXML
    private ComboBox<String> cbxTimeDistance;
    
    @FXML
    private ComboBox<String> cbxChartType;
 
    @FXML
    private ComboBox<String> cbxChartType2;

    @FXML
    private ComboBox<String> cbxChartType3;
    
    @FXML
    private ComboBox<Long> cbxSmoothFactor;
    
    @FXML
    private StackPane stackPaneCharts;

    @FXML
    private Label lblChartTime;

    @FXML
    private Label lblChartDistance;

    @FXML
    private Label lblChartAlitude;

    @FXML
    private Label lblChartHeartRate;

    @FXML
    private Label lblChartPace;

    @FXML
    private Label lblChartSplitPace;
    
    @FXML
    private Tab tabMap;
    
    @FXML
    private ToggleButton btnRoadMap;

    @FXML
    private ToggleButton btnSatellite;

    @FXML
    private ToggleButton btnHybrid;

    @FXML
    private ToggleButton btnLandscape;

    @FXML
    private StackPane stackPaneMap;
    
    @FXML
    private WebView webMapView;
    
    @FXML
    private Tab tabStatistic;

    @FXML
    private StackPane stackPaneStatistic;

    @FXML
    private ComboBox<String> cbxStatisticPredefined;

    @FXML
    private DatePicker dateStatisticFrom;

    @FXML
    private DatePicker dateStatisticTo;

    @FXML
    private ComboBox<String> cbxStatisticType;

    @FXML
    private Tab tabEquipment;

    @FXML
    private StackPane stackPaneEquipment;

    @FXML
    private ComboBox<String> cbxStatisticEquipment;
    
    /*
     * Getters for all Gui Controls...
     */
    
	public TreeView<String> getActivityTreeView() {
		return activityTreeView;
	}

	public SplitPane gethSplitPane() {
		return hSplitPane;
	}

	public Tab getTabOverview() {
		return tabOverview;
	}

	public ComboBox<String> getCbxActivityType() {
		return cbxActivityType;
	}

	public TextField getTxtActivityDate() {
		return txtActivityDate;
	}

	public TextField getTxtActivityName() {
		return txtActivityName;
	}

	public TextField getTxtActivityDistance() {
		return txtActivityDistance;
	}

	public TextField getTxtActivityTime() {
		return txtActivityTime;
	}

	public TextField getTxtActivityPace() {
		return txtActivityPace;
	}

	public TextArea getTxtAreaDescription() {
		return txtAreaDescription;
	}

	public TextField getTxtActivityAvgHeartRate() {
		return txtActivityAvgHeartRate;
	}

	public TextField getTxtActivityMaxHeartRate() {
		return txtActivityMaxHeartRate;
	}

	public TextField getTxtActivityAscent() {
		return txtActivityAscent;
	}

	public TextField getTxtActivityDescent() {
		return txtActivityDescent;
	}

	public TextField getTxtActivityCalories() {
		return txtActivityCalories;
	}

	public TextField getTxtActivityMinAltitude() {
		return txtActivityMinAltitude;
	}

	public TextField getTxtActivityMaxAltitude() {
		return txtActivityMaxAltitude;
	}

	public Button getBtnActivityEdit() {
		return btnActivityEdit;
	}

	public Button getBtnActivityCancel() {
		return btnActivityCancel;
	}

	public TableView<SplitTime> getTableSplits() {
		return tableSplits;
	}

	public TableColumn<SplitTime, String> getColSplitRound() {
		return colSplitRound;
	}

	public TableColumn<SplitTime, String> getColSplitTime() {
		return colSplitTime;
	}

	public TableColumn<SplitTime, String> getColSplitTotalDistance() {
		return colSplitTotalDistance;
	}

	public TableColumn<SplitTime, String> getColSplitDistance() {
		return colSplitDistance;
	}

	public TableColumn<SplitTime, String> getColSplitPace() {
		return colSplitPace;
	}

	public TableColumn<SplitTime, String> getColSplitTotalTime() {
		return colSplitTotalTime;
	}

	public TableColumn<SplitTime, String> getColSplitAverageHeartRate() {
		return colSplitAverageHeartRate;
	}

	public TableColumn<SplitTime, String> getColSplitMaximumHeartRate() {
		return colSplitMaximumHeartRate;
	}

	public TableColumn<SplitTime, String> getColSplitAscent() {
		return colSplitAscent;
	}

	public TableColumn<SplitTime, String> getColSplitDescent() {
		return colSplitDescent;
	}

	public TextField getTxtBestTimeSplit() {
		return txtBestTimeSplit;
	}

	public TextField getTxtAverageTimeSplit() {
		return txtAverageTimeSplit;
	}

	public TextField getTxtBestPaceSplit() {
		return txtBestPaceSplit;
	}

	public TextField getTxtAveragePaceSplit() {
		return txtAveragePaceSplit;
	}

	public CheckBox getChkBoxSplitSnapIn() {
		return chkBoxSplitSnapIn;
	}

	public Button getBtnFilterTreeView() {
		return btnFilterTreeView;
	}

	public Button getBtnActivityDelete() {
		return btnActivityDelete;
	}

	public TextField getTxtTotalTimeSplit() {
		return txtTotalTimeSplit;
	}

	public Tab getTabSplits() {
		return tabSplits;
	}

	public Tab getTabCharts() {
		return tabCharts;
	}

	public TabPane getTabPane() {
		return tabPane;
	}

	public ComboBox<String> getCbxTimeDistance() {
		return cbxTimeDistance;
	}

	public ComboBox<Long> getCbxSmoothFactor() {
		return cbxSmoothFactor;
	}

	public StackPane getStackPaneCharts() {
		return stackPaneCharts;
	}

	public Label getLblChartTime() {
		return lblChartTime;
	}

	public Label getLblChartDistance() {
		return lblChartDistance;
	}

	public Label getLblChartAlitude() {
		return lblChartAlitude;
	}

	public Label getLblChartPace() {
		return lblChartPace;
	}

	public Label getLblChartSplitPace() {
		return lblChartSplitPace;
	}

	public Label getLblChartHeartRate() {
		return lblChartHeartRate;
	}

	public ComboBox<String> getCbxChartType() {
		return cbxChartType;
	}

	public ComboBox<String> getCbxChartType2() {
		return cbxChartType2;
	}

	public ComboBox<String> getCbxChartType3() {
		return cbxChartType3;
	}

	public Tab getTabMap() {
		return tabMap;
	}

	public WebView getWebMapView() {
		return webMapView;
	}

	public ToggleButton getBtnRoadMap() {
		return btnRoadMap;
	}

	public ToggleButton getBtnSatellite() {
		return btnSatellite;
	}

	public ToggleButton getBtnHybrid() {
		return btnHybrid;
	}

	public StackPane getStackPaneMap() {
		return stackPaneMap;
	}

	public ToggleButton getBtnLandscape() {
		return btnLandscape;
	}

	public TextField getTxtTotalDistanceSplit() {
		return txtTotalDistanceSplit;
	}

	public StackPane getStackPaneTree() {
		return stackPaneTree;
	}

	public TreeView<String> getLapTreeView() {
		return lapTreeView;
	}

	public ComboBox<String> getCbxEquipmentType1() {
		return cbxEquipmentType1;
	}

	public ComboBox<String> getCbxEquipmentType2() {
		return cbxEquipmentType2;
	}

	public ComboBox<String> getCbxEquipmentType3() {
		return cbxEquipmentType3;
	}

	public Button getBtnEditEquipment() {
		return btnEditEquipment;
	}

	public Tab getTabStatistic() {
		return tabStatistic;
	}

	public StackPane getStackPaneStatistic() {
		return stackPaneStatistic;
	}

	public ComboBox<String> getCbxStatisticPredefined() {
		return cbxStatisticPredefined;
	}

	public DatePicker getDateStatisticFrom() {
		return dateStatisticFrom;
	}

	public DatePicker getDateStatisticTo() {
		return dateStatisticTo;
	}

	public ComboBox<String> getCbxStatisticType() {
		return cbxStatisticType;
	}

	public ComboBox<String> getCbxStatisticEquipment() {
		return cbxStatisticEquipment;
	}

	public Tab getTabEquipment() {
		return tabEquipment;
	}

	public StackPane getStackPaneEquipment() {
		return stackPaneEquipment;
	}

	public Label getLblOverviewDistance() {
		return lblOverviewDistance;
	}

	public Label getLblOverviewTime() {
		return lblOverviewTime;
	}

	public Label getLblOverviewPace() {
		return lblOverviewPace;
	}

	public Label getLblOverviewAscent() {
		return lblOverviewAscent;
	}

	public Label getLblOverviewDescent() {
		return lblOverviewDescent;
	}

	public Label getLblOverviewHeartRate() {
		return lblOverviewHeartRate;
	}

	public Label getLblOverviewHeartRateMax() {
		return lblOverviewHeartRateMax;
	}

	public Label getLblOverviewCalories() {
		return lblOverviewCalories;
	}

	public Label getLblOverviewAltMin() {
		return lblOverviewAltMin;
	}

	public Label getLblOverviewAltMax() {
		return lblOverviewAltMax;
	}

	public Label getLblSplitBestTime() {
		return lblSplitBestTime;
	}

	public Label getLblSplitAvgTime() {
		return lblSplitAvgTime;
	}

	public Label getLblSplitTotalTime() {
		return lblSplitTotalTime;
	}

	public Label getLblSplitTotalDist() {
		return lblSplitTotalDist;
	}

	public Label getLblSplitBestPace() {
		return lblSplitBestPace;
	}

	public Label getLblSplitAvgPace() {
		return lblSplitAvgPace;
	}

	public Label getLblFilterText() {
		return lblFilterText;
	}

}
