package de.acwhadk.rz.filemyrun.jpa.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.acwhadk.rz.filemyrun.core.model.EquipmentMan;
import de.acwhadk.rz.filemyrun.xml.equipment.EquipmentUsedEntry;

public class EquipmentManImpl implements EquipmentMan {

	@Override
	public List<String> getEquipmentTypes() {
		// TODO Auto-generated method stub
		return new ArrayList<>();
	}

	@Override
	public Map<Long, String> getEquipmentItems(String type) {
		// TODO Auto-generated method stub
		return new HashMap<>();
	}

	@Override
	public void setEquipmentItems(String type, Map<Long, String> itemMap) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public long getEquipmentUsedId(Date activity, String type) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String getEquipmentUsedName(Date activity, String type) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getEquipmentUsedTime(Date activity, String type) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public double getEquipmentUsedDistance(Date activity, String type) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void setEquipmentUsedEntry(Date activity, String type, String usedEquipment, long usedTime,
			double usedDistance) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void removeEquipmentUsedEntry(Date activity, String type) throws Exception {
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
	public List<EquipmentUsedEntry> getEquipmentUsedEntryList() {
		// TODO Auto-generated method stub
		return null;
	}

}
