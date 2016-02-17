package de.acwhadk.rz.filemyrun.gui;

import de.acwhadk.rz.filemyrun.controller.Controller;
import de.acwhadk.rz.filemyrun.controller.EquipmentMan;
import de.acwhadk.rz.filemyrun.controller.GuiControl;
import de.acwhadk.rz.filemyrun.controller.TrainingFileMan;
import de.acwhadk.rz.filemyrun.setup.Setup;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class App extends Application {

	@Override
	public void start(Stage primaryStage) {
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
			final FXMLLoader loader = new FXMLLoader(getClass().getResource("filemyrun.fxml"));
			root = (Parent) loader.load();
			controller = loader.<Controller>getController();
			
			new GuiControl(controller, tm, em, primaryStage);
			Scene scene = new Scene(root);
			primaryStage.setScene(scene);
			primaryStage.setTitle(setup.getApplicationName());
		} catch (Exception e) {
			GuiControl.showException(e);
			System.exit(1);
		}
		
		primaryStage.show();
	}

	public static void main(String[] args) {
		launch(args);
	}
}
