package de.acwhadk.rz.filemyrun.xml.data;

import java.util.Date;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import com.garmin.tcdbv2.TrainingCenterDatabaseT;

import de.acwhadk.rz.filemyrun.xml.file.TrainingFile;

/**
 * This is the central data storage for a training activity.
 * 
 * It contains the data from the garmin tcx file (class TrainingCenterDatabaseT) 
 * and the additional data from the training file. 
 * 
 * @author Ralf
 *
 */
@XmlRootElement(name="TrainingActivity")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "TrainingActivity", namespace = "http://rz.acwhadk.de/data/filemyrun/", propOrder = {
	    "trainingFile",
	    "trainingCenterDatabase"
})
public class TrainingActivity
{
    @XmlElement()
    protected TrainingFile trainingFile;
    
    @XmlElement()
    protected TrainingCenterDatabaseT trainingCenterDatabase;

    public String getName() {
        return trainingFile.getName();
    }

    public void setName(String value) {
    	trainingFile.setName(value);
    }

    public String getType() {
        return trainingFile.getType();
    }

    public void setType(String value) {
    	trainingFile.setType(value);
    }

	public TrainingCenterDatabaseT getTrainingCenterDatabase() {
		return trainingCenterDatabase;
	}

	public void setTrainingCenterDatabase(TrainingCenterDatabaseT trainingCenterDatabase) {
		this.trainingCenterDatabase = trainingCenterDatabase;
	}

	public String getDescription() {
		return trainingFile.getComment();
	}

	public void setDescription(String description) {
		trainingFile.setComment(description);
	}

	public Date getTime() {
		return trainingFile.getTime();
	}

	public void setTime(Date time) {
		trainingFile.setTime(time);
	}

	public Double getDistance() {
		return trainingFile.getDistance();
	}

	public void setDistance(Double distance) {
		trainingFile.setDistance(distance);
	}

	public TrainingFile getTrainingFile() {
		return trainingFile;
	}

	public void setTrainingFile(TrainingFile trainingFile) {
		this.trainingFile = trainingFile;
	}

}
