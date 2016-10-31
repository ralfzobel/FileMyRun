package de.acwhadk.rz.filemyrun.xml.equipment;

import java.io.Serializable;
import java.util.Date;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

/**
 * A bean used to describe one equipment item, e.g. a specific shoe.
 * 
 * @author Ralf
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "EquipmentUsedEntry", propOrder = {
    "activity",
    "type",
    "id",
    "usedTime",
    "usedDistance"
})
public class EquipmentUsedEntry implements Serializable {

	private static final long serialVersionUID = 1L;

	private Date activity;
	private String type;
	private long id;				// Id of the used equipment item (because the name may change..)
	private long usedTime;
	private double usedDistance;
	
	public Date getActivity() {
		return activity;
	}
	public void setActivity(Date activity) {
		this.activity = activity;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public long getUsedTime() {
		return usedTime;
	}
	public void setUsedTime(long usedTime) {
		this.usedTime = usedTime;
	}
	public double getUsedDistance() {
		return usedDistance;
	}
	public void setUsedDistance(double usedDistance) {
		this.usedDistance = usedDistance;
	}
	
}
