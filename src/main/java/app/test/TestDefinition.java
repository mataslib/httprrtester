package app.test;

import java.util.ArrayList;
import javax.xml.bind.annotation.*;

@XmlRootElement(name = "test")
public class TestDefinition {

  @XmlElement(name = "request", required = true)
  public Request request;

  @XmlElement(name = "response")
  public Response response;

  /**
   * Object representation and mapping of xml test definition
   */
  public TestDefinition() {}

  public static class Request {

    @XmlElement(name = "url", required = true)
    public String url;

    @XmlElement(name = "method")
    public MethodEnum method;

    @XmlElement(name = "header")
    public ArrayList<Header> headers = new ArrayList<>();

    @XmlElement(name = "body")
    public String body;
  }

  public static class Header {
    public Header() {
    }

    public Header(String key, String value) {
      this.key = key;
      this.value = value;
    }

    @XmlAttribute(name = "key")
    public String key;

    @XmlValue()
    public String value;

  }

  @XmlEnum
  public enum MethodEnum {
    GET,
    HEAD,
    POST,
    PUT,
    PATCH,
    DELETE,
    CONNECT,
    OPTIONS,
    TRACE;

    public String value() {
      return name();
    }

    public static MethodEnum fromValue(String value) {
      return valueOf(value);
    }
  }


  public static class Response {
    @XmlElementWrapper(name = "tests")
    @XmlElements({
        @XmlElement(name = "test-valid-html", type = TestValidHtml.class),
        @XmlElement(name = "test-working-links", type = TestWorkingLinks.class),
//        // upravit pro Predicate
        @XmlElement(name = "test-json-path-exist", type = TestJsonPathExist.class),
        @XmlElement(name = "test-xpath-exist", type = TestXpathExist.class),
//        // nedokonceno
        @XmlElement(name = "test-status-code", type = TestsStatusCode.class),
        @XmlElement(name = "test-time", type = TestTime.class),
        @XmlElement(name = "test-body", type = TestBody.class),
        @XmlElement(name = "test-header", type = TestHeader.class)
    })
    public ArrayList<ITest> tests;
  }


}
