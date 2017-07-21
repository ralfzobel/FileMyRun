package de.acwhadk.rz.filemyrun.jpa.model;

import java.util.Date;

import de.acwhadk.rz.filemyrun.core.model.TrainingFile;
import de.acwhadk.rz.filemyrun.jpa.data.Activity;

public class TrainingFileImpl implements TrainingFile {

	private Activity activity;
	
	public TrainingFileImpl(Activity activity) {
		this.activity = activity;
	}

	@Override
	public String getName() {
		return activity.getName();
	}

	@Override
	public void setName(String name) {
		activity.setName(name);
//		throw new UnsupportedOperationException();
	}

	@Override
	public Date getTime() {
		return activity.getTime();
	}

	@Override
	public void setTime(Date time) {
		throw new UnsupportedOperationException();
	}

	@Override
	public String getTrainingFile() {
		return Integer.toString(activity.getActivityData().getId());
	}

	@Override
	public void setTrainingFile(String trainingFile) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Double getDistance() {
		return activity.getDistance();
	}

	@Override
	public void setDistance(Double distance) {
		activity.setDistance(distance);
//		throw new UnsupportedOperationException();
	}

	@Override
	public String getType() {
		return activity.getActivityData().getType();
	}

	@Override
	public void setType(String type) {
		activity.getActivityData().setType(type);
//		throw new UnsupportedOperationException();
	}

	@Override
	public String getComment() {
		return activity.getActivityData().getDescription();
	}

	@Override
	public void setComment(String comment) {
		activity.getActivityData().setDescription(comment);
//		throw new UnsupportedOperationException();
	}

}
