package app.test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.Test;

class PredicateBetweenTest {

  @Test
  void test() {

    assertTrue(
        new PredicateBetween(Long.MIN_VALUE, Long.MAX_VALUE).test(Long.MIN_VALUE)
    );
    assertTrue(
        new PredicateBetween(Long.MIN_VALUE, Long.MAX_VALUE).test(Long.MAX_VALUE)
    );
    assertTrue(
        new PredicateBetween(Double.MIN_VALUE, Double.MAX_VALUE).test(Double.MIN_VALUE)
    );
    assertTrue(
        new PredicateBetween(Double.MIN_VALUE, Double.MAX_VALUE).test(Double.MAX_VALUE)
    );
    assertTrue(
        new PredicateBetween(0, 0).test(0)
    );
    assertFalse(
        new PredicateBetween(-1, 1).test(1.1)
    );
    assertFalse(
        new PredicateBetween(-1, 1).test(-1.1)
    );
    assertFalse(
        new PredicateBetween(-1, 1).test(-1.1)
    );
  }
}