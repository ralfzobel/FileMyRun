package de.acwhadk.rz.filemyrun.gui;

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
import de.acwhadk.rz.filemyrun.setup.Const;
import de.acwhadk.rz.filemyrun.setup.Lang;
import javafx.scene.control.TreeItem;

/**
 * This class holds the data that is used to display the tree with the activities.
 * 
 * @author Ralf
 *
 */
public class FileTree {

	private Map<String, TrainingFile> files = new HashMap<>();

	private TreeItem<String> root;
	private Map<String, TreeItem<String>> items = new HashMap<>();
	private String filterText;
	
	public FileTree(TrainingFileMan trainingFileMan, FileFilter filter) {
		root = new TreeItem<>(Const.ROOT);
		root.setExpanded(true);
		initFilterText(filter);
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
		Format formatter = new SimpleDateFormat(Const.FORMAT_DAYNAME_DAY); 
		String name = formatter.format(file.getTime()) + Const.SPACE + Const.DASH + 
				Const.SPACE + (file.getName() != null ? file.getName() : Lang.get().text(Lang.UNKNOWN_NAME));
		if (file.getDistance() != null && file.getDistance() > 0.0) {
			DecimalFormat df = new DecimalFormat(Const.FORMAT_DECIMAL_1);
			name += Const.KOMMA + Const.SPACE + df.format(file.getDistance()) + Const.SPACE + 
					Lang.get().text(Lang.DISTANCE_ABBREVIATED);
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
		SimpleDateFormat sdf = new SimpleDateFormat(Const.FORMAT_YEAR_MONTH);
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

	private void initFilterText(FileFilter filter) {
		filterText = "";
		if (filter == null) {
			filterText = "";
			return;
		}
		if (filter.isDistFilter()) {
			filterText += Lang.get().text(Lang.FILEFILTER_LBL_DISTANCE) + Const.COLON + Const.SPACE + 
					filter.getFromDist() + Const.SPACED_DASH + filter.getToDist();
			return;
		}
		if (filter.isTimeFilter()) {
			filterText += Lang.get().text(Lang.FILEFILTER_LBL_TIME) + Const.COLON + Const.SPACE +
					Formatter.formatDate(filter.getFromTime()) + Const.SPACED_DASH + 
					Formatter.formatDate(filter.getToTime());
			return;
		}
		if (filter.isTextFilter()) {
			filterText = Lang.get().text(Lang.FILEFILTER_LBL_TEXT) + Const.COLON + Const.SPACE +
					filter.getText();
			return;
		}
		if (filter.isTypeFilter()) {
			filterText = Lang.get().text(Lang.FILEFILTER_LBL_TYPE) + Const.COLON + Const.SPACE +
					filter.getType();
			return;
		}
	}

	public String getFilterText() {
		return filterText;
	}
}
