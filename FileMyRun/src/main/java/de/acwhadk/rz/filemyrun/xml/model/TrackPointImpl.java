package de.acwhadk.rz.filemyrun.xml.model;

import java.util.Calendar;

import javax.xml.datatype.XMLGregorianCalendar;

import com.garmin.tcdbv2.HeartRateInBeatsPerMinuteT;
import com.garmin.tcdbv2.TrackpointT;

import de.acwhadk.rz.filemyrun.core.model.Position;
import de.acwhadk.rz.filemyrun.core.model.TrackPoint;

public class TrackPointImpl implements TrackPoint {

	private TrackpointT trackpoint;
	
	public TrackPointImpl(TrackpointT trackpoint) {
		this.trackpoint = trackpoint;
	}

	@Override
	public Calendar getTime() {
		XMLGregorianCalendar cal = trackpoint.getTime();
		return cal.toGregorianCalendar();
	}

	@Override
	public void setTime(Calendar time) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Position getPosition() {
		if (trackpoint.getPosition() == null) {
			return null;
		}
		return new PositionImpl(trackpoint.getPosition());
	}

	@Override
	public void setPosition(Position position) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Double getAltitudeMeters() {
		return trackpoint.getAltitudeMeters();
	}

	@Override
	public void setAltitudeMeters(Double altitudeMeters) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Double getDistanceMeters() {
		return trackpoint.getDistanceMeters();
	}

	@Override
	public void setDistanceMeters(Double distanceMeters) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Integer getHeartRateBpm() {
		HeartRateInBeatsPerMinuteT hr = trackpoint.getHeartRateBpm();
		if (hr == null) {
			return null;
		}
		return (int) hr.getValue();
	}

	@Override
	public void setHeartRateBpm(Integer heartRateBpm) {
		throw new UnsupportedOperationException();
	}

}
