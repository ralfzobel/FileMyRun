package de.acwhadk.rz.filemyrun.jpa.data;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class EquipmentType {

	@Id
	private String type;

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
		
}
