package de.acwhadk.rz.filemyrun.core.gui;

import org.apache.log4j.Logger;

import de.acwhadk.rz.filemyrun.core.setup.Const;
import de.acwhadk.rz.filemyrun.core.setup.Lang;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class ExceptionDialog {

	final static Logger logger = Logger.getLogger(ExceptionDialog.class);

	private ExceptionDialog() {}
	
	public static void showException(Exception e) {
		logger.error(Lang.get().text(Lang.EXCEPTION) + Const.COLON + Const.SPACE, e);
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle(Lang.get().text(Lang.EXCEPTION));
        alert.setHeaderText(Lang.get().text(Lang.EXCEPTION));
        alert.setContentText(e.toString());
        alert.showAndWait();
	}
}
