package net.seninp.wicketrna;

import java.nio.file.Paths;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import org.apache.wicket.authroles.authentication.AuthenticatedWebSession;
import org.apache.wicket.feedback.ComponentFeedbackMessageFilter;
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
import net.seninp.wicketrna.logic.PiretChangeEvent;
import net.seninp.wicketrna.logic.PiretChangeListener;
import net.seninp.wicketrna.logic.PiretProperties;
import net.seninp.wicketrna.security.PiretWebSession;

/**
 * The panel which wraps a file upload widget.
 * 
 * @author psenin
 *
 */
public class FileUploadPanel extends Panel {

  private static final long serialVersionUID = -6725615122875891173L;

  private FileUploadField fileUploadField;

  // Use CopyOnWriteArrayList to avoid ConcurrentModificationExceptions if a
  // listener attempts to remove itself during event notification.
  private final CopyOnWriteArrayList<PiretChangeListener> listeners;

  /**
   * fix those DTD warnings.
   */
  static {
    WicketTagIdentifier.registerWellKnownTagName(PANEL);
  }

  /**
   * Constructor.
   * 
   * @param id
   * @param model
   */
  public FileUploadPanel(String id, IModel<String> model) {

    super(id, model);

    this.listeners = new CopyOnWriteArrayList<PiretChangeListener>();

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

            String userFolder = Paths.get(PiretProperties.getFilesystemPath(), homeFolder)
                .toString();

            File file = new File(userFolder, fileUpload.getClientFileName());

            fileUpload.writeTo(file);

            this.info("file uploaded: " + file.getName() + "; absolute path: "
                + file.getAbsolutePath() + "; file size: " + file.length() + " bytes");

            fireChangeEvent();

          }
          catch (Exception e) {
            e.printStackTrace();
          }
        }
      }
    };
    form.setMultiPart(true);

    // set a limit for uploaded file's size
    form.setMaxSize(Bytes.gigabytes(20));
    form.add(fileUploadField);

    ComponentFeedbackMessageFilter filter = new ComponentFeedbackMessageFilter(form);
    feedbackPane.setFilter(filter);
    feedbackPane.setEscapeModelStrings(false);

    add(form);

  }

  /**
   * Adds a listener which will be notified about the change.
   * 
   * @param listener
   */
  public void addPiretChangeListener(PiretChangeListener listener) {
    this.listeners.add(listener);
  }

  /**
   * Deletes a listener.
   * 
   * @param listener
   */
  public void removePiretChangeListener(PiretChangeListener listener) {
    this.listeners.remove(listener);
  }

  /**
   * Event firing method. Called internally by other class methods.
   *
   */
  protected void fireChangeEvent() {
    PiretChangeEvent evt = new PiretChangeEvent(this);
    for (PiretChangeListener l : listeners) {
      l.changeEventReceived(evt);
    }
  }

}
