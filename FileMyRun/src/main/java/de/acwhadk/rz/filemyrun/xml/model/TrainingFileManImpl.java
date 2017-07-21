package de.acwhadk.rz.filemyrun.xml.model;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.xml.bind.JAXBException;
import javax.xml.datatype.XMLGregorianCalendar;

import com.garmin.tcdbv2.ActivityLapT;
import com.garmin.tcdbv2.ActivityT;
import com.garmin.tcdbv2.TrainingCenterDatabaseT;

import de.acwhadk.rz.filemyrun.core.gui.ExceptionDialog;
import de.acwhadk.rz.filemyrun.core.gui.ProgressDialog;
import de.acwhadk.rz.filemyrun.core.model.Activity;
import de.acwhadk.rz.filemyrun.core.model.ObjectFactory;
import de.acwhadk.rz.filemyrun.core.model.TrainingFileMan;
import de.acwhadk.rz.filemyrun.core.setup.Const;
import de.acwhadk.rz.filemyrun.core.setup.Lang;
import de.acwhadk.rz.filemyrun.core.setup.Setup;
import de.acwhadk.rz.filemyrun.gui.GuiControl;
import de.acwhadk.rz.filemyrun.xml.data.TrainingActivity;
import de.acwhadk.rz.filemyrun.xml.file.TrainingFile;
import de.acwhadk.rz.filemyrun.xml.file.TrainingFileContainer;
import de.acwhadk.rz.filemyrun.xml.tools.TcxLoader;
import de.acwhadk.rz.filemyrun.xml.tools.TrainingActivityToXML;
import de.acwhadk.rz.filemyrun.xml.tools.TrainingFileContainerToXML;
import javafx.concurrent.Task;

/**
 * This class is a manager for training files.
 * On instantiation it looks if there are new files to import.
 * After that it gives access to the training files.
 * 
 * @author Ralf
 *
 */
public class TrainingFileManImpl implements TrainingFileMan {

	private String workdir;
	private String importdir;
	private String importeddir;
	private List<TrainingFile> files;
	private ObjectFactory objectFactory;

	public TrainingFileManImpl(ObjectFactory objectFactory) throws IOException, JAXBException {
		this.objectFactory = objectFactory;
		this.workdir = Setup.getInstance().getActivitiesFolder();
		this.importdir = Setup.getInstance().getDownloadFolder();
		this.importeddir = Setup.getInstance().getImportedFolder();
		readTrainingFileListXML();
		importTrainingFilesFromTcx();
		commit();
	}

	@Override
	public SortedMap<Date, de.acwhadk.rz.filemyrun.core.model.TrainingFile> getTrainingFiles() {
		SortedMap<Date, de.acwhadk.rz.filemyrun.core.model.TrainingFile> map = new TreeMap<>(new Comparator<Date>() {
			@Override
			public int compare(Date o1, Date o2) {				
				return o2.compareTo(o1);
			}
		});
		for(TrainingFile f : files) {
			TrainingFileImpl tf = new TrainingFileImpl(f);
			if (tf.getTime() != null) {
				map.put(tf.getTime(), tf);
			}
		}
		return map;
	}
	
	private void readTrainingFileListXML() {
		files = new ArrayList<>();
		File file = new File(workdir + File.separator + Const.INDEX_FILE);
		try {
			TrainingFileContainer container = TrainingFileContainerToXML.load(file);
			files.addAll(container.getFilelist());
		} catch (JAXBException e) {
			rebuildTrainingFileList();
		}
	}

