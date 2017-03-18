package net.seninp.wicketrna.logic;

import java.util.EventObject;

public class PiretChangeEvent extends EventObject {

  private static final long serialVersionUID = 1L;

  // This event definition is stateless but you could always
  // add other information here.
  public PiretChangeEvent(Object source) {
    super(source);
  }
}
