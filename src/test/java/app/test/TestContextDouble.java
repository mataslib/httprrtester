package app.test;

import java.net.HttpURLConnection;

public class TestContextDouble implements ITestContext {

  private String responseBody;
  private Long totalTime;
  private HttpURLConnection connection;

  public TestContextDouble setResponseBody(String responseBody) {
    this.responseBody = responseBody;
    return this;
  }

  public TestContextDouble setTotalTime(Long totalTime) {
    this.totalTime = totalTime;
    return this;
  }

  public TestContextDouble setConnection(HttpURLConnection connection) {
    this.connection = connection;
    return this;
  }

  @Override
  public String getResponseBody() {
    return responseBody;
  }

  @Override
  public Long getTotalTime() {
    return totalTime;
  }

  @Override
  public HttpURLConnection getConnection() {
    return connection;
  }
}
