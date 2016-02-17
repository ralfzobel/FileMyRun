package de.acwhadk.rz.filemyrun.controller;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.xml.datatype.XMLGregorianCalendar;

import com.garmin.tcdbv2.ActivityLapT;
import com.garmin.tcdbv2.ActivityListT;
import com.garmin.tcdbv2.ActivityT;
import com.garmin.tcdbv2.HeartRateInBeatsPerMinuteT;
import com.garmin.tcdbv2.TrackT;
import com.garmin.tcdbv2.TrackpointT;
import com.garmin.tcdbv2.TrainingCenterDatabaseT;

import de.acwhadk.rz.filemyrun.data.TrainingActivity;
import de.acwhadk.rz.filemyrun.file.TrainingFile;
import de.acwhadk.rz.filemyrun.xml.TrainingActivityToXML;

public class Activity {

	private static final double THRESHOLD = 5.0;
	private TrainingActivity trainingActivity;
	private TrainingCenterDatabaseT tcxData;
	private ActivityT tcxActivity;
	private String filename;
	
	private Altitude totalAltitude;
	
	public Activity(TrainingFile tf) throws Exception {
		filename = tf.getTrainingFile();
		trainingActivity = TrainingActivityToXML.load(new File(filename));
		tcxData = trainingActivity.getTrainingCenterDatabase();
		ActivityListT activityList = tcxData.getActivities();
		if (activityList.getActivity().size() != 1) {
			throw new RuntimeException("TCX File contains " + activityList.getActivity().size() + " activities. Should be exactly one activity");
		}
		tcxActivity = activityList.getActivity().get(0);
	}

	public TrainingActivity getTrainingActivity() {
		return trainingActivity;
	}

	public String getName() {
		return trainingActivity.getName() == null ? "<n/a>" : trainingActivity.getName();
	}
	
	public void setName(String name) {
		trainingActivity.setName(name);
	}
	
	public String getDescription() {
		return trainingActivity.getDescription() == null ? "" : trainingActivity.getDescription();
	}
	
	public void setDescription(String description) {
		trainingActivity.setDescription(description);
	}
	
	public Date getDate() {
		GregorianCalendar cal = tcxActivity.getId().toGregorianCalendar(null, null, null);
		return cal.getTime();
	}

	public String getType() {
		String type = trainingActivity.getType();
		if (type == null || type.isEmpty()) {
			if (tcxActivity != null) {
				trainingActivity.setType(tcxActivity.getSport().toString());
			} else {
				trainingActivity.setType("");
			}
		}
		return trainingActivity.getType();
	}
	
	public void setType(String type) {
		trainingActivity.setType(type);
	}
	
	public Double getDistance() throws Exception {
		if (trainingActivity.getDistance() == null) {
			double distance = getTrackDistance();
			distance /= 1000.0;
			trainingActivity.setDistance(distance);
			save();
		}
		return trainingActivity.getDistance();
	}

	public double getTrackDistance() throws Exception {
		double distance = 0.0;
		List<ActivityLapT> laps = tcxActivity.getLap();
		for(ActivityLapT lap : laps) {
			distance += lap.getDistanceMeters();
		}
		return distance;
	}


	public void setDistance(String dist) {
		String distance = dist.replace(',', '.').replace("km", "").trim();
		try {
			double d = Double.parseDouble(distance);
			trainingActivity.setDistance(d);
		} catch(NumberFormatException e) {
			// ignore
		}
	}
	
	public long getTotalTime() {
		double time = 0.0;
		List<ActivityLapT> laps = tcxActivity.getLap();
		for(ActivityLapT lap : laps) {
			time += lap.getTotalTimeSeconds();
		}
		return Math.round(time);
	}
	
	public double getPace() throws Exception {
		return getTotalTime() / getDistance();
	}
	
	public Long getAverageHeartRate() {
		double heartrate = 0.0;
		double time = 0.0;
		List<ActivityLapT> laps = tcxActivity.getLap();
		for(ActivityLapT lap : laps) {
			if (lap.getMaximumHeartRateBpm() == null) {
				return null;
			}
			heartrate += lap.getAverageHeartRateBpm().getValue() * lap.getTotalTimeSeconds();
			time += lap.getTotalTimeSeconds();
		}
		return Math.round(heartrate / time);
	}
	
