package de.acwhadk.rz.filemyrun.core.model;

import java.util.Calendar;

public interface Lap {

	double getTotalTimeSeconds();
	void setTotalTimeSeconds(double totalTimeSeconds);

	double getDistanceMeters();
	void setDistanceMeters(double distanceMeters);

	Integer getCalories();
	void setCalories(Integer calories);

	Integer getAverageHeartRateBpm();
	void setAverageHeartRateBpm(Integer averageHeartRateBpm);

	Integer getMaximumHeartRateBpm();
	void setMaximumHeartRateBpm(Integer maximumHeartRateBpm);

	Track getTrack();
	void setTrack(Track track);

	Calendar getStartTime();
	void setStartTime(Calendar startTime);

}