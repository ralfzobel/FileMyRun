package de.acwhadk.rz.filemyrun.controller;

import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.chart.AreaChart;
import javafx.scene.chart.Axis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Series;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.util.StringConverter;

public class GuiTabCharts {

	private static String[] TimeDist = { "Zeit", "Distanz" };
	private static String[] ChartType = { "Herzfrequenz", "Höhe", "Pace", "Split-Pace" };

	private Controller controller;
	@SuppressWarnings("unused")
	private GuiControl guiControl; 
	private Activity activity;

	private ObservableList<Long> comboBoxItemsSmooth = FXCollections.observableArrayList();

	private AreaChart<Number,Number> baseChart;
	private double lowerBound;
	private double upperBound;
	private boolean distanceOnXAxis;
	private boolean paceOnYAxis;
	
	private ChartData chartData;

	public GuiTabCharts(Controller controller, GuiControl guiControl) {
		super();
		this.controller = controller;
		this.guiControl = guiControl;

		initialize();
	}

	public void setActivity(Activity activity) {
		this.activity = activity;
	}

	private void initialize() {
		controller.getCbxTimeDistance().setEditable(false);

		ObservableList<String> comboBoxItemsTimeDist = FXCollections.observableArrayList();
		comboBoxItemsTimeDist.addAll(TimeDist);
		controller.getCbxTimeDistance().setItems(comboBoxItemsTimeDist);
		controller.getCbxTimeDistance().getSelectionModel().selectFirst();
		controller.getCbxTimeDistance().setEditable(false);
		controller.getCbxTimeDistance().valueProperty().addListener((ChangeListener<String>) (ov, t, t1) -> initActivity());

		initChartComboBox(controller.getCbxChartType(), false);
		initChartComboBox(controller.getCbxChartType2(), true);
		initChartComboBox(controller.getCbxChartType3(), true);

		comboBoxItemsSmooth.addAll(0L, 5L, 10L, 15L, 20L, 30L, 60L);
		controller.getCbxSmoothFactor().setItems(comboBoxItemsSmooth);
		controller.getCbxSmoothFactor().setEditable(false);
		controller.getCbxSmoothFactor().valueProperty().addListener((ChangeListener<Long>) (ov, t, t1) -> initActivity());

		baseChart = new AreaChart<>(new NumberAxis(), new NumberAxis());
		setDefaultChartProperties(baseChart);
		baseChart.setCreateSymbols(false);

		controller.getStackPaneCharts().getChildren().addAll(baseChart);
		
		initHoverOnChart(baseChart);

	}

	public void initActivity() {
		try {
			baseChart.getData().clear();

			distanceOnXAxis = controller.getCbxTimeDistance().getSelectionModel().getSelectedIndex() == 1;
			initXAxis(baseChart);

			long smooth = 0;
			Long smoothFactor = controller.getCbxSmoothFactor().getSelectionModel().getSelectedItem();
			if (smoothFactor != null) {
				smooth = smoothFactor;
			}
			chartData = new ChartData(activity.getTrainingActivity(), smooth);

			Series<Number,Number> series1 = getSeries(controller.getCbxChartType(), true);
			if (series1 == null) {
				return;
			}
			baseChart.getData().add(series1);
			initYAxis();

			Series<Number,Number> series2 = getSeries(controller.getCbxChartType2(), false);
			if (series2 != null) {
				baseChart.getData().add(series2);
			}

			Series<Number,Number> series3 = getSeries(controller.getCbxChartType3(), false);
			if (series3 != null) {
				baseChart.getData().add(series3);
			}
			
		} catch (Exception e) {
			GuiControl.showException(e);
		}
	}		

	private Series<Number,Number> getSeries(ComboBox<String> comboBox, boolean firstChart) throws Exception {
		String chartType = comboBox.getSelectionModel().getSelectedItem();
		if (chartType.equals(ChartType[0])) {
			if (chartData.hasHeartRateData()) {
				return heartRate(chartData, firstChart);
			}
		}
		if (chartType.equals(ChartType[1])) {
			if (chartData.hasAltitudeData()) {
				return altitude(chartData, firstChart);
			}
		}
		if (chartType.equals(ChartType[2])) {
			if (chartData.hasSecondPerKmData()) {
				return pace(chartData, firstChart);
			}
		}
		if (chartType.equals(ChartType[3])) {
			if (chartData.hasSecondPerKmData()) {
				return splitPace(chartData, firstChart);
			}
		}
		return null;
	}
	
	private void initChartComboBox(ComboBox<String> comboBox, boolean addEmpty) {
		ObservableList<String> comboBoxItemsChartType = FXCollections.observableArrayList();
		if (addEmpty) {
			comboBoxItemsChartType.add("");
		}
		comboBoxItemsChartType.addAll(ChartType);
		comboBox.setItems(comboBoxItemsChartType);
		comboBox.getSelectionModel().selectFirst();
		comboBox.setEditable(false);
		comboBox.valueProperty().addListener((ChangeListener<String>) (ov, t, t1) -> initActivity());	
	}
	
