package app.test;

import app.test.TestDefinition.Request;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;
import java.util.stream.Collectors;

public class RequestExecutor {

  /**
   * HTTP request configuration
   */
  private Request request;

  /**
   * Makes request and returns result
   * @param request HTTP request configuration object
   */
  public RequestExecutor(Request request) {
    this.request = request;
  }

  /**
   * Executes HTTP request in accordance with given Request configuration,
   * returns Result object populated of HTTP response and metadata.
   *
   * @return
   * @throws IOException
   */
  public Result execute() throws IOException {
    var startTime = System.currentTimeMillis();

    var connection = openConnection();
    applyRequestConfiguration(connection);
    outputRequestData(connection);
    connection.connect();
    var totalTime = System.currentTimeMillis() - startTime;

    var responseBody = readResponse(connection);

    return new Result(connection, responseBody, totalTime);
  }

  /**
   * Reads HTTP response body
   *
   * @param connection
   * @return
   * @throws IOException
   */
  private String readResponse(HttpURLConnection connection) throws IOException {
    var reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
    var responseBody = reader.lines().collect(Collectors.joining("\n"));
    reader.close();
    return responseBody;
  }

  /**
   * Outputs request body to endpoint
   *
   * @param connection
   * @throws IOException
   */
  private void outputRequestData(HttpURLConnection connection) throws IOException {
    if (request.body == null || request.body.length() <= 0) {
      connection.setDoOutput(false);
      return;
    }
    connection.setDoOutput(true);
    var out = connection.getOutputStream();
    var outWriter = new PrintWriter(out);
    outWriter.write(request.body);
    outWriter.flush();
    outWriter.close();
  }

  /**
   * Applies configuration values such as headers or http method to connection object
   *
   * @param connection
   * @throws ProtocolException
   */
  private void applyRequestConfiguration(HttpURLConnection connection) throws ProtocolException {
    if (request.method != null) {
      connection.setRequestMethod(request.method.value());
    }

    for (var header : request.headers) {
      connection.setRequestProperty(header.key, header.value);
    }
  }

  /**
   * Opens connection with endpoint
   *
   * @return
   * @throws IOException
   */
  private HttpURLConnection openConnection() throws IOException {
    return (HttpURLConnection) new URL(request.url).openConnection();
  }

  /**
   * Result object populated of HTTP response and metadata
   */
  public static class Result
  {
    public final HttpURLConnection connection;
    public final String responseBody;
    public final Long totalTime;

    public Result(HttpURLConnection connection, String responseBody, long totalTime) {
      this.connection = connection;
      this.responseBody = responseBody;
      this.totalTime = totalTime;
    }
  }
}
