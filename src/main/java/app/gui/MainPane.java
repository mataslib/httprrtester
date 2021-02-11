package app.gui;

import app.test.TestSummary;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.GridPane;

public class MainPane extends GridPane {

  /**
   * Custom control of MainPane
   */
  public MainPane() {
    try {
      FXMLLoader fxmlLoader = new FXMLLoader(
          getClass().getResource("MainPane.fxml")
      );
      fxmlLoader.setRoot(this);
      fxmlLoader.setController(this);
      fxmlLoader.load();
    } catch (Throwable exception) {
      exception.printStackTrace();
      throw new RuntimeException(exception);
    }
  }

  /**
   * Display test result
   *
   * @param testSummary
   */
  public void showTestPage(TestSummary testSummary) {
    this.getChildren().add(
        new TestDetail(testSummary)
    );
  }

  /**
   * Resets MainPane contents
   */
  public void reset() {
    this.getChildren().removeAll();
  }
}
