package app.test;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class PredicateEqualStringsTest {

  @Test
  void test() {
    assertTrue(
        new PredicateEqualStrings("").test("")
    );
    assertTrue(
        new PredicateEqualStrings("foobar").test("foobar")
    );
    assertFalse(
        new PredicateEqualStrings("foo").test("FOO")
    );
    assertFalse(
        new PredicateEqualStrings("foo").test("foobar")
    );
  }
}