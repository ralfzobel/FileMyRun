package de.acwhadk.rz.filemyrun.jpa.model;

import java.io.IOException;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.xml.bind.JAXBException;

import de.acwhadk.rz.filemyrun.core.model.TrainingFile;
import de.acwhadk.rz.filemyrun.core.model.TrainingFileMan;

public class TrainingFileManImpl implements TrainingFileMan {

	private ObjectFactoryImpl objectFactory;
	
	public TrainingFileManImpl(ObjectFactoryImpl objectFactory) {
		this.objectFactory = objectFactory;
	}

	@Override
	public SortedMap<Date, TrainingFile> getTrainingFiles() {
		
		SortedMap<Date, de.acwhadk.rz.filemyrun.core.model.TrainingFile> map = new TreeMap<>(new Comparator<Date>() {
			@Override
			public int compare(Date o1, Date o2) {				
				return o2.compareTo(o1);
			}
		});
		
		EntityManager em = objectFactory.getEntityManager();
		TypedQuery<de.acwhadk.rz.filemyrun.jpa.data.Activity> query = 
				em.createQuery("from Activity", de.acwhadk.rz.filemyrun.jpa.data.Activity.class);
		List<de.acwhadk.rz.filemyrun.jpa.data.Activity> activities = query.getResultList();
		for(de.acwhadk.rz.filemyrun.jpa.data.Activity a : activities) {
			TrainingFile tf = new TrainingFileImpl(a);
			map.put(tf.getTime(), tf);
		}
		return map;
	}

	@Override
	public TrainingFile getTrainingFile(Date date) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void save() throws IOException, JAXBException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deleteFile(TrainingFile trainingFileImplXml) {
		// TODO Auto-generated method stub
		
	}

}
