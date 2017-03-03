package net.seninp.wicketrna;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.parser.filter.WicketTagIdentifier;
import org.apache.wicket.model.IModel;

public class PipelinePanel extends Panel {

  private static final long serialVersionUID = -6725615122840221173L;

  static {
    WicketTagIdentifier.registerWellKnownTagName(PANEL);
  }

  public PipelinePanel(String id, IModel<String> model) {
    super(id, model);
    this.add(new Label("label", model.getObject()));
  }

}
