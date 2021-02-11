package app.test;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlValue;

@XmlRootElement(name = "contains")
public class PredicateContains implements ITestPredicate<String> {

  @XmlValue
  public String value;

  // empty for jaxb
  public PredicateContains() { }
  public PredicateContains(String value) {
    this.value = value;
  }

  public boolean test(String testValue)
  {
    return testValue.contains(value);
  }
}
