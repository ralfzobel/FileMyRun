package de.acwhadk.rz.filemyrun.controller;

import java.text.DecimalFormat;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.SortedMap;

import de.acwhadk.rz.filemyrun.dialog.FileFilter;
import de.acwhadk.rz.filemyrun.file.TrainingFile;
import javafx.scene.control.TreeItem;

public class FileTree {

	private Map<String, TrainingFile> files = new HashMap<>();

	private TreeItem<String> root;
	private Map<String, TreeItem<String>> items = new HashMap<>();
	
	public FileTree(TrainingFileMan trainingFileMan, FileFilter filter) {
		root = new TreeItem<>("Root");
		root.setExpanded(true);
		SortedMap<Date, TrainingFile> trainingFiles = trainingFileMan.getTrainingFiles();
		for(TrainingFile file : trainingFiles.values()) {
			if (match(file, filter)) {
				TreeItem<String> parentNode = getParentNode(file);
				String name = getName(file);
				TreeItem<String> fileItem = new TreeItem<>(name);
				parentNode.getChildren().add(fileItem);
				files.put(name, file);
			}
		}
	}
	
	public String getName(TrainingFile file) {
		Format formatter = new SimpleDateFormat("E., dd."); 
		String name = formatter.format(file.getTime()) + " - " + (file.getName() != null ? file.getName() : "???");
		if (file.getDistance() != null && file.getDistance() > 0.0) {
			DecimalFormat df = new DecimalFormat("#.0");
			name += ", " + df.format(file.getDistance()) + " km";
		}
		return name;
	}
	
	public void update(TrainingFile trainingfile) {
		String oldName = null;
		for(Entry<String, TrainingFile> entry : files.entrySet()) {
			if (entry.getValue() == trainingfile) {
				oldName = entry.getKey();
			}
		}		
		if (oldName != null) {
			files.remove(oldName);
			files.put(getName(trainingfile), trainingfile);
		}
	}
	
	private TreeItem<String> getParentNode(TrainingFile file) {
		String name = getYearMonthName(file.getTime());
		TreeItem<String> item = items.get(name);
		if (item == null) {
			item = new TreeItem<>(name);
			root.getChildren().add(item);
			items.put(name, item);
		}
		return item;
	}

	private String getYearMonthName(Date time) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
		return sdf.format(time); 
	}

	public TreeItem<String> getRoot() {
		return root;
	}
	
	public TrainingFile getTrainingFile(String name) {
		return files.get(name);
	}

	private boolean match(TrainingFile file, FileFilter filter) {
		if (filter == null) {
			return true;
		}
		if (filter.isDistFilter()) {
			if (file.getDistance() < filter.getFromDist() || file.getDistance() > filter.getToDist()) {
				return false;
			}
		}
		if (filter.isTimeFilter()) {
			GregorianCalendar cal = new GregorianCalendar();
			cal.setTime(file.getTime());
			LocalDate activityDate = LocalDate.of(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH)+1, cal.get(Calendar.DAY_OF_MONTH));
			if (activityDate.isBefore(filter.getFromTime()) || activityDate.isAfter(filter.getToTime())) {
				return false;
			}
		}
		if (filter.isTextFilter()) {
			String text = filter.getText().toLowerCase();
			boolean found = false;
			if (file.getName().toLowerCase().contains(text)) {
				found = true;
			}
			if (file.getComment() != null && file.getComment().toLowerCase().contains(text)) {
				found = true;
			}
			if (!found) {
				return false;
			}
		}
		if (filter.isTypeFilter()) {
			if (file.getType() == null || file.getType().equals(filter.getType()) == false) {
				return false;
			}
		}
		return true;
	}

}
