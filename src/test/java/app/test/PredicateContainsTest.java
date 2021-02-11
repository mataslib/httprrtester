package app.test;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class PredicateContainsTest {

  private final String testValue = "sveřepí šakali zavile vyli";

  @Test
  void test_ExistingNeedle() {
    assertTrue(
        new PredicateContains("šakali zav").test(testValue)
    );
  }

  @Test
  void test_NotExistingNeedle() {
    assertFalse(
        new PredicateContains("foobar").test(testValue)
    );
  }

}