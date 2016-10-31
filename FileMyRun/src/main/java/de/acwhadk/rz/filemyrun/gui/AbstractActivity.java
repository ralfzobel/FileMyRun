package de.acwhadk.rz.filemyrun.gui;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import de.acwhadk.rz.filemyrun.core.model.Activity;
import de.acwhadk.rz.filemyrun.core.model.Lap;
import de.acwhadk.rz.filemyrun.core.model.ObjectFactory;
import de.acwhadk.rz.filemyrun.core.model.Track;
import de.acwhadk.rz.filemyrun.core.model.TrackPoint;
import de.acwhadk.rz.filemyrun.core.setup.Const;
import de.acwhadk.rz.filemyrun.core.setup.Lang;

/**
 * This class provides the methods that the gui needs.
 * It implements getters for data that may be used at it is (e.g. name).
 * It implements methods that calculate the data from the raw data (most of the methods do that).
 * It supplies methods to manipulate the data (e.g. setters and the split* methods).
 *  
 * @author Ralf
 *
 */
public abstract class AbstractActivity implements Activity {

	private static final double THRESHOLD = 5.0;
	
	private Altitude totalAltitude;
	
	/* (non-Javadoc)
	 * @see de.acwhadk.rz.filemyrun.gui.ActivityX#getTrackDistance()
	 */
	@Override
	public double getTrackDistance() throws Exception {
		double distance = 0.0;
		List<Lap> laps = getLaps();
		for(Lap lap : laps) {
			distance += lap.getDistanceMeters();
		}
		return distance;
	}

	/* (non-Javadoc)
	 * @see de.acwhadk.rz.filemyrun.gui.ActivityX#getTotalTime()
	 */
	@Override
	public long getTotalTime() {
		double time = 0.0;
		List<Lap> laps = getLaps();
		for(Lap lap : laps) {
			time += lap.getTotalTimeSeconds();
		}
		return Math.round(time);
	}
	
	/* (non-Javadoc)
	 * @see de.acwhadk.rz.filemyrun.gui.ActivityX#getPace()
	 */
	@Override
	public double getPace() throws Exception {
		return getTotalTime() / getDistance();
	}
	
	/* (non-Javadoc)
	 * @see de.acwhadk.rz.filemyrun.gui.ActivityX#getAverageHeartRate()
	 */
	@Override
	public Long getAverageHeartRate() {
		double heartrate = 0.0;
		double time = 0.0;
		List<Lap> laps = getLaps();
		for(Lap lap : laps) {
			if (lap.getMaximumHeartRateBpm() == null) {
				return null;
			}
			heartrate += lap.getAverageHeartRateBpm()* lap.getTotalTimeSeconds();
			time += lap.getTotalTimeSeconds();
		}
		return Math.round(heartrate / time);
	}
	
	/* (non-Javadoc)
	 * @see de.acwhadk.rz.filemyrun.gui.ActivityX#getMaximumHeartRate()
	 */
	@Override
	public Long getMaximumHeartRate() {
		long heartrate = 0L;
		List<Lap> laps = getLaps();
		for(Lap lap : laps) {
			if (lap.getMaximumHeartRateBpm() == null) {
				return null;
			}
			long hr = lap.getMaximumHeartRateBpm();
			if (hr > heartrate) {
				heartrate = hr;
			}
		}
		return heartrate;
	}

	/* (non-Javadoc)
	 * @see de.acwhadk.rz.filemyrun.gui.ActivityX#getMaximumAltitude()
	 */
	@Override
	public Long getMaximumAltitude() {
		calculateAltitude();
		return totalAltitude != null && totalAltitude.valid ? totalAltitude.max : null;
	}
	
	/* (non-Javadoc)
	 * @see de.acwhadk.rz.filemyrun.gui.ActivityX#getMinimumAltitude()
	 */
	@Override
	public Long getMinimumAltitude() {
		calculateAltitude();
		return totalAltitude != null && totalAltitude.valid ? totalAltitude.min : null;
	}
	
	/* (non-Javadoc)
	 * @see de.acwhadk.rz.filemyrun.gui.ActivityX#getAscent()
	 */
	@Override
	public Long getAscent() {
		calculateAltitude();
		return totalAltitude != null && totalAltitude.valid ? totalAltitude.ascent : null;
	}
	
	/* (non-Javadoc)
	 * @see de.acwhadk.rz.filemyrun.gui.ActivityX#getDescent()
	 */
	@Override
	public Long getDescent() {
		calculateAltitude();
		return totalAltitude != null && totalAltitude.valid ? totalAltitude.descent : null;
	}
	
