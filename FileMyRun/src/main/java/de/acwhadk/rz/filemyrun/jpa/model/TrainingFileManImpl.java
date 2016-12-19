package de.acwhadk.rz.filemyrun.jpa.model;

import java.io.IOException;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.ParameterExpression;
import javax.persistence.criteria.Root;
import javax.xml.bind.JAXBException;

import de.acwhadk.rz.filemyrun.core.model.TrainingFile;
import de.acwhadk.rz.filemyrun.core.model.TrainingFileMan;
import de.acwhadk.rz.filemyrun.jpa.data.Activity;

public class TrainingFileManImpl implements TrainingFileMan {

	private ObjectFactoryImpl objectFactory;
	
	public TrainingFileManImpl(ObjectFactoryImpl objectFactory) {
		this.objectFactory = objectFactory;
	}

	@Override
	public SortedMap<Date, TrainingFile> getTrainingFiles() {
		
		SortedMap<Date, TrainingFile> map = new TreeMap<>(new Comparator<Date>() {
			@Override
			public int compare(Date o1, Date o2) {				
				return o2.compareTo(o1);
			}
		});
		
		List<Activity> activities = getActivities();
		for(Activity a : activities) {
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

	public List<Activity> getActivities() {
		EntityManager em = objectFactory.getEntityManager();
		TypedQuery<Activity> query = em.createQuery("from Activity", Activity.class);
		return query.getResultList();
	}
	
}
