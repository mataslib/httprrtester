package app.test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Collectors;

public class TestSummary {

  /**
   * Results of subtests
   */
  public ArrayList<TestResult> resultsOfTests = new ArrayList<>();

  public String responseBody;

  /**
   * Total time of Test
   */
  public long totalTime;

  /**
   * Time until reponse is retreived
   */
  public long requestTime;

  /**
   * Set when Test failed unexpectedly
   */
  public Throwable failedUnexpectedlyException;

  /**
   * Summary of single Test execution, holds data displayed in TestDetail.
   */
  public TestSummary() {}

  public boolean isTestSuccess() {
    if (failedUnexpectedlyException != null) {
      // Test failed unexpectedly, xml definition unmarshal error, http error, etc...
      return false;
    }

    var failedResults = this.getFailedResults();
    return failedResults.isEmpty();
  }

  /**
   * String representation of susccessful subtests
   *
   * @return
   */
  public String getSuccessedString() {
    var successResults = this.getSuccessfulResults();
    var successString = successResults
        .stream()
        .map(result -> {
          if (result.executionInfo == null) {
            return result.getIdentification();
          } else {
            return String.format("%s%n%n%s", result.getIdentification(), result.executionInfo);
          }
        })
        .collect(Collectors.joining("\n---\n"));
    return successString;
  }

  /**
   * String representation of failed subtests
   *
   * @return
   */
  public String getFailedString() {
    if (failedUnexpectedlyException != null) {
      return String.format(
          "Test selhal neočekávaně (%s)%n.",
          failedUnexpectedlyException.toString()
      );
    }

    var failedResults = this.getFailedResults();
    var failedString = failedResults
        .stream()
        .map(result -> {
          if (result.executionInfo == null) {
            return result.getIdentification();
          } else {
            return String.format("%s%n%n%s", result.getIdentification(), result.executionInfo);
          }
        })
        .collect(Collectors.joining("\n---\n"));
    return failedString;
  }

  public Collection<TestResult> getFailedResults() {
    return resultsOfTests
        .stream()
        .filter(testResult -> !testResult.isPassed())
        .collect(Collectors.toList());
  }

  public Collection<TestResult> getSuccessfulResults() {
    return resultsOfTests
        .stream()
        .filter(testResult -> testResult.isPassed())
        .collect(Collectors.toList());
  }

  public void failedUnexpectedly(Throwable e) {
    this.failedUnexpectedlyException = e;
  }
}