	/* (non-Javadoc)
	 * @see de.acwhadk.rz.filemyrun.gui.ActivityX#getCalories()
	 */
	@Override
	public int getCalories() {
		int calories = 0;
		List<Lap> laps = getLaps();
		for(Lap lap : laps) {
			calories += lap.getCalories();
		}
		return calories;
	}
	
	/* (non-Javadoc)
	 * @see de.acwhadk.rz.filemyrun.gui.ActivityX#save()
	 */
	/* (non-Javadoc)
	 * @see de.acwhadk.rz.filemyrun.gui.ActivityX#getTrackPoints()
	 */
	@Override
	public Map<String, List<TrackPoint>> getTrackPoints() {
		Map<String, List<TrackPoint>> tpMap = new TreeMap<>();
		for(Lap lap : getLaps()) {
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
	
	private Calendar getLapStartTime(Lap lap) {
		Calendar t = lap.getStartTime();
		if (t == null) {
			Track trk = lap.getTrack();
			if (trk != null && trk.getTrackpoints() != null) {
				t = trk.getTrackpoints().get(0).getTime();
			}
		}
		return t;
	}

	/* (non-Javadoc)
	 * @see de.acwhadk.rz.filemyrun.gui.ActivityX#getSplitTimes(boolean)
	 */
	@Override
	public List<SplitTime> getSplitTimes(boolean snapIn) {
		List<SplitTime> laplist = new ArrayList<>();
		List<Lap> activityLaps = getLaps();
		double totalSeconds = 0.0;
		double totalDistance = 0.0;
		int round = 1;
		for(Lap activityLap : activityLaps) {
			SplitTime splitTime = new SplitTime();
			splitTime.setRound(Integer.toString(round));
			
			double seconds = activityLap.getTotalTimeSeconds();
			splitTime.setTime(Formatter.formatSeconds(seconds));
			splitTime.setTimeInSeconds(seconds);

			totalSeconds += seconds;
			splitTime.setTotalTime(Formatter.formatSeconds(totalSeconds));
			
			double distance = activityLap.getDistanceMeters()/1000.0;
			if (snapIn) {
				long hektoMeters = Math.round(distance*10);
				distance = hektoMeters / 10.0;
			}
			splitTime.setDistance(Formatter.formatDistance(distance));
			splitTime.setDistanceInMeters(distance);
			
			totalDistance += distance;
			splitTime.setTotalDistance(Formatter.formatDistance(totalDistance));
			
			double pace = seconds / distance;
			splitTime.setPace(Formatter.formatPace(pace));
			splitTime.setPaceInSeconds(pace);

			if (activityLap.getAverageHeartRateBpm() != null) {
				splitTime.setAverageHeartRate(Integer.toString(activityLap.getAverageHeartRateBpm()));
				splitTime.setMaximumHeartRate(Integer.toString(activityLap.getMaximumHeartRateBpm()));
			} else {
				splitTime.setAverageHeartRate(Const.EMPTY);
				splitTime.setMaximumHeartRate(Const.EMPTY);
			}
			
			ArrayList<Lap> lst = new ArrayList<>();
			lst.add(activityLap);
			Altitude altitude = calculateAltitude(lst);
			if (altitude != null) {
				splitTime.setAscent(Long.toString(altitude.ascent));
				splitTime.setDescent(Long.toString(altitude.descent));
			} else {
				splitTime.setAscent(Const.EMPTY);
				splitTime.setDescent(Const.EMPTY);
			}
			
			laplist.add(splitTime);
			++round;
		}
		return laplist;
	}
	
	/* (non-Javadoc)
	 * @see de.acwhadk.rz.filemyrun.gui.ActivityX#deleteToHere(com.garmin.tcdbv2.TrackPoint)
	 */
	@Override
	public void deleteToHere(TrackPoint tp) throws Exception {
		boolean tpFound = false;
		Iterator<Lap> itLap = getLaps().iterator();
		while(itLap.hasNext()) {
			Lap lap = itLap.next();
			if (tpFound) {
				continue;
			}
			Track track = lap.getTrack();
			Iterator<TrackPoint> itTp = track.getTrackpoints().iterator();
			while(itTp.hasNext()) {
				TrackPoint curTp = itTp.next();
				if (!tpFound) {
					itTp.remove();
				}
				if (curTp == tp) {
					tpFound = true;
				}
			}
			if (!tpFound) {
				itLap.remove();
				continue;
			}
			if (tpFound) {
				calculateTimeAndDist(lap);
			}
		}
		setDistance(null);
		getDistance();
	}

	/* (non-Javadoc)
	 * @see de.acwhadk.rz.filemyrun.gui.ActivityX#deleteToEnd(com.garmin.tcdbv2.TrackPoint)
	 */
	@Override
	public void deleteToEnd(TrackPoint tp) throws Exception {
		boolean tpFound = false;
		Iterator<Lap> itLap = getLaps().iterator();
		while(itLap.hasNext()) {
			Lap lap = itLap.next();
			if (tpFound) {
				itLap.remove();
				continue;
			}
			Track track = lap.getTrack();
			Iterator<TrackPoint> itTp = track.getTrackpoints().iterator();
			while(itTp.hasNext()) {
				TrackPoint curTp = itTp.next();
				if (curTp == tp) {
					tpFound = true;
				}
				if (tpFound) {
					itTp.remove();
					continue;
				}
			}
			if (tpFound) {
				calculateTimeAndDist(lap);
			}
		}
		setDistance(null);
		getDistance();
	}
		
	/* (non-Javadoc)
	 * @see de.acwhadk.rz.filemyrun.gui.ActivityX#joinLap(int)
	 */
	@Override
	public void joinLap(int i) {
		if (i<0 || i>getLaps().size()-2) {
			return;
		}
		List<Lap> laps = getLaps();
		Lap lap1 = laps.get(i);
		Lap lap2 = laps.get(i+1);
		List<TrackPoint> tp1 = lap1.getTrack().getTrackpoints();
		List<TrackPoint> tp2 = lap2.getTrack().getTrackpoints();
		tp1.addAll(tp2);
		laps.remove(i+1);
		calculateTimeAndDist(lap1);
		calculateHeartRate(lap1);
	}

	/* (non-Javadoc)
	 * @see de.acwhadk.rz.filemyrun.gui.ActivityX#splitLapHalfTime(int)
	 */
	@Override
	public void splitLapHalfTime(int i) {
		if (i<0 || i>getLaps().size()-1) {
			return;
		}
		List<Lap> laps = getLaps();
		Lap lap1 = laps.get(i);
		Lap lap2 = getObjectFactory().createLap();
		Track track = getObjectFactory().createTrack();
		lap2.setTrack(track);
		long start = lap1.getStartTime().getTimeInMillis();
		long halfTime = start + Math.round((lap1.getTotalTimeSeconds()*1000)/2.);
		List<TrackPoint> tpList = lap1.getTrack().getTrackpoints();
		Iterator<TrackPoint> it = tpList.iterator();
		while (it.hasNext()) {
			TrackPoint tp = it.next();
			long tpTime = tp.getTime().getTimeInMillis();
			if (tpTime > halfTime) {
				track.getTrackpoints().add(tp);
				if (lap2.getStartTime() == null) {
					lap2.setStartTime(tp.getTime());
				}
				it.remove();
			}
		}
		laps.add(i+1, lap2);
		calculateTimeAndDist(lap1);
		calculateHeartRate(lap1);
		calculateTimeAndDist(lap2);
		calculateHeartRate(lap2);
	}
	
	/* (non-Javadoc)
	 * @see de.acwhadk.rz.filemyrun.gui.ActivityX#splitLapHalfDist(int)
	 */
	@Override
	public void splitLapHalfDist(int i) {
		if (i<0 || i>getLaps().size()-1) {
			return;
		}
		List<Lap> laps = getLaps();
		Lap lap1 = laps.get(i);
		TrackPoint tp1 = lap1.getTrack().getTrackpoints().get(0);
		double start = tp1.getDistanceMeters() == null ? 0.0 : tp1.getDistanceMeters(); 
		double halfDist = start + lap1.getDistanceMeters()/2.;
		
		splitLapAtDist(laps, i, halfDist);
	}
	
	/* (non-Javadoc)
	 * @see de.acwhadk.rz.filemyrun.gui.ActivityX#splitLapAtDist(int, double)
	 */
	@Override
	public void splitLapAtDist(int i, double atDist) {
		if (i<0 || i>getLaps().size()-1) {
			return;
		}
		// normalize to km
		double dist = atDist < 100.0 ? atDist * 1000.0 : atDist; 
		List<Lap> laps = getLaps();
		Lap lap1 = laps.get(i);
		TrackPoint tp1 = lap1.getTrack().getTrackpoints().get(0);
		double start = tp1.getDistanceMeters() == null ? 0.0 : tp1.getDistanceMeters();
		// normalize to totalDistance		
		if (dist < start) {
			dist += start;
		}
		splitLapAtDist(laps, i, dist);
	}
	
	private void splitLapAtDist(List<Lap> laps, int i, double dist) {
		Lap lap1 = laps.get(i);
		Lap lap2 = getObjectFactory().createLap();
		Track track = getObjectFactory().createTrack();
		lap2.setTrack(track);
		List<TrackPoint> tpList = lap1.getTrack().getTrackpoints();
		Calendar lastTPTime = laps.get(0).getStartTime();
		Iterator<TrackPoint> it = tpList.iterator();
		while (it.hasNext()) {
			TrackPoint tp = it.next();
			// remove useless track points
			if (tp.getDistanceMeters() == null) {
				it.remove();
				continue;
			}
			double tpDist = tp.getDistanceMeters() == null ? 0.0 : tp.getDistanceMeters();
			if (tpDist > dist) {
				track.getTrackpoints().add(tp);
				if (lap2.getStartTime() == null) {
					lap2.setStartTime(lastTPTime);
				}
				it.remove();
			}
			lastTPTime = tp.getTime();
		}
		if (track.getTrackpoints().isEmpty()) {
			throw new IllegalArgumentException(Lang.get().text(Lang.DISTANCE_OUT_OF_RANGE));
		}
		laps.add(i+1, lap2);
		calculateTimeAndDist(lap1);
		calculateHeartRate(lap1);
		calculateTimeAndDist(lap2);
		calculateHeartRate(lap2);
	}
	
	private void calculateHeartRate(Lap lap) {
		List<TrackPoint> tpList = lap.getTrack().getTrackpoints();
		int cnt = 0;
		int sum = 0;
		int max = 0;
		for(TrackPoint tp : tpList) {
			Integer hr = tp.getHeartRateBpm();
			if (hr != null) {
				if (hr > max) {
					max = hr;
				}
				sum += hr;
				++cnt;
			}
		}
		if (cnt > 0) {
			lap.setAverageHeartRateBpm((int)Math.round((double)sum/cnt));
			lap.setMaximumHeartRateBpm(max);
		} else {
			lap.setAverageHeartRateBpm(null);
			lap.setMaximumHeartRateBpm(null);
		}
	}

	private void calculateTimeAndDist(Lap lap) {
		TrackPoint firstTP = getLastTPOfPreviousLap(lap);
		if (firstTP.getDistanceMeters() == null) {
			return;
		}
		Track track = lap.getTrack();
		Calendar startTime = lap.getStartTime();
		int lastIndex = track.getTrackpoints().size()-1;
		TrackPoint lastTP = track.getTrackpoints().get(lastIndex);
		Calendar endTime = lastTP.getTime();
		while (lastTP.getDistanceMeters() == null && lastIndex > 0) {
			lastTP = track.getTrackpoints().get(--lastIndex);
		}
		double totalDistance = lastTP.getDistanceMeters() - firstTP.getDistanceMeters();
		Date end = endTime.getTime();
		Date start = startTime.getTime();
		double totalTimeSeconds = (end.getTime() - start.getTime()) / 1000.;
		
		lap.setDistanceMeters(totalDistance);
		lap.setTotalTimeSeconds(totalTimeSeconds);
	}

	private TrackPoint getLastTPOfPreviousLap(Lap lap) {
		int i=0;
		for(Lap l : getLaps()) {
			if (l == lap) {
				break;
			}
			++i;
		}
		if (i == 0) {
			TrackPoint t = getObjectFactory().createTrackPoint();
			t.setTime(lap.getStartTime());
			t.setDistanceMeters(0.0);
			return t;
		}
		Lap prevLap = getLaps().get(--i);
		List<TrackPoint> tpList = prevLap.getTrack().getTrackpoints();
		for(int j=tpList.size()-1; j>=0; --j) {
			TrackPoint tp = tpList.get(j);
			if (tp.getDistanceMeters() != null) {
				return tp;
			}
		}
		return null;
	}

	private void calculateAltitude() {
		if (totalAltitude != null) {
			return;
		}
		List<Lap> laps = getLaps();
		totalAltitude = calculateAltitude(laps);
	}
	
	private Altitude calculateAltitude(List<Lap> laps) {
		Altitude altitude = new Altitude();
		altitude.valid = false;
		
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
		altitude.min = Math.round(min);
		altitude.max = Math.round(max);
		altitude.ascent = Math.round(ascent);
		altitude.descent = Math.round(descent);
		altitude.valid = true;
		
		return altitude;
	}
	
	private class Altitude {
		long min;
		long max;
		long ascent;
		long descent;
		boolean valid;
	}
	
	protected abstract ObjectFactory getObjectFactory();
}
