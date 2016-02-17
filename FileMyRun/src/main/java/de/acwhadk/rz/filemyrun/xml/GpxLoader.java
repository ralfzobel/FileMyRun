package de.acwhadk.rz.filemyrun.xml;

import java.io.File;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import com.topografix.gpx.GpxType;

public class GpxLoader {

	public GpxType loadGpx(File file) throws JAXBException {
		JAXBContext jc = JAXBContext.newInstance("com.topografix.gpx:com.garmin.tpextensionsv1");

        Unmarshaller unmarshaller = jc.createUnmarshaller();
		@SuppressWarnings("unchecked")
		JAXBElement<GpxType> obj = (JAXBElement<GpxType>) unmarshaller.unmarshal(file);
		return obj.getValue();
	}
	
	
}
