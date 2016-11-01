package de.acwhadk.rz.filemyrun.jpa.model;

import de.acwhadk.rz.filemyrun.core.model.Position;

public class PositionImpl implements Position {

	private double latitude;
	private double longitude;
	
	public PositionImpl(double latitude, double longitude) {
		super();
		this.latitude = latitude;
		this.longitude = longitude;
	}

	@Override
	public double getLatitude() {
		return latitude;
	}

	@Override
	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	@Override
	public double getLongitude() {
		return longitude;
	}

	@Override
	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	
}
