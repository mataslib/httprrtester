package app.test;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.Collectors;
import javax.xml.bind.annotation.XmlRootElement;
import org.jsoup.Jsoup;
import org.jsoup.select.Elements;

@XmlRootElement(name = "test-working-links")
public class TestWorkingLinks implements ITest {

  private static final int numberOfWorkThreads = 3;

  /**
   * Tests that response body contains working links.
   *
   * @param context
   * @return
   */
  @Override
  public TestResult test(ITestContext context) throws Throwable {
    var anchors = parseAnchors(context);
    var resultsOfTasks = executeTests(context, anchors);
    var isSuccess = true;
    StringBuilder executionInfo = new StringBuilder();

    for (var taskResult: resultsOfTasks) {
      var result = taskResult.get();
      executionInfo.append(result.executionInfo);
      if (!result.isPassed()) {
        isSuccess = false;
      }
    }

    return new TestResult(this, isSuccess)
      .setExecutionInfo(executionInfo.toString())
    ;
  }

  /**
   * Executes tests as tasks
   *
   * @param context
   * @param anchors
   * @return
   * @throws InterruptedException
   */
  private List<Future<TestResult>> executeTests(ITestContext context, Elements anchors)
      throws InterruptedException {
    var threadPool = Executors.newFixedThreadPool(numberOfWorkThreads);
    var tasks = anchors
        .stream()
        .map(anchor -> new TestTask(
            anchor.attr("href"),
            context.getConnection().getURL().toString())
        )
        .collect(Collectors.toList());
    var resultsOfTasks = threadPool.invokeAll(tasks);
    threadPool.shutdown();
    return resultsOfTasks;
  }

  /**
   * Parses anchors of response body
   *
   * @param context
   * @return
   */
  private Elements parseAnchors(ITestContext context) {
    var parsedHtml = Jsoup.parse(context.getResponseBody());
    var anchors = parsedHtml.getElementsByTag("a");
    return anchors;
  }

  /**
   * Task for test execution
   */
  public static class TestTask implements Callable<TestResult>
  {
    private final String href;
    private final String hostname;

    public TestTask(
        String href,
        String hostname)
    {
      this.href = href;
      this.hostname = hostname;
    }

    @Override
    public TestResult call() throws Exception {
      return new TestWorkingLink().test(this.href, this.hostname);
    }
  }
}
