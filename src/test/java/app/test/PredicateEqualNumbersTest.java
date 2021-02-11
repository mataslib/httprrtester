package app.test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import org.junit.jupiter.api.Test;

class PredicateEqualNumbersTest {

  @Test
  void test() {

    assertTrue(
        new PredicateEqualNumbers(0).test(0)
    );
    assertTrue(
        new PredicateEqualNumbers(0.5).test(0.5)
    );
    assertTrue(
        new PredicateEqualNumbers(Double.MAX_VALUE).test(Double.MAX_VALUE)
    );
    assertTrue(
        new PredicateEqualNumbers(Double.MIN_VALUE).test(Double.MIN_VALUE)
    );
    assertTrue(
        new PredicateEqualNumbers(Long.MAX_VALUE).test(Long.MAX_VALUE)
    );
    assertTrue(
        new PredicateEqualNumbers(Long.MIN_VALUE).test(Long.MIN_VALUE)
    );
  }
}