package de.acwhadk.rz.filemyrun.controller;

import java.io.File;
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

import com.garmin.tcdbv2.ActivityT;
import com.garmin.tcdbv2.TrainingCenterDatabaseT;
import com.topografix.gpx.GpxType;
import com.topografix.gpx.TrkType;

import de.acwhadk.rz.filemyrun.data.TrainingActivity;
import de.acwhadk.rz.filemyrun.dialog.ProgressDialog;
import de.acwhadk.rz.filemyrun.file.TrainingFile;
import de.acwhadk.rz.filemyrun.file.TrainingFileContainer;
import de.acwhadk.rz.filemyrun.setup.Setup;
import de.acwhadk.rz.filemyrun.xml.GpxLoader;
import de.acwhadk.rz.filemyrun.xml.TcxLoader;
import de.acwhadk.rz.filemyrun.xml.TrainingActivityToXML;
import de.acwhadk.rz.filemyrun.xml.TrainingFileContainerToXML;
import javafx.concurrent.Task;

public class TrainingFileMan {

	private String workdir;
	private String importdir;
	private String importeddir;
	private List<TrainingFile> files;

	public TrainingFileMan() throws IOException, JAXBException {
		this.workdir = Setup.getInstance().getActivitiesFolder();
		this.importdir = Setup.getInstance().getDownloadFolder();
		this.importeddir = Setup.getInstance().getImportedFolder();
		readTrainingFileListXML();
		importTrainingFilesFromTcx();
		save();
	}

	public SortedMap<Date, TrainingFile> getTrainingFiles() {
		SortedMap<Date, TrainingFile> map = new TreeMap<>(new Comparator<Date>() {
			@Override
			public int compare(Date o1, Date o2) {				
				return o2.compareTo(o1);
			}
		});
		for(TrainingFile f : files) {
			if (f.getTime() != null) {
				map.put(f.getTime(), f);
			}
		}
		return map;
	}
	
	private void readTrainingFileListXML() {
		files = new ArrayList<>();
		File file = new File(workdir + "/files.xml");
		try {
			TrainingFileContainer container = TrainingFileContainerToXML.load(file);
			files.addAll(container.getFilelist());
		} catch (JAXBException e) {
			rebuildTrainingFileList();
		}
	}

	private void rebuildTrainingFileList() {
		ProgressDialog pb = new ProgressDialog("Training File Index wird erstellt");
		Task<Void> task = new Task<Void>() {
            @Override
            public Void call() {
        		File[] trxFiles = new File(workdir).listFiles();
        		double max = trxFiles.length;
        		int cnt = 0;
        		for(File file : trxFiles) {
        			++cnt;
        			if (!file.getName().toLowerCase().matches(".*trx")) {
        				continue;
        			}
        			try {
        				TrainingActivity activity = TrainingActivityToXML.load(file);
        				TrainingFile tf = activity.getTrainingFile();
        				tf.setTrainingFile(file.getAbsolutePath());        				
        				initTrainingFile(tf, activity);
        				files.add(tf);
        				if (cnt / max > 0.1) {
        					updateProgress(cnt, max);
        				}
        			} catch (Exception e) {
        				System.out.println("cannot read TRX-File " + file.getName() + ": " + e);
        			}
        		}		
				return null;            	
            }

			private void initTrainingFile(TrainingFile tf, TrainingActivity activity) throws Exception {
				if (tf.getDistance() == null) {
					Activity a = new Activity(tf);
					tf.setDistance(a.getDistance());
				}
				if (tf.getType() == null) {
					Activity a = new Activity(tf);
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
		Format formatter = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
		File[] tcxFiles = new File(importdir).listFiles();
		for(File file : tcxFiles) {
			try {
				if (!file.getName().toLowerCase().matches(".*tcx")) {
					continue;
				}
				TcxLoader tcxLoader = new TcxLoader();
				TrainingCenterDatabaseT tcx = tcxLoader.loadTcx(file);
				TrainingFile tf = getTrainingFile(tcx);
				if (tf == null) {
					TrainingActivity activity = new TrainingActivity();
					activity.setTrainingCenterDatabase(tcx);
					Date date = getDate(tcx);				

					addGpxData(file, activity);

					String filename = workdir + "/" + formatter.format(date) + ".trx";
					tf = new TrainingFile("<n/a>", date, filename);
					activity.setTrainingFile(tf);
					TrainingActivityToXML.save(filename, activity);
					files.add(tf);
				}
			} catch (IOException | JAXBException e) {
				System.out.println("cannot import file " + file.getAbsolutePath() + ": " + e);
			}
			moveFile(file);
		}
	}

	private void moveFile(File file) {
		String oldPathName = file.getAbsolutePath();
		String newPathName = importeddir + "/" + file.getName();
		Path source = Paths.get(oldPathName);
		Path target = Paths.get(newPathName);
		try {
			Files.move(source, target, StandardCopyOption.REPLACE_EXISTING);
		} catch (IOException e) {
			System.out.println("cannot move file " + file.getAbsolutePath() + " to imported directory: " + e);
		}
	}

	private void addGpxData(File file, TrainingActivity activity) throws JAXBException {
		String filename = file.getAbsolutePath().toLowerCase().replace(".tcx", ".gpx");
		GpxLoader gpx = new GpxLoader();
		File gpxFile = new File(filename);
		if (gpxFile.exists()) {
			GpxType t = gpx.loadGpx(gpxFile);
	
			List<TrkType> tracks = t.getTrk();
			if (!tracks.isEmpty()) {
				activity.setName(tracks.get(0).getName());
			}	
			moveFile(gpxFile);	
		}
	}

	private Date getDate(TrainingCenterDatabaseT t) {
		ActivityT activity = t.getActivities().getActivity().get(0);
		XMLGregorianCalendar xmlTime = activity.getId();
		return xmlTime.toGregorianCalendar().getTime();
	}
	
	private TrainingFile getTrainingFile(TrainingCenterDatabaseT tcx) {
		Date date = getDate(tcx);	
		return getTrainingFile(date);
	}

	public TrainingFile getTrainingFile(Date date) {
		for(TrainingFile f : files) {
			if (f.getTime().equals(date)) {
				return f;
			}
		}
		return null;
	}
	
	public void save() throws IOException, JAXBException {
		TrainingFileContainer container = new TrainingFileContainer();
		container.setFileList(files);
//		writeTrainingFileList(container);
		TrainingFileContainerToXML.save(workdir + "/files.xml", container);
	}

	public void deleteFile(TrainingFile trainingFile) {
		if (!files.remove(trainingFile)) {
			System.out.println("couldn't delete training file " + trainingFile.getName() + " from file list");
		}
		
		try {
			save();
		} catch (IOException | JAXBException e) {
			System.out.println("couldn't write new training file list: " + e);
		}
		
		File file = new File(trainingFile.getTrainingFile());
		if (!file.delete()) {
			System.out.println("couldn't delete training file " + trainingFile.getName() + " from file system");
		}
	}
	
}
