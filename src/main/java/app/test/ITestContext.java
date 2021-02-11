package app.test;

import java.net.HttpURLConnection;

/**
 * Context is data structure passed to tests holding relevant data
 */
public interface ITestContext {

  public String getResponseBody();

  /**
   * @return time until response
   */
  public Long getTotalTime();

  /**
   * @return request connection
   */
  public HttpURLConnection getConnection();

}
