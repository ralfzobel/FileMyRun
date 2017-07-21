package de.acwhadk.rz.filemyrun.jpa.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.apache.log4j.Logger;

import de.acwhadk.rz.filemyrun.core.model.EquipmentMan;
import de.acwhadk.rz.filemyrun.jpa.data.ActivityData;
import de.acwhadk.rz.filemyrun.jpa.data.EquipmentItem;
import de.acwhadk.rz.filemyrun.jpa.data.EquipmentType;
import de.acwhadk.rz.filemyrun.jpa.data.EquipmentUsedEntry;

public class EquipmentManImpl implements EquipmentMan {

	final static Logger logger = Logger.getLogger(EquipmentManImpl.class);
	
	private ObjectFactoryImpl objectFactory;
	public EquipmentManImpl(ObjectFactoryImpl objectFactory) {
		this.objectFactory = objectFactory;
	}
	
	@Override
	public List<String> getEquipmentTypes() {
		EntityManager em = objectFactory.getEntityManager();
		TypedQuery<EquipmentType> query = em.createQuery("from EquipmentType", EquipmentType.class);
		ArrayList<String> types = new ArrayList<>();
		for(EquipmentType t : query.getResultList()) {
			types.add(t.getType());
		}
		return types;
	}

	@Override
	public Map<Long, String> getEquipmentItems(String type) {
		EntityManager em = objectFactory.getEntityManager();
		TypedQuery<EquipmentItem> query = em.createQuery("from EquipmentItem", EquipmentItem.class);
		Map<Long, String> items = new HashMap<>();
		for(EquipmentItem i : query.getResultList()) {
			if (i.getType().getType().equals(type)) {
				items.put((long) i.getId(), i.getName());
			}
		}
		return items;
	}

	@Override
	public void setEquipmentItems(String type, Map<Long, String> itemMap) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public long getEquipmentUsedId(long activity, String type) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String getEquipmentUsedName(long activity, String type) {
		List<EquipmentUsedEntry> uses = getEquipmentUse();
		for(EquipmentUsedEntry use : uses) {
			if (use.getItem().getType().getType().equals(type) && use.getActivity().getId() == activity) {
				return use.getItem().getName();
			}
		}
		return null;
	}

	@Override
	public long getEquipmentUsedTime(long activity, String type) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public double getEquipmentUsedDistance(long activity, String type) {
		return 0;
	}

	@Override
	public void setEquipmentUsedEntry(long activity, String type, String usedEquipment, long usedTime,
			double usedDistance) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void removeEquipmentUsedEntry(long activity, String type) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void save() throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Long getNextId() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void addType(String type) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public double getEquipmentUsedTotalDistance(Long itemId) {
		logger.debug("getEquipmentUsedTotalDistance(itemId) " + itemId + " entered");
		List<EquipmentUsedEntry> uses = getEquipmentUse();
		double totaldist = 0.0;
		for(EquipmentUsedEntry use : uses) {
			int id = use.getItem().getId();
			if ((long) id == itemId) {
				ActivityData a = use.getActivity();
				Double distance = a.getActivity().getDistance();
				totaldist += distance;
			}
		}
		logger.debug("getEquipmentUsedTotalDistance(itemId) " + itemId + " returned " + totaldist);
		return totaldist;
	}
	
	private ActivityData getActivity(long id) {
		EntityManager em = objectFactory.getEntityManager();
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<ActivityData> criteriaQuery = cb.createQuery(ActivityData.class);
		Root<ActivityData> c = criteriaQuery.from(ActivityData.class);
		criteriaQuery.select(c);
		criteriaQuery.where(cb.equal(c.get("id"), id));
		TypedQuery<de.acwhadk.rz.filemyrun.jpa.data.ActivityData> query = 
				em.createQuery(criteriaQuery);
		List<ActivityData> activities = query.getResultList();
		return null;
	}

	private List<EquipmentUsedEntry> getEquipmentUse() {
		EntityManager em = objectFactory.getEntityManager();
		TypedQuery<EquipmentUsedEntry> query = em.createQuery("from EquipmentUsedEntry", EquipmentUsedEntry.class);
		return query.getResultList();
	}
	
}
