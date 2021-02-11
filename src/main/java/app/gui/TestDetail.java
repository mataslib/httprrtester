package app.gui;

import app.test.TestSummary;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.text.Text;

public class TestDetail extends ScrollPane implements Initializable {

  @FXML
  private TextArea response;

  @FXML
  private TextArea successfulTests;

  @FXML
  private TextArea failedTests;

  /**
   * Total time of test execution
   */
  @FXML
  private Text totalTime;

  /**
   * Time of request until response
   */
  @FXML
  private Text requestTime;

  /**
   * Holder of data about test execution
   */
  private TestSummary testSummary;

  /**
   * Custom control that shows Test result (detail)
   * Loads fxml
   *
   * @param testSummary
   */
  public TestDetail(TestSummary testSummary) {
    this.testSummary = testSummary;
    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(
        "TestDetail.fxml"));
    fxmlLoader.setRoot(this);
    fxmlLoader.setController(this);

    try {
      fxmlLoader.load();
    } catch (IOException exception) {
      exception.printStackTrace();
      throw new RuntimeException(exception);
    }
  }

  /**
   * Initializes fields with data
   *
   * @param location
   * @param resources
   */
  @Override
  public void initialize(URL location, ResourceBundle resources) {
    totalTime.setText(testSummary.totalTime + " ms");
    requestTime.setText(testSummary.requestTime + " ms");
    response.setText(testSummary.responseBody);
    successfulTests.setText(testSummary.getSuccessedString());
    failedTests.setText(testSummary.getFailedString());
  }

}
