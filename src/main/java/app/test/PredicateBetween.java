package app.test;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "between")
public class PredicateBetween implements ITestPredicate<Number> {
  @XmlElement
  public Double lower;

  @XmlElement
  public Double upper;

  public PredicateBetween() { }
  public PredicateBetween(Number lower, Number upper) {
    this.lower = lower.doubleValue();
    this.upper = upper.doubleValue();
  }

  public boolean test(Number testValue)
  {
    var comparableTestValue = testValue.doubleValue();
    return comparableTestValue >= lower && comparableTestValue <= upper;
  }
}
