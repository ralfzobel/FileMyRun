package de.acwhadk.rz.filemyrun;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.IOException;

import javax.xml.bind.JAXBException;

import org.junit.Test;

import com.garmin.tcdbv2.ActivityLapT;
import com.garmin.tcdbv2.ActivityT;
import com.garmin.tcdbv2.HeartRateInBeatsPerMinuteT;
import com.garmin.tcdbv2.TrainingCenterDatabaseT;

import de.acwhadk.rz.filemyrun.data.TrainingActivity;
import de.acwhadk.rz.filemyrun.file.TrainingFile;
import de.acwhadk.rz.filemyrun.xml.TcxLoader;
import de.acwhadk.rz.filemyrun.xml.TrainingActivityToXML;

public class TestTcxLoader {


	@Test
	public void test() throws JAXBException, IOException {
		TcxLoader tcx = new TcxLoader();
		TrainingCenterDatabaseT t = tcx.loadTcx(new File("src/test/resources/sample_activity.tcx"));
		ActivityT a = t.getActivities().getActivity().get(0);
		ActivityLapT b = a.getLap().get(0);
		HeartRateInBeatsPerMinuteT c = b.getAverageHeartRateBpm();
		short hr = c.getValue();
		assertEquals(149, (int) hr);
		
		TrainingFile tf = new TrainingFile();
		TrainingActivity tr = new TrainingActivity();
		tr.setTrainingFile(tf);
		tr.setName("Hugo");
		tr.setTrainingCenterDatabase(t);
		
		TrainingActivityToXML.save("src/test/resources/sample_activity.trx", tr);
		
		File file = new File("src/test/resources/sample_activity.trx");
		TrainingActivity trz = TrainingActivityToXML.load(file);
		assertEquals("Hugo", trz.getName());
	}
}
