package net.seninp.wicketrna.logic;

/**
 * An interface which takes care about passing events.
 * 
 * @author psenin
 *
 */
public interface PiretChangeListener {
  /**
   * Event processing.
   * 
   * @param evt
   */
  public void changeEventReceived(PiretChangeEvent evt);
}
