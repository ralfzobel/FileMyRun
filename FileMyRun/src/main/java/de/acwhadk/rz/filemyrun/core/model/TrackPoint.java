package de.acwhadk.rz.filemyrun.core.model;

import java.util.Calendar;

public interface TrackPoint {

	public Calendar getTime();
	public void setTime(Calendar time);
	
	public Position getPosition();
	public void setPosition(Position position);
	
	public Double getAltitudeMeters();
	public void setAltitudeMeters(Double altitudeMeters);
	
	public Double getDistanceMeters();
	public void setDistanceMeters(Double distanceMeters);
	
	public Integer getHeartRateBpm();
	public void setHeartRateBpm(Integer heartRateBpm);    
}
