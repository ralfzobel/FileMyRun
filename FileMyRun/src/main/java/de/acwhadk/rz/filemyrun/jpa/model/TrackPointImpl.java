package de.acwhadk.rz.filemyrun.jpa.model;

import java.util.Calendar;

import de.acwhadk.rz.filemyrun.core.model.Position;
import de.acwhadk.rz.filemyrun.core.model.TrackPoint;

public class TrackPointImpl implements TrackPoint {

	private de.acwhadk.rz.filemyrun.jpa.data.TrackPoint tp;
	
	public TrackPointImpl(de.acwhadk.rz.filemyrun.jpa.data.TrackPoint tp) {
		this.tp = tp;
	}

	@Override
	public Calendar getTime() {
		return tp.getTime();
	}

	@Override
	public void setTime(Calendar time) {
		tp.setTime(time);
	}

	@Override
	public Position getPosition() {
		return new PositionImpl(tp.getLatitude(), tp.getLongitude());
	}

	@Override
	public void setPosition(Position position) {
		tp.setLatitude(position.getLatitude());
		tp.setLongitude(position.getLongitude());
	}

	@Override
	public Double getAltitudeMeters() {
		return tp.getAltitude();
	}

	@Override
	public void setAltitudeMeters(Double altitudeMeters) {
		tp.setAltitude(altitudeMeters);
	}

	@Override
	public Double getDistanceMeters() {
		return tp.getDistance();
	}

	@Override
	public void setDistanceMeters(Double distanceMeters) {
		tp.setDistance(distanceMeters);
	}

	@Override
	public Integer getHeartRateBpm() {
		return tp.getHeartrate();
	}

	@Override
	public void setHeartRateBpm(Integer heartRateBpm) {
		tp.setHeartrate(heartRateBpm);
	}

}
