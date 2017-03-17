package net.seninp.wicketrna;

import java.nio.file.Paths;
import java.util.List;
import org.apache.wicket.authroles.authentication.AuthenticatedWebSession;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.upload.FileUpload;
import org.apache.wicket.markup.html.form.upload.FileUploadField;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.parser.filter.WicketTagIdentifier;
import org.apache.wicket.model.IModel;
import org.apache.wicket.util.file.File;
import org.apache.wicket.util.lang.Bytes;
import net.seninp.wicketrna.db.WicketRNADb;
import net.seninp.wicketrna.security.PiretWebSession;

public class FileUploadPanel extends Panel {

  private static final long serialVersionUID = -6725615122875891173L;

  private FileUploadField fileUploadField;

  /**
   * fix those DTD warnings.
   */
  static {
    WicketTagIdentifier.registerWellKnownTagName(PANEL);
  }

  public FileUploadPanel(String id, IModel<String> model) {
    super(id, model);

    //
    // a feedback panel
    final FeedbackPanel feedbackPane = new FeedbackPanel("feedback-upload");
    add(feedbackPane);

    //
    // the upload panel widget itself
    fileUploadField = new FileUploadField("fileUploadField");

    Form<?> form = new Form<Object>("form") {

      private static final long serialVersionUID = 1L;

      @Override
      protected void onSubmit() {
        super.onSubmit();

        List<FileUpload> fileUploads = fileUploadField.getFileUploads();

        for (FileUpload fileUpload : fileUploads) {
          try {

            String username = ((PiretWebSession) AuthenticatedWebSession.get()).getUser();
            if (null == username) {
              username = "test";
            }
            String homeFolder = WicketRNADb.getUser(username).getUser_folder();

            File file = new File(Paths.get("/Users/psenin", homeFolder, "files").toString(),
                fileUpload.getClientFileName());

            fileUpload.writeTo(file);

            feedbackPane.debug("file uploaded: " + file.getName() + "; absolute path: "
                + file.getAbsolutePath() + "; file size: " + file.length() + " bytes");

          }
          catch (Exception e) {
            e.printStackTrace();
          }
        }
      }
    };

    form.setMultiPart(true);

    // set a limit for uploaded file's size
    form.setMaxSize(Bytes.gigabytes(10));
    form.add(fileUploadField);

    add(form);

  }

}
