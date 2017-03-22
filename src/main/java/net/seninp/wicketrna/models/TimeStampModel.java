package net.seninp.wicketrna.models;

import java.util.Date;
import org.apache.wicket.model.Model;

/**
 * A simple model returning a timestamp as a string.
 * 
 * @author psenin
 *
 */
public class TimeStampModel extends Model<String> {

  private static final long serialVersionUID = 1L;

  @Override
  public String getObject() {
    return new Date().toString();
  }

}
