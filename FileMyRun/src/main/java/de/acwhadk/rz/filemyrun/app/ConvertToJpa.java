package de.acwhadk.rz.filemyrun.app;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.SortedMap;

import javax.persistence.EntityManager;

import org.apache.log4j.Logger;

import de.acwhadk.rz.filemyrun.core.gui.ExceptionDialog;
import de.acwhadk.rz.filemyrun.core.gui.ProgressDialog;
import de.acwhadk.rz.filemyrun.core.model.Activity;
import de.acwhadk.rz.filemyrun.core.model.Lap;
import de.acwhadk.rz.filemyrun.core.model.ObjectFactory;
import de.acwhadk.rz.filemyrun.core.model.Position;
import de.acwhadk.rz.filemyrun.core.model.TrackPoint;
import de.acwhadk.rz.filemyrun.core.model.TrainingFile;
import de.acwhadk.rz.filemyrun.core.model.TrainingFileMan;
import de.acwhadk.rz.filemyrun.core.setup.Lang;
import de.acwhadk.rz.filemyrun.jpa.data.ActivityData;
import de.acwhadk.rz.filemyrun.jpa.data.EquipmentItem;
import de.acwhadk.rz.filemyrun.jpa.data.EquipmentType;
import de.acwhadk.rz.filemyrun.jpa.data.Track;
import de.acwhadk.rz.filemyrun.jpa.model.ObjectFactoryImpl;
import de.acwhadk.rz.filemyrun.jpa.model.TrainingFileManImpl;
import de.acwhadk.rz.filemyrun.xml.equipment.EquipmentUsedEntry;
import de.acwhadk.rz.filemyrun.xml.model.EquipmentManImpl;
import javafx.concurrent.Task;

public class ConvertToJpa {

	final static Logger logger = Logger.getLogger(ConvertToJpa.class);

	private static final int COUNT = 10000;

	public static void convert(ObjectFactory objectFactoryXml, ObjectFactoryImpl objectFactoryJpa) {
		ProgressDialog pb = new ProgressDialog(Lang.get().text(Lang.PROCESS_DLG_CONVERSION));
		Task<Void> task = new Task<Void>() {
			@Override
			public Void call() {
				convert();
				return null;            	
			}
			private void convert() {
				TrainingFileMan trainingFileMan = objectFactoryXml.createTrainingFileMan();
				SortedMap<Date, TrainingFile> trainingFiles = trainingFileMan.getTrainingFiles();

				EntityManager em = objectFactoryJpa.getEntityManager();
				em.getTransaction().begin();
				try {
					int i=0;
					double max = trainingFiles.values().size();
					if (max > COUNT) {
						max = COUNT;
					}
					for(TrainingFile file : trainingFiles.values()) {
						logger.info("converting " + i + ": " + file.getTime() + ":" + file.getName());
						Activity activity = objectFactoryXml.createActivity(file);

						de.acwhadk.rz.filemyrun.jpa.data.ActivityData activityData = createActivityData(activity);
						em.persist(activityData);

						de.acwhadk.rz.filemyrun.jpa.data.Activity activityHead = createActivityHead(activity);
						activityHead.setActivityData(activityData);
						em.persist(activityHead);
						activityData.setActivity(activityHead);

						List<de.acwhadk.rz.filemyrun.jpa.data.Lap> lapList = new ArrayList<>();
						for(Lap lap : activity.getLaps()) {
							de.acwhadk.rz.filemyrun.jpa.data.Lap lapJpa = createLap(lap);
							lapJpa.setActivity(activityData);
							lapJpa.setSeqNum(lapList.size());
							em.persist(lapJpa);

							de.acwhadk.rz.filemyrun.jpa.data.Track track = createTrack(lap.getTrack());
							em.persist(track);
							lapJpa.setTrack(track);

							lapList.add(lapJpa);
						}
						activityData.setLaps(lapList);

						if (i / max > 0.1) {
							updateProgress(i, max);
						}
						// TODO remove
						if (++i > COUNT) {
							logger.warn("conversion stopped after " + COUNT + " activities");
							break;
						}
					}
					logger.info("committing converted activities");
					em.getTransaction().commit();
					logger.info("conversion of activities finished");
				} catch(Exception e) {
					em.getTransaction().rollback();
					Exception e2 = new RuntimeException(e.getMessage(), e);
					ExceptionDialog.showException(e2);
				}
				// TODO remove
				//        		objectFactoryJpa.close();
				//        		logger.warn("SHUTDOWN, please restart");
				//        		System.exit(0);
			}
		};
		task.setOnSucceeded(event -> { pb.close(); } );

		Thread thread = new Thread(task);
		thread.start();
		pb.activateProgressBar(task);
	}


	private static de.acwhadk.rz.filemyrun.jpa.data.Activity createActivityHead(Activity activity) {
		de.acwhadk.rz.filemyrun.jpa.data.Activity activityHead = new de.acwhadk.rz.filemyrun.jpa.data.Activity();
		activityHead.setDistance(activity.getDistance());
		activityHead.setName(activity.getName());
		activityHead.setTime(activity.getDate());
		return activityHead;
	}

