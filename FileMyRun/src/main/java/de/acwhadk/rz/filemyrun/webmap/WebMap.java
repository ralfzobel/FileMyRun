package de.acwhadk.rz.filemyrun.webmap;

import java.util.List;

import de.acwhadk.rz.filemyrun.core.model.Activity;
import de.acwhadk.rz.filemyrun.core.model.Position;
import de.acwhadk.rz.filemyrun.core.model.TrackPoint;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Worker.State;
import javafx.scene.control.Tab;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

/**
 * Show a track on google maps by calling the javascript methods by a java fx web engine.
 * 
 * @author Ralf
 *
 */
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

	public void setTrack(Activity tcActivity) {
		webEngine.executeScript("document.startTrack()");
		Double minLat = null;
		Double minLon = null;
		Double maxLat = null;
		Double maxLon = null;
		for(List<TrackPoint> lap : tcActivity.getTrackPoints().values()) {
			for(TrackPoint tp : lap) {
				Position pos = tp.getPosition();
				if (pos != null) {
					double lat = pos.getLatitude();
					double lon = pos.getLongitude();
					if (lat == 0.0 || lon == 0.0) {
						continue;
					}
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