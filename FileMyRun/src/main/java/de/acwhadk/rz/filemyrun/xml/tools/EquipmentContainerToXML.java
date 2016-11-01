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

import de.acwhadk.rz.filemyrun.xml.equipment.EquipmentContainer;

/**
 * EquipmentContainerToXML is used to read and write the xml file that contains
 * the equipment data. This is the definition of the equipment and the use of
 * the equipment too.
 * 
 * @author Ralf
 *
 */
public class EquipmentContainerToXML {

	private static final String JAXB_PACKAGES = "de.acwhadk.rz.filemyrun.xml.equipment";
	
	public static EquipmentContainer load(File file) throws JAXBException {
		JAXBContext jc = JAXBContext.newInstance(JAXB_PACKAGES);

		Unmarshaller unmarshaller = jc.createUnmarshaller();
		@SuppressWarnings("unchecked")
		JAXBElement<EquipmentContainer> obj = (JAXBElement<EquipmentContainer>) unmarshaller.unmarshal(file);
		return obj.getValue();
	}

	public static void save(String filename, EquipmentContainer fileContainer) throws IOException, JAXBException {
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
