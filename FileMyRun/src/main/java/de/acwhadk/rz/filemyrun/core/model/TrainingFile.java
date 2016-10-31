package de.acwhadk.rz.filemyrun.core.model;

import java.util.Date;

public interface TrainingFile {

	String getName();

	void setName(String name);

	Date getTime();

	String getTrainingFile();

	Double getDistance();

	void setDistance(Double distance);

	void setTime(Date time);

	void setTrainingFile(String trainingFile);

	String getType();

	void setType(String type);

	String getComment();

	void setComment(String comment);

}