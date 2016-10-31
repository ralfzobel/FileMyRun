package de.acwhadk.rz.filemyrun.xml.model;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.xml.bind.JAXBException;

import de.acwhadk.rz.filemyrun.core.model.EquipmentMan;
import de.acwhadk.rz.filemyrun.core.setup.Const;
import de.acwhadk.rz.filemyrun.core.setup.Lang;
import de.acwhadk.rz.filemyrun.core.setup.Setup;
import de.acwhadk.rz.filemyrun.xml.equipment.EquipmentContainer;
import de.acwhadk.rz.filemyrun.xml.equipment.EquipmentDefinition;
import de.acwhadk.rz.filemyrun.xml.equipment.EquipmentItem;
import de.acwhadk.rz.filemyrun.xml.equipment.EquipmentUsedEntry;
import de.acwhadk.rz.filemyrun.xml.tools.EquipmentContainerToXML;

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
public class EquipmentManImpl implements EquipmentMan {

	private String workdir;
	private EquipmentContainer equipment;

	public EquipmentManImpl() {
		this.workdir = Setup.getInstance().getActivitiesFolder();
		File file = new File(workdir + File.separator + Setup.EQUIPMENT_FILENAME);
		try {
			equipment = EquipmentContainerToXML.load(file);
		} catch (JAXBException e) {
			equipment = new EquipmentContainer();
		}
	}
	
	/* (non-Javadoc)
	 * @see de.acwhadk.rz.filemyrun.gui.EquipmentMan#getEquipmentTypes()
	 */
	@Override
	public List<String> getEquipmentTypes() {
		List<String> types = new ArrayList<>();
		for(EquipmentDefinition def : equipment.getEquipmentDefinitionList()) {
			types.add(def.getType());
		}
		return types;
	}

	/* (non-Javadoc)
	 * @see de.acwhadk.rz.filemyrun.gui.EquipmentMan#getEquipmentItems(java.lang.String)
	 */
	@Override
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

	/* (non-Javadoc)
	 * @see de.acwhadk.rz.filemyrun.gui.EquipmentMan#setEquipmentItems(java.lang.String, java.util.Map)
	 */
	@Override
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

	/* (non-Javadoc)
	 * @see de.acwhadk.rz.filemyrun.gui.EquipmentMan#getEquipmentUsedId(java.util.Date, java.lang.String)
	 */
	@Override
	public long getEquipmentUsedId(Date activity, String type) {
		EquipmentUsedEntry entry = getEquipmentUsedEntry(activity, type);
		if (entry == null) {
			return 0L;
		}
		return entry.getId();
	}

	/* (non-Javadoc)
	 * @see de.acwhadk.rz.filemyrun.gui.EquipmentMan#getEquipmentUsedName(java.util.Date, java.lang.String)
	 */
	@Override
	public String getEquipmentUsedName(Date activity, String type) {
		long id = getEquipmentUsedId(activity, type);
		Map<Long, String> items = getEquipmentItems(type);
		String name = items.get(id);
		return name == null ? Const.EMPTY : name;
	}
	
	/* (non-Javadoc)
	 * @see de.acwhadk.rz.filemyrun.gui.EquipmentMan#getEquipmentUsedTime(java.util.Date, java.lang.String)
	 */
	@Override
	public long getEquipmentUsedTime(Date activity, String type) {
		EquipmentUsedEntry entry = getEquipmentUsedEntry(activity, type);
		if (entry == null) {
			return 0L;
		}
		return entry.getUsedTime();
	}
	
	/* (non-Javadoc)
	 * @see de.acwhadk.rz.filemyrun.gui.EquipmentMan#getEquipmentUsedDistance(java.util.Date, java.lang.String)
	 */
	@Override
	public double getEquipmentUsedDistance(Date activity, String type) {
		EquipmentUsedEntry entry = getEquipmentUsedEntry(activity, type);
		if (entry == null) {
			return 0.0;
		}
		return entry.getUsedDistance();
	}
	
	/* (non-Javadoc)
	 * @see de.acwhadk.rz.filemyrun.gui.EquipmentMan#setEquipmentUsedEntry(java.util.Date, java.lang.String, java.lang.String, long, double)
	 */
	@Override
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
	
	/* (non-Javadoc)
	 * @see de.acwhadk.rz.filemyrun.gui.EquipmentMan#removeEquipmentUsedEntry(java.util.Date, java.lang.String)
	 */
	@Override
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
		String message = String.format(Lang.get().text(Lang.EQUIPMENT_UNKNOWN), type, itemName);
		throw new RuntimeException(message);
	}

	/* (non-Javadoc)
	 * @see de.acwhadk.rz.filemyrun.gui.EquipmentMan#save()
	 */
	@Override
	public void save() throws Exception {
		EquipmentContainerToXML.save(workdir + File.separator + Setup.EQUIPMENT_FILENAME, equipment);		
	}

	/* (non-Javadoc)
	 * @see de.acwhadk.rz.filemyrun.gui.EquipmentMan#getNextId()
	 */
	@Override
	public Long getNextId() {
		long curId = equipment.getCurrentId();
		equipment.setCurrentId(++curId);
		return curId;
	}

	/* (non-Javadoc)
	 * @see de.acwhadk.rz.filemyrun.gui.EquipmentMan#addType(java.lang.String)
	 */
	@Override
	public void addType(String type) {
		for(EquipmentDefinition def : equipment.getEquipmentDefinitionList()) {
			if (def.getType().equals(type)) {
				return;
			}
		}
		equipment.getEquipmentDefinitionList().add(new EquipmentDefinition(type));
	}

	/* (non-Javadoc)
	 * @see de.acwhadk.rz.filemyrun.gui.EquipmentMan#getEquipmentUsedEntryList()
	 */
	@Override
	public List<EquipmentUsedEntry> getEquipmentUsedEntryList() {
		return equipment.getEquipmentUsedEntryList();
	}
}
