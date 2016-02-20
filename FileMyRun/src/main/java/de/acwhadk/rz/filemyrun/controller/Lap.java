package de.acwhadk.rz.filemyrun.controller;

/**
 * A bean holding the data needed for display of one row in the split time table.
 * 
 * @author Ralf
 *
 */
public class Lap {
	
	private String round;
	private String time;
	private Double timeInSeconds;
	private String distance;
	private Double distanceInMeters;
	private String pace;
	private Double paceInSeconds;
	private String totalTime;
	private String averageHeartRate;
	private String maximumHeartRate;
	private String ascent;
	private String descent;
	private String totalDistance;

	public Lap() {
		this.round = "";
		this.time = "";
		this.distance = "";
		this.pace = "";
		this.totalTime = "";
		this.averageHeartRate = "";
		this.maximumHeartRate = "";
		this.ascent = "";
		this.descent = "";
	}
	public String getRound() {
		return round;
	}
	public void setRound(String round) {
		this.round = round;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public String getTotalDistance() {
		return totalDistance;
	}
	public void setTotalDistance(String totalDistance) {
		this.totalDistance = totalDistance;
	}
	public String getDistance() {
		return distance;
	}
	public void setDistance(String distance) {
		this.distance = distance;
	}
	public String getPace() {
		return pace;
	}
	public void setPace(String pace) {
		this.pace = pace;
	}
	public String getTotalTime() {
		return totalTime;
	}
	public void setTotalTime(String totalTime) {
		this.totalTime = totalTime;
	}
	public String getAverageHeartRate() {
		return averageHeartRate;
	}
	public void setAverageHeartRate(String averageHeartRate) {
		this.averageHeartRate = averageHeartRate;
	}
	public String getMaximumHeartRate() {
		return maximumHeartRate;
	}
	public void setMaximumHeartRate(String maximumHeartRate) {
		this.maximumHeartRate = maximumHeartRate;
	}
	public String getAscent() {
		return ascent;
	}
	public void setAscent(String ascent) {
		this.ascent = ascent;
	}
	public String getDescent() {
		return descent;
	}
	public void setDescent(String descent) {
		this.descent = descent;
	}
	public Double getTimeInSeconds() {
		return timeInSeconds;
	}
	public void setTimeInSeconds(Double timeInSeconds) {
		this.timeInSeconds = timeInSeconds;
	}
	public Double getDistanceInMeters() {
		return distanceInMeters;
	}
	public void setDistanceInMeters(Double distanceInMeters) {
		this.distanceInMeters = distanceInMeters;
	}
	public Double getPaceInSeconds() {
		return paceInSeconds;
	}
	public void setPaceInSeconds(Double paceInSeconds) {
		this.paceInSeconds = paceInSeconds;
	}		
}
