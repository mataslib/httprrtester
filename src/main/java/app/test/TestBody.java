package app.test;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "test-body")
public class TestBody implements ITest {

  @XmlElements({
      @XmlElement(name = "equal", type = PredicateEqualStrings.class, required = true),
      @XmlElement(name = "contains", type = PredicateContains.class, required = true),
  })
  public ITestPredicate<String> predicate;

  /**
   * Tests reponse body
   * @param context
   * @return
   */
  public TestResult test(ITestContext context) {
    var testedValue = context.getResponseBody();
    var isSuccess = predicate.test(testedValue);
    return new TestResult(this, isSuccess);
  }

}
