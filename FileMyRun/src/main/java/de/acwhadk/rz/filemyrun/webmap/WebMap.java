package de.acwhadk.rz.filemyrun.webmap;

import com.garmin.tcdbv2.ActivityLapT;
import com.garmin.tcdbv2.ActivityT;
import com.garmin.tcdbv2.PositionT;
import com.garmin.tcdbv2.TrackT;
import com.garmin.tcdbv2.TrackpointT;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Worker.State;
import javafx.scene.control.Tab;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

public class WebMap {
	private WebEngine webEngine;
	private Tab tab;
	public WebMap(WebView webView, Tab tab) {
		webEngine = webView.getEngine();
		this.tab = tab;
		initialize();
	}

	public void initialize() {
		webEngine.getLoadWorker().stateProperty().addListener(
		new ChangeListener<State>() {
			public void changed(@SuppressWarnings("rawtypes") ObservableValue ov, State oldState, State newState) {
				if (newState == State.SUCCEEDED) {
					tab.setDisable(false);
				}
			}
		});
		webEngine.load(getClass().getResource("googlemap.html").toExternalForm());
	}
	
	public void setMapTypeRoad() {
		webEngine.executeScript("document.setMapTypeRoad()");
	}

	public void setMapTypeSatellite() {
		webEngine.executeScript("document.setMapTypeSatellite()");
	}

	public void setMapTypeHybrid() {
		webEngine.executeScript("document.setMapTypeHybrid()");
	}

	public void webMapsetMapTypeTerrain() {
		webEngine.executeScript("document.setMapTypeTerrain()");
	}

	public void setTrack(ActivityT tcActivity) {
		webEngine.executeScript("document.startTrack()");
		Double minLat = null;
		Double minLon = null;
		Double maxLat = null;
		Double maxLon = null;
		for(ActivityLapT lap : tcActivity.getLap()) {
			for(TrackT t : lap.getTrack()) {
				for(TrackpointT tp : t.getTrackpoint()) {
					PositionT pos = tp.getPosition();
					if (pos != null) {
						double lat = pos.getLatitudeDegrees();
						double lon = pos.getLongitudeDegrees();
						webEngine.executeScript("document.addTrackPoint(" + lat + "," + lon + ")");
						if (minLat == null || minLat > lat) {
							minLat = lat;
						}
						if (maxLat == null || maxLat < lat) {
							maxLat = lat;
						}
						if (minLon == null || minLon > lon) {
							minLon = lon;
						}
						if (maxLon == null || maxLon < lon) {
							maxLon = lon;
						}
					}
				}
			}
		}
		webEngine.executeScript("document.commitTrack()");
		webEngine.executeScript("document.setBounds(" + minLat + "," + minLon + "," + maxLat + "," + maxLon + ")");
	}

	public void setTrackPoint(double latitude, double longitude) {
		webEngine.executeScript("document.markTrackPoint(" + latitude + "," + longitude + ")");
	}

	public void clearTrackPoint() {
		webEngine.executeScript("document.clearTrackPoint()");
	}
}