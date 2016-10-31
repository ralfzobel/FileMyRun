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

import de.acwhadk.rz.filemyrun.xml.file.TrainingFileContainer;

/**
 * Read and write an index xml file for all activities referencing the activity files
 * and containing some important data.
 * 
 * @author Ralf
 *
 */
public class TrainingFileContainerToXML {

	private static final String JAXB_PACKAGES = "de.acwhadk.rz.filemyrun.xml.file";
	
	public static TrainingFileContainer load(File file) throws JAXBException {
		JAXBContext jc = JAXBContext.newInstance(JAXB_PACKAGES);

		Unmarshaller unmarshaller = jc.createUnmarshaller();
		@SuppressWarnings("unchecked")
		JAXBElement<TrainingFileContainer> obj = (JAXBElement<TrainingFileContainer>) unmarshaller.unmarshal(file);
		return obj.getValue();
	}

	public static void save(String filename, TrainingFileContainer fileContainer) throws IOException, JAXBException {
		JAXBContext jc;
		jc = JAXBContext.newInstance(JAXB_PACKAGES);
		Marshaller m = jc.createMarshaller();
		m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
		QName name = new QName("http://rz.acwhadk.de/file/filemyrun/", fileContainer.getClass().getSimpleName());

		try (FileOutputStream file = new FileOutputStream(filename)) {
			JAXBElement<Object> element = new JAXBElement<Object>(name,
					Object.class, null, fileContainer);
			m.marshal(element, file);
		}
	}
}
