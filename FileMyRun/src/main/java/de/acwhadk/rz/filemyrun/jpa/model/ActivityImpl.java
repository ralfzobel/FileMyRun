package de.acwhadk.rz.filemyrun.jpa.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;

import de.acwhadk.rz.filemyrun.core.model.Activity;
import de.acwhadk.rz.filemyrun.core.model.Lap;
import de.acwhadk.rz.filemyrun.core.model.TrackPoint;
import de.acwhadk.rz.filemyrun.core.model.TrainingFile;
import de.acwhadk.rz.filemyrun.jpa.data.ActivityData;

public class ActivityImpl implements Activity {

	private ActivityData activityData;
	private ObjectFactoryImpl objectFactoryImpl;
	
	public ActivityImpl(TrainingFile trainingFile, ObjectFactoryImpl objectFactory) {
		this.objectFactoryImpl = objectFactory;
		EntityManager em = objectFactoryImpl.getEntityManager();
		activityData = em.find(ActivityData.class, Integer.parseInt(trainingFile.getTrainingFile()));
	}

	@Override
	public long getId() {
		return activityData.getId();
	}

	@Override
	public String getName() {
		return activityData.getActivity().getName();
	}

	@Override
	public void setName(String name) {
		activityData.getActivity().setName(name);
	}

	@Override
	public String getDescription() {
		return activityData.getDescription();
	}

	@Override
	public void setDescription(String description) {
		activityData.setDescription(description);
	}

	@Override
	public Date getDate() {
		return activityData.getActivity().getTime();
	}

	@Override
	public String getType() {
		return activityData.getType();
	}

	@Override
	public void setType(String type) {
		activityData.setType(type);
	}

	@Override
	public Double getDistance() {
		return activityData.getActivity().getDistance();
	}

	@Override
	public void setDistance(Double dist) {
		activityData.getActivity().setDistance(dist);
	}

	@Override
	public void setTotalTime(long t) {
		activityData.setTotalTime(t);
	}

	@Override
	public void save() throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<Lap> getLaps() {
		List<Lap> laps = new ArrayList<>();
		for(de.acwhadk.rz.filemyrun.jpa.data.Lap lap : activityData.getLaps()) {
			LapImpl l = new LapImpl(lap);
			laps.add(l);
		}
		return laps;
	}

	@Override
	public double getTrackDistance() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public long getTotalTime() {
		return activityData.getTotalTime();
	}

	@Override
	public double getPace() {
		return activityData.getPace();
	}

	@Override
	public Integer getAverageHeartRate() {
		return activityData.getAverageHeartRate();
	}

	@Override
	public Integer getMaximumHeartRate() {
		return activityData.getMaximumHeartRate();
	}

	@Override
	public Integer getMaximumAltitude() {
		return activityData.getMaximumAltitude();
	}

	@Override
	public Integer getMinimumAltitude() {
		return activityData.getMinimumAltitude();
	}

	@Override
	public Integer getAscent() {
		return activityData.getAscent();
	}

	@Override
	public Integer getDescent() {
		return activityData.getDescent();
	}

	@Override
	public int getCalories() {
		return activityData.getCalories();
	}

	@Override
	public void deleteToHere(TrackPoint tp) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deleteToEnd(TrackPoint tp) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void joinLap(int i) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void splitLapHalfTime(int i) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void splitLapHalfDist(int i) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void splitLapAtDist(int i, double atDist) {
		// TODO Auto-generated method stub
		
	}

}
