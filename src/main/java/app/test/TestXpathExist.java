package app.test;

import java.io.StringReader;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlValue;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPathFactory;
import org.xml.sax.InputSource;
import javax.xml.xpath.XPathConstants;

@XmlRootElement(name = "test-xpath-exist")
public class TestXpathExist implements ITest {

  @XmlValue
  public String xpath;

  @XmlAttribute(required = false)
  public boolean negate = false;

  /**
   * Tests XPath existence
   * eg. `//failure`
   *
   * @return
   */
  public TestResult test(ITestContext context) throws Throwable {
    boolean exists;
    var factory = DocumentBuilderFactory.newInstance();
    factory.setNamespaceAware(true);
    var builder = factory.newDocumentBuilder();
    var xpathFactory = XPathFactory.newInstance();
    var xpath = xpathFactory.newXPath();

    var xmlSource = new InputSource(new StringReader(context.getResponseBody()));
    var document = builder.parse(xmlSource);
    var xpathExpr = xpath.compile(this.xpath);
    exists = (boolean) xpathExpr.evaluate(document, XPathConstants.BOOLEAN);

    if (exists) {
      return success();
    }

    return failure();
  }

  private TestResult success() {
    if (negate) {
      return new TestResult(this, false)
          .addExecutionInfoLine("Chyba: Xpath existuje.");
    }
    return new TestResult(this, true);
  }

  private TestResult failure() {
    if (negate) {
      return new TestResult(this, true);
    }
    return new TestResult(this, false)
        .addExecutionInfoLine("Chyba: Xpath neexistuje.");
  }

  public TestXpathExist setXpath(String xpath) {
    this.xpath = xpath;
    return this;
  }

  public TestXpathExist setNegate(boolean negate) {
    this.negate = negate;
    return this;
  }
}
