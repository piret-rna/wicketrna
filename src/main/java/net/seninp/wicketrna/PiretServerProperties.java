package net.seninp.wicketrna;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Map;
import java.util.Properties;
import java.util.TreeMap;

/**
 * So far unused class...
 * 
 * @author psenin
 *
 */
public class PiretServerProperties {

  /** The root folder. */
  public static final String APPLICATION_FOLDER_KEY = "piret.application.folder";
  /** The users data folders. */
  public static final String USERS_FOLDER_KEY = "piret.filesystem.folder";
  /** The DB folder. */
  public static final String DB_DIR_KEY = "piret.db.folder";
  /** The hostname. */
  public static final String HOSTNAME_KEY = "piret.hostname";
  /** The port. */
  public static final String PORT_KEY = "piret.port";
  /** The context root. */
  public static final String CONTEXT_ROOT_KEY = "piret.context.root";

  /** Where we store the properties. */
  private Properties properties;

  /**
   * Creates a new ServerProperties instance. Prints an error to the console if problems occur on
   * loading.
   */
  public PiretServerProperties() {
    try {
      initializeProperties();
    }
    catch (Exception e) {
      System.out.println("Error initializing server properties.");
    }
  }

  /**
   * Reads in the properties in ~/.piret/piret.properties if this file exists, and provides default
   * values for all properties not mentioned in this file. Will also add any pre-existing System
   * properties that start with "piret.".
   * 
   * @throws Exception if errors occur.
   */
  private void initializeProperties() throws Exception {

    String userHome = System.getProperty("user.home");
    String piretHome = userHome + "/.piret";
    String propFile = userHome + "/.piret/piret.properties";

    this.properties = new Properties();

    // Set defaults for 'standard' operation. These will override any previously
    properties.setProperty(APPLICATION_FOLDER_KEY, "/Users/psenin/.piret");
    properties.setProperty(DB_DIR_KEY, piretHome + "/db");
    properties.setProperty(USERS_FOLDER_KEY, piretHome + "/userdata");
    properties.setProperty(HOSTNAME_KEY, "localhost");
    properties.setProperty(PORT_KEY, "8080");
    properties.setProperty(CONTEXT_ROOT_KEY, "piret");

    FileInputStream stream = null;
    try {
      stream = new FileInputStream(propFile);
      properties.load(stream);
      System.out.println("Loading piret properties from: " + propFile);
    }
    catch (IOException e) {
      System.out.println(propFile + " not found. Using default piret properties.");
    }
    finally {
      if (stream != null) {
        stream.close();
      }
    }
    addPiretSystemProperties(this.properties);
    trimProperties(properties);

    Properties systemProperties = System.getProperties();
    systemProperties.putAll(properties);
    System.setProperties(systemProperties);
  }

  /**
   * Finds any System properties whose key begins with "piret.", and adds those key-value pairs to
   * the passed Properties object.
   * 
   * @param properties The properties instance to be updated with the Piret system properties.
   */
  private void addPiretSystemProperties(Properties properties) {
    Properties systemProperties = System.getProperties();
    for (Map.Entry<Object, Object> entry : systemProperties.entrySet()) {
      String sysPropName = (String) entry.getKey();
      if (sysPropName.startsWith("piret.")) {
        String sysPropValue = (String) entry.getValue();
        properties.setProperty(sysPropName, sysPropValue);
      }
    }
  }

  /**
   * Returns a string containing all current properties in alphabetical order.
   * 
   * @return A string with the properties.
   */
  public String echoProperties() {
    String cr = System.getProperty("line.separator");
    String eq = " = ";
    String pad = "                ";
    // Adding them to a treemap has the effect of alphabetizing them.
    TreeMap<String, String> alphaProps = new TreeMap<String, String>();
    for (Map.Entry<Object, Object> entry : this.properties.entrySet()) {
      String propName = (String) entry.getKey();
      String propValue = (String) entry.getValue();
      alphaProps.put(propName, propValue);
    }
    StringBuffer buff = new StringBuffer(25);
    buff.append("Piret Properties:").append(cr);
    for (String key : alphaProps.keySet()) {
      buff.append(pad).append(key).append(eq).append(get(key)).append(cr);
    }
    return buff.toString();
  }

  /**
   * Returns the value of the Server Property specified by the key.
   * 
   * @param key Should be one of the public static final strings in this class.
   * @return The value of the key, or null if not found.
   */
  public String get(String key) {
    return this.properties.getProperty(key);
  }

  /**
   * Ensures that the there is no leading or trailing whitespace in the property values. The fact
   * that we need to do this indicates a bug in Java's Properties implementation to me.
   * 
   * @param properties The properties.
   */
  private void trimProperties(Properties properties) {
    // Have to do this iteration in a Java 5 compatible manner. no stringPropertyNames().
    for (Map.Entry<Object, Object> entry : properties.entrySet()) {
      String propName = (String) entry.getKey();
      properties.setProperty(propName, properties.getProperty(propName).trim());
    }
  }

  /**
   * Returns the fully qualified host name, such as "http://localhost:9876/piret/".
   * 
   * @return The fully qualified host name.
   */
  public String getFullHost() {
    return "http://" + get(HOSTNAME_KEY) + ":" + get(PORT_KEY) + "/" + get(CONTEXT_ROOT_KEY) + "/";
  }

}
