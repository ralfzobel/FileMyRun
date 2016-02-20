package de.acwhadk.rz.filemyrun.controller;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import de.acwhadk.rz.filemyrun.file.TrainingFile;

import java.util.TreeMap;

import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.chart.Axis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.util.StringConverter;

/**
 * This class controls the contents of the statistic tab.
 * 
 * @author Ralf
 *
 */
public class GuiTabStatistic {

	private static final String THIS_YEAR = "dieses Jahr";
	private static final String LAST_CALENDAR_YEAR = "letztes Kalenderjahr";
	private static final String LAST_YEAR = "letztes Jahr";
	private static final String LAST_3_MONTH = "die letzten 3 Monate";
	private static final String LAST_6_MONTH = "die letzten 6 Monate";
	private static final String USER_DEFINED = "benutzerdefiniert";
	
	private static final String WEEK_DIST = "Wochen-km";
	private static final String MONTH_DIST = "Monats-km";
	
	private static String[] PredefinedTimes = { 
			THIS_YEAR,
			LAST_CALENDAR_YEAR,
			LAST_YEAR,
			LAST_3_MONTH, 
			LAST_6_MONTH, 
			USER_DEFINED 
			};	
	
	private static String[] AggregationType = { 
			WEEK_DIST, 
			MONTH_DIST 
			};

	private Controller controller;
	private GuiControl guiControl; 

	private LineChart<Number,Number> chart;
	
	public GuiTabStatistic(Controller controller, GuiControl guiControl) {
		super();
		this.controller = controller;
		this.guiControl = guiControl;

		initialize();
	}

	private void initialize() {
		chart = new LineChart<>(new NumberAxis(), new NumberAxis());
		setDefaultChartProperties(chart);
		chart.setCreateSymbols(true);
		
		controller.getCbxTimeDistance().setEditable(false);

		ObservableList<String> comboBoxItemsPredefined = FXCollections.observableArrayList();
		comboBoxItemsPredefined.addAll(PredefinedTimes);
		controller.getCbxStatisticPredefined().setItems(comboBoxItemsPredefined);
		controller.getCbxStatisticPredefined().getSelectionModel().selectFirst();
		controller.getCbxStatisticPredefined().setEditable(false);
		controller.getCbxStatisticPredefined().valueProperty().addListener((ChangeListener<String>) (ov, t, t1) -> handlePredefined());

		ObservableList<String> comboBoxItemsType = FXCollections.observableArrayList();
		comboBoxItemsType.addAll(AggregationType);
		controller.getCbxStatisticType().setItems(comboBoxItemsType);
		controller.getCbxStatisticType().getSelectionModel().selectFirst();
		controller.getCbxStatisticType().setEditable(false);
		controller.getCbxStatisticType().valueProperty().addListener((ChangeListener<String>) (ov, t, t1) -> initStatistic());

		handlePredefined();
		
		controller.getDateStatisticFrom().valueProperty().addListener((ChangeListener<LocalDate>) (ov, t, t1) -> initStatistic());
		controller.getDateStatisticTo().valueProperty().addListener((ChangeListener<LocalDate>) (ov, t, t1) -> initStatistic());
		controller.getStackPaneStatistic().getChildren().addAll(chart);

	}

