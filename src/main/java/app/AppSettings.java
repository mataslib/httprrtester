package app;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import javax.xml.bind.JAXB;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

public class AppSettings {

  private static final String savePath = "app_settings.xml";

  private Settings settings;

  private static AppSettings instance;

  /**
   * Application settings singleton
   */
  private AppSettings() {}

  public static AppSettings instance()
  {
    if (instance == null) {
      instance = new AppSettings();
    }
    return instance;
  }

  public Settings get() throws IOException {
    if (this.settings != null) {
      return this.settings;
    }

    if (!Files.exists(Path.of(savePath))) {
      save(new Settings());
    }
    this.settings = JAXB.unmarshal(Path.of(savePath).toFile(), Settings.class);

    return this.settings;
  }

  /**
   * Saves settings as xml.
   *
   * @param settings
   * @throws IOException
   */
  public void save(Settings settings) throws IOException {
    var writer = new StringWriter();
    JAXB.marshal(settings, writer);
    Files.writeString(Path.of(savePath), writer.toString());
  }


  @XmlRootElement(name = "settings")
  public static class Settings {
    /**
     * Mapping and object representation of xml
     */
    public Settings() {}

    @XmlElement
    public String responsesSavePath;
    @XmlElement
    public boolean doSaveResponses;
  }
}
