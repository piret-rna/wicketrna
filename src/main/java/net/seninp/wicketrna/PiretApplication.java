package net.seninp.wicketrna;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.wicket.authroles.authentication.AbstractAuthenticatedWebSession;
import org.apache.wicket.authroles.authentication.AuthenticatedWebApplication;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.response.filter.ServerAndClientTimeFilter;
import net.seninp.wicketrna.db.WicketRNADb;
import net.seninp.wicketrna.security.PiretWebSession;
import net.seninp.wicketrna.util.StackTrace;

/**
 * Application object for your web application. If you want to run this application without
 * deploying, run the Start class.
 * 
 * @see net.seninp.wicketrna.Start#main(String[])
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

    mountPage("testwidget", TestWidget.class);
    logger.warn("mounted TEST page to /testwidget");

    //
    // check and create folders if not existing
    try {
      Path piretFolder = Paths
          .get(System.getProperty(PiretServerProperties.APPLICATION_FOLDER_KEY));
      Path piretDBFolder = Paths.get(System.getProperty(PiretServerProperties.DB_DIR_KEY));
      Path piretUserFolder = Paths.get(System.getProperty(PiretServerProperties.USERS_FOLDER_KEY));

      if (!Files.exists(piretFolder)) {
        logger.info("creating the app folder: " + piretFolder.toString());
        Files.createDirectories(piretFolder);
      }

      if (!Files.exists(piretDBFolder)) {
        logger.info("creating the app DB folder: " + piretDBFolder.toString());
        Files.createDirectories(piretDBFolder);
      }

      if (!Files.exists(piretUserFolder)) {
        logger.info("creating the app Users folder: " + piretUserFolder.toString());
        Files.createDirectories(piretUserFolder);
      }

    }
    catch (IOException e) {
      System.err.println("Unable to create application folders: " + StackTrace.toString(e));
      System.exit(10);
    }

    //
    // init the DB connection
    logger.debug("connecting to the DB");
    WicketRNADb.connect("");

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