	private void rebuildTrainingFileList() {
		ProgressDialog pb = new ProgressDialog(Lang.get().text(Lang.PROCESS_DLG_INDEX));
		Task<Void> task = new Task<Void>() {
            @Override
            public Void call() {
        		File[] trxFiles = new File(workdir).listFiles();
        		double max = trxFiles.length;
        		int cnt = 0;
        		for(File file : trxFiles) {
        			++cnt;
        			if (!file.getName().toLowerCase().matches(Const.PATTERN_TRX)) {
        				continue;
        			}
        			try {
        				TrainingActivity activity = TrainingActivityToXML.load(file);
        				TrainingFile tf = activity.getTrainingFile();
        				tf.setTrainingFile(file.getAbsolutePath());
        				TrainingFileImpl f = new TrainingFileImpl(tf);
        				initTrainingFile(f, activity);
        				files.add(tf);
        				if (cnt / max > 0.1) {
        					updateProgress(cnt, max);
        				}
        			} catch (Exception e) {
        				String msg = String.format(Lang.get().text(Lang.TFM_READ_TRX_FAILED), file.getName());
        				Exception e2 = new RuntimeException(msg, e);
        				ExceptionDialog.showException(e2);
        			}
        		}		
				return null;            	
            }

			private void initTrainingFile(TrainingFileImpl tf, TrainingActivity activity) throws Exception {
				if (tf.getDistance() == null) {
					Activity a = objectFactory.createActivity(tf);
					tf.setDistance(a.getDistance());
				}
				if (tf.getType() == null) {
					Activity a = objectFactory.createActivity(tf);
					tf.setType(a.getType());
				}
			}
		};
        task.setOnSucceeded(event -> { pb.close(); } );
        
		Thread thread = new Thread(task);
        thread.start();
		pb.activateProgressBar(task);
    }
	
	private void importTrainingFilesFromTcx() {
		Format formatter = new SimpleDateFormat(Const.FORMAT_FILE_DATE);
		File[] tcxFiles = new File(importdir).listFiles();
		for(File file : tcxFiles) {
			try {
				if (!file.getName().toLowerCase().matches(Const.PATTERN_TCX)) {
					continue;
				}
				TcxLoader tcxLoader = new TcxLoader();
				TrainingCenterDatabaseT tcx = tcxLoader.loadTcx(file);
				importLapDataFromCSV();
				TrainingFile tf = getTrainingFile(tcx);
				if (tf == null) {
					TrainingActivity activity = new TrainingActivity();
					activity.setTrainingCenterDatabase(tcx);
					Date date = getDate(tcx);				

					String filename = workdir + File.separator + formatter.format(date) + Const.SUFFIX_TRX;
					tf = new TrainingFile(Lang.get().text(Lang.NOT_AVAILABLE), date, filename);
					activity.setTrainingFile(tf);
					TrainingActivityToXML.save(filename, activity);
					files.add(tf);
				}
			} catch (IOException | JAXBException e) {
				String msg = String.format(Lang.get().text(Lang.TFM_READ_TCX_FAILED), file.getAbsolutePath());
				Exception e2 = new RuntimeException(msg, e);
				ExceptionDialog.showException(e2);
  			}
			moveFile(file);
		}
	}

