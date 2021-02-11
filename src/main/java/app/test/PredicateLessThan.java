package app.test;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlValue;

@XmlRootElement(name = "less-than")
public class PredicateLessThan implements ITestPredicate<Number> {

  @XmlValue
  public Double value;

  // empty for jaxb
  public PredicateLessThan() {
  }
  public PredicateLessThan(Number value) {
    this.value = value.doubleValue();
  }

  public boolean test(Number testValue)
  {
    return testValue.doubleValue() < value;
  }
}
