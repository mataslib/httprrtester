package app.test;

import app.test.RequestExecutor.Result;
import java.net.HttpURLConnection;

public class TestContext implements ITestContext  {

  public final Result result;

  /**
   * Holds data context relevant for tests
   * @see ITestContext for more info
   * @param result
   */
  public TestContext(Result result) {
    this.result = result;
  }

  @Override
  public String getResponseBody() {
    return result.responseBody;
  }

  @Override
  public Long getTotalTime() {
    return result.totalTime;
  }

  @Override
  public HttpURLConnection getConnection() {
    return result.connection;
  }

}
