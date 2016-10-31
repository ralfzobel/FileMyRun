package de.acwhadk.rz.filemyrun.core.gui;

import de.acwhadk.rz.filemyrun.core.setup.Lang;
import javafx.concurrent.Task;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * A dialog showing a progress bar.
 * 
 * @author Ralf
 *
 */
public class ProgressDialog {

	private final Stage dialogStage;
	private final ProgressBar pb = new ProgressBar();

	public ProgressDialog(String text) {
		dialogStage = new Stage();
		dialogStage.initStyle(StageStyle.UTILITY);
		dialogStage.setResizable(false);
		dialogStage.initModality(Modality.APPLICATION_MODAL);
		dialogStage.setTitle(Lang.get().text(Lang.PROGRESS_DLG_WAIT));
		dialogStage.setOnCloseRequest(e -> {
			e.consume();
		});
		
		// PROGRESS BAR
		final Label label = new Label();
		label.setText(text);

		pb.setProgress(-1F);

		final VBox vBox = new VBox();
		vBox.setSpacing(20);
		Insets insets = new Insets(20.);
		vBox.setPadding(insets);
		vBox.setAlignment(Pos.BASELINE_LEFT);
		vBox.getChildren().addAll(label, pb);

		Scene scene = new Scene(vBox);
		dialogStage.setScene(scene);
	}

	public void activateProgressBar(final Task<?> task)  {
        pb.progressProperty().bind(task.progressProperty());
		dialogStage.showAndWait();
	}

	public void close() {
		dialogStage.close();
	}

}
