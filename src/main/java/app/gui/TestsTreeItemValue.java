package app.gui;

import app.test.TestSummary;
import app.model.Test;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class TestsTreeItemValue {

  @Override
  public String toString() {
    return text == null ? "" : text;
  }

  /**
   * Label of row
   */
  public String text;

  /**
   * Style property of row
   */
  public StringProperty styleProperty = new SimpleStringProperty();

  /**
   * Underlying Test model of row
   */
  public Test test;
  private TestState testState = TestState.INITIAL;

  /**
   * summary of last Test execution
   */
  private TestSummary testSummary;

  private boolean isHighlighted = false;

  /**
   * Value behind tree row. Only text for parent rows.
   *
   * @param text
   */
  public TestsTreeItemValue(
      String text
  ) {
    this.text = text;
  }

  /**
   * Value behind tree row. Leafs holds Test.
   *
   * @param text
   * @param test
   */
  public TestsTreeItemValue(
      String text,
      Test test
  ) {
    this.text = text;
    this.test = test;
  }

  /**
   * Notifies this with executed test summary.
   *
   * @param testSummary
   */
  public void setTestSummary(TestSummary testSummary) {
    this.setTestState(testSummary);
    this.testSummary = testSummary;
  }


  /**
   * Sets state of Test model and updates GUI to reflect it.
   *
   * @param testSummary
   */
  private void setTestState(TestSummary testSummary) {
    this.testState = testSummary == null
        ? TestState.INITIAL
        : testSummary.isTestSuccess()
            ? TestState.SUCCESS
            : TestState.FAIL;
    updateStyle();
  }

  /**
   * Updates style of row.
   */
  private void updateStyle() {
    var style = String.format("-fx-background-color: %s;", this.testState.getColor());
    if (isHighlighted) {
      style += "-fx-text-fill: purple;";
    }
    styleProperty.setValue(style);
  }

  /**
   * Highlights row (eg. searched test).
   *
   * @param value
   */
  public void setIsHighlighted(boolean value) {
    this.isHighlighted = value;
    updateStyle();
  }


  /**
   * Resets styles of row and test summary of last execution
   */
  public void reset() {
    this.testSummary = null;
    this.styleProperty.set(null);
  }

  public TestSummary getTestSummary() {
    return this.testSummary;
  }

  public enum TestState {
    INITIAL,
    SUCCESS,
    FAIL;

    public String getColor() {
      switch (name()) {
        case "INITIAL":
          return "transparent";
        case "SUCCESS":
          return "#a8e6cf";
        case "FAIL":
          return "#ffaaa5";
      }
      return null;
    }
  }
}
