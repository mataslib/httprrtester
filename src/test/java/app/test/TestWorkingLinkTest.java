package app.test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import org.junit.jupiter.api.Test;

public class TestWorkingLinkTest {

  @Test
  public void test_ShouldFail_NotExistent() {
    new TestWorkingLink().test("http://--not-existing--", "http://localhost:3000");
  }

  @Test
  public void test_ShouldPass_SuccessfulCommonEndpoints() {
    var scenarios = Arrays.asList(
        "200-success",
        "201-created",
        "301-moved-permanently",
        "302-found"
    );
    scenarios.forEach(href -> {
      var result = new TestWorkingLink().test(href, "http://localhost:3000");
      assertTrue(result.isPassed());
    });
  }

  @Test
  public void test_ShouldFail_FailedCommonEndpoints() {
    var scenarios = Arrays.asList(
        "400-bad-request",
        "404-not-found",
        "405-method-not-allowed",
        "500-internal-server-error"
    );
    scenarios.forEach(href -> {
      var result = new TestWorkingLink().test(href, "http://localhost:3000");
      assertFalse(result.isPassed());
    });
  }

  @Test
  public void test_ShouldPass_AuthenticationRequired() {
    var scenarios = Arrays.asList(
        "401-unauthorized",
        "403-forbidden"
    );
    scenarios.forEach(href -> {
      var result = new TestWorkingLink().test(href, "http://localhost:3000");
      assertTrue(result.isPassed());
    });
  }

  @Test
  public void test_ShouldPass_UnsupportedProtocols() {
    var scenarios = Arrays.asList(
        "tel:+420123456789",
        "mailto:foo@gmail.com"
    );

    scenarios.forEach(href -> {
      var result = new TestWorkingLink().test(href, "http://localhost:3000");
      assertTrue(result.isPassed());
    });
  }

  @Test
  public void test_ShouldPass_ValidAbsuluteUrl() {
    var test = new TestWorkingLink();
    assertTrue(test.test("http://localhost:3000200-success", null).isPassed());
    assertTrue(
        test.test("http://localhost:3000200-success",
            "shouldBeIgnored"
        ).isPassed()
    );
  }
}
