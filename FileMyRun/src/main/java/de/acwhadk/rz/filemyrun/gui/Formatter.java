package de.acwhadk.rz.filemyrun.gui;

import java.text.DecimalFormat;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;

import de.acwhadk.rz.filemyrun.core.setup.Const;

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
			return Const.DOUBLE_DASH;
		}
		return formatSeconds(new Double(seconds));
	}
	
	public static String formatSeconds(Double seconds) {
		if (seconds == null) {
			return Const.DOUBLE_DASH;
		}
		long time = seconds.longValue();
		if (seconds > 3559.) {
			return String.format(Const.FORMAT_TIME_H_MM_SS, time / 3600, (time % 3600) / 60, (time % 60));
		}
		long tenth = Math.round((seconds - time) * 10);
		if (tenth == 10) {
			tenth = 0;
			++time;
		}
		return String.format(Const.FORMAT_TIME_MM_SS_TENTH, (time % 3600) / 60, (time % 60), tenth);
	}
	
	public static String formatSecondsForCharts(Long seconds) {
		if (seconds == null) {
			return Const.DOUBLE_DASH;
		}
		if (seconds > 3559) {
			return String.format(Const.FORMAT_TIME_H_MM_SS, seconds / 3600, (seconds % 3600) / 60, (seconds % 60));
		}
		return String.format(Const.FORMAT_TIME_MM_SS, (seconds % 3600) / 60, (seconds % 60));
	}
	
	public static String formatPace(Double seconds) {
		if (seconds == null) {
			return Const.DOUBLE_DASH;
		}
		int time = seconds.intValue();
		if (seconds > 599) {
			return Const.DASH;
		}
		long tenth = Math.round((seconds - time) * 10);
		if (tenth == 10) {
			tenth = 0;
			++time;
		}
		return String.format(Const.FORMAT_TIME_MM_SS_TENTH, (time % 3600) / 60, (time % 60), tenth);
	}
	
	public static String formatDistance(Double distance) {
		if (distance == null) {
			return Const.DOUBLE_DASH;
		}
		DecimalFormat df = new DecimalFormat(Const.FORMAT_DECIMAL_2);
		return df.format(distance);
	}
	
	public static String formatDistanceToKm(Double distance) {
		if (distance == null) {
			return Const.DOUBLE_DASH;
		}
		DecimalFormat df = new DecimalFormat(Const.FORMAT_DECIMAL_2);
		return df.format(distance/1000.);
	}
	
	public static String formatDistanceToKm3(Double distance) {
		if (distance == null) {
			return Const.DOUBLE_DASH;
		}
		DecimalFormat df = new DecimalFormat(Const.FORMAT_DECIMAL_3);
		return df.format(distance/1000.);
	}
	
	public static String formatFullDate(Date date) {
		if (date == null) {
			return Const.DOUBLE_DASH;
		}
		Format formatter = new SimpleDateFormat(Const.FORMAT_FULL_DATE);
		return formatter.format(date);
	}

	public static String formatDate(LocalDate date) {
		if (date == null) {
			return Const.DOUBLE_DASH;
		}
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern(Const.FORMAT_DATE);
		return date.format(formatter);
	}

	public static String formatAsInteger(Double n) {
		if (n == null) {
			return Const.DOUBLE_DASH;
		}
		DecimalFormat df = new DecimalFormat(Const.FORMAT_DECIMAL_0);
		return df.format(n);
	}

}