	private Series<Number,Number> altitude(ChartData chartData, boolean initBounds) throws Exception {
		double min = Math.round(chartData.getMinAltitude()/50.-.5) * 50.;
		double max = Math.round(chartData.getMaxAltitude()/50.+.5) * 50.;
		if (initBounds) {
			lowerBound = min;
			upperBound = max;
			paceOnYAxis = false;
		}
		XYChart.Series<Number,Number> series = new XYChart.Series<>();
		for(ChartDataItem item : chartData.getData()) {
			Double x = getX(item);
			Double y = item.getAltitude();
			if (x != null && y != null) {
				if (!initBounds) {
					y = transformY(y, min, max, lowerBound, upperBound);
				}
				series.getData().add(new XYChart.Data<Number,Number>(x, y));
			}
		}
		return series;
	}

	private Series<Number,Number> heartRate(ChartData chartData, boolean initBounds) throws Exception {
		double min = Math.round(chartData.getMinHeartRate()/10.-.5) * 10.;
		double max = Math.round(chartData.getMaxHeartRate()/10.+.5) * 10.;
		if (initBounds) {
			lowerBound = min;
			upperBound = max;
			paceOnYAxis = false;
		}
		XYChart.Series<Number,Number> series = new XYChart.Series<>();
		for(ChartDataItem item : chartData.getData()) {
			Double x = getX(item);
			Double y = item.getHeartrate();
			if (x != null && y != null) {
				if (!initBounds) {
					y = transformY(y, min, max, lowerBound, upperBound);
				}
				series.getData().add(new XYChart.Data<Number,Number>(x, y));
			}
		}
		return series;
	}

	private Series<Number, Number> pace(ChartData chartData, boolean initBounds) throws Exception {
		double min = 0.;
		double max = 480.;
		if (initBounds) {
			lowerBound = 0.0;
			upperBound = 480.0;
			paceOnYAxis = true;
		}
		XYChart.Series<Number,Number> series = new XYChart.Series<>();
		for(ChartDataItem item : chartData.getData()) {
			Double x = getX(item);
			Double y = item.getSecondsPerKm();
			if (x != null && y != null) {
				y = 600 - y;
				if (y < 0) {
					y = 0.;
				}
				if (!initBounds) {
					y = transformY(y, min, max, lowerBound, upperBound);
				}
				series.getData().add(new XYChart.Data<Number,Number>(x, y));
			}
		}
		return series;
	}

	private Series<Number,Number> splitPace(ChartData chartData, boolean initBounds) throws Exception {
		double min = 0.;
		double max = 480.;
		if (initBounds) {
			lowerBound = 0.0;
			upperBound = 480.0;
			paceOnYAxis = true;
		}
		XYChart.Series<Number,Number> series = new XYChart.Series<>();
		for(ChartDataItem item : chartData.getData()) {
			Double x = getX(item);
			Double y = item.getSplitSecondsPerKm();
			if (x != null && y != null) {
				y = 600 - y;
				if (y < 0) {
					y = 0.;
				}
				if (!initBounds) {
					y = transformY(y, min, max, lowerBound, upperBound);
				}
				series.getData().add(new XYChart.Data<Number,Number>(x, y));
			}
		}
		return series;
	}

	private Double transformY(double y, double lower, double upper, double lowerBound2, double upperBound2) {
		double stretch = (upperBound2 - lowerBound2) / (upper - lower); 
		return (y - lower) * stretch + lowerBound2;
	}

	private Double getX(ChartDataItem item) {
		if (distanceOnXAxis) {
			return item.getDistance();
		} else {
			return (double) item.getTime();
		}
	}

	private void initXAxis(XYChart<Number,Number> chart) throws Exception {
		@SuppressWarnings("rawtypes")
		Axis xAxis = chart.getXAxis();
		if (xAxis instanceof NumberAxis) {
			NumberAxis x = (NumberAxis) xAxis;
			x.setTickLabelFormatter(new StringConverter<Number>() {
				@Override
				public Long fromString(String arg0) {
					return null;
				}
				@Override
				public String toString(Number arg0) {
					double d = (Double) arg0;
					if (distanceOnXAxis) {
						return Formatter.formatDistanceToKm(d);
					} else {
						long t = Math.round(d);
						return Formatter.formatSecondsForCharts(t);
					}
				}
			});
			x.setAutoRanging(false);
			x.setLowerBound(0.);
			x.setUpperBound(distanceOnXAxis ? activity.getTrackDistance() : activity.getTotalTime());
			x.setTickUnit(distanceOnXAxis ? 1000. : 300.);
		}
//		chart.getXAxis().setLabel(distanceOnXAxis);
	}

