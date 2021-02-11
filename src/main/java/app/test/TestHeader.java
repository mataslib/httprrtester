package app.test;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "test-header")
public class TestHeader implements ITest {

  @XmlAttribute
  public String key;

  @XmlElements({
      @XmlElement(name = "equal", type = PredicateEqualStrings.class, required = true),
      @XmlElement(name = "contains", type = PredicateContains.class, required = true)
  })
  public ITestPredicate<String> predicate;

  /**
   * Tests http header value
   * @param context
   * @return
   * @throws Throwable
   */
  @Override
  public TestResult test(ITestContext context) throws Throwable {
    var testedValue = context.getConnection().getHeaderField(key);
    var isSuccess = predicate.test(testedValue);
    return new TestResult(this, isSuccess)
        .setExecutionInfo(String.format("Tested value: %s%n", testedValue));
  }
}
