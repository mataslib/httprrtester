package app.gui;

import javafx.scene.control.cell.CheckBoxTreeCell;

public class TestsTreeCell extends CheckBoxTreeCell<TestsTreeItemValue> {

  private final TestItemContextMenu testItemContextMenu;
  private final TestItemParentContextMenu testItemParentContextMenu;

  public TestsTreeCell()
  {
    testItemContextMenu = new TestItemContextMenu(this);
    testItemParentContextMenu = new TestItemParentContextMenu(this);
  }

  /**
   * Binds style property of tree row with style property of tree value.
   * (Coloring of rows based on test execution, test search, ...)
   * Sets context menus onto rows.
   *
   * @param value
   * @param empty
   */
  @Override
  public void updateItem(TestsTreeItemValue value, boolean empty) {
    super.updateItem(value, empty);

    var item = getTreeItem();
    var isItemEmpty = empty || item == null;
    var isTestItem = !isItemEmpty && item.isLeaf() && item.getValue().test != null;

    if (isItemEmpty) {
      // tato metoda se vola ruzne (scroll, unwrap, init, ...),
      // musim zde unbindovat a resetovat styl, jinak to dela neplechu a obarvuji
      // se neexistujici (virtualni) itemy
      styleProperty().unbind();
      setStyle(null);
    } else if (isTestItem) {
      setContextMenu(testItemContextMenu);
      styleProperty().bind(item.getValue().styleProperty);
    } else {
      setContextMenu(testItemParentContextMenu);
    }
  }

}
