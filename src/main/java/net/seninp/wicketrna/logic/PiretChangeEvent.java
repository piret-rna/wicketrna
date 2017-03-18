package net.seninp.wicketrna.logic;

import java.util.EventObject;

/**
 * An utility class for passing events between components.
 * 
 * @author psenin
 *
 */
public class PiretChangeEvent extends EventObject {

  private static final long serialVersionUID = 1L;

  /**
   * This event definition is stateless.
   * 
   * @param source
   */
  public PiretChangeEvent(Object source) {
    super(source);
  }

}