	private static ActivityData createActivityData(Activity activity) {
		de.acwhadk.rz.filemyrun.jpa.data.ActivityData activityData = new de.acwhadk.rz.filemyrun.jpa.data.ActivityData();
		activityData.setAscent(activity.getAscent());
		activityData.setAverageHeartRate(activity.getAverageHeartRate());
		activityData.setCalories(activity.getCalories());
		activityData.setDescent(activity.getDescent());
		activityData.setDescription(activity.getDescription());
		activityData.setMaximumAltitude(activity.getMaximumAltitude());
		activityData.setMaximumHeartRate(activity.getMaximumHeartRate());
		activityData.setMinimumAltitude(activity.getMinimumAltitude());
		activityData.setPace(activity.getPace());
		activityData.setTotalTime(activity.getTotalTime());
		activityData.setType(activity.getType());
		return activityData;
	}

	private static de.acwhadk.rz.filemyrun.jpa.data.Lap createLap(Lap lap) {
		de.acwhadk.rz.filemyrun.jpa.data.Lap newLap = new de.acwhadk.rz.filemyrun.jpa.data.Lap();
		newLap.setAverageHeartRate(lap.getAverageHeartRateBpm());
		newLap.setMaximumHeartRate(lap.getMaximumHeartRateBpm());
		newLap.setCalories(lap.getCalories());
		newLap.setDistance(lap.getDistanceMeters());
		newLap.setStartTime(lap.getStartTime());
		newLap.setTotalTime(lap.getTotalTimeSeconds());
		return newLap;
	}

	private static Track createTrack(de.acwhadk.rz.filemyrun.core.model.Track track) {
		de.acwhadk.rz.filemyrun.jpa.data.Track trk = new de.acwhadk.rz.filemyrun.jpa.data.Track();
		if (track == null) {
			return trk;
		}
		int cnt=0;
		List<de.acwhadk.rz.filemyrun.jpa.data.TrackPoint> trkPts = new ArrayList<>();
		for(TrackPoint tp : track.getTrackpoints()) {
			de.acwhadk.rz.filemyrun.jpa.data.TrackPoint trkPt = new de.acwhadk.rz.filemyrun.jpa.data.TrackPoint();
			trkPt.setTime(tp.getTime());
			Position pos = tp.getPosition();
			if (pos != null) {
				trkPt.setLatitude(pos.getLatitude());
				trkPt.setLongitude(pos.getLongitude());
			}
			trkPt.setAltitude(tp.getAltitudeMeters());
			trkPt.setDistance(tp.getDistanceMeters());
			trkPt.setHeartrate(tp.getHeartRateBpm());
			trkPt.setSeqNum(cnt++);
			trkPts.add(trkPt);
		}
		trk.setTrackpoints(trkPts);
		return trk;
	}

	public static void convertEquipment(ObjectFactory objectFactoryXml, ObjectFactoryImpl objectFactoryJpa) {
		EquipmentManImpl equipMan = (EquipmentManImpl) objectFactoryXml.createEquipmentMan();
		TrainingFileManImpl trainingFileManJpa = (TrainingFileManImpl) objectFactoryJpa.createTrainingFileMan();
		SortedMap<Date, TrainingFile> files = trainingFileManJpa.getTrainingFiles();
		List<de.acwhadk.rz.filemyrun.jpa.data.Activity> activities = trainingFileManJpa.getActivities();
		EntityManager em = objectFactoryJpa.getEntityManager();
		em.getTransaction().begin();
		try {
			List<String> types = equipMan.getEquipmentTypes();
			Map<Long, EquipmentItem> lookup = new HashMap<>();
			for(String t : types) {
				EquipmentType equipmentType = new EquipmentType();
				equipmentType.setType(t);
				em.persist(equipmentType);

				Map<Long, String> items = equipMan.getEquipmentItems(t);
				for(Entry<Long, String> entry : items.entrySet()) {
					EquipmentItem item = new EquipmentItem();
					item.setType(equipmentType);
					item.setName(entry.getValue());
					item.setInUse(true);
					em.persist(item);
					lookup.put(entry.getKey(), item);
				}
			}
			for(EquipmentUsedEntry entry : equipMan.getEquipmentUsedEntryList()) {
				Date activityDate = entry.getActivity();
				TrainingFile file = files.get(activityDate);
				if (file == null) {
					logger.error("no activity found for equipment");
					continue;
				}
				de.acwhadk.rz.filemyrun.jpa.data.EquipmentUsedEntry e = new de.acwhadk.rz.filemyrun.jpa.data.EquipmentUsedEntry();
				de.acwhadk.rz.filemyrun.jpa.data.Activity activity = getActivity(activityDate, activities);
				e.setActivity(activity.getActivityData());
				EquipmentItem item = lookup.get(entry.getId());
				e.setItem(item);
				em.persist(e);
			}
			logger.info("committing converted equipment usage");
			em.getTransaction().commit();
			logger.info("converting equipment usage finished");
		} catch(Exception e) {
			em.getTransaction().rollback();
			Exception e2 = new RuntimeException(e.getMessage(), e);
			ExceptionDialog.showException(e2);
		}

	}


	private static de.acwhadk.rz.filemyrun.jpa.data.Activity getActivity(Date activityDate, List<de.acwhadk.rz.filemyrun.jpa.data.Activity> activities) {
		for(de.acwhadk.rz.filemyrun.jpa.data.Activity a : activities) {
			if (a.getTime().equals(activityDate)) {
				return a;
			}
		}
		return null;
	}
}
