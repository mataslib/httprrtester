package app.test;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "test-time")
public class TestTime implements ITest {

  @XmlElements({
      @XmlElement(name = "less-than", type = PredicateLessThan.class, required = true)
  })
  public ITestPredicate<Number> predicate;

  /**
   * Tests time until response
   *
   * @param context
   * @return
   * @throws Throwable
   */
  @Override
  public TestResult test(ITestContext context) throws Throwable {
    var testedValue = context.getTotalTime();
    var result = predicate.test(testedValue);
    return new TestResult(this, result)
      .setExecutionInfo(String.format("Tested value: %d%n", testedValue))
    ;
  }
}
