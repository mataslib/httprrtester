package app.test;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class PredicateLessThanTest {

  @Test
  void test() {
    assertFalse(new PredicateLessThan(0).test(0));
    assertTrue(new PredicateLessThan(0).test(-1));
    assertTrue(new PredicateLessThan(0).test(-0.5));
  }
}