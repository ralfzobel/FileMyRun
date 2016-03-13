package de.acwhadk.rz.filemyrun.gui;

import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;

import com.garmin.tcdbv2.ActivityLapT;
import com.garmin.tcdbv2.ActivityListT;
import com.garmin.tcdbv2.ActivityT;
import com.garmin.tcdbv2.HeartRateInBeatsPerMinuteT;
import com.garmin.tcdbv2.TrackT;
import com.garmin.tcdbv2.TrackpointT;
import com.garmin.tcdbv2.TrainingCenterDatabaseT;

import de.acwhadk.rz.filemyrun.data.TrainingActivity;
import de.acwhadk.rz.filemyrun.setup.Lang;

/**
 * This class contains the data that is needed to display charts.
 * 
 * @author Ralf
 *
 */
public class ChartData {

	private boolean hasHeartRateData=false;
	private short minHeartRate=9999;
	private short maxHeartRate=0;
	
	private boolean hasDistanceData=false;
	private double maxDistance=0.;
	
	private boolean hasAltitudeData=false;
	private double minAltitude=9999.;
	private double maxAltitude=0.;
	
	private boolean hasSecondPerKmData=false;
	
	private List<ChartDataItem> data = new ArrayList<>(1000);
	private double maxMetersPerSecond=0.;
	
	public ChartData(TrainingActivity activity, long smooth) {
		TrainingCenterDatabaseT tcxData = activity.getTrainingCenterDatabase();
		ActivityListT activityList = tcxData.getActivities();
		if (activityList.getActivity().size() != 1) {
			String msg = String.format(Lang.get().text(Lang.NOT_ONE_ACTIVICTY), activityList.getActivity().size());
			throw new RuntimeException(msg);
		}
		ActivityT tcxActivity = activityList.getActivity().get(0);
		List<ActivityLapT> laps = tcxActivity.getLap();
		long startTime = 0;
		for(ActivityLapT lap : laps) {
			double lapSecsPerKm = lap.getTotalTimeSeconds() / lap.getDistanceMeters() * 1000.;
			List<TrackT> tracks = lap.getTrack();
			for(TrackT track : tracks) {
				List<TrackpointT> trackpoints = track.getTrackpoint();
				for(TrackpointT tp : trackpoints) {
					GregorianCalendar t = tp.getTime().toGregorianCalendar(null, null, null);
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
					HeartRateInBeatsPerMinuteT hf = tp.getHeartRateBpm();
					if (hf != null) {
						short h = hf.getValue();
						if (minHeartRate > h) { 
							minHeartRate = h;							
						}
						if (maxHeartRate < h) { 
							maxHeartRate = h;							
						}
						hasHeartRateData = true;
						item.setHeartrate((double)h);
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

	public short getMinHeartRate() {
		return minHeartRate;
	}

	public short getMaxHeartRate() {
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
