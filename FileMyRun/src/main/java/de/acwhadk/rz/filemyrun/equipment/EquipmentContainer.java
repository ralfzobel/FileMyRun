package de.acwhadk.rz.filemyrun.equipment;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * The container holding all equipment related data.
 * 
 * @author Ralf
 *
 */
@XmlRootElement(name="EquipmentContainer")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "EquipmentContainer", namespace = "http://rz.acwhadk.de/file/filemyrun/", propOrder = {
		"currentId",
	    "equipmentDefinitionList",
	    "equipmentUsedEntryList"
})
public class EquipmentContainer implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private long currentId;
	private List<EquipmentDefinition> equipmentDefinitionList;
	private List<EquipmentUsedEntry> equipmentUsedEntryList;

	public List<EquipmentDefinition> getEquipmentDefinitionList() {
		if (equipmentDefinitionList == null) {
			equipmentDefinitionList = new ArrayList<>();
		}
		return equipmentDefinitionList;
	}

	public List<EquipmentUsedEntry> getequipmentUsedEntryList() {
		if (equipmentUsedEntryList == null) {
			equipmentUsedEntryList = new ArrayList<>();
		}
		return equipmentUsedEntryList;
	}

	public long getCurrentId() {
		return currentId;
	}

	public void setCurrentId(long currentId) {
		this.currentId = currentId;
	}
	
	
}
