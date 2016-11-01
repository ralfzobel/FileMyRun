package de.acwhadk.rz.filemyrun.jpa.data;

import java.util.Calendar;

import javax.persistence.Embeddable;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Embeddable
public class TrackPoint {

	private int seqNum;
	
	private double latitude;
	private double longitude;
	private Double altitude;
	private Double distance;
	private Integer heartrate;
	@Temporal(TemporalType.TIMESTAMP) 
	private Calendar time;
	
	public int getSeqNum() {
		return seqNum;
	}
	public void setSeqNum(int seqNum) {
		this.seqNum = seqNum;
	}
	public double getLatitude() {
		return latitude;
	}
	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}
	public double getLongitude() {
		return longitude;
	}
	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}
	public Double getAltitude() {
		return altitude;
	}
	public void setAltitude(Double altitude) {
		this.altitude = altitude;
	}
	public Double getDistance() {
		return distance;
	}
	public void setDistance(Double distance) {
		this.distance = distance;
	}
	public Integer getHeartrate() {
		return heartrate;
	}
	public void setHeartrate(Integer heartrate) {
		this.heartrate = heartrate;
	}
	public Calendar getTime() {
		return time;
	}
	public void setTime(Calendar time) {
		this.time = time;
	}	

}
