package de.acwhadk.rz.filemyrun.app;

import org.apache.log4j.Logger;

import de.acwhadk.rz.filemyrun.gui.Controller;
import de.acwhadk.rz.filemyrun.gui.EquipmentMan;
import de.acwhadk.rz.filemyrun.gui.GuiControl;
import de.acwhadk.rz.filemyrun.gui.TrainingFileMan;
import de.acwhadk.rz.filemyrun.setup.Const;
import de.acwhadk.rz.filemyrun.setup.Setup;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

/**
 * The App class contains the start method that is called by java fx.
 * 
 * @author Ralf
 *
 */
public class App extends Application {

	final static Logger logger = Logger.getLogger(App.class);
	
	@Override
	public void start(Stage primaryStage) {
		logger.info("FileMyRun started");
		System.setProperty("java.net.useSystemProxies", "true");
		try {
			Setup setup = Setup.getInstance();
			if (setup.getIoError() != null) {
				throw setup.getIoError(); 
			}
			Image icon = new Image(getClass().getResourceAsStream("running_man_16.png"));
			primaryStage.getIcons().add(icon);
			icon = new Image(getClass().getResourceAsStream("running_man_32.png"));
			primaryStage.getIcons().add(icon);
			
			TrainingFileMan tm = new TrainingFileMan();
			EquipmentMan em = new EquipmentMan();
			
			Parent root=null;
			Controller controller=null;
			final FXMLLoader loader = new FXMLLoader(getClass().getResource(Const.FXML_FILEMYRUN));
			root = (Parent) loader.load();
			controller = loader.<Controller>getController();
			
			new GuiControl(controller, tm, em, primaryStage);
			Scene scene = new Scene(root);
			primaryStage.setScene(scene);
			primaryStage.setTitle(setup.getApplicationName());
		} catch (Exception e) {
			GuiControl.showException(e);
			Platform.exit();
		}
		
		primaryStage.show();
	}

	public static void main(String[] args) {
		launch(args);
	}
}
