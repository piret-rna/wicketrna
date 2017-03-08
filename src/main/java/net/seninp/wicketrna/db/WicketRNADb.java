package net.seninp.wicketrna.db;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import net.seninp.wicketrna.entities.User;
import net.seninp.wicketrna.util.StackTrace;

public class WicketRNADb {

  private static SqlSessionFactory sqlSessionFactory;

  public static void connect() {

    //
    // trying to get the DB connected
    StringBuilder dbHome = new StringBuilder();
    dbHome.append("jdbc:hsqldb:file:");
    dbHome.append(System.getProperty("user.home"));
    dbHome.append(java.io.File.separator);
    dbHome.append(".rnadb/database");
    dbHome.append(";shutdown=true");

    Properties properties = new Properties();
    properties.setProperty("url", dbHome.toString());

    String resource = "SqlMapConfig.xml";
    InputStream inputStream;
    try {

      // load the config
      inputStream = Resources.getResourceAsStream(resource);
      sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream, properties);

      // DB stuff goes here
      SqlSession session = sqlSessionFactory.openSession();

      try {

        // create the users table if not exists
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

  }

  public static String getFolderForUser(String username) {
    SqlSession session = sqlSessionFactory.openSession();
    User usr = session.selectOne("getUserByUsername", username);
    session.close();
    if (null == usr) {
      return "";
    }
    return usr.getUser_folder();
  }

  public static String getdbURL() {
    SqlSession session = sqlSessionFactory.openSession();
    String dbURL = (String) session.getConfiguration().getVariables().get("url");
    session.close();
    return dbURL;
  }

}
