package app.gui;

import app.AppSettings;
import app.AppSettings.Settings;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.text.Text;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

public class SettingsController {

  @FXML
  private Text savePathInput;

  @FXML
  private CheckBox doSaveResponsesInput;

  @FXML
  private Text statusText;

  private Settings settings;

  private Stage stage;

  /**
   * Controller that handles settings form usecase
   */
  public SettingsController() {
  }

  /**
   * Initializes controller, fills fields with Settings data
   *
   * @param stage
   */
  public void initialize(Stage stage) {
    try {
      this.stage = stage;
      settings = AppSettings.instance().get();
      savePathInput.setText(settings.responsesSavePath);
      doSaveResponsesInput.setSelected(settings.doSaveResponses);
    } catch (IOException e) {
      e.printStackTrace();
      throw new RuntimeException(e);
    }
  }

  /**
   * Handles select Directory path
   *
   * @param event
   */
  @FXML
  private void handleSelectSavePath(ActionEvent event) {
    var directoryChooser = new DirectoryChooser();
    directoryChooser.setTitle("Vybrat složku");

    if (settings.responsesSavePath != null) {
      var currentPath = Path.of(settings.responsesSavePath);
      if (Files.exists(currentPath)
          && Files.isDirectory(currentPath)
          && currentPath.toFile().isFile()
      ) {
        var defaultDirectory = currentPath.toFile();
        directoryChooser.setInitialDirectory(defaultDirectory);
      }
    }

    var selectedDirectory = directoryChooser.showDialog(stage);
    if (selectedDirectory == null) { // image not selected
      return;
    }

    savePathInput.setText(selectedDirectory.getAbsolutePath());
  }

  /**
   * Handles save of settings
   *
   * @param event
   */
  @FXML
  private void handleSave(ActionEvent event) {
    try {
      settings.responsesSavePath = savePathInput.getText();
      settings.doSaveResponses = doSaveResponsesInput.isSelected();
      AppSettings.instance().save(settings);
      statusText.setText("Uloženo");
    } catch (Throwable e) {
      statusText.setText(String.format("Došlo k chybě. (%s)%n", e.getMessage()));
    }
  }
}