	private void importLapDataFromCSV(String tcxFilename, TrainingCenterDatabaseT tcx) {
		String filename = tcxFilename.replace(".tcx", ".csv");
		File file = new File(filename);
		if (!file.exists() || !file.isFile()) {
			return;
		}
		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(file));
			String line;
			int i=0;
			List<LapFromCsv> laps = new ArrayList<>();
			while ((line = br.readLine()) != null) {
				String[] parts = line.split(",");
				if (parts.length < 4) {
					continue;
				}
				if (i==0) {
					// check columns
					if (!"Zwischenzeit".equals(parts[0])) {
						GuiControl.showException(new RuntimeException("CSV-Format has changed"));
					}
					if (!"Zeit".equals(parts[1])) {
						GuiControl.showException(new RuntimeException("CSV-Format has changed"));
					}
					if (!"Distanz".equals(parts[3])) {
						GuiControl.showException(new RuntimeException("CSV-Format has changed"));
					}
				} else {
					// import data
					if ("Summary".equals(parts[0])) {
						continue;
					}
					int n = Integer.parseInt(parts[0]);
					if (n != i) {
						GuiControl.showException(new RuntimeException("CSV-Format has invalid line count"));
					}
					double time = getTotalTime(parts[1]);					
					double distance = Double.parseDouble(parts[3]) * 1000.0;
					LapFromCsv lap = new LapFromCsv(n-1, time, distance);
					laps.add(lap);
				}
				++i;
			}
			ActivityT activity = tcx.getActivities().getActivity().get(0);
			if (laps.size() != activity.getLap().size()) {
				GuiControl.showException(new RuntimeException("CSV-Format has invalid lap count"));
			}
			for(LapFromCsv lap : laps) {
				ActivityLapT activityLap = activity.getLap().get(lap.n);
				activityLap.setTotalTimeSeconds(lap.time);
				activityLap.setDistanceMeters(lap.distance);
			}
		} catch (Exception e) {
			GuiControl.showException(e);
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
	  				GuiControl.showException(e);
				}
			}
		}
		moveFile(file);
	}

	private double getTotalTime(String t) {
		String[] parts = t.split("[:\\.]");
		if (parts.length != 4) {
			throw new RuntimeException("invalid total time: " + t);
		}
		int hours = Integer.parseInt(parts[0]);
		int minutes = Integer.parseInt(parts[1]);
		int seconds = Integer.parseInt(parts[2]);
		int millis = Integer.parseInt(parts[3]);
		return hours*3600.0 + minutes*60.0 + seconds + millis/1000.0;
	}

	private class LapFromCsv {
		int n;
		double time;
		double distance;
		public LapFromCsv(int n, double time, double distance) {
			super();
			this.n = n;
			this.time = time;
			this.distance = distance;
		}
	}
	
	private void moveFile(File file) {
		String oldPathName = file.getAbsolutePath();
		String newPathName = importeddir + File.separator + file.getName();
		Path source = Paths.get(oldPathName);
		Path target = Paths.get(newPathName);
		try {
			Files.move(source, target, StandardCopyOption.REPLACE_EXISTING);
		} catch (IOException e) {
			String msg = String.format(Lang.get().text(Lang.TFM_MOVE_TCX_FAILED), file.getAbsolutePath());
			Exception e2 = new RuntimeException(msg, e);
			ExceptionDialog.showException(e2);
		}
	}

	private Date getDate(TrainingCenterDatabaseT t) {
		ActivityT activity = t.getActivities().getActivity().get(0);
		XMLGregorianCalendar xmlTime = activity.getId();
		return xmlTime.toGregorianCalendar().getTime();
	}
	
	private TrainingFile getTrainingFile(TrainingCenterDatabaseT tcx) {
		Date date = getDate(tcx);	
		return getTrainingFileByDate(date);
	}

	public TrainingFile getTrainingFileByDate(Date date) {
		for(TrainingFile f : files) {
			if (f.getTime().equals(date)) {
				return f;
			}
		}
		return null;
	}
	
	@Override
	public de.acwhadk.rz.filemyrun.core.model.TrainingFile getTrainingFile(Date date) {
		for(TrainingFile f : files) {
			if (f.getTime().equals(date)) {
				return new TrainingFileImpl(f);
			}
		}
		return null;
	}
	
	@Override
	public void commit() throws IOException, JAXBException {
		TrainingFileContainer container = new TrainingFileContainer();
		container.setFileList(files);
		TrainingFileContainerToXML.save(workdir + File.separator + Const.INDEX_FILE, container);
	}

	@Override
	public void deleteFile(de.acwhadk.rz.filemyrun.core.model.TrainingFile trainingFileImplXml) {
		files.remove(trainingFileImplXml);

		try {
			commit();
		} catch (IOException | JAXBException e) {
			String msg = Lang.get().text(Lang.TFM_SAVE_INDEX_FAILED);
			Exception e2 = new RuntimeException(msg, e);
			ExceptionDialog.showException(e2);
		}
		
		File file = new File(trainingFileImplXml.getTrainingFile());
		if (!file.delete()) {
			String msg = String.format(Lang.get().text(Lang.TFM_REMOVE_FAILED), file.getName());
			Exception e = new RuntimeException(msg);
			ExceptionDialog.showException(e);
		}
	}

	@Override
	public void beginTransaction() {
	}
	
}
