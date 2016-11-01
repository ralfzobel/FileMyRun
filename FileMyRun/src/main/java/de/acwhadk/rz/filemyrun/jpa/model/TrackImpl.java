package de.acwhadk.rz.filemyrun.jpa.model;

import java.util.ArrayList;
import java.util.List;

import de.acwhadk.rz.filemyrun.core.model.Track;
import de.acwhadk.rz.filemyrun.core.model.TrackPoint;

public class TrackImpl implements Track {

	private de.acwhadk.rz.filemyrun.jpa.data.Track track;
	
	public TrackImpl(de.acwhadk.rz.filemyrun.jpa.data.Track track) {
		this.track = track;
	}

	@Override
	public List<TrackPoint> getTrackpoints() {
		List<TrackPoint> trkPts = new ArrayList<>();
		if (track != null) {
			for(de.acwhadk.rz.filemyrun.jpa.data.TrackPoint tp : track.getTrackpoints()) {
				TrackPoint trkPt = new TrackPointImpl(tp);
				trkPts.add(trkPt);
			}
		}
		return trkPts;
	}

}
