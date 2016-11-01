package de.acwhadk.rz.filemyrun.core.algo;

import java.util.List;

import de.acwhadk.rz.filemyrun.core.model.Lap;
import de.acwhadk.rz.filemyrun.core.model.Track;
import de.acwhadk.rz.filemyrun.core.model.TrackPoint;

public class AltitudeCreator {

	private static final double THRESHOLD = 5.0;
	
	public static Altitude calculateAltitude(List<Lap> laps) {
		Altitude altitude = new Altitude();
		altitude.setValid(false);
		
		Double max = null;
		Double min = null;
		double ascent = 0.0;
		double descent = 0.0;
		double lastDown = 0.0;
		double lastUp = 0.0;
		boolean ascending = false;
		boolean descending = false;
		int cnt = 0;
		int cntBad = 0;
		for(Lap lap : laps) {
			if (lap.getTrack() == null) {
				return null;
			}
			Track trk = lap.getTrack();
			if (trk.getTrackpoints() == null) {
				continue;
			}
			for(TrackPoint tp : trk.getTrackpoints()) {
				++cnt;
				Double alt = tp.getAltitudeMeters();
				if (alt == null) {
					++cntBad;
					continue;
				}
				if (min == null) {
					lastDown = alt;
				}
				if (max == null) {
					lastUp = alt;
				}
				if (min == null || min > alt) {
					min = alt;
				}
				if (max == null || max < alt) {
					max = alt;
				}
				if (alt > lastUp) {
					if (!ascending && alt > lastUp + THRESHOLD) {
						ascending = true;							
						descending = false;
					} 
					if (ascending && alt > lastUp) {
						ascent += (alt - lastUp);
						lastUp = alt;
					}
					if (!descending) {
						lastDown = alt;
					}
				}
				if (alt < lastDown) {
					if (!descending && alt < lastDown + THRESHOLD) {
						descending = true;							
						ascending = false;
					} 
					if (descending && alt < lastDown) {
						descent += (lastDown -alt);
						lastDown = alt;
					}
					if (!ascending) {
						lastUp = alt;
					}
				}
			}
		}
		if (max == null || min == null || cntBad*10 > cnt) {
			return null;
		}
		altitude.setMin((int) Math.round(min));
		altitude.setMax((int) Math.round(max));
		altitude.setAscent((int) Math.round(ascent));
		altitude.setDescent((int) Math.round(descent));
		altitude.setValid(true);
		
		return altitude;
	}
	
}
