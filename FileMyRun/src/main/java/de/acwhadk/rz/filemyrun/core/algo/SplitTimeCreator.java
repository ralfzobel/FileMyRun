package de.acwhadk.rz.filemyrun.core.algo;

import java.util.ArrayList;
import java.util.List;

import de.acwhadk.rz.filemyrun.core.model.Activity;
import de.acwhadk.rz.filemyrun.core.model.Lap;
import de.acwhadk.rz.filemyrun.core.setup.Const;

public class SplitTimeCreator {

	public static List<SplitTime> getSplitTimes(Activity activity, boolean snapIn) {
		List<SplitTime> laplist = new ArrayList<>();
		List<Lap> activityLaps = activity.getLaps();
		double totalSeconds = 0.0;
		double totalDistance = 0.0;
		int round = 1;
		for(Lap activityLap : activityLaps) {
			SplitTime splitTime = new SplitTime();
			splitTime.setRound(Integer.toString(round));
			
			double seconds = activityLap.getTotalTimeSeconds();
			splitTime.setTime(Formatter.formatSeconds(seconds));
			splitTime.setTimeInSeconds(seconds);

			totalSeconds += seconds;
			splitTime.setTotalTime(Formatter.formatSeconds(totalSeconds));
			
			double distance = activityLap.getDistanceMeters()/1000.0;
			if (snapIn) {
				long hektoMeters = Math.round(distance*10);
				distance = hektoMeters / 10.0;
			}
			splitTime.setDistance(Formatter.formatDistance(distance));
			splitTime.setDistanceInMeters(distance);
			
			totalDistance += distance;
			splitTime.setTotalDistance(Formatter.formatDistance(totalDistance));
			
			double pace = seconds / distance;
			splitTime.setPace(Formatter.formatPace(pace));
			splitTime.setPaceInSeconds(pace);

			if (activityLap.getAverageHeartRateBpm() != null) {
				splitTime.setAverageHeartRate(Integer.toString(activityLap.getAverageHeartRateBpm()));
				splitTime.setMaximumHeartRate(Integer.toString(activityLap.getMaximumHeartRateBpm()));
			} else {
				splitTime.setAverageHeartRate(Const.EMPTY);
				splitTime.setMaximumHeartRate(Const.EMPTY);
			}
			
			ArrayList<Lap> lst = new ArrayList<>();
			lst.add(activityLap);
			Altitude altitude = AltitudeCreator.calculateAltitude(lst);
			if (altitude != null) {
				splitTime.setAscent(Long.toString(altitude.getAscent()));
				splitTime.setDescent(Long.toString(altitude.getDescent()));
			} else {
				splitTime.setAscent(Const.EMPTY);
				splitTime.setDescent(Const.EMPTY);
			}
			
			laplist.add(splitTime);
			++round;
		}
		return laplist;
	}
	
}
