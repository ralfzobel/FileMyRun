package de.acwhadk.rz.filemyrun.jpa.data;

import java.util.Calendar;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
public class Lap {

	@Id
	@GeneratedValue(strategy=GenerationType.TABLE)
	private int id;
	
	private int seqNum;
	
	@ManyToOne
	@JoinColumn
	private ActivityData activity;
	
	@Temporal(TemporalType.TIMESTAMP) 
	private Calendar startTime;
	
	private Double totalTime;
	private Double distance;
	private Integer averageHeartRate;
	private Integer maximumHeartRate;
	private Integer calories;

	@OneToOne
	@JoinColumn
	private Track track;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getSeqNum() {
		return seqNum;
	}

	public void setSeqNum(int seqNum) {
		this.seqNum = seqNum;
	}

	public ActivityData getActivity() {
		return activity;
	}

	public void setActivity(ActivityData activity) {
		this.activity = activity;
	}

	public Calendar getStartTime() {
		return startTime;
	}

	public void setStartTime(Calendar startTime) {
		this.startTime = startTime;
	}

	public Double getTotalTime() {
		return totalTime;
	}

	public void setTotalTime(Double totalTime) {
		this.totalTime = totalTime;
	}

	public Double getDistance() {
		return distance;
	}

	public void setDistance(Double distance) {
		this.distance = distance;
	}

	public Integer getAverageHeartRate() {
		return averageHeartRate;
	}

	public void setAverageHeartRate(Integer averageHeartRate) {
		this.averageHeartRate = averageHeartRate;
	}

	public Integer getMaximumHeartRate() {
		return maximumHeartRate;
	}

	public void setMaximumHeartRate(Integer maximumHeartRate) {
		this.maximumHeartRate = maximumHeartRate;
	}

	public Integer getCalories() {
		return calories;
	}

	public void setCalories(Integer calories) {
		this.calories = calories;
	}

	public Track getTrack() {
		return track;
	}

	public void setTrack(Track track) {
		this.track = track;
	}

}
