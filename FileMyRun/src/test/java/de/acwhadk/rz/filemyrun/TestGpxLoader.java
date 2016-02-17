package de.acwhadk.rz.filemyrun;

import static org.junit.Assert.assertEquals;

import java.io.File;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;

import org.junit.Test;

import com.garmin.tpextensionsv1.TrackPointExtensionT;
import com.topografix.gpx.ExtensionsType;
import com.topografix.gpx.GpxType;
import com.topografix.gpx.TrkType;
import com.topografix.gpx.TrksegType;
import com.topografix.gpx.WptType;

import de.acwhadk.rz.filemyrun.xml.GpxLoader;

public class TestGpxLoader {

	@Test
	public void test() throws JAXBException {
		GpxLoader gpx = new GpxLoader();
		GpxType t = gpx.loadGpx(new File("C:\\Users\\Ralf\\Downloads\\activity_911196904.gpx"));
		TrkType a = t.getTrk().get(0);
		TrksegType b = a.getTrkseg().get(0);
		WptType c = b.getTrkpt().get(0);
		Short hr = getHeartRate(c);
		assertEquals(107, (int) hr);
	}

	private Short getHeartRate(WptType tp) {
		ExtensionsType extensions = tp.getExtensions();
		for(Object elem : extensions.getAny()) {
			if (elem instanceof JAXBElement) {
				@SuppressWarnings("rawtypes")
				JAXBElement jaxbElem = (JAXBElement) elem;
				Object ext = jaxbElem.getValue();
				if (ext instanceof TrackPointExtensionT) {
					TrackPointExtensionT tpExt = (TrackPointExtensionT) ext;
					return tpExt.getHr();
				}
			}
		}
		return null;
	}
	
}
