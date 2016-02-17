package de.acwhadk.rz.filemyrun.controller;

public class ChartDataItem {

	private long time;
	private Double distance;
	private Double heartrate;
	private Double altitude;
	private Double secondsPerKm;
	private Double splitSecondsPerKm;
	
	public ChartDataItem(long time) {
		this.time = time;
	}

	public Double getDistance() {
		return distance;
	}

	public void setDistance(Double distance) {
		this.distance = distance;
	}

	public Double getHeartrate() {
		return heartrate;
	}

	public void setHeartrate(Double heartrate) {
		this.heartrate = heartrate;
	}

	public Double getAltitude() {
		return altitude;
	}

	public void setAltitude(Double altitude) {
		this.altitude = altitude;
	}

	public Double getSecondsPerKm() {
		return secondsPerKm;
	}

	public void setSecondsPerKm(Double secondsPerKm) {
		this.secondsPerKm = secondsPerKm;
	}

	public Double getSplitSecondsPerKm() {
		return splitSecondsPerKm;
	}

	public void setSplitSecondsPerKm(Double splitSecondsPerKm) {
		this.splitSecondsPerKm = splitSecondsPerKm;
	}

	public long getTime() {
		return time;
	}
	
	
}
