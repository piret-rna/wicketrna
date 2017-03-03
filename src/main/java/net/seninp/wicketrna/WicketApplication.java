package net.seninp.wicketrna;

import java.io.IOException;
import java.io.InputStream;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.wicket.authroles.authentication.AbstractAuthenticatedWebSession;
import org.apache.wicket.authroles.authentication.AuthenticatedWebApplication;
import org.apache.wicket.markup.html.WebPage;

/**
 * Application object for your web application. If you want to run this application without
 * deploying, run the Start class.
 * 
 * @see net.seninp.wicketrna.Start#main(String[])
 */
public class WicketApplication extends AuthenticatedWebApplication {

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
    String resource = "SqlMapConfig.xml";
    InputStream inputStream;
    try {

      // load the config
      inputStream = Resources.getResourceAsStream(resource);
      SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);

      // create the users table if not exists
      SqlSession session = sqlSessionFactory.openSession();
      try {
        session.insert("createUserTable");
      }
      catch (Exception e) {

      }
      finally {
        session.close();
      }

    }
    catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }

  @Override
  protected Class<? extends WebPage> getSignInPageClass() {
    return LoginPage.class;
  }

  @Override
  protected Class<? extends AbstractAuthenticatedWebSession> getWebSessionClass() {
    return SignInSession.class;
  }
}
