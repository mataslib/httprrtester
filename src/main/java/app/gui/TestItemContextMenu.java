package app.gui;

import app.model.DbManager;
import app.model.Test;
import java.io.IOException;
import java.sql.SQLException;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.stage.Stage;

public class TestItemContextMenu extends ContextMenu {

  private final TestsTreeCell cell;

  /**
   * Context menu of Test items (leafs)
   *
   * @param cell
   */
  public TestItemContextMenu(TestsTreeCell cell)
  {
    super();
    this.cell = cell;
    this.getItems().addAll(buildDeleteItem(), buildEditItem());
  }

  /**
   * Builds delete Test action
   * @return
   */
  private MenuItem buildDeleteItem()
  {
    var deleteItem = new MenuItem("Smazat");
    deleteItem.setOnAction(new EventHandler<ActionEvent>() {
      @Override
      public void handle(ActionEvent event) {
        var selectedItem = cell.getTreeItem();
        // obcas lze nejak vytvorit context nabidku mimo item, pak je null
        if (selectedItem == null) {
          return;
        }

        Alert alert = new Alert(AlertType.CONFIRMATION, "Opravdu vymazat?", ButtonType.YES, ButtonType.CANCEL);
        alert.showAndWait();
        if (alert.getResult() != ButtonType.YES) {
          return;
        }

        var testDao = DbManager.instance().dao(Test.class);
        try {
          testDao.deleteById(selectedItem.getValue().test.id);
          selectedItem.getParent().getChildren().remove(selectedItem);
        } catch (SQLException throwables) {
          Alert errorAlert = new Alert(AlertType.ERROR, throwables.toString());
          errorAlert.show();
          throwables.printStackTrace();
        }
      }
    });

    return deleteItem;
  }

  /**
   * Builds edit Test  action
   * @return
   */
  private MenuItem buildEditItem()
  {
    var editItem = new MenuItem("Upravit");
    editItem.setOnAction(new EventHandler<ActionEvent>() {
      @Override
      public void handle(ActionEvent event) {
        var selectedItem = cell.getTreeItem();
        // obcas lze nejak vytvorit context nabidku mimo item, pak je null
        if (selectedItem == null) {
          return;
        }

        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("TestForm.fxml"));
        Scene scene = null;
        try {
          scene = new Scene(fxmlLoader.load());
        } catch (IOException e) {
          e.printStackTrace();
        }

        var controller = (TestFormController) fxmlLoader.getController();
        controller.initializeWithTestId(selectedItem.getValue().test.id);

        Stage stage = new Stage();
        stage.setTitle("Formulář testu");
        stage.setScene(scene);
        stage.show();
      }
    });
    return editItem;
  }
}
