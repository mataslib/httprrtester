package app.test;

import app.JaxbFacade;

public class TestResult {

  public String xmlDefinition;

  public String testclass;

  public boolean passed;

  public String executionInfo;

  /**
   * Data structure holding result data of single response test
   *
   * @param test
   * @param passed
   */
  public TestResult(
      ITest test,
      boolean passed
  ) {
    this.passed = passed;

    if (test != null) {
      try {
        this.xmlDefinition = JaxbFacade.marshalOnlyElementToString(test);
      } catch (Throwable e) {
        e.printStackTrace();
      }
      testclass = test.toString();
    }
  }

  /**
   * Gets identification of response test.
   * It is its xml definition in best case.
   *
   * @return
   */
  public String getIdentification()
  {
    if (xmlDefinition != null) {
      return xmlDefinition;
    }

    return testclass;
  }

  public boolean isPassed() {
    return passed;
  }

  public TestResult setXmlDefinition(String xmlDefinition) {
    this.xmlDefinition = xmlDefinition;
    return this;
  }

  public TestResult setPassed(boolean passed) {
    this.passed = passed;
    return this;
  }

  public TestResult setExecutionInfo(String executionInfo) {
    this.executionInfo = executionInfo;
    return this;
  }

  public TestResult addExecutionInfoLine(String executionInfo) {
    var newLine = executionInfo + "\n";
    if (this.executionInfo == null) {
      this.executionInfo = newLine;
    } else {
      this.executionInfo += newLine;
    }
    return this;
  }
}
