package de.acwhadk.rz.filemyrun.xml.model;

import com.garmin.tcdbv2.PositionT;

import de.acwhadk.rz.filemyrun.core.model.Position;

public class PositionImpl implements Position {

	private PositionT position;
	
	public PositionImpl(PositionT position) {
		this.position = position;
	}

	@Override
	public double getLongitude() {
		return position.getLongitudeDegrees();
	}

	@Override
	public void setLongitude(double longitude) {
		throw new UnsupportedOperationException();
	}

	@Override
	public double getLatitude() {
		return position.getLatitudeDegrees();
	}

	@Override
	public void setLatitude(double latitude) {
		throw new UnsupportedOperationException();
	}

}
