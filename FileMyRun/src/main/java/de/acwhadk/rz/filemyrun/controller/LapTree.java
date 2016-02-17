package de.acwhadk.rz.filemyrun.controller;

import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.garmin.tcdbv2.TrackpointT;

import javafx.scene.control.TreeItem;

public class LapTree {

	private TreeItem<String> root;
	private Map<String, TrackpointT> items = new HashMap<>();

	public LapTree(Activity activity) {
		root = new TreeItem<>("Root");
		root.setExpanded(true);
		Map<String, List<TrackpointT>> lapMap = activity.getTrackPoints();
		for(Entry<String, List<TrackpointT>> entry : lapMap.entrySet()) {
			String lap = entry.getKey();
			List<TrackpointT> tpList = entry.getValue();
			TreeItem<String> lapItem = new TreeItem<>(lap);
			root.getChildren().add(lapItem);
			for(TrackpointT tp : tpList) {
				GregorianCalendar cal = tp.getTime().toGregorianCalendar(null, null, null);
				String tpName = Formatter.formatDate(cal.getTime());
				if (tp.getDistanceMeters() != null) {					
					tpName += " - " + Formatter.formatDistanceToKm3(tp.getDistanceMeters());
				}
				lapItem.getChildren().add(new TreeItem<String>(tpName));
				items.put(tpName, tp);
			}
		}
	}

	public TreeItem<String> getRoot() {
		return root;
	}

	public TrackpointT getTrackPoint(String tpName) {
		return items.get(tpName);
	}
	
}
