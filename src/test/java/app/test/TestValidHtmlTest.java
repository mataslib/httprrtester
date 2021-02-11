package app.test;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TestValidHtmlTest {

  private TestValidHtml testValidHtml;

  @BeforeEach
  void setUp() {
    this.testValidHtml = new TestValidHtml();
  }

  @Test
  void test_ShouldPass_WithValidHtml() throws Throwable {
    var result = testValidHtml.test(
        new TestContextDouble()
            .setResponseBody(
                "<!DOCTYPE html><html lang=\"cs\"><head><meta charset=\"UTF-8\"/><title>pass</title></head></html>"
            )
    );
    assertTrue(result.isPassed());
  }

  @Test
  void test_ShouldFail_WithInvalidHtml() throws Throwable {
    var result = testValidHtml.test(
        new TestContextDouble()
            .setResponseBody("<html></html>")
    );
    assertFalse(result.isPassed());
  }
}