	public void initStatistic() {
		try {
			chart.getData().clear();

			Map<Integer, Double> distances = new TreeMap<>();
			Map<Integer, LocalDate> dates = new TreeMap<>();
			Map<LocalDate, Integer> indices = new HashMap<>();
			boolean weekly = controller.getCbxStatisticType().getSelectionModel().getSelectedItem().equals(WEEK_DIST);
			LocalDate firstDate = controller.getDateStatisticFrom().getValue();
			LocalDate lastDate = controller.getDateStatisticTo().getValue();
			int index=0;
			if (weekly) {
				LocalDate curDate = firstDate;
				while(curDate.getDayOfWeek() != DayOfWeek.MONDAY) {
					curDate = curDate.minusDays(1L);
				}
				while(curDate.isBefore(lastDate) || curDate.isEqual(lastDate)) {
					distances.put(index, new Double(0.0));
					dates.put(index, curDate);
					indices.put(curDate, index);
					curDate = curDate.plusDays(7L);
					++index;
				}
			} else {
				LocalDate curDate = firstDate;			
				if (firstDate.getDayOfMonth() > 1) {
					curDate = firstDate.minusDays(firstDate.getDayOfMonth()-1);
				}
				while(curDate.isBefore(lastDate)) {
					distances.put(index, new Double(0.0));
					dates.put(index, curDate);
					indices.put(curDate, index);
					curDate = curDate.plusMonths(1L);
					++index;
				}
				
			}
			Collection<TrainingFile> files = guiControl.getTrainingFileMan().getTrainingFiles().values();
			for(TrainingFile file : files) {
				Date date = file.getTime();
				GregorianCalendar cal = new GregorianCalendar();
				cal.setTime(date);
				LocalDate ldate = LocalDate.of(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH)+1, cal.get(Calendar.DAY_OF_MONTH));
				if (ldate.isBefore(firstDate) || ldate.isAfter(lastDate)) {
					continue;
				}
				ldate = getLocalDate(weekly, date);
				index=indices.get(ldate);
				Double dist = distances.get(index);
				if (dist != null && file.getDistance() != null) {
					dist += file.getDistance();
					index = indices.get(ldate);
					distances.put(index, dist);
				}
			}
			XYChart.Series<Number, Number> series = new XYChart.Series<>();
			for(Entry<Integer, LocalDate> entry : dates.entrySet()) {
				int i = entry.getKey();
				LocalDate ldate = entry.getValue();
				Double y = distances.get(i);
				System.out.println("index " + i +", date " + ldate + ", y " + y);
				series.getData().add(new XYChart.Data<Number,Number>(i, y));
			}
			initXAxis(weekly, dates);
			chart.getData().add(series);			
		} catch (Exception e) {
			GuiControl.showException(e);
		}
	}		

	private LocalDate getLocalDate(boolean weekly, Date date) {
		GregorianCalendar cal = new GregorianCalendar();
		cal.setTime(date);
		LocalDate ldate = LocalDate.of(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH)+1, cal.get(Calendar.DAY_OF_MONTH));
		if (weekly) {
			while(ldate.getDayOfWeek() != DayOfWeek.MONDAY) {
				ldate = ldate.minusDays(1L);
			}
		} else {
			if (ldate.getDayOfMonth() > 1) {
				ldate = ldate.minusDays(ldate.getDayOfMonth()-1);
			}
		}
		return ldate;
	}

	private Object handlePredefined() {
		GregorianCalendar now = new GregorianCalendar();
		int fromYear = now.get(Calendar.YEAR);
		int fromMonth = now.get(Calendar.MONTH);
		int fromDay = now.get(Calendar.DAY_OF_MONTH);
		int toYear = fromYear;
		int toMonth = fromMonth;
		int toDay = fromDay;
		String item = controller.getCbxStatisticPredefined().getSelectionModel().getSelectedItem();
		switch (item) {
		case LAST_CALENDAR_YEAR:
			--fromYear;
			fromMonth = 0;
			fromDay = 1;
			--toYear;
			toMonth = 11;
			toDay = 31;
			break;
		case THIS_YEAR:
			fromMonth = 0;
			fromDay = 1;
			break;
		case LAST_YEAR:
			--fromYear;
			break;
		case LAST_3_MONTH:
			fromMonth -= 3;
			if (fromMonth < 0) {
				--fromYear;
				fromMonth += 12;
			}
			break;
		case LAST_6_MONTH:
			fromMonth -= 6;
			if (fromMonth < 0) {
				--fromYear;
				fromMonth += 12;
			}
			break;
		}
		controller.getDateStatisticFrom().setValue(LocalDate.of(fromYear, fromMonth+1, fromDay));
		controller.getDateStatisticTo().setValue(LocalDate.of(toYear, toMonth+1, toDay));
		
		initStatistic();
		return null;
	}
	
	private void initXAxis(boolean weekly, Map<Integer, LocalDate> dates) throws Exception {
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
					int i = arg0.intValue();
					LocalDate date = dates.get(i);
					if (date == null) {
						return null;
					}
					DateTimeFormatter formatter = null;
					if (weekly) {
						if (date.getDayOfMonth() > 7) {
							return null;
						}
					}
					formatter = DateTimeFormatter.ofPattern("MMM");
					return date.format(formatter);
				}
			});
			x.setAutoRanging(false);
			x.setLowerBound(0);
			x.setUpperBound(dates.size()-1);
			x.setTickUnit(1.);
		}
//		chart.getXAxis().setLabel(distanceOnXAxis);
	}

	private void setDefaultChartProperties(final LineChart<Number,Number> chart2) {
		chart2.setLegendVisible(false);
		chart2.setAnimated(false);
	}

}
