package net.seninp.wicketrna;

import java.util.ArrayList;
import java.util.List;
import org.apache.wicket.feedback.IFeedback;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.upload.FileUpload;
import org.apache.wicket.markup.html.form.upload.FileUploadField;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.parser.filter.WicketTagIdentifier;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.util.file.File;
import org.apache.wicket.util.lang.Bytes;
import net.seninp.wicketrna.util.FileLister;
import net.seninp.wicketrna.util.FileRecord;

public class FileManagementPanel extends Panel {

  private static final long serialVersionUID = -6725615122875891173L;

  private FileUploadField fileUploadField;

  /**
   * fix those DTD warnings.
   */
  static {
    WicketTagIdentifier.registerWellKnownTagName(PANEL);
  }

  public FileManagementPanel(String id, IModel<String> model) {
    super(id, model);

    //
    // existing files list
    final FeedbackPanel feedback = new FeedbackPanel("feedback");
    add(feedback);
    add(new InputForm("inputForm", feedback));

  }

  /** form for processing the input. */
  private class InputForm extends Form<Object> {

    private static final long serialVersionUID = 1L;

    // holds NameWrapper elements
    private List<FileNameWrapper> data;

    public InputForm(String name, IFeedback feedback) {

      super(name);

      FileLister fl = new FileLister();
      List<FileRecord> files = fl.listFiles("/Users/psenin/piretfs/test/files");

      data = new ArrayList<FileNameWrapper>();
      for (FileRecord f : files) {
        data.add(new FileNameWrapper(f));
      }

      // add a nested list view; as the list is nested in the form, the form will
      // update all FormComponent childs automatically.
      ListView<FileNameWrapper> listView = new ListView<FileNameWrapper>("list", data) {
        private static final long serialVersionUID = 1L;

        protected void populateItem(ListItem<FileNameWrapper> item) {
          FileNameWrapper wrapper = (FileNameWrapper) item.getModelObject();
          item.add(new Label("name", wrapper.getName()));
          item.add(new CheckBox("check", new PropertyModel<Boolean>(wrapper, "selected")));
        }
      };
      listView.setReuseItems(true);
      add(listView);
    }

    public void onSubmit() {
      info("data: " + data); // print current contents
    }
  }

}
