package app.model;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity(name = "test")
public class Test {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  public Integer id;

  @DatabaseField(columnName="test_definition", dataType = DataType.LONG_STRING)
  public String testDefinition;

  @DatabaseField
  public String name;

  @DatabaseField(columnDefinition = "varchar(255) not null default CURRENT_USER", readOnly = true)
  public String dbuser;

  @DatabaseField(columnName = "shared_for_read", defaultValue = "0")
  public Boolean sharedForRead = false;

  @DatabaseField(columnName = "shared_for_update", defaultValue = "0")
  public Boolean sharedForUpdate = false;

  /**
   * Model of test
   */
  public Test() {

  }

  /**
   * Logic is same as `other_update_if_shared_for_update` policy of database
   *
   * @return if Other has privilege to update this Test
   */
  public boolean otherIsAllowedToUpdate()
  {
    return sharedForUpdate;
  }

  /**
   * Returns whether dbuser is allowed to update Test,
   * mimicking the database behaviour
   * @return
   */
  public boolean isDbuserAllowedToUpdate(String dbusername) {
    return dbuser.equals(dbusername) || otherIsAllowedToUpdate();
  }
}
