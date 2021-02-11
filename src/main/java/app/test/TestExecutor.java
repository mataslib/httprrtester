package app.test;

import app.AppSettings;
import app.Utils;
import app.model.Test;
import app.test.RequestExecutor.Result;
import app.JaxbFacade;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class TestExecutor {

  private final Test test;
  private TestDefinition testDefinition;
  private Result requestResult;
  private TestSummary testSummary;
  private long startTime;

  /**
   * Orchestrates and executes test
   */
  public TestExecutor(Test test) {
    this.test = test;
  }

  public TestSummary execute() throws Throwable {
    testSummary = new TestSummary();
    try {
      startTime = System.currentTimeMillis();

      unmarshalTestDefinition();
      executeRequest();
      executeTest();

      var totalTime = System.currentTimeMillis() - startTime;
      testSummary.totalTime = totalTime;

      try {
        saveResponse();
      } catch (Throwable e) {
        e.printStackTrace(); // ignore save responses error - is not primary usecase of app
      }

      return testSummary;
    } catch (Throwable e) {
      testSummary.failedUnexpectedly(e);
      return testSummary;
    }
  }

  private void executeTest() {
    var testContext = new TestContext(requestResult);
    for (var testMember : testDefinition.response.tests) {
      try {
        testSummary.resultsOfTests.add(
            testMember.test(testContext)
        );
      } catch (Throwable e) {
        testSummary.resultsOfTests.add(
            new TestResult(testMember, false)
                .addExecutionInfoLine(String.format(
                    "Něco se pokazilo (%s).%n",
                    e.getMessage()
                ))
        );
      }
    }
  }

  private void executeRequest() throws IOException {
    requestResult = new RequestExecutor(testDefinition.request).execute();
    testSummary.requestTime = requestResult.totalTime;
    testSummary.responseBody = requestResult.responseBody;
  }

  private void unmarshalTestDefinition() {
    testDefinition = JaxbFacade.unmarshalString(
        test.testDefinition,
        TestDefinition.class
    );
  }

  private void saveResponse() throws IOException {
    var savedir = AppSettings.instance().get().responsesSavePath;
    var responseContentType = requestResult.connection.getHeaderField("Content-Type");
    if (
        !AppSettings.instance().get().doSaveResponses
        || responseContentType == null
        || savedir == null
        || !Files.exists(Path.of(savedir))
    ) {
      return;
    }

    var fileExtension = Utils.resolveFileExtensionFromContentType(responseContentType);
    if (fileExtension == null) {
      return;
    }
    var now = Calendar.getInstance().getTime();
    var dateTimeStamp = new SimpleDateFormat("HH_mm_ss__dd_MM_yyyy");
    var filename = String
        .format("%s_%s_%s.%s", "test" + test.dbuser, test.name, dateTimeStamp.format(now),
            fileExtension);

    var filePath = savedir + File.separator + filename;
    Files.writeString(Path.of(filePath), requestResult.responseBody);
  }
}
