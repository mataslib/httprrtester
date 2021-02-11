package app.gui;

import app.model.DbManager;
import app.model.DbManager.Config;
import java.net.URL;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class LoginController extends Application {

  Stage primaryStage;

  @FXML
  private TextField hostInput;
  @FXML
  private TextField portInput;
  @FXML
  private TextField databaseInput;
  @FXML
  private TextField usernameInput;
  @FXML
  private TextField passwordInput;
  @FXML
  private Text statusText;

  /**
   * Controller that handles login into app usecase
   */
  public LoginController() {
    super();
  }

  public static void main(String[] args) {
    launch(args);
  }

  /**
   * Loads and shows fxml.
   *
   * @param primaryStage
   */
  @Override
  public void start(Stage primaryStage) {
    try {
      this.primaryStage = primaryStage;

      // loads FXML
      var loader = new FXMLLoader();
      loader.setController(this);
      URL xmlUrl = getClass().getResource("Login.fxml");
      loader.setLocation(xmlUrl);
      Parent root = loader.load();

      var scene = new Scene(root);
      primaryStage.setTitle("Login into Http Request Response Tester");
      primaryStage.setScene(scene);
      primaryStage.show();
    } catch (Throwable exception) {
      exception.printStackTrace();
    }
  }

  /**
   * Handles login usecase, configures database access with given credentials
   * and then redirects to main controller
   *
   * @param event
   */
  @FXML
  private void handleLoginClick(ActionEvent event) {
    try {
      DbManager.configure(new Config(
          hostInput.getText(),
          Integer.parseInt(portInput.getText()),
          databaseInput.getText(),
          usernameInput.getText(),
          passwordInput.getText()
      ));
      DbManager.instance(); // test connect to db
      statusText.setText("Úspěch!");
      // init main controller

      var main = new MainGuiController();
      var mainScene = main.getScene();
      primaryStage.setTitle("Http Request Response Tester");
      primaryStage.setScene(mainScene);

    } catch (Throwable e) {
      e.printStackTrace();
      statusText.setText(String.format("Něco se pokazilo. (%s)%n", e.toString()));
      throw new RuntimeException(e);
    }
  }
}