	public Long getMaximumHeartRate() {
		long heartrate = 0L;
		List<ActivityLapT> laps = tcxActivity.getLap();
		for(ActivityLapT lap : laps) {
			if (lap.getMaximumHeartRateBpm() == null) {
				return null;
			}
			long hr = lap.getMaximumHeartRateBpm().getValue();
			if (hr > heartrate) {
				heartrate = hr;
			}
		}
		return heartrate;
	}

	public Long getMaximumAltitude() {
		calculateAltitude();
		return totalAltitude != null && totalAltitude.valid ? totalAltitude.max : null;
	}
	
	public Long getMinimumAltitude() {
		calculateAltitude();
		return totalAltitude != null && totalAltitude.valid ? totalAltitude.min : null;
	}
	
	public Long getAscent() {
		calculateAltitude();
		return totalAltitude != null && totalAltitude.valid ? totalAltitude.ascent : null;
	}
	
	public Long getDescent() {
		calculateAltitude();
		return totalAltitude != null && totalAltitude.valid ? totalAltitude.descent : null;
	}
	
	public int getCalories() {
		int calories = 0;
		List<ActivityLapT> laps = tcxActivity.getLap();
		for(ActivityLapT lap : laps) {
			calories += lap.getCalories();
		}
		return calories;
	}
	
	public void save() throws Exception {
		TrainingActivityToXML.save(filename, trainingActivity);
	}

	public Map<String, List<TrackpointT>> getTrackPoints() {
		Map<String, List<TrackpointT>> tpMap = new TreeMap<>();
		for(ActivityLapT lap : tcxActivity.getLap()) {
			List<TrackpointT> tpList = new ArrayList<>();
			GregorianCalendar cal = lap.getStartTime().toGregorianCalendar(null, null, null);
			tpMap.put(Formatter.formatDate(cal.getTime()), tpList );
			for( TrackT trk : lap.getTrack()) {
				if (trk.getTrackpoint() == null) {
					continue;
				}
				for(TrackpointT tp : trk.getTrackpoint()) {
					tpList.add(tp);
				}
			}
		}
		return tpMap;
	}
	
	public List<Lap> getLaps(boolean snapIn) {
		List<Lap> laplist = new ArrayList<>();
		List<ActivityLapT> activityLaps = tcxActivity.getLap();
		double totalSeconds = 0.0;
		double totalDistance = 0.0;
		int round = 1;
		for(ActivityLapT activityLap : activityLaps) {
			Lap lap = new Lap();
			lap.setRound(Integer.toString(round));
			
			double seconds = activityLap.getTotalTimeSeconds();
			lap.setTime(Formatter.formatSeconds(seconds));
			lap.setTimeInSeconds(seconds);

			totalSeconds += seconds;
			lap.setTotalTime(Formatter.formatSeconds(totalSeconds));
			
			double distance = activityLap.getDistanceMeters()/1000.0;
			if (snapIn) {
				long hektoMeters = Math.round(distance*10);
				distance = hektoMeters / 10.0;
			}
			lap.setDistance(Formatter.formatDistance(distance));
			lap.setDistanceInMeters(distance);
			
			totalDistance += distance;
			lap.setTotalDistance(Formatter.formatDistance(totalDistance));
			
			double pace = seconds / distance;
			lap.setPace(Formatter.formatPace(pace));
			lap.setPaceInSeconds(pace);

			if (activityLap.getAverageHeartRateBpm() != null) {
				lap.setAverageHeartRate(Short.toString(activityLap.getAverageHeartRateBpm().getValue()));
				lap.setMaximumHeartRate(Short.toString(activityLap.getMaximumHeartRateBpm().getValue()));
			} else {
				lap.setAverageHeartRate("");
				lap.setMaximumHeartRate("");
			}
			
			ArrayList<ActivityLapT> lst = new ArrayList<>();
			lst.add(activityLap);
			Altitude altitude = calculateAltitude(lst);
			if (altitude != null) {
				lap.setAscent(Long.toString(altitude.ascent));
				lap.setDescent(Long.toString(altitude.descent));
			} else {
				lap.setAscent("");
				lap.setDescent("");
			}
			
			laplist.add(lap);
			++round;
		}
		return laplist;
	}
	
