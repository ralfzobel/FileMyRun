package de.acwhadk.rz.filemyrun.file;

import java.io.Serializable;
import java.util.Date;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

/**
 * A bean used to hold the main data of one training activity
 * including the access path to the trx file that holds the detailed data.
 * 
 * @author Ralf
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "TrainingFile", propOrder = {
    "type",
    "name",
    "distance",
    "time",
    "comment",
    "trainingFile"
})
public class TrainingFile implements Serializable {

	private static final long serialVersionUID = 1L;

	private String type;
	private String name;
	private Double distance;
	private Date time;
	private String comment;
	private String trainingFile;
	
	public TrainingFile() {
		super();
	}

	public TrainingFile(String name, Date time, String trainingFile) {
		super();
		this.name = name;
		this.time = time;
		this.trainingFile = trainingFile;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Date getTime() {
		return time;
	}

	public String getTrainingFile() {
		return trainingFile;
	}

	public Double getDistance() {
		return distance;
	}

	public void setDistance(Double distance) {
		this.distance = distance;
	}

	public void setTime(Date time) {
		this.time = time;
	}

	public void setTrainingFile(String trainingFile) {
		this.trainingFile = trainingFile;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

}
