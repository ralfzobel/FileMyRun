package de.acwhadk.rz.filemyrun.gui;

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

import de.acwhadk.rz.filemyrun.data.TrainingActivity;
import de.acwhadk.rz.filemyrun.dialog.ProgressDialog;
import de.acwhadk.rz.filemyrun.file.TrainingFile;
import de.acwhadk.rz.filemyrun.file.TrainingFileContainer;
import de.acwhadk.rz.filemyrun.setup.Const;
import de.acwhadk.rz.filemyrun.setup.Lang;
import de.acwhadk.rz.filemyrun.setup.Setup;
import de.acwhadk.rz.filemyrun.xml.TcxLoader;
import de.acwhadk.rz.filemyrun.xml.TrainingActivityToXML;
import de.acwhadk.rz.filemyrun.xml.TrainingFileContainerToXML;
import javafx.concurrent.Task;

/**
 * This class is a manager for training files.
 * On instantiation it looks if there are new files to import.
 * After that it gives access to the training files.
 * 
 * @author Ralf
 *
 */
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
        				initTrainingFile(tf, activity);
        				files.add(tf);
        				if (cnt / max > 0.1) {
        					updateProgress(cnt, max);
        				}
        			} catch (Exception e) {
        				String msg = String.format(Lang.get().text(Lang.TFM_READ_TRX_FAILED), file.getName());
        				Exception e2 = new RuntimeException(msg, e);
        				GuiControl.showException(e2);
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
		Format formatter = new SimpleDateFormat(Const.FORMAT_FILE_DATE);
		File[] tcxFiles = new File(importdir).listFiles();
		for(File file : tcxFiles) {
			try {
				if (!file.getName().toLowerCase().matches(Const.PATTERN_TCX)) {
					continue;
				}
				TcxLoader tcxLoader = new TcxLoader();
				TrainingCenterDatabaseT tcx = tcxLoader.loadTcx(file);
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
  				GuiControl.showException(e2);
  			}
			moveFile(file);
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
			GuiControl.showException(e2);
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
		TrainingFileContainerToXML.save(workdir + File.separator + Const.INDEX_FILE, container);
	}

	public void deleteFile(TrainingFile trainingFile) {
		files.remove(trainingFile);

		try {
			save();
		} catch (IOException | JAXBException e) {
			String msg = Lang.get().text(Lang.TFM_SAVE_INDEX_FAILED);
			Exception e2 = new RuntimeException(msg, e);
			GuiControl.showException(e2);
		}
		
		File file = new File(trainingFile.getTrainingFile());
		if (!file.delete()) {
			String msg = String.format(Lang.get().text(Lang.TFM_REMOVE_FAILED), file.getName());
			Exception e = new RuntimeException(msg);
			GuiControl.showException(e);
		}
	}
	
}
