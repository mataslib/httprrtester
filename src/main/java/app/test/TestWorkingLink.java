package app.test;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;

public class TestWorkingLink {

  /**
   * Fake User-Agent when making request, prevents pages like Twitter to fail
   * with default with eg. 400 Bad Request
   */
  private final String userAgent = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/87.0.4280.88 Safari/537.36 Edg/87.0.664.66";

  /**
   * Protocols that are supported by this test. This eliminates links such as
   * `mailto:foo@bar.com` which can not be tested.
   *
   * All urls of different protocol than http or https are autoconsidered PASS -
   * since can not be tested.
   */
  private final ArrayList<String> supportedProtocols = new ArrayList<>(
      Arrays.asList("http", "https")
  );

  /**
   * Tests status code of link used in HTML anchor.
   * Links that can not be tested are considered pass, such as:
   * - Unsupported protocols are considered PASS (maitlo, ...).
   * - Required authorization is considered PASS.
   *
   * @param href       eg.:
   *                   <ul>
   *                    <li>https://google.com</li>
   *                    <li>//google.com</li>
   *                    <li>index.php</li>
   *                    <li>/index.php</li>
   *                   </ul>
   * @param currentUrl <p>Relevant for relative hrefs only. Eg.:
   *                   <ul>
   *                    <li>`currentUrl`: "https://prf.jcu.cz"</li>
   *                    <li>`href` (relative): "index.php"</li>
   *                    <li> Resolves to: "https://prf.jcu.cz/index.php"</li>
   *                   </ul>
   * @return
   */
  public TestResult test(
      String href,
      String currentUrl
  ) {
    boolean isSuccess = true;
    URL targetUrl;
    StringBuilder executionInfo = new StringBuilder();

    try {
      targetUrl = resolveTargetUrl(href, currentUrl);
    } catch (Throwable e) {
      // can not be resolved (unknown protocol like 'tel:', ...
      // can not be tested -> consider pass
      return new TestResult(null, true);
    }
    if (!isSupportedProtocol(targetUrl)) {
      return new TestResult(null, true);
    }

    try {
      var connection = pingEndpoint(targetUrl);
      var responseCode = connection.getResponseCode();
      isSuccess = this.isSuccess(responseCode);
      if (!isSuccess) {
        executionInfo.append(
            String.format("Http response code pro url \"%s\" není úspěšný: %d.%n",
                targetUrl,
                responseCode
            )
        );
      }
    } catch (Throwable e) {
      isSuccess = false;
      executionInfo.append(String.format("Něco se pokazilo pro url \"%s\" (%s).%n",
          targetUrl,
          e.getMessage())
      );
    }

    return new TestResult(null, isSuccess)
        .setExecutionInfo(executionInfo.toString());
  }

  /**
   * Executes HTTP request to endpoint
   *
   * @param targetUrl
   * @return
   * @throws IOException
   */
  private HttpURLConnection pingEndpoint(URL targetUrl) throws IOException {
    var connection = (HttpURLConnection) targetUrl.openConnection();
    connection.setRequestProperty("User-Agent", userAgent);
    connection.setConnectTimeout(10000);
    connection.setDoOutput(false);
    connection.setUseCaches(false);
    connection.setInstanceFollowRedirects(true);
    // some pages has problem with HEAD (get without body - faster),
    // 405-method-not-allowed should be returned in such case,
    // but it's target's good will dependent - so let's use GET
    connection.setRequestMethod("GET");

    return connection;
  }


  /**
   * Resolves target URL. When href is relative,
   * then current url is used as context for resolved url.
   *
   * @param href
   * @param currentUrl
   * @return
   * @throws Exception
   */
  private URL resolveTargetUrl(String href, String currentUrl) throws Exception {
    URL targetUrl;
    // not absolute, try resolve with context
    if (!currentUrl.endsWith("/")) {
      currentUrl += "/";
    };

    var currentUrlContext = new URL(currentUrl);
    targetUrl = new URL(currentUrlContext, href);
    return targetUrl;
  }

  private boolean isSupportedProtocol(URL targetUrl) {
    return supportedProtocols.contains(targetUrl.getProtocol());
  }

  /**
   * Whether response code is considered successful
   *
   * @param responseCode
   * @return
   */
  private boolean isSuccess(int responseCode) {
    // Auth required -> consider as working link
    if (responseCode == 401 || responseCode == 403) {
      return true;
    }

    // Consider 2xx success codes 3xx and redirect codes  as working link
    if (responseCode >= 200 && responseCode < 400) {
      return true;
    }

    return false;
  }
}
