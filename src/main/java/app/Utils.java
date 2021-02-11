package app;

import java.util.Map;

public class Utils {
  /**
   * Resolves file extension from Content Type
   *
   * List of common used:
   * https://developer.mozilla.org/en-US/docs/Web/HTTP/Basics_of_HTTP/MIME_types/Common_types
   * Official list:
   * http://www.iana.org/assignments/media-types/media-types.xhtml
   *
   * @param contentType
   * @return null if contentType is not recognized or supported
   */
  public static String resolveFileExtensionFromContentType(String contentType) {
    var mapOfSupported = Map.ofEntries(
        Map.entry("application/xhtml+xml", "xhtml"),
        Map.entry("application/json", "json"),
        Map.entry("application/json+ld", "jsonld"),
        Map.entry("application/xml", "xml"),
        Map.entry("text/xml", "xml"),
        Map.entry("text/plain", "txt"),
        Map.entry("text/html", "html")
    );

    var fileExtensionEntry = mapOfSupported
        .entrySet().stream()
        .filter(entry -> contentType.contains(entry.getKey()))
        .findFirst().orElse(null);

    return fileExtensionEntry != null
        ? fileExtensionEntry.getValue()
        : null;
  }
}
