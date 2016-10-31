package de.acwhadk.rz.filemyrun.gui;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import de.acwhadk.rz.filemyrun.core.model.Activity;
import de.acwhadk.rz.filemyrun.core.model.Lap;
import de.acwhadk.rz.filemyrun.core.model.Track;
import de.acwhadk.rz.filemyrun.core.model.TrackPoint;

/**
 * This class contains the data that is needed to display charts.
 * 
 * @author Ralf
 *
 */
public class ChartData {

	private boolean hasHeartRateData=false;
	private int minHeartRate=9999;
	private int maxHeartRate=0;
	
	private boolean hasDistanceData=false;
	private double maxDistance=0.;
	
	private boolean hasAltitudeData=false;
	private double minAltitude=9999.;
	private double maxAltitude=0.;
	
	private boolean hasSecondPerKmData=false;
	
	private List<ChartDataItem> data = new ArrayList<>(1000);
	private double maxMetersPerSecond=0.;
	
	public ChartData(Activity activity, long smooth) {
		long startTime = 0;
		for(Lap lap : activity.getLaps()) {
			double lapSecsPerKm = lap.getTotalTimeSeconds() / lap.getDistanceMeters() * 1000.;
			Track track = lap.getTrack();
			if (track == null) {
				continue;
			}
			List<TrackPoint> trackpoints = track.getTrackpoints();
			for(TrackPoint tp : trackpoints) {
				Calendar t = tp.getTime();
				long time = t.getTimeInMillis();
				if (startTime == 0) {
					startTime = time;
				}
				ChartDataItem item = new ChartDataItem((time-startTime)/1000L);
				Double dist = tp.getDistanceMeters();
				if (dist != null) {
					if (dist > maxDistance) {
						maxDistance = dist;
					}
					item.setDistance(dist);
					hasDistanceData = true;
				}
				Integer hf = tp.getHeartRateBpm();
				if (hf != null) {
					if (minHeartRate > hf) { 
						minHeartRate = hf;							
					}
					if (maxHeartRate < hf) { 
						maxHeartRate = hf;							
					}
					hasHeartRateData = true;
					item.setHeartrate((double)hf);
				}
				Double altitude = tp.getAltitudeMeters();
				if (altitude != null) {
					if (minAltitude > altitude) {
						minAltitude = altitude;
					}
					if (maxAltitude < altitude) {
						maxAltitude = altitude;
					}
					hasAltitudeData = true;
					item.setAltitude(altitude);
				}
				item.setSplitSecondsPerKm(lapSecsPerKm);
				data.add(item);
			}
		}
		calculatePace(smooth);		
	}

	private void calculatePace(long smooth) {
		for(int i=0; i<data.size(); ++i) {
			ChartDataItem item = data.get(i);
			if (item.getDistance() == null) {
				continue;
			}
			for(int j=i-1; j>=0; --j) {
				ChartDataItem lastItem = data.get(j);
				long t = item.getTime() - lastItem.getTime();
				if (t > smooth) {
					if (lastItem.getDistance() != null) {
						Double d = item.getDistance() - lastItem.getDistance();
						double s = t/d*1000.;
						item.setSecondsPerKm(s);
						hasSecondPerKmData = true;
					}
					break;
				}
			}
		}		
	}

	public boolean hasHeartRateData() {
		return hasHeartRateData;
	}

	public int getMinHeartRate() {
		return minHeartRate;
	}

	public int getMaxHeartRate() {
		return maxHeartRate;
	}

	public boolean isHasDistanceData() {
		return hasDistanceData;
	}

	public double getMaxDistance() {
		return maxDistance;
	}

	public boolean hasAltitudeData() {
		return hasAltitudeData;
	}

	public double getMinAltitude() {
		return minAltitude;
	}

	public double getMaxAltitude() {
		return maxAltitude;
	}

	public boolean hasSecondPerKmData() {
		return hasSecondPerKmData;
	}

	public List<ChartDataItem> getData() {
		return data;
	}

	public double getMaxMetersPerSecond() {
		return maxMetersPerSecond;
	}

	public ChartDataItem getChartItem(Number xValue, boolean distanceOnXAxis) {
		if (data.isEmpty()) {
			return null;
		}
		if (!(xValue instanceof Double)) {
			return data.get(0);
		}
		Double x = (Double) xValue;
		if (distanceOnXAxis) {
			for(ChartDataItem item : data) {
				if (item.getDistance() != null && item.getDistance() >= x) {
					return item;
				}
			}
		} else {
			for(ChartDataItem item : data) {
				if (item.getTime() >= x) {
					return item;
				}
			}
		}
		return data.get(data.size()-1);
	}
}
