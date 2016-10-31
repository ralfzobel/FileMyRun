package de.acwhadk.rz.filemyrun.xml.model;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;

import org.opengis.referencing.FactoryException;
import org.opengis.referencing.operation.TransformException;

import com.garmin.tcdbv2.ActivityLapT;
import com.garmin.tcdbv2.ActivityListT;
import com.garmin.tcdbv2.ActivityT;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.PrecisionModel;

import de.acwhadk.rz.filemyrun.core.model.Lap;
import de.acwhadk.rz.filemyrun.core.model.ObjectFactory;
import de.acwhadk.rz.filemyrun.core.model.Position;
import de.acwhadk.rz.filemyrun.core.model.Track;
import de.acwhadk.rz.filemyrun.core.model.TrackPoint;
import de.acwhadk.rz.filemyrun.core.model.TrainingFile;
import de.acwhadk.rz.filemyrun.core.setup.Const;
import de.acwhadk.rz.filemyrun.core.setup.Lang;
import de.acwhadk.rz.filemyrun.core.tools.CoordinateTransformer;
import de.acwhadk.rz.filemyrun.gui.AbstractActivity;
import de.acwhadk.rz.filemyrun.xml.data.TrainingActivity;
import de.acwhadk.rz.filemyrun.xml.tools.TrainingActivityToXML;

/**
 * This class provides the methods that the gui needs.
 * It implements getters for data that may be used at it is (e.g. name).
 * It implements methods that calculate the data from the raw data (most of the methods do that).
 * It supplies methods to manipulate the data (e.g. setters and the split* methods).
 *  
 * @author Ralf
 *
 */
public class ActivityImpl extends AbstractActivity {

	private static final int SRID = 31466;
	private TrainingActivity trainingActivity;
	private ActivityT tcxActivity;
	private String filename;
	private ObjectFactory objectFactory;
	
	public ActivityImpl(TrainingFile tf, ObjectFactory objectFactory) throws Exception {
		filename = tf.getTrainingFile();
		trainingActivity = TrainingActivityToXML.load(new File(filename));
		ActivityListT activityList = trainingActivity.getTrainingCenterDatabase().getActivities();
		if (activityList.getActivity().size() != 1) {
			String msg = String.format(Lang.get().text(Lang.NOT_ONE_ACTIVICTY), activityList.getActivity().size());
			throw new RuntimeException(msg);
		}
		tcxActivity = activityList.getActivity().get(0);
	}

	/* (non-Javadoc)
	 * @see de.acwhadk.rz.filemyrun.gui.ActivityX#getName()
	 */
	@Override
	public String getName() {
		return trainingActivity.getName() == null ? 
				Lang.get().text(Lang.NOT_AVAILABLE) : trainingActivity.getName();
	}
	
	/* (non-Javadoc)
	 * @see de.acwhadk.rz.filemyrun.gui.ActivityX#setName(java.lang.String)
	 */
	@Override
	public void setName(String name) {
		trainingActivity.setName(name);
	}
	
	/* (non-Javadoc)
	 * @see de.acwhadk.rz.filemyrun.gui.ActivityX#getDescription()
	 */
	@Override
	public String getDescription() {
		return trainingActivity.getDescription() == null ? Const.EMPTY : trainingActivity.getDescription();
	}
	
	/* (non-Javadoc)
	 * @see de.acwhadk.rz.filemyrun.gui.ActivityX#setDescription(java.lang.String)
	 */
	@Override
	public void setDescription(String description) {
		trainingActivity.setDescription(description);
	}
	
	/* (non-Javadoc)
	 * @see de.acwhadk.rz.filemyrun.gui.ActivityX#getDate()
	 */
	@Override
	public Date getDate() {
		GregorianCalendar cal = tcxActivity.getId().toGregorianCalendar(null, null, null);
		return cal.getTime();
	}

