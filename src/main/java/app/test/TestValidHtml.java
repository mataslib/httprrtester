package app.test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.stream.Collectors;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "test-valid-html")
class TestValidHtml implements ITest {
  private static final String APIURL = "https://validator.w3.org/nu/?out=gnu";

  /**
   * Tests that response body is valid html using w3c validator API.
   *
   * @param context
   * @return
   */
  @Override
  public TestResult test(ITestContext context) throws Throwable {
    var connection = postToApi(context);
    var apiResult = readResult(connection);
    var isSuccess = apiResult.isEmpty();

    return new TestResult(this, isSuccess)
        .setExecutionInfo(isSuccess ? null : apiResult);
  }

  /**
   * Post to API
   *
   * @param context
   * @return
   * @throws IOException
   */
  private HttpURLConnection postToApi(ITestContext context) throws IOException {
    var connection = (HttpURLConnection) new URL(APIURL).openConnection();
    connection.setRequestMethod("POST");
    connection.setDoOutput(true);
    connection.setRequestProperty("Content-Type", "text/html");
    var out = new PrintWriter(connection.getOutputStream());
    out.print(context.getResponseBody());
    out.flush();
    out.close();
    return connection;
  }

  /**
   * Read API result
   *
   * @param connection
   * @return
   * @throws IOException
   */
  private String readResult(HttpURLConnection connection) throws IOException {
    var in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
    connection.connect();
    var result = in
        .lines().parallel()
        .collect(Collectors.joining("\n"));
    return result;
  }
}