	private void initYAxis() throws Exception {
		@SuppressWarnings("rawtypes")
		Axis yAxis = baseChart.getYAxis();
		if (yAxis instanceof NumberAxis) {
			NumberAxis y = (NumberAxis) yAxis;
			y.setAutoRanging(false);
			y.setLowerBound(lowerBound);
			y.setUpperBound(upperBound);
			if (paceOnYAxis) {
				y.setTickLabelFormatter(new StringConverter<Number>() {
					@Override
					public Long fromString(String arg0) {
						return null;
					}
					@Override
					public String toString(Number arg0) {
						double secondsPerKm = 600 - (Double) arg0;
						return Formatter.formatPace(secondsPerKm);
					}
				});
			} else {
				y.setTickLabelFormatter(null);
			}
			y.setTickUnit(Math.round((upperBound-lowerBound)/10.));
		}
		//		chart.getYAxis().setLabel(label);
	}

	private void setDefaultChartProperties(final XYChart<Number, Number> chart) {
		chart.setLegendVisible(false);
		chart.setAnimated(false);
	}

	private void initHoverOnChart(XYChart<Number, Number> lineChart) {
		final Axis<Number> xAxis = lineChart.getXAxis();
		final Axis<Number> yAxis = lineChart.getYAxis();

		final Label lblTime = controller.getLblChartTime();
		final Label lblDistance = controller.getLblChartDistance();
		final Label lblAltitude = controller.getLblChartAlitude();
		final Label lblHeartRate = controller.getLblChartHeartRate();
		final Label lblPace = controller.getLblChartPace();
		final Label lblSplitPace = controller.getLblChartSplitPace();

		final Node chartBackground = lineChart.lookup(".chart-plot-background");
		for (Node n: chartBackground.getParent().getChildrenUnmodifiable()) {
			if (n != chartBackground && n != xAxis && n != yAxis) {
				n.setMouseTransparent(true);
			}
		}

		chartBackground.setOnMouseEntered(new EventHandler<MouseEvent>() {
			@Override public void handle(MouseEvent mouseEvent) {
				lblTime.setVisible(true);
				lblDistance.setVisible(true);
				lblAltitude.setVisible(true);
				lblHeartRate.setVisible(true);
				lblPace.setVisible(true);
				lblSplitPace.setVisible(true);
			}
		});

		chartBackground.setOnMouseMoved(new EventHandler<MouseEvent>() {
			@Override public void handle(MouseEvent mouseEvent) {
				ChartDataItem chartItem = chartData.getChartItem(xAxis.getValueForDisplay(mouseEvent.getX()), distanceOnXAxis);
				if (chartItem != null) {
					lblTime.setText("Zeit: " + Formatter.formatSecondsForCharts(chartItem.getTime()));
					lblDistance.setText("Distanz: "+ Formatter.formatDistanceToKm(chartItem.getDistance()));
					lblAltitude.setText("Höhe: " + Formatter.formatAsInteger(chartItem.getAltitude()));
					lblHeartRate.setText("Hf: " + Formatter.formatAsInteger(chartItem.getHeartrate()));
					lblPace.setText("Pace: " + Formatter.formatPace(chartItem.getSecondsPerKm()));
					lblSplitPace.setText("SplitPace: " + Formatter.formatPace(chartItem.getSplitSecondsPerKm()));
				}
			}
		});

		chartBackground.setOnMouseExited(new EventHandler<MouseEvent>() {
			@Override public void handle(MouseEvent mouseEvent) {
				lblTime.setVisible(false);
				lblDistance.setVisible(false);
				lblAltitude.setVisible(false);
				lblHeartRate.setVisible(false);
				lblPace.setVisible(false);
				lblSplitPace.setVisible(false);
			}
		});
/*
		xAxis.setOnMouseEntered(new EventHandler<MouseEvent>() {
			@Override public void handle(MouseEvent mouseEvent) {
				lblTime.setVisible(true);
			}
		});

		xAxis.setOnMouseMoved(new EventHandler<MouseEvent>() {
			@Override public void handle(MouseEvent mouseEvent) {
				lblTime.setText(
						String.format(
								"x = %.2f",
								xAxis.getValueForDisplay(mouseEvent.getX())
								)
						);
			}
		});

		xAxis.setOnMouseExited(new EventHandler<MouseEvent>() {
			@Override public void handle(MouseEvent mouseEvent) {
				lblTime.setVisible(false);
			}
		});

		yAxis.setOnMouseEntered(new EventHandler<MouseEvent>() {
			@Override public void handle(MouseEvent mouseEvent) {
				lblTime.setVisible(true);
			}
		});

		yAxis.setOnMouseMoved(new EventHandler<MouseEvent>() {
			@Override public void handle(MouseEvent mouseEvent) {
				lblTime.setText(
						String.format(
								"y = %.2f",
								yAxis.getValueForDisplay(mouseEvent.getY())
								)
						);
			}
		});

		yAxis.setOnMouseExited(new EventHandler<MouseEvent>() {
			@Override public void handle(MouseEvent mouseEvent) {
				lblTime.setVisible(false);
			}
		});
*/
	}
}
