package app;

import java.io.StringReader;
import java.io.StringWriter;
import javax.xml.bind.JAXB;

public class JaxbFacade {
  /**
   * Marshals element to string without xml header
   *
   * @param model
   * @return
   */
  public static String marshalOnlyElementToString(Object model)
  {
    var writer = new StringWriter();
    JAXB.marshal(model, writer);
    var marshaled = writer.toString();
    // strip first line that contains xml definition
    var pureMarshaled = marshaled.substring(marshaled.indexOf("\n")).trim();
    return pureMarshaled;
  }

  /**
   * Method for easier unmarshal from string
   *
   * @param xml
   * @param modelClass
   * @param <T>
   * @return
   */
  public static <T> T unmarshalString(String xml, Class<T> modelClass)
  {
    var reader = new StringReader(xml);
    return (T) JAXB.unmarshal(reader, modelClass);
  }
}
