package net.seninp.wicketrna.db;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import net.seninp.wicketrna.entities.User;
import net.seninp.wicketrna.util.StackTrace;

/**
 * Static wrapper for the myBatis-based mapper.
 * 
 * @author psenin
 *
 */
public class WicketRNADb {

  // SQL session factory
  private static SqlSessionFactory sqlSessionFactory;

  /**
   * Attempts to establish the database connection. Also creates a database if not exists and
   * populates the test user.
   */
  public static void connect() {

    // compose the HSQL DB url
    // <HSQLDB prefix> + <USER HOME> + ".rnadb/database"
    //
    StringBuilder dbHome = new StringBuilder();
    dbHome.append("jdbc:hsqldb:file:");
    dbHome.append(System.getProperty("user.home"));
    dbHome.append(java.io.File.separator);
    dbHome.append(".rnadb/database");
    dbHome.append(";shutdown=true");

    // set URL with factory builder properties
    Properties properties = new Properties();
    properties.setProperty("url", dbHome.toString());

    // locate the mapper config file and bootstrap the DB
    String resource = "SqlMapConfig.xml";
    InputStream inputStream;
    try {

      // load the config
      inputStream = Resources.getResourceAsStream(resource);
      sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream, properties);

      // open session
      SqlSession session = sqlSessionFactory.openSession();

      // bootstrapping
      try {

        // ***DROP*** and re-create the users table if not exists
        session.insert("dropUserTable"); // drops the user table...
        session.insert("createUserTable");

        // add the test user if not in there
        User testUser = session.selectOne("getUserByUsername", "test");
        if (null == testUser) {
          int id = session.insert("addNewUser",
              new User(null, "test", "test", "psenin@lanl.gov", "piretfs/test", ""));
          System.out.println(" --> " + id);
          session.commit();
        }

      }
      catch (Exception e) {
        System.err.println(StackTrace.toString(e));
      }
      finally {
        session.close();
      }

    }
    catch (IOException e) {
      System.err.println(StackTrace.toString(e));
    }

    // done with DB re-creation
    //

  }

  /**
   * Reports the actual database URI.
   * 
   * @return the database URI.
   */
  public static String getDbURI() {
    SqlSession session = sqlSessionFactory.openSession();
    String dbURL = (String) session.getConfiguration().getVariables().get("url");
    session.close();
    return dbURL;
  }

  /**
   * Queries the DB and gets a home folder for a specified user.
   * 
   * @param username the username.
   * @return the home folder.
   */
  public static String getFolderForUser(String username) {
    SqlSession session = sqlSessionFactory.openSession();
    User usr = session.selectOne("getUserByUsername", username);
    session.close();
    if (null == usr) {
      return "";
    }
    return usr.getUser_folder();
  }

  /**
   * Get the user record using the username and password (authentication).
   * 
   * @param params the map with populated username and password entries.
   * @return the user record.
   */
  public static User getUser(Map<String, String> params) {
    SqlSession session = sqlSessionFactory.openSession();
    User usr = session.selectOne("getUserByUsernameAndPassword", params);
    session.close();
    return usr;
  }

  /**
   * Get the user record using the username and password (authentication).
   * 
   * @param username
   * @param password
   * 
   * @return true if user exists.
   */
  public static boolean authinticate(String username, String password) {
    Map<String, String> params = new HashMap<String, String>();
    params.put("username", username);
    params.put("password", password);
    SqlSession session = sqlSessionFactory.openSession();
    User usr = session.selectOne("getUserByUsernameAndPassword", params);
    session.close();
    return null != usr;
  }

}
