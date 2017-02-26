package net.seninp;

import org.apache.wicket.model.IModel;

/**
 * This is dummy class which produces an empty text for the label... Its a placeholder.
 * 
 * @author psenin
 *
 */
public class DummyHomePanelModel implements IModel<String> {

  private static final long serialVersionUID = 5166750069643630485L;

  @Override
  public void detach() {
    // TODO Auto-generated method stub

  }

  @Override
  public String getObject() {
    // TODO Auto-generated method stub
    return "";
  }

  @Override
  public void setObject(String object) {
    // TODO Auto-generated method stub

  }

}
