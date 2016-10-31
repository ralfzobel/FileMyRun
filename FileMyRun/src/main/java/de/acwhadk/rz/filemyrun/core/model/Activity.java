package de.acwhadk.rz.filemyrun.core.model;

import java.util.Date;
import java.util.List;
import java.util.Map;

import de.acwhadk.rz.filemyrun.gui.SplitTime;

public interface Activity {

	String getName();
	void setName(String name);

	String getDescription();
	void setDescription(String description);

	Date getDate();

	String getType();
	void setType(String type);

	Double getDistance() throws Exception;
	void setDistance(Double dist);

	double getTrackDistance() throws Exception;

	long getTotalTime();
	void setTotalTime(long t);

	double getPace() throws Exception;

	Long getAverageHeartRate();

	Long getMaximumHeartRate();

	Long getMaximumAltitude();

	Long getMinimumAltitude();

	Long getAscent();

	Long getDescent();

	int getCalories();

	void save() throws Exception;

	Map<String, List<TrackPoint>> getTrackPoints();

	List<SplitTime> getSplitTimes(boolean snapIn);
	
	void deleteToHere(TrackPoint tp) throws Exception;

	void deleteToEnd(TrackPoint tp) throws Exception;

	void joinLap(int i);

	void splitLapHalfTime(int i);

	void splitLapHalfDist(int i);

	void splitLapAtDist(int i, double atDist);

	List<Lap> getLaps();
}