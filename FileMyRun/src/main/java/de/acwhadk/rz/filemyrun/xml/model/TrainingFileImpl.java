package de.acwhadk.rz.filemyrun.xml.model;

import java.util.Date;

import de.acwhadk.rz.filemyrun.xml.file.TrainingFile;

public class TrainingFileImpl implements de.acwhadk.rz.filemyrun.core.model.TrainingFile {

	private TrainingFile trainingFile;
	
	public TrainingFileImpl(TrainingFile trainingFile) {
		super();
		this.trainingFile = trainingFile;
	}

	@Override
	public String getName() {
		return trainingFile.getName();
	}

	@Override
	public void setName(String name) {
		trainingFile.setName(name);
	}

	@Override
	public Date getTime() {
		return trainingFile.getTime();
	}

	@Override
	public void setTime(Date time) {
		trainingFile.setTime(time);
	}

	@Override
	public String getTrainingFile() {
		return trainingFile.getTrainingFile();
	}

	@Override
	public void setTrainingFile(String trainingFileName) {
		trainingFile.setTrainingFile(trainingFileName);
	}

	@Override
	public Double getDistance() {
		return trainingFile.getDistance();
	}

	@Override
	public void setDistance(Double distance) {
		trainingFile.setDistance(distance);
	}

	@Override
	public String getType() {
		return trainingFile.getType();
	}

	@Override
	public void setType(String type) {
		trainingFile.setType(type);
	}

	@Override
	public String getComment() {
		return trainingFile.getComment();
	}

	@Override
	public void setComment(String comment) {
		trainingFile.setComment(comment);
	}

}
