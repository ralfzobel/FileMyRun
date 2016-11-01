package de.acwhadk.rz.filemyrun.jpa.model;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import de.acwhadk.rz.filemyrun.core.model.Activity;
import de.acwhadk.rz.filemyrun.core.model.EquipmentMan;
import de.acwhadk.rz.filemyrun.core.model.Lap;
import de.acwhadk.rz.filemyrun.core.model.ObjectFactory;
import de.acwhadk.rz.filemyrun.core.model.Position;
import de.acwhadk.rz.filemyrun.core.model.Track;
import de.acwhadk.rz.filemyrun.core.model.TrackPoint;
import de.acwhadk.rz.filemyrun.core.model.TrainingFile;
import de.acwhadk.rz.filemyrun.core.model.TrainingFileMan;

public class ObjectFactoryImpl implements ObjectFactory {

	private EntityManagerFactory entityManagerFactory;
	private EntityManager entityManager;
	
	private TrainingFileManImpl trainingFileManImpl;
	private EquipmentManImpl equipmentManImpl;
	public ObjectFactoryImpl() {
		super();
		entityManagerFactory = Persistence.createEntityManagerFactory("ActivityDB");
		entityManager = entityManagerFactory.createEntityManager();
	}

	@Override
	public TrainingFileMan createTrainingFileMan() {
		try {
			if (trainingFileManImpl == null) {
				trainingFileManImpl = new TrainingFileManImpl(this);
			}
			return trainingFileManImpl;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	@Override
	public EquipmentMan createEquipmentMan() {
		if (equipmentManImpl == null) {
			equipmentManImpl = new EquipmentManImpl();
		}
		return equipmentManImpl;
	}
	
	@Override
	public Activity createActivity(TrainingFile trainingFile) {
		try {
			return new ActivityImpl(trainingFile, this);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public Lap createLap() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Track createTrack() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TrackPoint createTrackPoint() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Position createPosition() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void close() {
		entityManager.close();
		entityManagerFactory.close();
	}

	public EntityManager getEntityManager() {
		return entityManager;
	}

}
