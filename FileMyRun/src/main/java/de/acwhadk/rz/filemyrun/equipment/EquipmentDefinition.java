package de.acwhadk.rz.filemyrun.equipment;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

/**
 * A bean used to hold one type of equipment (e.g. shoes) and its defined items.
 * 
 * @author Ralf
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "EquipmentDefinition", propOrder = {
    "type",
    "items"
})
public class EquipmentDefinition implements Serializable {

	private static final long serialVersionUID = 1L;

	private String type;
	private List<EquipmentItem> items;

	public EquipmentDefinition() {
		this(null, null);
	}

	public EquipmentDefinition(String type) {
		this(type, null);
	}

	public EquipmentDefinition(String type, List<EquipmentItem> items) {
		super();
		this.type = type;
		this.items = items;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public List<EquipmentItem> getItems() {
		if (items == null) {
			items = new ArrayList<>();
		}
		return items;
	}
	
}
