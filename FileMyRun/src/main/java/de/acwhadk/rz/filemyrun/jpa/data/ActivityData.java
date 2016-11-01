package de.acwhadk.rz.filemyrun.jpa.data;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.OrderBy;

@Entity
public class ActivityData {

	@Id
	@GeneratedValue(strategy=GenerationType.TABLE)
	private int id;

	@OneToOne(mappedBy="activityData")
	private Activity activity;
	
	private String description;
	private String type;
	private Long totalTime;
	private Double pace;
	private Integer averageHeartRate;
	private Integer maximumHeartRate;
	private Integer maximumAltitude;
	private Integer minimumAltitude;
	private Integer ascent;
	private Integer descent;
	private Integer calories;
	
	@OneToMany(mappedBy="activity")
	@OrderBy(value="seqNum")
	private List<Lap> laps;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Long getTotalTime() {
		return totalTime;
	}

	public void setTotalTime(Long totalTime) {
		this.totalTime = totalTime;
	}

	public Double getPace() {
		return pace;
	}

	public void setPace(Double pace) {
		this.pace = pace;
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

	public Integer getMaximumAltitude() {
		return maximumAltitude;
	}

	public void setMaximumAltitude(Integer maximumAltitude) {
		this.maximumAltitude = maximumAltitude;
	}

	public Integer getMinimumAltitude() {
		return minimumAltitude;
	}

	public void setMinimumAltitude(Integer minimumAltitude) {
		this.minimumAltitude = minimumAltitude;
	}

	public Integer getAscent() {
		return ascent;
	}

	public void setAscent(Integer ascent) {
		this.ascent = ascent;
	}

	public Integer getDescent() {
		return descent;
	}

	public void setDescent(Integer descent) {
		this.descent = descent;
	}

	public Integer getCalories() {
		return calories;
	}

	public void setCalories(Integer calories) {
		this.calories = calories;
	}

	public List<Lap> getLaps() {
		return laps;
	}

	public void setLaps(List<Lap> laps) {
		this.laps = laps;
	}

	public Activity getActivity() {
		return activity;
	}

	public void setActivity(Activity activity) {
		this.activity = activity;
	}

}
