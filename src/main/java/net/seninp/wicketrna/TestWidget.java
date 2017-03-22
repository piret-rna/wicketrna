package net.seninp.wicketrna;

import java.util.Date;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;
import net.seninp.wicketrna.models.DummyHomePanelModel;

/**
 * The main application page.
 * 
 * @author psenin
 *
 */
public final class TestWidget extends WebPage {

  private static final long serialVersionUID = 2799448818773645768L;

  @Override
  protected void onConfigure() {
    super.onConfigure();
  }

  @Override
  protected void onInitialize() {

    super.onInitialize();

    //
    // the timestamp model to print the current time on the screen
    Model<String> timeStampModel = new Model<String>() {
      private static final long serialVersionUID = 1L;

      @Override
      public String getObject() {
        return new Date().toString();
      }
    };

    final Panel fileUplodPanel = new FileUploadPanel("fileupload_panel", new DummyHomePanelModel());
    add(fileUplodPanel);

    final Panel fileManagementPanel = new FileManagementPanel("filemanagement_panel",
        new DummyHomePanelModel());
    ((FileUploadPanel) fileUplodPanel)
        .addPiretChangeListener((FileManagementPanel) fileManagementPanel);

    add(fileManagementPanel);

    add(new Label("timeStamp", timeStampModel));

  }

}