package de.acwhadk.rz.filemyrun.jpa.model;

import java.util.Calendar;

import de.acwhadk.rz.filemyrun.core.model.Lap;
import de.acwhadk.rz.filemyrun.core.model.Track;

public class LapImpl implements Lap {

	private de.acwhadk.rz.filemyrun.jpa.data.Lap lap;
		
	public LapImpl(de.acwhadk.rz.filemyrun.jpa.data.Lap lap) {
		super();
		this.lap = lap;
	}

	@Override
	public double getTotalTimeSeconds() {
		return lap.getTotalTime();
	}

	@Override
	public void setTotalTimeSeconds(double totalTimeSeconds) {
		throw new UnsupportedOperationException();		
	}

	@Override
	public double getDistanceMeters() {
		return lap.getDistance();
	}

	@Override
	public void setDistanceMeters(double distanceMeters) {
		throw new UnsupportedOperationException();		
	}

	@Override
	public Integer getCalories() {
		return lap.getCalories();
	}

	@Override
	public void setCalories(Integer calories) {
		throw new UnsupportedOperationException();		
	}

	@Override
	public Integer getAverageHeartRateBpm() {
		return lap.getAverageHeartRate();
	}

	@Override
	public void setAverageHeartRateBpm(Integer averageHeartRateBpm) {
		throw new UnsupportedOperationException();		
	}

	@Override
	public Integer getMaximumHeartRateBpm() {
		return lap.getMaximumHeartRate();
	}

	@Override
	public void setMaximumHeartRateBpm(Integer maximumHeartRateBpm) {
		throw new UnsupportedOperationException();		
	}

	@Override
	public Track getTrack() {
		if (lap.getTrack() == null) {
			return null;
		}
		return new TrackImpl(lap.getTrack());
	}

	@Override
	public void setTrack(Track track) {
		throw new UnsupportedOperationException();		
	}

	@Override
	public Calendar getStartTime() {
		return lap.getStartTime();
	}

	@Override
	public void setStartTime(Calendar startTime) {
		throw new UnsupportedOperationException();		
	}

}
