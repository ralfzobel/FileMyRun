package de.acwhadk.rz.filemyrun.equipment;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

/**
 * a bean used to describe one equipment item, e.g. a specific shoe
 * 
 * @author Ralf
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "EquipmentItem", propOrder = {
    "id",
    "name",
    "inUse"
})
public class EquipmentItem implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private long id;
	private String name;
	private boolean inUse;
	
	public EquipmentItem() {
		this(0, null);
	}
	
	public EquipmentItem(long id, String name) {
		super();
		this.id = id;
		this.name = name;
		this.inUse = true;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isInUse() {
		return inUse;
	}

	public void setInUse(boolean inUse) {
		this.inUse = inUse;
	}		
}
