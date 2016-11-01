package de.acwhadk.rz.filemyrun.jpa.data;

import java.util.List;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OrderBy;

@Entity
public class Track {

	@Id
	@GeneratedValue(strategy=GenerationType.TABLE)
	private int id;

	@ElementCollection
	@OrderBy(value="seqNum")
	private List<TrackPoint> trackpoints;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public List<TrackPoint> getTrackpoints() {
		return trackpoints;
	}

	public void setTrackpoints(List<TrackPoint> trackpoints) {
		this.trackpoints = trackpoints;
	}

}
