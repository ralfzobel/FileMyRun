package de.acwhadk.rz.filemyrun.core.model;

import java.util.List;
import java.util.Map;

public interface EquipmentMan {

	List<String> getEquipmentTypes();

	Map<Long, String> getEquipmentItems(String type);

	void setEquipmentItems(String type, Map<Long, String> itemMap);

	long getEquipmentUsedId(long activity, String type);

	String getEquipmentUsedName(long activity, String type);

	long getEquipmentUsedTime(long activity, String type);

	double getEquipmentUsedDistance(long activity, String type);

	void setEquipmentUsedEntry(long activity, String type, String usedEquipment, long usedTime, double usedDistance)
			throws Exception;

	void removeEquipmentUsedEntry(long activity, String type) throws Exception;

	void save() throws Exception;

	Long getNextId();

	void addType(String type);

	double getEquipmentUsedTotalDistance(Long itemId);

}