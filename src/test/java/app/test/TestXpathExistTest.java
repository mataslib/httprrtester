package app.test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import org.junit.jupiter.api.Test;

class TestXpathExistTest {

  private String testXml = ""
      + "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
      + "<methodResponse>\n"
      + "  <fault>\n"
      + "    <value>\n"
      + "      <struct>\n"
      + "        <member>\n"
      + "          <name>faultCode</name>\n"
      + "          <value><int>4</int></value>\n"
      + "        </member>\n"
      + "        <member>\n"
      + "          <name>faultString</name>\n"
      + "          <value><string>Too many parameters.</string></value>\n"
      + "        </member>\n"
      + "      </struct>\n"
      + "    </value>\n"
      + "  </fault>\n"
      + "</methodResponse>";

  @Test
  void test_Scenarios() {
    var scenarios = Arrays.asList(
        Arrays.asList("/methodResponse/fault", false, true),
        Arrays.asList("/methodResponse/fault", true, false),
        Arrays.asList("/methodResponse/params", false, false),
        Arrays.asList("/methodResponse/params", true, true)
    );

    scenarios.forEach(scenario -> {
      TestResult result = null;
      try {
        result = resultOfTestXml((String) scenario.get(0), (Boolean) scenario.get(1));
      } catch (Throwable throwable) {
        throwable.printStackTrace();
        fail(throwable.getMessage());
      }
      assertEquals((Boolean) scenario.get(2), result.isPassed());
    });
  }

  private TestResult resultOfTestXml(
      String path,
      boolean negate
  ) throws Throwable {
    return new TestXpathExist()
        .setXpath(path)
        .setNegate(negate)
        .test(
            new TestContextDouble().setResponseBody(testXml)
        );
  }
}