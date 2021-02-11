package app.test;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlValue;

@XmlRootElement(name = "equal")
public class PredicateEqualNumbers implements ITestPredicate<Number> {

  @XmlValue
  public Double value;

  // empty for jaxb
  public PredicateEqualNumbers() {
  }
  public PredicateEqualNumbers(Number value) {
    this.value = value.doubleValue();
  }

  public boolean test(Number testValue)
  {
    return value == testValue.doubleValue();
  }
}
