package de.acwhadk.rz.filemyrun.jpa.data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class EquipmentUsedEntry {

	@Id
	private int id;
	
	@ManyToOne
	@JoinColumn
	private EquipmentItem item;
	
	@ManyToOne
	@JoinColumn
	private ActivityData activity;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public EquipmentItem getItem() {
		return item;
	}

	public void setItem(EquipmentItem item) {
		this.item = item;
	}

	public ActivityData getActivity() {
		return activity;
	}

	public void setActivity(ActivityData activity) {
		this.activity = activity;
	}
		
}
