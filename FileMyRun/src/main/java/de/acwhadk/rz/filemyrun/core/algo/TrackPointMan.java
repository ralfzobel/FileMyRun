package de.acwhadk.rz.filemyrun.core.algo;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import de.acwhadk.rz.filemyrun.core.model.Activity;
import de.acwhadk.rz.filemyrun.core.model.Lap;
import de.acwhadk.rz.filemyrun.core.model.Track;
import de.acwhadk.rz.filemyrun.core.model.TrackPoint;

public class TrackPointMan {

	public static Map<String, List<TrackPoint>> getTrackPoints(Activity activity) {
		Map<String, List<TrackPoint>> tpMap = new TreeMap<>();
		for(Lap lap : activity.getLaps()) {
			List<TrackPoint> tpList = new ArrayList<>();
			Calendar cal = getLapStartTime(lap);
			if (cal == null) {
				continue;
			}
			tpMap.put(Formatter.formatFullDate(cal.getTime()), tpList );
			Track trk = lap.getTrack();
			if (trk.getTrackpoints() == null) {
				continue;
			}
			for(TrackPoint tp : trk.getTrackpoints()) {
				tpList.add(tp);
			}
		}
		return tpMap;
	}
	
	private static Calendar getLapStartTime(Lap lap) {
		Calendar t = lap.getStartTime();
		if (t == null) {
			Track trk = lap.getTrack();
			if (trk != null && trk.getTrackpoints() != null) {
				t = trk.getTrackpoints().get(0).getTime();
			}
		}
		return t;
	}
}
