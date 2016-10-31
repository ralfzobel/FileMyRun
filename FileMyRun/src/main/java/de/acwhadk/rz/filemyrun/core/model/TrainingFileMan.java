package de.acwhadk.rz.filemyrun.core.model;

import java.io.IOException;
import java.util.Date;
import java.util.SortedMap;

import javax.xml.bind.JAXBException;

public interface TrainingFileMan {

	SortedMap<Date, de.acwhadk.rz.filemyrun.core.model.TrainingFile> getTrainingFiles();

	TrainingFile getTrainingFile(Date date);

	void save() throws IOException, JAXBException;

	void deleteFile(de.acwhadk.rz.filemyrun.core.model.TrainingFile trainingFileImplXml);

}