	/* (non-Javadoc)
	 * @see de.acwhadk.rz.filemyrun.gui.ActivityX#getType()
	 */
	@Override
	public String getType() {
		String type = trainingActivity.getType();
		if (type == null || type.isEmpty()) {
			if (tcxActivity != null) {
				trainingActivity.setType(tcxActivity.getSport().toString());
			} else {
				trainingActivity.setType(Const.EMPTY);
			}
		}
		return trainingActivity.getType();
	}
	
	/* (non-Javadoc)
	 * @see de.acwhadk.rz.filemyrun.gui.ActivityX#setType(java.lang.String)
	 */
	@Override
	public void setType(String type) {
		trainingActivity.setType(type);
	}
	
	/* (non-Javadoc)
	 * @see de.acwhadk.rz.filemyrun.gui.ActivityX#getDistance()
	 */
	@Override
	public Double getDistance() throws Exception {
		if (trainingActivity.getDistance() == null) {
			double distance = getTrackDistance();
			if (distance > 0.0) {
				distance /= 1000.0;
				trainingActivity.setDistance(distance);
			} else {
				calculateAll();
			}
			save();
		}
		return trainingActivity.getDistance();
	}
	
	@Override
	public void setTotalTime(long t) {
		
	}
	/* (non-Javadoc)
	 * @see de.acwhadk.rz.filemyrun.gui.ActivityX#setDistance(java.lang.String)
	 */
	@Override
	public void setDistance(Double dist) {
		trainingActivity.setDistance(dist);
	}
	public void setDistance(String dist) {
		String distance = dist.replace(',', '.').
				replace(Lang.get().text(Lang.DISTANCE_ABBREVIATED), Const.EMPTY).trim();
		try {
			double d = Double.parseDouble(distance);
			trainingActivity.setDistance(d);
		} catch(NumberFormatException e) {
			// ignore
		}
	}
	
	/* (non-Javadoc)
	 * @see de.acwhadk.rz.filemyrun.gui.ActivityX#save()
	 */
	@Override
	public void save() throws Exception {
		TrainingActivityToXML.save(filename, trainingActivity);
	}

	/* (non-Javadoc)
	 * @see de.acwhadk.rz.filemyrun.gui.ActivityX#getLaps(boolean)
	 */
	@Override
	public List<Lap> getLaps() {
		List<Lap> lapList = new ArrayList<>();
		for(ActivityLapT lap : tcxActivity.getLap()) {
			LapImpl l = new LapImpl(lap);
			lapList.add(l);
		}
		return lapList;
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

	private void calculateTimeAndDistByCoors(Lap lap) throws FactoryException, TransformException {
		Track track = lap.getTrack();
		Calendar startTime = lap.getStartTime();
		Calendar endTime = null;
		List<Coordinate> coorList = new ArrayList<>();
		for(TrackPoint tp : track.getTrackpoints()) {
			Position pos = tp.getPosition();
			Coordinate c = new Coordinate(pos.getLatitude(), pos.getLongitude());
			coorList.add(c);
			endTime = tp.getTime();
		}
		CoordinateTransformer trans = new CoordinateTransformer(SRID);		
		Coordinate[] coors = trans.transform(coorList.toArray(new Coordinate[0]));
		GeometryFactory f = new GeometryFactory(new PrecisionModel(), SRID);
		LineString line = f.createLineString(coors);
		Date end = endTime.getTime();
		Date start = startTime.getTime();
		double totalTimeSeconds = (end.getTime() - start.getTime()) / 1000.;
		
		lap.setDistanceMeters(line.getLength());
		lap.setTotalTimeSeconds(totalTimeSeconds);
	}

	private void calculateAll() throws FactoryException, TransformException {
		double time = 0.0;
		double dist = 0.0;
		for(Lap lap : getLaps()) {
			calculateTimeAndDistByCoors(lap);
			calculateHeartRate(lap);
			dist += lap.getDistanceMeters();
			time += lap.getTotalTimeSeconds();
		}
		trainingActivity.setDistance(dist / 1000.0);
		long t = new Double(time).longValue() / 1000;
		trainingActivity.setTime(new Date(t));
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

	@Override
	protected ObjectFactory getObjectFactory() {
		return objectFactory;
	}

}
