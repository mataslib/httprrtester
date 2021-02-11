package app.gui;

import javafx.scene.control.TreeCell;
import javafx.scene.control.TreeView;
import javafx.util.Callback;

public class TestsTreeCellFactory implements Callback<TreeView<TestsTreeItemValue>, TreeCell<TestsTreeItemValue>> {

  @Override
  public TreeCell<TestsTreeItemValue> call(TreeView param) {
    var cell = new TestsTreeCell();

    return cell;
  }
}