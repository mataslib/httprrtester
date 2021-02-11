package app.test;

/**
 * Interface of predicates that are used inside some tets for comparing with actual value
 *
 * @param <T>
 */
public interface ITestPredicate<T> {

  /**
   *
   * @param t tested valu
   * @return whether predicate test is passed
   */
  public boolean test(T t);
}
