package net.seninp;

import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.upload.FileUpload;
import org.apache.wicket.markup.html.form.upload.FileUploadField;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.parser.filter.WicketTagIdentifier;
import org.apache.wicket.model.IModel;
import org.apache.wicket.util.file.File;
import org.apache.wicket.util.lang.Bytes;

public class FileUploadPanel extends Panel {

  private static final long serialVersionUID = -6725615122875891173L;

  private FileUploadField fileUploadField;

  static {
    WicketTagIdentifier.registerWellKnownTagName(PANEL);
  }

  public FileUploadPanel(String id, IModel<String> model) {
    super(id, model);

    fileUploadField = new FileUploadField("fileUploadField");

    Form<?> form = new Form<Object>("form") {

      private static final long serialVersionUID = 1L;

      @Override
      protected void onSubmit() {
        super.onSubmit();

        FileUpload fileUpload = fileUploadField.getFileUpload();

        try {
          File file = new File(
              System.getProperty("java.io.tmpdir") + "/" + fileUpload.getClientFileName());

          fileUpload.writeTo(file);
        }
        catch (Exception e) {
          e.printStackTrace();
        }
      }
    };

    form.setMultiPart(true);
    // set a limit for uploaded file's size
    form.setMaxSize(Bytes.gigabytes(4));
    form.add(fileUploadField);
    add(new FeedbackPanel("feedbackPanel"));
    add(form);

  }

}
