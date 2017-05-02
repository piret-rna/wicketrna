package net.seninp.wicketrna;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.wicket.authroles.authentication.AbstractAuthenticatedWebSession;
import org.apache.wicket.authroles.authentication.AuthenticatedWebApplication;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.response.filter.ServerAndClientTimeFilter;
import net.seninp.wicketrna.db.WicketRNADb;
import net.seninp.wicketrna.security.PiretWebSession;

/**
 * Application object for your web application. If you want to run this application without
 * deploying, run the Start class.
 * 
 * @see net.seninp.wicketrna.PiretStart#main(String[])
 */
public class PiretApplication extends AuthenticatedWebApplication {

  private static final Logger logger = LogManager.getLogger(PiretApplication.class);

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
    logger.info("Starting the PiReT application");
    super.init();
    this.getMarkupSettings().setStripWicketTags(true);

    //
    //
    getDebugSettings().setDevelopmentUtilitiesEnabled(true);
    getRequestCycleSettings().addResponseFilter(new ServerAndClientTimeFilter());
    getMarkupSettings().setStripWicketTags(true);

    //
    // mounting pages
    mountPage("login", LoginPage.class);
    logger.debug("mounted login page to /login");

    mountPage("piret", PiretPage.class);
    logger.debug("mounted main page (PiretPage) to /piret");

    // mountPage("testwidget", TestWidget.class);
    // logger.warn("mounted TEST page to /testwidget");

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
    return WicketRNADb.getDbURI();
  }

}
