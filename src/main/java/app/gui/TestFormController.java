package app.gui;

import app.model.DbManager;
import app.model.Test;
import com.j256.ormlite.dao.Dao;
import java.sql.SQLException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

public class TestFormController {

  /**
   * Database Access Object of Test model
   */
  private final Dao<Test, Integer> testDao = DbManager.instance().dao(Test.class);

  @FXML
  private TextArea testDefinitionInput;
  @FXML
  private TextField nameInput;
  @FXML
  private CheckBox sharedForReadInput;
  @FXML
  private CheckBox sharedForUpdateInput;
  @FXML
  private Text statusText;
  @FXML
  private Button saveButton;

  private Test test;

  /**
   * Hnadles creation and update of Test via form
   */
  public TestFormController() {
  }

  /**
   * Initializes controller and its fields
   *
   * @param testId
   */
  public void initializeWithTestId(int testId) {
    this.fetchTest(testId);
    nameInput.setText(test.name);
    testDefinitionInput.setText(test.testDefinition);
    sharedForReadInput.setSelected(test.sharedForRead);
    sharedForUpdateInput.setSelected(test.sharedForUpdate);

    if (!test.isDbuserAllowedToUpdate(DbManager.getConfig().username)) {
      saveButton.setDisable(true);
      statusText.setText("Nemáte oprávnění k úpravě testu.");
    }
  }

  /**
   * fetches Test model from db
   *
   * @param testId
   */
  private void fetchTest(int testId) {
    try {
      test = testDao.queryForId(testId);
    } catch (Throwable exception) {
      exception.printStackTrace();
      throw new RuntimeException(exception);
    }
  }

  /**
   * Handles save of Test model.
   *
   * @param event
   */
  @FXML
  private void handleSave(ActionEvent event) {
    statusText.setText("");
    try {
      var isNew = test == null;
      if (isNew) {
        this.createTest();
      } else {
        this.updateTest();
      }
      statusText.setText("Uloženo.");
    } catch (Throwable exception) {
      exception.printStackTrace();
      statusText.setText(String.format("Došlo k chybě. (%s)%n", exception.getMessage()));
    }
  }

  /**
   * Creates new Test model from form fields
   *
   * @throws SQLException
   */
  private void createTest() throws SQLException {
    var newTest = new Test();
    applyInputs(newTest);
    testDao.create(newTest);
    test = newTest;
  }

  /**
   * Updates existing Test model from form fields
   *
   * @throws SQLException
   */
  private void updateTest() throws SQLException {
    applyInputs(test);
    testDao.update(test);
  }

  /**
   * Fills Test model with form field values
   *
   * @param test
   */
  private void applyInputs(Test test) {
    test.testDefinition = testDefinitionInput.getText();
    test.name = nameInput.getText();
    test.sharedForRead = sharedForReadInput.isSelected();
    test.sharedForUpdate = sharedForUpdateInput.isSelected();
  }
}
