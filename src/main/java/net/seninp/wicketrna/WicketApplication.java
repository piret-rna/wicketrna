package net.seninp.wicketrna;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.wicket.authroles.authentication.AbstractAuthenticatedWebSession;
import org.apache.wicket.authroles.authentication.AuthenticatedWebApplication;
import org.apache.wicket.markup.html.WebPage;
import net.seninp.wicketrna.security.PiretWebSession;
import net.seninp.wicketrna.util.StackTrace;

/**
 * Application object for your web application. If you want to run this application without
 * deploying, run the Start class.
 * 
 * @see net.seninp.wicketrna.Start#main(String[])
 */
public class WicketApplication extends AuthenticatedWebApplication {

  SqlSessionFactory sqlSessionFactory;

  private String dbURL;

  /**
   * @see org.apache.wicket.Application#getHomePage()
   */
  @Override
  public Class<? extends WebPage> getHomePage() {
    return HomePage.class;
  }

  /**
   * @see org.apache.wicket.Application#init()
   */
  @Override
  public void init() {

    //
    // App init
    super.init();
    this.getMarkupSettings().setStripWicketTags(true);

    //
    // mounting pages
    mountPage("login", LoginPage.class);
    mountPage("piret", PiretPage.class);

    //
    // trying to get the DB connected
    StringBuilder dbHome = new StringBuilder();
    dbHome.append("jdbc:hsqldb:file:");
    dbHome.append(System.getProperty("user.home"));
    dbHome.append(java.io.File.separator);
    dbHome.append(".rnadb/database");

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
        session.insert("createUserTable");

        // the database url
        dbURL = (String) session.getConfiguration().getVariables().get("url");

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

  @Override
  protected Class<? extends WebPage> getSignInPageClass() {
    return LoginPage.class;
  }

  @Override
  protected Class<? extends AbstractAuthenticatedWebSession> getWebSessionClass() {
    return PiretWebSession.class;
  }

  public String getDBInfo() {
    return dbURL;
  }

}
