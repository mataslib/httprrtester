package app.gui;

import app.model.DbManager;
import app.model.Test;
import java.io.IOException;

import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.ResourceBundle;
import java.util.function.Predicate;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBoxTreeItem;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.layout.GridPane;

public class TestsTreeView extends GridPane implements Initializable {

  @FXML
  public TreeView<TestsTreeItemValue> treeView;

  @FXML
  private TextField searchInput;

  /**
   * Custom control
   */
  public TestsTreeView() {
    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(
        "TestsTreeView.fxml"));
    fxmlLoader.setRoot(this);
    fxmlLoader.setController(this);

    try {
      fxmlLoader.load();
    } catch (IOException exception) {
      exception.printStackTrace();
      throw new RuntimeException(exception);
    }
  }

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    try {
      treeView.setCellFactory(new TestsTreeCellFactory());
      var root = createItems();
      treeView.setRoot(root);
    } catch (Throwable exception) {
      exception.printStackTrace();
      throw new RuntimeException(exception);
    }
  }

  public void reset() {
    treeView.setRoot(null);
    var root = createItems();
    treeView.setRoot(root);
    treeView.refresh();
  }

  public void resetColors() {
    streamLeafs().forEach(item -> {
      item.getValue().styleProperty.setValue("");
    });
  }

  private CheckBoxTreeItem<TestsTreeItemValue> createItems() {
    try {
      var testDao = DbManager.instance().dao(Test.class);

      var root = new CheckBoxTreeItem<>(
          new TestsTreeItemValue("Testy")
      );
      root.setExpanded(true);

      var myTestsRoot = new CheckBoxTreeItem<>(
          new TestsTreeItemValue("Mé testy")
      );
      myTestsRoot.setExpanded(true);
      var myTests = testDao.queryBuilder()
          .where().eq("dbuser", DbManager.getConfig().username).queryBuilder()
          .orderBy("id", true)
          .query();
      myTests.stream().forEach(
          test -> myTestsRoot.getChildren().add(
              new CheckBoxTreeItem<>(new TestsTreeItemValue(test.name, test))
          )
      );


      var sharedTestsRoot = new CheckBoxTreeItem<>(
          new TestsTreeItemValue("Sdílené testy")
      );

      sharedTestsRoot.setExpanded(true);
      var sharedTestsGroupedByUser = testDao.queryBuilder()
          .where().not().eq("dbuser", DbManager.getConfig().username).queryBuilder()
          .orderBy("id", true)
          .query()
          .stream()
          .collect(Collectors.groupingBy(t -> t.dbuser))
      ;

      for (var sharedTestsOfUser: sharedTestsGroupedByUser.entrySet()) {
        var userSharedTestsRoot = new CheckBoxTreeItem<>(
            new TestsTreeItemValue(sharedTestsOfUser.getKey())
        );
        sharedTestsOfUser.getValue().stream().forEach(
            test -> userSharedTestsRoot.getChildren().add(
                new CheckBoxTreeItem<>(new TestsTreeItemValue(test.name, test))
            )
        );
        sharedTestsRoot.getChildren().add(userSharedTestsRoot);
      }

      root.getChildren().addAll(myTestsRoot, sharedTestsRoot);
      return root;
    } catch (Throwable exception) {
      exception.printStackTrace();
      throw new RuntimeException(exception);
    }
  }

  @FXML
  public void handleSearchEnter(ActionEvent event)
  {
    var searchTerm = searchInput.getText();
    streamAllItems()
        .forEach(item -> item.getValue().setIsHighlighted(false));
    if (searchTerm.length() > 0) {
      streamAllItems()
          .filter(item -> item.getValue().text.toLowerCase()
              .contains(searchTerm.toLowerCase())
          )
          .forEach(item -> item.getValue().setIsHighlighted(true));
    }
  }

  public Stream<CheckBoxTreeItem<TestsTreeItemValue>> streamLeafs()
  {
    return streamAllItems().filter(TreeItem::isLeaf);
  }

  public Stream<CheckBoxTreeItem<TestsTreeItemValue>> streamNotLeafs()
  {
    return streamAllItems().filter(Predicate.not(TreeItem::isLeaf));
  }

  public Stream<CheckBoxTreeItem<TestsTreeItemValue>> streamCheckedLeafs()
  {
    return streamLeafs().filter(CheckBoxTreeItem::isSelected);
  }

  public Stream<CheckBoxTreeItem<TestsTreeItemValue>> streamAllItems()
  {
    return streamItemsOf((CheckBoxTreeItem<TestsTreeItemValue>) treeView.getRoot());
  }

  public Stream<CheckBoxTreeItem<TestsTreeItemValue>> streamItemsOf(
      CheckBoxTreeItem<TestsTreeItemValue> testsTreeItemValueTreeItem
  ) {
    return Stream.concat(
        // Stream parametr itemu
        Stream.of(testsTreeItemValueTreeItem),
        // Spojit se streamem deti
        testsTreeItemValueTreeItem.getChildren().stream().flatMap(
            // A na kazde dite aplikuju (mapuju) opet streamItemsOf -> rekurze
            item -> this.streamItemsOf(
                (CheckBoxTreeItem<TestsTreeItemValue>) item
            )
        )
    );
  }


  /**
   * Returns checked tests
   *
   * @return
   */
  public Collection<CheckBoxTreeItem<TestsTreeItemValue>> getCheckedTests() {
    return streamLeafs().filter(CheckBoxTreeItem::isSelected).collect(Collectors.toList());
  }
}
