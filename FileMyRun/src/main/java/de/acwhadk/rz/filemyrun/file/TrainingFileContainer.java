package de.acwhadk.rz.filemyrun.file;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name="TrainingFileContainer")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "TrainingFileContainer", namespace = "http://rz.acwhadk.de/file/filemyrun/", propOrder = {
	    "filelist"
})
public class TrainingFileContainer implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private ArrayList<TrainingFile> filelist;

	public void setFileList(List<TrainingFile> filelist) {
		this.filelist = new ArrayList<>();
		this.filelist.addAll(filelist);
	}

	public List<TrainingFile> getFilelist() {
		return filelist;
	}
}
