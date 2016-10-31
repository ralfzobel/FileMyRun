package de.acwhadk.rz.filemyrun.gui;

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

import de.acwhadk.rz.filemyrun.core.model.TrainingFile;
import de.acwhadk.rz.filemyrun.core.setup.Const;
import de.acwhadk.rz.filemyrun.core.setup.Lang;

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

	private static String[] predefinedTimes;	
	private static String[] aggregationType;

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

		predefinedTimes = new String[6];
		predefinedTimes[0] = Lang.get().text(Lang.STATISTIC_CBX_THIS_YEAR);
		predefinedTimes[1] = Lang.get().text(Lang.STATISTIC_CBX_LAST_YEAR);
		predefinedTimes[2] = Lang.get().text(Lang.STATISTIC_CBX_12_MONTH);
		predefinedTimes[3] = Lang.get().text(Lang.STATISTIC_CBX_6_MONTH);
		predefinedTimes[4] = Lang.get().text(Lang.STATISTIC_CBX_3_MONTH);
		predefinedTimes[5] = Lang.get().text(Lang.STATISTIC_CBX_USER_DEFINED);
		ObservableList<String> comboBoxItemsPredefined = FXCollections.observableArrayList();
		comboBoxItemsPredefined.addAll(predefinedTimes);
		controller.getCbxStatisticPredefined().setItems(comboBoxItemsPredefined);
		controller.getCbxStatisticPredefined().getSelectionModel().selectFirst();
		controller.getCbxStatisticPredefined().setEditable(false);
		controller.getCbxStatisticPredefined().valueProperty().addListener((ChangeListener<String>) (ov, t, t1) -> handlePredefined());

		aggregationType = new String[2];
		aggregationType[0] = Lang.get().text(Lang.STATISTIC_CBX_WEEK_DIST);
		aggregationType[1] = Lang.get().text(Lang.STATISTIC_CBX_MONTH_DIST);
		ObservableList<String> comboBoxItemsType = FXCollections.observableArrayList();
		comboBoxItemsType.addAll(aggregationType);
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
			boolean weekly = controller.getCbxStatisticType().getSelectionModel().getSelectedIndex() == 0;
			LocalDate firstDate = controller.getDateStatisticFrom().getValue();
			LocalDate lastDate = controller.getDateStatisticTo().getValue();
			int index=0;
			if (weekly) {
				// set first day to monday
				while(firstDate.getDayOfWeek() != DayOfWeek.MONDAY) {
					firstDate = firstDate.minusDays(1L);
				}
				// set last day to sunday
				while(lastDate.getDayOfWeek() != DayOfWeek.SUNDAY) {
					lastDate = lastDate.plusDays(1L);
				}
				LocalDate curDate = firstDate;
				while(curDate.isBefore(lastDate) || curDate.isEqual(lastDate)) {
					distances.put(index, new Double(0.0));
					dates.put(index, curDate);
					indices.put(curDate, index);
					curDate = curDate.plusDays(7L);
					++index;
				}
			} else {
				// set first day to first day of month
				if (firstDate.getDayOfMonth() > 1) {
					firstDate = firstDate.minusDays(firstDate.getDayOfMonth()-1);
				}
				// set last day to last day of month
				while(lastDate.getDayOfMonth() != 1) {
					lastDate = lastDate.plusDays(1);
				}
				lastDate = lastDate.minusDays(1);				
				// create indices
				LocalDate curDate = firstDate;			
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
					distances.put(index, dist);
				}
			}
			XYChart.Series<Number, Number> series = new XYChart.Series<>();
			for(Entry<Integer, LocalDate> entry : dates.entrySet()) {
				int i = entry.getKey();
				Double y = distances.get(i);
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
		int item = controller.getCbxStatisticPredefined().getSelectionModel().getSelectedIndex();
		switch (item) {
		case 0: // this year
			fromMonth = 0;
			fromDay = 1;
			break;
		case 1: // last year
			--fromYear;
			fromMonth = 0;
			fromDay = 1;
			--toYear;
			toMonth = 11;
			toDay = 31;
			break;
		case 2: // last 12 month
			--fromYear;
			break;
		case 3: // last 6 month
			fromMonth -= 6;
			if (fromMonth < 0) {
				--fromYear;
				fromMonth += 12;
			}
			break;
		case 4: // last 3 month
			fromMonth -= 3;
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
						formatter = DateTimeFormatter.ofPattern(Const.FORMAT_DAY_MONTH);
						return date.format(formatter);
//						if (date.getDayOfMonth() > 7) {
//							return null;
//						}
					}
					formatter = DateTimeFormatter.ofPattern(Const.FORMAT_MONTH);
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