	public void deleteToHere(TrackpointT tp) throws Exception {
		boolean tpFound = false;
		Iterator<ActivityLapT> itLap = tcxActivity.getLap().iterator();
		while(itLap.hasNext()) {
			ActivityLapT lap = itLap.next();
			if (tpFound) {
				continue;
			}
			TrackT track = lap.getTrack().get(0);
			Iterator<TrackpointT> itTp = track.getTrackpoint().iterator();
			while(itTp.hasNext()) {
				TrackpointT curTp = itTp.next();
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
		trainingActivity.setDistance(null);
		getDistance();
	}

	public void deleteToEnd(TrackpointT tp) throws Exception {
		boolean tpFound = false;
		Iterator<ActivityLapT> itLap = tcxActivity.getLap().iterator();
		while(itLap.hasNext()) {
			ActivityLapT lap = itLap.next();
			if (tpFound) {
				itLap.remove();
				continue;
			}
			TrackT track = lap.getTrack().get(0);
			Iterator<TrackpointT> itTp = track.getTrackpoint().iterator();
			while(itTp.hasNext()) {
				TrackpointT curTp = itTp.next();
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
		trainingActivity.setDistance(null);
		getDistance();
	}
		
	public void joinLap(int i) {
		if (i<0 || i>tcxActivity.getLap().size()-2) {
			return;
		}
		List<ActivityLapT> laps = tcxActivity.getLap();
		ActivityLapT lap1 = laps.get(i);
		ActivityLapT lap2 = laps.get(i+1);
		List<TrackpointT> tp1 = lap1.getTrack().get(0).getTrackpoint();
		List<TrackpointT> tp2 = lap2.getTrack().get(0).getTrackpoint();
		tp1.addAll(tp2);
		laps.remove(i+1);
		calculateTimeAndDist(lap1);
		calculateHeartRate(lap1);
	}

	public void splitLapHalfTime(int i) {
		if (i<0 || i>tcxActivity.getLap().size()-1) {
			return;
		}
		List<ActivityLapT> laps = tcxActivity.getLap();
		ActivityLapT lap1 = laps.get(i);
		ActivityLapT lap2 = new ActivityLapT();
		TrackT track = new TrackT();
		lap2.getTrack().add(track);
		long start = lap1.getStartTime().toGregorianCalendar().getTimeInMillis();
		long halfTime = start + Math.round((lap1.getTotalTimeSeconds()*1000)/2.);
		List<TrackpointT> tpList = lap1.getTrack().get(0).getTrackpoint();
		Iterator<TrackpointT> it = tpList.iterator();
		while (it.hasNext()) {
			TrackpointT tp = it.next();
			long tpTime = tp.getTime().toGregorianCalendar().getTimeInMillis();
			if (tpTime > halfTime) {
				track.getTrackpoint().add(tp);
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
	
	public void splitLapHalfDist(int i) {
		if (i<0 || i>tcxActivity.getLap().size()-1) {
			return;
		}
		List<ActivityLapT> laps = tcxActivity.getLap();
		ActivityLapT lap1 = laps.get(i);
		TrackpointT tp1 = lap1.getTrack().get(0).getTrackpoint().get(0);
		double start = tp1.getDistanceMeters() == null ? 0.0 : tp1.getDistanceMeters(); 
		double halfDist = start + lap1.getDistanceMeters()/2.;
		
		splitLapAtDist(laps, i, halfDist);
	}
	
	public void splitLapAtDist(int i, double atDist) {
		if (i<0 || i>tcxActivity.getLap().size()-1) {
			return;
		}
		// normalize to km
		double dist = atDist < 100.0 ? atDist * 1000.0 : atDist; 
		List<ActivityLapT> laps = tcxActivity.getLap();
		ActivityLapT lap1 = laps.get(i);
		TrackpointT tp1 = lap1.getTrack().get(0).getTrackpoint().get(0);
		double start = tp1.getDistanceMeters() == null ? 0.0 : tp1.getDistanceMeters();
		// normalize to totalDistance		
		if (dist < start) {
			dist += start;
		}
		splitLapAtDist(laps, i, dist);
	}
	
	private void splitLapAtDist(List<ActivityLapT> laps, int i, double dist) {
		ActivityLapT lap1 = laps.get(i);
		ActivityLapT lap2 = new ActivityLapT();
		TrackT track = new TrackT();
		lap2.getTrack().add(track);
		List<TrackpointT> tpList = lap1.getTrack().get(0).getTrackpoint();
		Iterator<TrackpointT> it = tpList.iterator();
		while (it.hasNext()) {
			TrackpointT tp = it.next();
			double tpDist = tp.getDistanceMeters() == null ? 0.0 : tp.getDistanceMeters();
			if (tpDist > dist) {
				track.getTrackpoint().add(tp);
				if (lap2.getStartTime() == null) {
					lap2.setStartTime(tp.getTime());
				}
				it.remove();
			}
		}
		if (track.getTrackpoint().isEmpty()) {
			throw new IllegalArgumentException("Distance is out of range.");
		}
		laps.add(i+1, lap2);
		calculateTimeAndDist(lap1);
		calculateHeartRate(lap1);
		calculateTimeAndDist(lap2);
		calculateHeartRate(lap2);
	}
	
	private void calculateHeartRate(ActivityLapT lap) {
		List<TrackpointT> tpList = lap.getTrack().get(0).getTrackpoint();
		int cnt = 0;
		int sum = 0;
		short max = 0;
		for(TrackpointT tp : tpList) {
			HeartRateInBeatsPerMinuteT hr = tp.getHeartRateBpm();
			if (hr != null) {
				short f = hr.getValue();
				if (f > max) {
					max = f;
				}
				sum += f;
				++cnt;
			}
		}
		if (cnt > 0) {
			HeartRateInBeatsPerMinuteT hrBpm = new HeartRateInBeatsPerMinuteT();
			hrBpm.setValue((short) Math.round((double)sum/cnt));
			lap.setAverageHeartRateBpm(hrBpm);
			hrBpm = new HeartRateInBeatsPerMinuteT();
			hrBpm.setValue(max);
			lap.setMaximumHeartRateBpm(hrBpm);
		} else {
			lap.setAverageHeartRateBpm(null);
			lap.setMaximumHeartRateBpm(null);
		}
	}

	private void calculateTimeAndDist(ActivityLapT lap) {
		TrackT track = lap.getTrack().get(0);
		Iterator<TrackpointT> itTp = track.getTrackpoint().iterator();
		TrackpointT firstTP = track.getTrackpoint().get(0);
		XMLGregorianCalendar startTime = firstTP.getTime();
		while (firstTP.getDistanceMeters() == null && itTp.hasNext()) {
			firstTP = itTp.next();
		}
		if (firstTP.getDistanceMeters() == null) {
			return;
		}
		int lastIndex = track.getTrackpoint().size()-1;
		TrackpointT lastTP = track.getTrackpoint().get(lastIndex);
		XMLGregorianCalendar endTime = lastTP.getTime();
		while (lastTP.getDistanceMeters() == null && lastIndex > 0) {
			lastTP = track.getTrackpoint().get(--lastIndex);
		}
		double totalDistance = lastTP.getDistanceMeters() - firstTP.getDistanceMeters();
		Date end = endTime.toGregorianCalendar().getTime();
		Date start = startTime.toGregorianCalendar().getTime();
		double totalTimeSeconds = (end.getTime() - start.getTime()) / 1000.;
		
		lap.setDistanceMeters(totalDistance);
		lap.setTotalTimeSeconds(totalTimeSeconds);
	}

	private void calculateAltitude() {
		if (totalAltitude != null) {
			return;
		}
		List<ActivityLapT> laps = tcxActivity.getLap();
		totalAltitude = calculateAltitude(laps);
	}
	
	private Altitude calculateAltitude(List<ActivityLapT> laps) {
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
		for(ActivityLapT lap : laps) {
			if (lap.getTrack() == null) {
				return null;
			}
			for( TrackT trk : lap.getTrack()) {
				if (trk.getTrackpoint() == null) {
					continue;
				}
				for(TrackpointT tp : trk.getTrackpoint()) {
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

}
