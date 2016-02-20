package de.acwhadk.rz.filemyrun.controller;

import java.text.DecimalFormat;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * A utility class dealing with formatting.
 * 
 * @author Ralf
 *
 */
public class Formatter {

	private Formatter() {
	}
	
	public static String formatSeconds(Long seconds) {
		if (seconds == null) {
			return "--";
		}
		return formatSeconds(new Double(seconds));
	}
	
	public static String formatSeconds(Double seconds) {
		if (seconds == null) {
			return "--";
		}
		long time = seconds.longValue();
		if (seconds > 3559.) {
			return String.format("%d:%02d:%02d", time / 3600, (time % 3600) / 60, (time % 60));
		}
		long tenth = Math.round((seconds - time) * 10);
		if (tenth == 10) {
			tenth = 0;
			++time;
		}
		return String.format("%2d:%02d,%d", (time % 3600) / 60, (time % 60), tenth);
	}
	
	public static String formatSecondsForCharts(Long seconds) {
		if (seconds == null) {
			return "--";
		}
		if (seconds > 3559) {
			return String.format("%d:%02d:%02d", seconds / 3600, (seconds % 3600) / 60, (seconds % 60));
		}
		return String.format("%2d:%02d", (seconds % 3600) / 60, (seconds % 60));
	}
	
	public static String formatPace(Double seconds) {
		if (seconds == null) {
			return "--";
		}
		int time = seconds.intValue();
		if (seconds > 599) {
			return "-";
		}
		long tenth = Math.round((seconds - time) * 10);
		if (tenth == 10) {
			tenth = 0;
			++time;
		}
		return String.format("%2d:%02d,%d", (time % 3600) / 60, (time % 60), tenth);
	}
	
	public static String formatDistance(Double distance) {
		if (distance == null) {
			return "--";
		}
		DecimalFormat df = new DecimalFormat("0.00");
		return df.format(distance);
	}
	
	public static String formatDistanceToKm(Double distance) {
		if (distance == null) {
			return "--";
		}
		DecimalFormat df = new DecimalFormat("0.00");
		return df.format(distance/1000.);
	}
	
	public static String formatDistanceToKm3(Double distance) {
		if (distance == null) {
			return "--";
		}
		DecimalFormat df = new DecimalFormat("0.000");
		return df.format(distance/1000.);
	}
	
	public static String formatDate(Date date) {
		if (date == null) {
			return "--";
		}
		Format formatter = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
		return formatter.format(date);
	}

	public static String formatAsInteger(Double n) {
		if (n == null) {
			return "--";
		}
		DecimalFormat df = new DecimalFormat("0");
		return df.format(n);
	}

}
