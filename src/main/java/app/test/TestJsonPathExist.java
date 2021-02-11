package app.test;

import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.PathNotFoundException;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlValue;

@XmlRootElement(name = "test-json-path-exist")
public class TestJsonPathExist implements ITest {

  @XmlValue
  public String jsonPath;

  @XmlAttribute(required = false)
  public boolean negate = false;

  /**
   * Tests JSON Path existence
   * eg: `response.results[0].success`
   *
   * @param context
   * @return
   */
  @Override
  public TestResult test(ITestContext context) throws Throwable {
    try {
      JsonPath.read(context.getResponseBody(), jsonPath);
      return success();
    } catch (PathNotFoundException exception) {
      return failure();
    }
  }

  private TestResult success()
  {
    if (negate) {
      return new TestResult(this, false)
          .setExecutionInfo("JSON path existuje.");
    }
    return new TestResult(this, true);
  }

  private TestResult failure()
  {
    if (negate) {
      return new TestResult(this, true);
    }
    return new TestResult(this, false)
        .setExecutionInfo("JSON path neexistuje.");
  }

  public TestJsonPathExist setNegate(boolean negate) {
    this.negate = negate;
    return this;
  }

  public TestJsonPathExist setJsonPath(String jsonPath) {
    this.jsonPath = jsonPath;
    return this;
  }
}
