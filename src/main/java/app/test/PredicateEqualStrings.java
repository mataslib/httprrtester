package app.test;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlValue;

@XmlRootElement(name = "equal")
public class PredicateEqualStrings implements ITestPredicate<String> {

  @XmlValue
  public String value;

  // empty for jaxb
  public PredicateEqualStrings() {
  }
  public PredicateEqualStrings(String value) {
    this.value = value;
  }

  public boolean test(String testValue)
  {
    return testValue.equals(value);
  }
}
