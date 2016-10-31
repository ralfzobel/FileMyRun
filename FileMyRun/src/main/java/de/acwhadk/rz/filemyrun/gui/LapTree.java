package de.acwhadk.rz.filemyrun.gui;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import de.acwhadk.rz.filemyrun.core.model.Activity;
import de.acwhadk.rz.filemyrun.core.model.TrackPoint;
import de.acwhadk.rz.filemyrun.core.setup.Const;
import javafx.scene.control.TreeItem;

/**
 * This class holds the data that is needed to display the lap tree at the left side of the map.
 * 
 * @author Ralf
 *
 */
public class LapTree {

	private TreeItem<String> root;
	private Map<String, TrackPoint> items = new HashMap<>();

	public LapTree(Activity activity) {
		root = new TreeItem<>(Const.ROOT);
		root.setExpanded(true);
		Map<String, List<TrackPoint>> lapMap = activity.getTrackPoints();
		for(Entry<String, List<TrackPoint>> entry : lapMap.entrySet()) {
			String lap = entry.getKey();
			List<TrackPoint> tpList = entry.getValue();
			TreeItem<String> lapItem = new TreeItem<>(lap);
			root.getChildren().add(lapItem);
			for(TrackPoint tp : tpList) {
				Calendar cal = tp.getTime();
				String tpName = Formatter.formatFullDate(cal.getTime());
				if (tp.getDistanceMeters() != null) {					
					tpName += Const.SPACED_DASH + Formatter.formatDistanceToKm3(tp.getDistanceMeters());
				}
				lapItem.getChildren().add(new TreeItem<String>(tpName));
				items.put(tpName, tp);
			}
		}
	}

	public TreeItem<String> getRoot() {
		return root;
	}

	public TrackPoint getTrackPoint(String tpName) {
		return items.get(tpName);
	}
	
}
