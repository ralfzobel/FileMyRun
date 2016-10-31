package de.acwhadk.rz.filemyrun.xml.model;

import java.util.ArrayList;
import java.util.List;

import com.garmin.tcdbv2.TrackT;
import com.garmin.tcdbv2.TrackpointT;

import de.acwhadk.rz.filemyrun.core.model.Track;
import de.acwhadk.rz.filemyrun.core.model.TrackPoint;

public class TrackImpl implements Track {

	private TrackT track;
	
	public TrackImpl(TrackT track) {
		this.track = track;
	}

	@Override
	public List<TrackPoint> getTrackpoints() {
		List<TrackPoint> tpList = new ArrayList<>();
		for(TrackpointT trackpoint : track.getTrackpoint()) {
			TrackPoint tp = new TrackPointImpl(trackpoint);
			tpList.add(tp);
		}
		return tpList;
	}

}
