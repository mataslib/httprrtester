package app.test;

/**
 * Interface of response tests
 */
public interface ITest {
  public TestResult test(ITestContext context) throws Throwable;
}
