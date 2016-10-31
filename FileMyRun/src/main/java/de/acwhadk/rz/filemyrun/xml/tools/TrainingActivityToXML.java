package de.acwhadk.rz.filemyrun.xml.tools;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.namespace.QName;

import de.acwhadk.rz.filemyrun.xml.data.TrainingActivity;

/**
 * Read and write an xml file for a single activity.
 * 
 * @author Ralf
 *
 */
public class TrainingActivityToXML {

	private static final String JAXB_PACKAGES = "com.topografix.gpx:com.garmin.tpextensionsv1:de.acwhadk.rz.filemyrun.xml.data";

	private TrainingActivityToXML() {		
	}
	
	public static TrainingActivity load(File file) throws JAXBException {
		JAXBContext jc = JAXBContext.newInstance(JAXB_PACKAGES);

		Unmarshaller unmarshaller = jc.createUnmarshaller();
		@SuppressWarnings("unchecked")
		JAXBElement<TrainingActivity> obj = (JAXBElement<TrainingActivity>) unmarshaller.unmarshal(file);
		return obj.getValue();
	}

	public static void save(String filename, TrainingActivity activity) throws IOException, JAXBException {
		JAXBContext jc;
		jc = JAXBContext.newInstance(JAXB_PACKAGES);
		Marshaller m = jc.createMarshaller();
		m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
		QName name = new QName("http://rz.acwhadk.de/data/filemyrun/", activity.getClass().getSimpleName());

		try (FileOutputStream file = new FileOutputStream(filename)) {
			JAXBElement<Object> element = new JAXBElement<Object>(name,
					Object.class, null, activity);
			m.marshal(element, file);
		}
	}

}
