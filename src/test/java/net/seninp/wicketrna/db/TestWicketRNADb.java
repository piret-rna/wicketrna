package net.seninp.wicketrna.db;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Properties;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.BeforeClass;
import org.junit.Test;
import net.seninp.wicketrna.entities.User;

public class TestWicketRNADb {

  private static SqlSessionFactory sqlSessionFactory;

  private static final String UNAME = "test";
  private static final String PASS = "test";
  private static final String EMAIL = "psenin@lanl.gov";
  private static final String HOMEFOLDER = "test";
  private static final String KEYVALUES = "key aaa; value bbb";

  @BeforeClass
  public static void Before() throws IOException {

    // set URL with factory builder properties
    Properties properties = new Properties();
    properties.setProperty("url", "jdbc:hsqldb:mem:testrnadb");

    // locate the mapper config file and bootstrap the DB
    String resource = "SqlMapConfig.xml";
    InputStream inputStream;
    inputStream = Resources.getResourceAsStream(resource);
    sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream, properties);

  }

  @Test
  public void testUserXML() {
    // open session
    SqlSession session = sqlSessionFactory.openSession();

    // check that table doesn't exist
    List<Object> res = session.selectList("getUserTable");
    assertTrue(res.isEmpty());

    // check that table is created
    session.insert("createUserTable");
    session.commit();
    res = session.selectList("getUserTable");
    assertFalse(res.isEmpty());

    // check it has no users
    List<String> users = session.selectList("getAllUserNames");
    assertTrue(users.isEmpty());

    // check the user creation/retrieval
    int id = session.insert("addNewUser",
        new User(null, UNAME, PASS, EMAIL, HOMEFOLDER, KEYVALUES));
    session.commit();
    assertEquals(1, id);
    User testUser = session.selectOne("getUserByUsername", "test");
    assertTrue(UNAME.equals(testUser.getUsername()));
    assertTrue(PASS.equals(testUser.getSalt()));
    assertTrue(EMAIL.equals(testUser.getEmail()));
    assertTrue(HOMEFOLDER.equals(testUser.getUser_folder()));
    assertTrue(KEYVALUES.equals(testUser.getKey_values()));

    // check user deletion
    session.insert("dropUserByName", UNAME);
    testUser = session.selectOne("getUserByUsername", "test");
    assertNull(testUser);

    // check that table is dropped
    session.insert("dropUserTable");
    session.commit();
    res = session.selectList("getUserTable");
    assertTrue(res.isEmpty());

  }

  @Test
  public void testUserRNADb() {

    WicketRNADb.connect(sqlSessionFactory);

    User user = WicketRNADb.getUser(UNAME);

    assertTrue(PASS.equalsIgnoreCase(user.getUser_folder()));// db doesnt report passwords

    assertTrue(EMAIL.equals(user.getEmail()));
    assertTrue(HOMEFOLDER.equals(user.getUser_folder()));
    assertTrue(user.getKey_values().isEmpty());

  }

}
