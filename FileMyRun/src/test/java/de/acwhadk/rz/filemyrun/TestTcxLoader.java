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
import de.acwhadk.rz.filemyrun.xml.TcxLoader;
import de.acwhadk.rz.filemyrun.xml.TrainingActivityToXML;

public class TestTcxLoader {


	@Test
	public void test() throws JAXBException, IOException {
		TcxLoader tcx = new TcxLoader();
		TrainingCenterDatabaseT t = tcx.loadTcx(new File("C:\\Users\\Ralf\\Downloads\\imported\\activity_917243787.tcx"));
		ActivityT a = t.getActivities().getActivity().get(0);
		ActivityLapT b = a.getLap().get(0);
		HeartRateInBeatsPerMinuteT c = b.getAverageHeartRateBpm();
		short hr = c.getValue();
		assertEquals(168, (int) hr);
		
		TrainingActivity tr = new TrainingActivity();
		tr.setName("Hugo");
		tr.setTrainingCenterDatabase(t);
		
		TrainingActivityToXML.save("C:\\Users\\Ralf\\Downloads\\activity_917243787.trx", tr);
		
		File file = new File("C:\\Users\\Ralf\\Downloads\\activity_917243787.trx");
		TrainingActivity trz = TrainingActivityToXML.load(file);
		System.out.println(trz.getName());
	}
}
