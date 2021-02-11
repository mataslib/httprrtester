package app.gui;

import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.stage.Stage;

public class TestItemParentContextMenu extends ContextMenu {

  private final TestsTreeCell cell;

  /**
   * Context menu of parent items
   *
   * @param cell
   */
  public TestItemParentContextMenu(TestsTreeCell cell) {
    super();
    this.cell = cell;
    this.getItems().addAll(buildNewItem());
  }

  /**
   * Builds new Test action
   *
   * @return
   */
  private MenuItem buildNewItem() {
    var newItem = new MenuItem("Nový");
    newItem.setOnAction(new EventHandler<ActionEvent>() {
      @Override
      public void handle(ActionEvent event) {
        var selectedItem = cell.getTreeItem();
        // obcas lze nejak vytvorit context nabidku mimo item, pak je null
        if (selectedItem == null) {
          return;
        }

        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("TestForm.fxml"));
        Scene scene;
        try {
          scene = new Scene(fxmlLoader.load());
        } catch (IOException e) {
          e.printStackTrace();
          throw new RuntimeException(e);
        }

        var controller = (TestFormController) fxmlLoader.getController();

        Stage stage = new Stage();
        stage.setTitle("Test form");
        stage.setScene(scene);
        stage.show();
      }
    });

    return newItem;
  }
}
