package net.seninp.wicketrna;

/**
 * Implements a PiReT main menu link.
 * 
 * @author psenin
 *
 */
public class MainMenuLink {

  private String displayName;
  private String actionKey;

  public MainMenuLink(String displayName, String actionKey) {
    this.displayName = displayName;
    this.actionKey = actionKey;
  }

  public String getDisplayName() {
    return displayName;
  }

  public String getActionKey() {
    return actionKey;
  }

}
