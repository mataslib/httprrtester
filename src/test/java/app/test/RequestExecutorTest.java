package app.test;

import static org.junit.jupiter.api.Assertions.*;

import app.test.RequestExecutor.Result;
import app.test.TestDefinition.Header;
import app.test.TestDefinition.MethodEnum;
import app.test.TestDefinition.Request;
import java.io.IOException;
import java.util.Arrays;
import org.junit.jupiter.api.Test;


class RequestExecutorTest {

  /**
   * Endpoint returns values defined in scenarios
   * @throws IOException
   */
  @Test
  void executeGet() throws IOException {

    var scenarios = Arrays.asList(
        new Object[]{"plaintext", "success"},
        new Object[]{"html", "<html><head></head><body>success</body></html>"},
        new Object[]{"xml", "<?xml version=\"1.0\" encoding=\"UTF-8\" ?>"},
        new Object[]{"json", "{\"success\":\"success\"}"}
    );

    for (var scenario : scenarios) {
      var request = new Request();
      request.url = String.format(
          "http://localhost:3000/request-executor/execute-get/%s",
          scenario[0]
      );
      request.method = MethodEnum.GET;
      var result = new RequestExecutor(request).execute();
      assertEquals(scenario[1], result.responseBody);
      assertNotNull(result.totalTime);
      assertNotNull(result.connection);
    }

  }

  /**
   * Endpoint returns content of post in given Content-Type via header
   * @throws IOException
   */
  @Test
  void executePost() throws IOException {
    var scenarios = Arrays.asList(
        new String[]{"text/plain", "success"},
        new String[]{"text/html", "<html><head></head><body>success</body></html>"},
        new String[]{"application/xml", "<?xml version=\"1.0\" encoding=\"UTF-8\" ?>"},
        new String[]{"application/json", "{\"success\":\"success\"}"}
    );

    for (var scenario : scenarios) {
      var request = new Request();
      request.url = "http://localhost:3000/request-executor/execute-post";
      request.headers.add(new Header("Content-Type", scenario[0]));
      request.body = scenario[0];
      Result result = null;
      result = new RequestExecutor(request).execute();
      assertEquals(scenario[0], result.responseBody);
    }
  }
}