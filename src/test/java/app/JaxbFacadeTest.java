package app;

import static org.junit.jupiter.api.Assertions.*;

import app.JaxbFacade;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import org.junit.jupiter.api.Test;

class JaxbFacadeTest {
  @Test
  public void testUnmarshallString()
  {
    try {
      var parsed = JaxbFacade.unmarshalString(
          "<?xml version=\"1.0\" encoding=\"UTF-8\" ?><test-root test=\"true\"/>",
          TestRoot.class
      );
      assertTrue(parsed instanceof TestRoot);
      assertTrue(parsed.test);
    } catch (Throwable e) {
      fail(e.toString());
    }
  }

  @Test
  public void testMarshallOnlyElementToString()
  {
    var root = new TestRoot();
    var result = JaxbFacade.marshalOnlyElementToString(root);
    assertEquals("<test-root test=\"false\"/>", result);
  }

  @XmlRootElement(name = "test-root")
  static class TestRoot {
    @XmlAttribute(name = "test")
    public boolean test;
  }
}

