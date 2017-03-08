package net.seninp.wicketrna;

import org.apache.wicket.authroles.authentication.AbstractAuthenticatedWebSession;
import org.apache.wicket.authroles.authentication.AuthenticatedWebApplication;
import org.apache.wicket.markup.html.WebPage;
import net.seninp.wicketrna.db.WicketRNADb;
import net.seninp.wicketrna.security.PiretWebSession;

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
    // init the DB connection
    WicketRNADb.connect();

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
    return WicketRNADb.getdbURL();
  }

}
