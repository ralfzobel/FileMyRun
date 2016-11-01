package de.acwhadk.rz.filemyrun.core.model;

import java.util.Date;

public interface TrainingFile {

	String getName();
	void setName(String name);

	Date getTime();
	void setTime(Date time);

	String getTrainingFile();
	void setTrainingFile(String trainingFile);

	Double getDistance();
	void setDistance(Double distance);

	String getType();
	void setType(String type);

	String getComment();
	void setComment(String comment);
}