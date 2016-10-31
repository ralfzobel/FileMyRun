package de.acwhadk.rz.filemyrun.xml.tools;

import java.io.File;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import com.topografix.gpx.GpxType;

/**
 * A Loader for garmin gpx files.
 * When the tcx files are imported, it is also checked if there is a gpx file too.
 * If so, it is read for some additional data that is not contained in the tcx file.
 * 
 * @author Ralf
 *
 */
public class GpxLoader {

	public GpxType loadGpx(File file) throws JAXBException {
		JAXBContext jc = JAXBContext.newInstance("com.topografix.gpx:com.garmin.tpextensionsv1");

        Unmarshaller unmarshaller = jc.createUnmarshaller();
		@SuppressWarnings("unchecked")
		JAXBElement<GpxType> obj = (JAXBElement<GpxType>) unmarshaller.unmarshal(file);
		return obj.getValue();
	}
	
	
}
