package de.acwhadk.rz.filemyrun.core.model;

import java.util.Date;
import java.util.List;

public interface Activity {

	String getName();
	void setName(String name);

	String getDescription();
	void setDescription(String description);

	Date getDate();

	String getType();
	void setType(String type);

	Double getDistance();
	void setDistance(Double dist);

	double getTrackDistance();

	long getTotalTime();
	void setTotalTime(long t);

	double getPace();

	Integer getAverageHeartRate();

	Integer getMaximumHeartRate();

	Integer getMaximumAltitude();

	Integer getMinimumAltitude();

	Integer getAscent();

	Integer getDescent();

	int getCalories();

	void save() throws Exception;

	void deleteToHere(TrackPoint tp);

	void deleteToEnd(TrackPoint tp);

	void joinLap(int i);

	void splitLapHalfTime(int i);

	void splitLapHalfDist(int i);

	void splitLapAtDist(int i, double atDist);

	List<Lap> getLaps();
}