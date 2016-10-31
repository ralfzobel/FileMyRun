package de.acwhadk.rz.filemyrun.core.model;

import java.util.Date;
import java.util.List;
import java.util.Map;

import de.acwhadk.rz.filemyrun.xml.equipment.EquipmentUsedEntry;

public interface EquipmentMan {

	List<String> getEquipmentTypes();

	Map<Long, String> getEquipmentItems(String type);

	void setEquipmentItems(String type, Map<Long, String> itemMap);

	long getEquipmentUsedId(Date activity, String type);

	String getEquipmentUsedName(Date activity, String type);

	long getEquipmentUsedTime(Date activity, String type);

	double getEquipmentUsedDistance(Date activity, String type);

	void setEquipmentUsedEntry(Date activity, String type, String usedEquipment, long usedTime, double usedDistance)
			throws Exception;

	void removeEquipmentUsedEntry(Date activity, String type) throws Exception;

	void save() throws Exception;

	Long getNextId();

	void addType(String type);

	List<EquipmentUsedEntry> getEquipmentUsedEntryList();

}