package de.acwhadk.rz.filemyrun.controller;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.xml.bind.JAXBException;

import de.acwhadk.rz.filemyrun.equipment.EquipmentContainer;
import de.acwhadk.rz.filemyrun.equipment.EquipmentDefinition;
import de.acwhadk.rz.filemyrun.equipment.EquipmentItem;
import de.acwhadk.rz.filemyrun.equipment.EquipmentUsedEntry;
import de.acwhadk.rz.filemyrun.setup.Setup;
import de.acwhadk.rz.filemyrun.xml.EquipmentContainerToXML;

/**
 * A manager class to deal with equipment.
 * It reads the equipment definitions and the used equipment
 * and supplies access to this data for the gui.
 * 
 * This class could have also be implemented as a singleton.
 *  
 * @author Ralf
 *
 */
public class EquipmentMan {

	private String workdir;
	private EquipmentContainer equipment;

	public EquipmentMan() {
		this.workdir = Setup.getInstance().getActivitiesFolder();
		File file = new File(workdir + "/equipment.xml");
		try {
			equipment = EquipmentContainerToXML.load(file);
		} catch (JAXBException e) {
			equipment = new EquipmentContainer();
		}
	}
	
	public List<String> getEquipmentTypes() {
		List<String> types = new ArrayList<>();
		for(EquipmentDefinition def : equipment.getEquipmentDefinitionList()) {
			types.add(def.getType());
		}
		return types;
	}

	public Map<Long, String> getEquipmentItems(String type) {
		Map<Long, String> items = new HashMap<>();
		for(EquipmentDefinition def : equipment.getEquipmentDefinitionList()) {
			if (def.getType().equals(type)) {
				for(EquipmentItem item : def.getItems()) {
					items.put(item.getId(), item.getName());
				}
			}
		}
		return items;
	}

	public void setEquipmentItems(String type, Map<Long, String> itemMap) {
		for (EquipmentDefinition def : equipment.getEquipmentDefinitionList()) {
			if (def.getType().equals(type)) {
				setEquipmentItems(def, itemMap);
			}
		}		
	}
	private void setEquipmentItems(EquipmentDefinition def, Map<Long, String> itemMap) {
		for(EquipmentItem myItem : def.getItems()) {
			String item = itemMap.get(myItem.getId());
			if (item != null) {
				myItem.setInUse(true);
				myItem.setName(item);
				itemMap.remove(myItem.getId());
			} else {
				myItem.setInUse(false);
			}
		}
		for(Entry<Long, String> entry : itemMap.entrySet()) {
			EquipmentItem myItem = new EquipmentItem(entry.getKey(), entry.getValue());
			def.getItems().add(myItem);
		}
	}

	public long getEquipmentUsedId(Date activity, String type) {
		EquipmentUsedEntry entry = getEquipmentUsedEntry(activity, type);
		if (entry == null) {
			return 0L;
		}
		return entry.getId();
	}

	public String getEquipmentUsedName(Date activity, String type) {
		long id = getEquipmentUsedId(activity, type);
		Map<Long, String> items = getEquipmentItems(type);
		String name = items.get(id);
		return name == null ? "" : name;
	}
	
	public long getEquipmentUsedTime(Date activity, String type) {
		EquipmentUsedEntry entry = getEquipmentUsedEntry(activity, type);
		if (entry == null) {
			return 0L;
		}
		return entry.getUsedTime();
	}
	
	public double getEquipmentUsedDistance(Date activity, String type) {
		EquipmentUsedEntry entry = getEquipmentUsedEntry(activity, type);
		if (entry == null) {
			return 0.0;
		}
		return entry.getUsedDistance();
	}
	
	public void setEquipmentUsedEntry(Date activity, String type, String usedEquipment, long usedTime, double usedDistance) throws Exception {
		if (usedEquipment == null) {
			return;
		}
		long id = getId(type, usedEquipment);
		EquipmentUsedEntry entry = getEquipmentUsedEntry(activity, type);
		if (entry == null) {
			entry = new EquipmentUsedEntry();
			entry.setActivity(activity);
			entry.setType(type);
			equipment.getEquipmentUsedEntryList().add(entry);
		}
		entry.setId(id);
		entry.setUsedTime(usedTime);
		entry.setUsedDistance(usedDistance);
		save();
	}
	
	public void removeEquipmentUsedEntry(Date activity, String type) throws Exception {
		EquipmentUsedEntry entry = getEquipmentUsedEntry(activity, type);
		equipment.getEquipmentUsedEntryList().remove(entry);
		save();
	}
	
	private EquipmentUsedEntry getEquipmentUsedEntry(Date activity, String type) {
		for(EquipmentUsedEntry entry : equipment.getEquipmentUsedEntryList()) {
			if (entry.getActivity().equals(activity) && entry.getType().equals(type)) {
				return entry;
			}
		}
		return null;
	}
	
	private long getId(String type, String itemName) {
		for(EquipmentDefinition def : equipment.getEquipmentDefinitionList()) {
			if (def.getType().equals(type)) {
				for(EquipmentItem item : def.getItems()) {
					if (item.getName().equals(itemName)) {
						return item.getId();
					}
				}
			}
		}
		throw new RuntimeException("Equipment with type " + type + " and name " + itemName + " is unknown");
	}

	public void save() throws Exception {
		EquipmentContainerToXML.save(workdir + "/equipment.xml", equipment);		
	}

	public Long getNextId() {
		long curId = equipment.getCurrentId();
		equipment.setCurrentId(++curId);
		return curId;
	}

	public void addType(String type) {
		for(EquipmentDefinition def : equipment.getEquipmentDefinitionList()) {
			if (def.getType().equals(type)) {
				return;
			}
		}
		equipment.getEquipmentDefinitionList().add(new EquipmentDefinition(type));
	}

	public List<EquipmentUsedEntry> getEquipmentUsedEntryList() {
		return equipment.getEquipmentUsedEntryList();
	}
}
