package app.model;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import java.sql.SQLException;
import java.util.HashMap;

public class DbManager {

  private static DbManager instance;
  private final JdbcConnectionSource connectionSource;
  private static String host;
  private static String database;
  private static int port;
  private static String username;
  private static String password;
  private static Config config;

  /**
   * Cache for DAOs
   */
  private HashMap<String, Dao> daos = new HashMap<>();

  public static void configure(
      Config config
  ) {
    if (instance != null) {
      throw new RuntimeException(
          "DbManager is not allowed to be reconfigured once it is already initialized!");
    }
    DbManager.config = config;
    DbManager.host = config.host;
    DbManager.port = config.port;
    DbManager.database = config.database;
    DbManager.username = config.username;
    DbManager.password = config.password;
  }

  /**
   * Singleton that provides DB access
   * private constructor, so can not be instantiated elsewhere
   *
   * @throws SQLException
   */
  private DbManager() throws SQLException {
    this.connectionSource = new JdbcConnectionSource(
        String.format("jdbc:postgresql://%s:%d/%s", host, port, database),
        username,
        password
    );
    // trigger getConnection to validate connection data,
    // eventually will throw exception and singleton won't be initialiezed
    // if data are wrong - so it can be reconfigured again with valid data
    this.connectionSource.getReadOnlyConnection("");
  }

  /**
   * gets instance
   *
   * @return
   */
  public static DbManager instance() {
    try {
      if (instance == null) {
        instance = new DbManager();
      }
    } catch (Throwable exception) {
      throw new RuntimeException(exception);
    }

    return instance;
  }

  public JdbcConnectionSource connectionSource() {
    return connectionSource;
  }

  /**
   * Returns and caches DAO of given model
   *
   * @param modelClass
   * @param <ModelClass>
   * @param <PrimaryKeyType>
   * @return
   */
  public <ModelClass, PrimaryKeyType> Dao<ModelClass, PrimaryKeyType> dao(
      Class<ModelClass> modelClass) {
    try {
      var daoKey = modelClass.toString();
      if (daos.containsKey(daoKey)) {
        return daos.get(daoKey);
      }

      Dao<ModelClass, PrimaryKeyType> dao = DaoManager
          .createDao(DbManager.instance().connectionSource(), modelClass);
      daos.put(daoKey, dao);

      return daos.get(daoKey);
    } catch (Throwable throwable) {
      throw new RuntimeException(throwable);
    }
  }

  public static Config getConfig() {
    return config;
  }

  public static class Config {
    public final String host;
    public final String database;
    public final int port;
    public final String username;
    public final String password;

    /**
     * Configur
     *
     * @param host
     * @param port
     * @param database
     * @param username
     * @param password
     */
    public Config(
        String host,
        int port,
        String database,
        String username,
        String password
    ) {
      this.host = host;
      this.port = port;
      this.database = database;
      this.username = username;
      this.password = password;
    }
  }

}
