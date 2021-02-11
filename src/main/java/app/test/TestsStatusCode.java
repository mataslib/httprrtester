package app.test;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "test-status-code")
public class TestsStatusCode implements ITest {

  @XmlElements({
      @XmlElement(name = "equal", type = PredicateEqualNumbers.class, required = true),
      @XmlElement(name = "between", type = PredicateBetween.class, required = true),
      @XmlElement(name = "less-than", type = PredicateLessThan.class, required = true),
  })
  public ITestPredicate<Number> predicate;

  /**
   * Tests response status code
   *
   * @param context
   * @return
   */
  @Override
  public TestResult test(ITestContext context) throws Throwable {
    var responseCode = context.getConnection().getResponseCode();
    var result = predicate.test(responseCode);
    return new TestResult(this, result)
      .setExecutionInfo(String.format("Tested value: %d%n", responseCode));
  }
}
