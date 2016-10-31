package de.acwhadk.rz.filemyrun.xml.model;

import java.util.Calendar;

import com.garmin.tcdbv2.ActivityLapT;
import com.garmin.tcdbv2.HeartRateInBeatsPerMinuteT;
import com.garmin.tcdbv2.TrackT;

import de.acwhadk.rz.filemyrun.core.model.Lap;
import de.acwhadk.rz.filemyrun.core.model.Track;

public class LapImpl implements Lap {

	private ActivityLapT lap;
	
	public LapImpl(ActivityLapT lap) {
		super();
		this.lap = lap;
	}

	@Override
	public double getTotalTimeSeconds() {
		return lap.getTotalTimeSeconds();
	}

	@Override
	public void setTotalTimeSeconds(double totalTimeSeconds) {
		lap.setTotalTimeSeconds(totalTimeSeconds);		
	}

	@Override
	public double getDistanceMeters() {
		return lap.getDistanceMeters();
	}

	@Override
	public void setDistanceMeters(double distanceMeters) {
		lap.setDistanceMeters(distanceMeters);		
	}

	@Override
	public Integer getCalories() {
		return lap.getCalories();
	}

	@Override
	public void setCalories(Integer calories) {
		lap.setCalories(calories);
	}

	@Override
	public Integer getAverageHeartRateBpm() {
		HeartRateInBeatsPerMinuteT hr = lap.getAverageHeartRateBpm();
		if (hr == null) {
			return null;
		}
		return (int) hr.getValue();
	}

	@Override
	public void setAverageHeartRateBpm(Integer averageHeartRateBpm) {
		HeartRateInBeatsPerMinuteT hr = new HeartRateInBeatsPerMinuteT(); 
		hr.setValue((short) averageHeartRateBpm.intValue());
		lap.setAverageHeartRateBpm(hr);
	}

	@Override
	public Integer getMaximumHeartRateBpm() {
		HeartRateInBeatsPerMinuteT hr = lap.getMaximumHeartRateBpm();
		if (hr == null) {
			return null;
		}
		return (int) hr.getValue();
	}

	@Override
	public void setMaximumHeartRateBpm(Integer maximumHeartRateBpm) {
		HeartRateInBeatsPerMinuteT hr = new HeartRateInBeatsPerMinuteT(); 
		hr.setValue((short) maximumHeartRateBpm.intValue());
		lap.setMaximumHeartRateBpm(hr);
	}

	@Override
	public Track getTrack() {
		if (lap.getTrack() == null || lap.getTrack().isEmpty()) {
			return null;
		}
		TrackT track = lap.getTrack().get(0);
		return new TrackImpl(track);
	}

	@Override
	public void setTrack(Track track) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Calendar getStartTime() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setStartTime(Calendar startTime) {
		// TODO Auto-generated method stub
		
	}

}
