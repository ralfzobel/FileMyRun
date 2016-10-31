package de.acwhadk.rz.filemyrun.core.model;

public interface ObjectFactory {
	public TrainingFileMan createTrainingFileMan();
	public EquipmentMan createEquipmentMan();
	public Activity createActivity(TrainingFile trainingFile);
	public Lap createLap();
	public Track createTrack();
	public TrackPoint createTrackPoint();
	public Position createPosition();
}
