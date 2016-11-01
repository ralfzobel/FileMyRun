package de.acwhadk.rz.filemyrun.core.model;

import java.io.Closeable;

public interface ObjectFactory extends Closeable {
	public TrainingFileMan createTrainingFileMan();
	public EquipmentMan createEquipmentMan();
	public Activity createActivity(TrainingFile trainingFile);
	public Lap createLap();
	public Track createTrack();
	public TrackPoint createTrackPoint();
	public Position createPosition();
}
