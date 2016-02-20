package de.acwhadk.rz.filemyrun.xml;

import java.io.File;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import com.garmin.tcdbv2.TrainingCenterDatabaseT;

/**
 * A loader for garmin tcx files.
 * 
 * @author Ralf
 *
 */
public class TcxLoader {

	public TrainingCenterDatabaseT loadTcx(File file) throws JAXBException {
		JAXBContext jc = JAXBContext.newInstance("com.garmin.tcdbv2");

        Unmarshaller unmarshaller = jc.createUnmarshaller();
		@SuppressWarnings("unchecked")
		JAXBElement<TrainingCenterDatabaseT> obj = (JAXBElement<TrainingCenterDatabaseT>) unmarshaller.unmarshal(file);
		return obj.getValue();
	}
}
