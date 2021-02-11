package app.test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import org.junit.jupiter.api.Test;

class TestJsonPathExistTest {

  private String testJson = "{\"jsonrpc\": \"2.0\", \"error\": {\"code\": -1, \"message\": \"Method not found\"}, \"id\": \"1\"}";

  @Test
  void test_Scenarios() {
    var scenarios = Arrays.asList(
        Arrays.asList("error.code", false, true),
        Arrays.asList("error.code", true, false),
        Arrays.asList("foobar", false, false),
        Arrays.asList("foobar", true, true)
    );

    scenarios.forEach(scenario -> {
      TestResult result = null;
      try {
        result = resultOfTestJson((String) scenario.get(0), (Boolean) scenario.get(1));
      } catch (Throwable throwable) {
        throwable.printStackTrace();
        fail(throwable.getMessage());
      }
      assertEquals((Boolean) scenario.get(2), result.isPassed());
    });
  }

  private TestResult resultOfTestJson(
      String path,
      boolean negate
  ) throws Throwable {
    return new TestJsonPathExist()
    .setJsonPath(path)
    .setNegate(negate)
    .test(
        new TestContextDouble().setResponseBody(testJson)
    );
  }

}