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
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import net.seninp.wicketrna.PiretServerProperties;
import net.seninp.wicketrna.entities.User;
import net.seninp.wicketrna.util.StackTrace;

/**
 * Static wrapper for the myBatis-based mapper.
 * 
 * @author psenin
 *
 */
public class WicketRNADb {

  private static final Logger logger = LogManager.getLogger(WicketRNADb.class);
  
  private static SqlSessionFactory sqlSessionFactory;

  /**
   * Disable constructor.
   */
  private WicketRNADb() {
    super();
    assert true;
  }

  /**
   * Attempts to establish the database connection. Also creates a database if not exists and
   * populates the test user.
   * 
   * @param dbURI the non-default DB URI, specify an empty string for default.
   */
  public static void connect(String dbURI) {

    logger.info("initializing the Piert database using URI:" + dbURI);
    
    // set URL with factory builder properties
    Properties properties = new Properties();

    if (null == dbURI || dbURI.isEmpty()) {
      // compose the HSQL DB url
      // <HSQLDB prefix> + <USER HOME> + ".rnadb/database"
      //
      StringBuilder dbHome = new StringBuilder();
      dbHome.append("jdbc:hsqldb:file:");
      dbHome.append(System.getProperty(PiretServerProperties.DB_DIR_KEY));
      dbHome.append(java.io.File.separator);
      dbHome.append("piretdatabase");
      dbHome.append(";shutdown=true");

      dbURI = dbHome.toString();
      
      logger.info("since DB URI is null, using the default settings:" + dbURI);
    }

    properties.setProperty("url", dbURI);

    // locate the mapper config file and bootstrap the DB
    String resource = "SqlMapConfig.xml";
    InputStream inputStream;
    try {
      // load the config
      inputStream = Resources.getResourceAsStream(resource);
      sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream, properties);
    }
    catch (IOException e) {
      System.err.println(StackTrace.toString(e));
    }

    recreateTables();

  }

  /**
   * In the case of building an SqlSession manually use this constructor.
   * 
   * @param sqlSessionFactory the SQL Session factory to use for db connection.
   * 
   */
  public static void connect(SqlSessionFactory sqlSessionFactory) {
    WicketRNADb.sqlSessionFactory = sqlSessionFactory;
    recreateTables();
  }

  /**
   * Re-creates the Schema tables.
   */
  private static void recreateTables() {

    // open session
    SqlSession session = sqlSessionFactory.openSession();

    // bootstrapping
    try {

      // ***DROP*** and re-create the USER table if not exists
      session.insert("dropUserTable"); // drops the user table...
      session.insert("createUserTable");

      // add the test user if not in there
      User testUser = session.selectOne("getUserByUsername", "test");
      if (null == testUser) {
        session.insert("addNewUser",
            new User(null, "test", "test", "psenin@lanl.gov", "test", ""));
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
   * Get the user record using the username.
   * 
   * @param username the username to use.
   * 
   * @return the user record.
   */
  public static User getUser(String username) {
    SqlSession session = sqlSessionFactory.openSession();
    User usr = session.selectOne("getUserByUsername", username);
    usr.setSalt("blabla");
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
