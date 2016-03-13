package de.acwhadk.rz.filemyrun.dialog;

import java.time.LocalDate;

/**
 * A bean containing the data the user entered in the file filter dialog.
 * 
 * @author Ralf
 *
 */
public class FileFilter {

	private boolean timeFilter;
	private boolean distFilter;
	private boolean typeFilter;
	private boolean textFilter;
	
	private LocalDate fromTime;
	private LocalDate toTime;
	
	private double fromDist;
	private double toDist;
	
	private String type;
	private String text;
	
	
	public boolean isTimeFilter() {
		return timeFilter;
	}
	public void setTimeFilter(boolean timeFilter) {
		this.timeFilter = timeFilter;
	}
	public boolean isDistFilter() {
		return distFilter;
	}
	public void setDistFilter(boolean distFilter) {
		this.distFilter = distFilter;
	}
	public boolean isTypeFilter() {
		return typeFilter;
	}
	public void setTypeFilter(boolean typeFilter) {
		this.typeFilter = typeFilter;
	}
	public boolean isTextFilter() {
		return textFilter;
	}
	public void setTextFilter(boolean textFilter) {
		this.textFilter = textFilter;
	}
	public LocalDate getFromTime() {
		return fromTime;
	}
	public void setFromTime(LocalDate fromTime) {
		this.fromTime = fromTime;
	}
	public LocalDate getToTime() {
		return toTime;
	}
	public void setToTime(LocalDate toTime) {
		this.toTime = toTime;
	}
	public double getFromDist() {
		return fromDist;
	}
	public void setFromDist(double fromDist) {
		this.fromDist = fromDist;
	}
	public double getToDist() {
		return toDist;
	}
	public void setToDist(double toDist) {
		this.toDist = toDist;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
}
