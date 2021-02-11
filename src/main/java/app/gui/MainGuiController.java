package app.gui;

import app.test.TestExecutor;
import app.test.TestSummary;
import app.model.Test;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressBar;
import javafx.stage.Stage;


public class MainGuiController implements Initializable {

  @FXML
  private TestsTreeView testsTreeView;

  @FXML
  private ProgressBar testsProgressBar;

  @FXML
  private Button startTestsButton;

  @FXML
  private Button stopTestsButton;

  @FXML
  private MainPane mainPane;

  /**
   * Executor of test Tasks
   */
  private ExecutorService testsExecutor;

  private SimpleBooleanProperty testsInProgress = new SimpleBooleanProperty(false);

  private SimpleDoubleProperty testsCount = new SimpleDoubleProperty(0);

  private SimpleDoubleProperty testsDone = new SimpleDoubleProperty(0);

  /**
   * Controller that handles MainGui usecases such as start of tests, stop, refresh tree view etc...
   */
  public MainGuiController() {}

  /**
   * Loads fxml and returns it's scene
   *
   * @return
   * @throws IOException
   */
  public Scene getScene() throws IOException {
    var loader = new FXMLLoader();
    URL xmlUrl = getClass().getResource("MainGui.fxml");
    loader.setLocation(xmlUrl);
    Parent root = loader.load();
    var scene = new Scene(root);
    return scene;
  }

  /**
   * GUI initialization.
   *
   * @param url
   * @param resourceBundle
   */
  @Override
  public void initialize(URL url, ResourceBundle resourceBundle) {
    this.initBindings();
    this.initTestsTree();
  }

  /**
   * Binds gui elements with class properties
   */
  private void initBindings() {
    testsProgressBar.progressProperty().bind(
        testsDone.divide(testsCount)
    );
    startTestsButton.disableProperty().bind(testsInProgress);
    stopTestsButton.disableProperty().bind(testsInProgress.not());
  }

  /**
   * initializes tests tree.
   */
  private void initTestsTree() {
    this.initOnTestItemSelect();
  }

  /**
   * Adds listener for on test item select.
   * Listener shows test detail on select.
   */
  private void initOnTestItemSelect() {
    testsTreeView.treeView.getSelectionModel().selectedItemProperty().addListener(
        (observable, oldItem, newItem) -> {
          if (newItem == null) {
            return;
          }

          var testSummary = newItem.getValue().getTestSummary();
          if (testSummary != null) {
            mainPane.showTestPage(testSummary);
          }
        }
    );
  }

  /**
   * Loads and shows settings fxml
   *
   * @param event
   */
  @FXML
  private void handleSettingsClick(ActionEvent event) {
    FXMLLoader fxmlLoader = new FXMLLoader();
    fxmlLoader.setLocation(getClass().getResource("Settings.fxml"));
    Scene scene;
    try {
      scene = new Scene(fxmlLoader.load());
    } catch (IOException e) {
      e.printStackTrace();
      throw new RuntimeException(e);
    }

    Stage stage = new Stage();
    var controller = (SettingsController) fxmlLoader.getController();
    controller.initialize(stage);
    stage.setTitle("Nastavení");
    stage.setScene(scene);
    stage.show();
  }

  /**
   * Creates and executes test tasks in separate thread via
   * ThreadPool executor.
   *
   * @param event
   * @throws IOException
   */
  @FXML
  private void handleStartTests(ActionEvent event)
      throws IOException {
    testsInProgress.set(true);
    testsTreeView.resetColors();

    testsExecutor = Executors.newCachedThreadPool();
    testsCount.set(testsTreeView.streamCheckedLeafs().count());
    testsDone.set(0);

    testsTreeView.streamCheckedLeafs().forEach(item -> {
      var task = new TestTask(item.getValue().test);
      task.setOnSucceeded(workStateEvent -> {
        var testSummary = task.getValue();
        synchronized (testsDone) {
          testsDone.set(testsDone.get() + 1);
        }
        item.getValue().setTestSummary(testSummary);
      });
      testsExecutor.submit(task);
    });

    testsExecutor.shutdown();
    // await shutdown
    awaitTestsDone();
  }

  /**
   * Awaits tests done in new thread.
   */
  private void awaitTestsDone() {
    new Thread(() -> {
      while (!testsExecutor.isTerminated()) {
        try {
          var terminated = testsExecutor.awaitTermination(1, TimeUnit.DAYS);
          if (terminated) {
            testsInProgress.set(false);
          }
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
      }
    }).start();
  }


  class TestTask extends Task<TestSummary> {

    private final Test test;

    /**
     * Tasks that executes Test and returns TestSummary
     *
     * @param test
     */
    TestTask(Test test) {
      this.test = test;
    }

    @Override
    public TestSummary call() {
      try {
        var testExecutor = new TestExecutor(test);
        var testSummary = testExecutor.execute();
        return testSummary;
      } catch (Throwable e) {
        e.printStackTrace();
        throw new RuntimeException(e);
      }
    }
  }


  /**
   * Sends shutdown signal to current tests executor
   *
   * @param event
   */
  @FXML
  private void handleStopTests(ActionEvent event) {
    if (testsExecutor != null) {
      testsExecutor.shutdownNow();
    }
  }

  /**
   * Handles refresh action
   *
   * @param event
   */
  @FXML
  private void handleRefresh(ActionEvent event) {
    mainPane.reset();
    testsTreeView.reset();
  }

}
