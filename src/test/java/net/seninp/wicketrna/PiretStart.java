package net.seninp.wicketrna;

import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import javax.management.MBeanServer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.jetty.jmx.MBeanContainer;
import org.eclipse.jetty.server.HttpConfiguration;
import org.eclipse.jetty.server.HttpConnectionFactory;
import org.eclipse.jetty.server.SecureRequestCustomizer;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.server.SslConnectionFactory;
import org.eclipse.jetty.util.resource.Resource;
import org.eclipse.jetty.util.ssl.SslContextFactory;
import org.eclipse.jetty.webapp.WebAppContext;
import net.seninp.wicketrna.db.WicketRNADb;
import net.seninp.wicketrna.util.StackTrace;

/**
 * Separate startup class for people that want to run the examples directly. Use parameter
 * -Dcom.sun.management.jmxremote to startup JMX (and e.g. connect with jconsole).
 */
public class PiretStart {
  
  private static final Logger logger = LogManager.getLogger(PiretStart.class);
  
  /**
   * Main function, starts the jetty server.
   *
   * @param args
   */
  public static void main(String[] args) {

    // take care about properties
    PiretServerProperties properties = new PiretServerProperties();
    System.err.println(properties.echoProperties());

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
      Path piretDummyFolder = Paths.get(piretUserFolder.toString(), "dummy");
      Files.createDirectories(piretDummyFolder);

    }
    catch (IOException e) {
      System.err.println("Unable to create application folders: " + StackTrace.toString(e));
      System.exit(10);
    }

    //
    // init the DB connection
    logger.debug("connecting to the DB");
    WicketRNADb.connect("");

    // wicket switch
    System.setProperty("wicket.configuration", "development");

    // configure the server
    Server server = new Server();

    HttpConfiguration http_config = new HttpConfiguration();
    http_config.setSecureScheme("https");
    http_config.setSecurePort(8443);
    http_config.setOutputBufferSize(32768);
    ServerConnector http = new ServerConnector(server, new HttpConnectionFactory(http_config));

    http.setPort(Integer.valueOf(System.getProperty(PiretServerProperties.PORT_KEY)));
    http.setIdleTimeout(1000 * 60 * 60);

    server.addConnector(http);

    Resource keystore = Resource.newClassPathResource("/keystore");
    if (keystore != null && keystore.exists()) {
      // if a keystore for a SSL certificate is available, start a SSL
      // connector on port 8443.
      // By default, the quickstart comes with a Apache Wicket Quickstart
      // Certificate that expires about half way september 2021. Do not
      // use this certificate anywhere important as the passwords are
      // available in the source.

      SslContextFactory sslContextFactory = new SslContextFactory();
      sslContextFactory.setKeyStoreResource(keystore);
      sslContextFactory.setKeyStorePassword("wicket");
      sslContextFactory.setKeyManagerPassword("wicket");

      HttpConfiguration https_config = new HttpConfiguration(http_config);
      https_config.addCustomizer(new SecureRequestCustomizer());

      ServerConnector https = new ServerConnector(server,
          new SslConnectionFactory(sslContextFactory, "http/1.1"),
          new HttpConnectionFactory(https_config));
      https.setPort(8443);
      https.setIdleTimeout(500000);

      server.addConnector(https);
      System.out.println("SSL access to the examples has been enabled on port 8443");
      System.out.println("You can access the application using SSL on https://localhost:8443");
      System.out.println();
    }

    WebAppContext bb = new WebAppContext();
    bb.setServer(server);
    bb.setContextPath("/");
    bb.setWar("src/main/webapp");

    // uncomment the next two lines if you want to start Jetty with WebSocket (JSR-356) support
    // you need org.apache.wicket:wicket-native-websocket-javax in the classpath!
    // ServerContainer serverContainer = WebSocketServerContainerInitializer.configureContext(bb);
    // serverContainer.addEndpoint(new WicketServerEndpointConfig());

    // uncomment next line if you want to test with JSESSIONID encoded in the urls
    // ((AbstractSessionManager)
    // bb.getSessionHandler().getSessionManager()).setUsingCookies(false);

    server.setHandler(bb);

    MBeanServer mBeanServer = ManagementFactory.getPlatformMBeanServer();
    MBeanContainer mBeanContainer = new MBeanContainer(mBeanServer);
    server.addEventListener(mBeanContainer);
    server.addBean(mBeanContainer);

    try {
      server.start();
      server.join();
    }
    catch (Exception e) {
      e.printStackTrace();
      System.exit(100);
    }
  }
}
