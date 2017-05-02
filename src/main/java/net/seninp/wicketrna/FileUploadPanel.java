package net.seninp.wicketrna;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.authroles.authentication.AuthenticatedWebSession;
import org.apache.wicket.extensions.markup.html.repeater.data.sort.OrderByBorder;
import org.apache.wicket.feedback.ComponentFeedbackMessageFilter;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.upload.FileUpload;
import org.apache.wicket.markup.html.form.upload.FileUploadField;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.navigation.paging.PagingNavigator;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.parser.filter.WicketTagIdentifier;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.ReuseIfModelsEqualStrategy;
import org.apache.wicket.markup.repeater.data.DataView;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.util.file.File;
import org.apache.wicket.util.lang.Bytes;
import net.seninp.wicketrna.db.WicketRNADb;
import net.seninp.wicketrna.files.FileRecord;
import net.seninp.wicketrna.logic.ConfirmationLink;
import net.seninp.wicketrna.logic.PiretChangeEvent;
import net.seninp.wicketrna.logic.PiretChangeListener;
import net.seninp.wicketrna.logic.SortableFileRecordProvider;
import net.seninp.wicketrna.security.PiretWebSession;
import net.seninp.wicketrna.util.StackTrace;

/**
 * The panel which wraps a file upload widget.
 * 
 * @author psenin
 *
 */
public class FileUploadPanel extends Panel {

  private static final long serialVersionUID = -6725615122875891173L;

  private FileUploadField fileUploadField;

  private static final Logger logger = LogManager.getLogger(FileUploadField.class);

  private static final DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

  private FileRecord selected;

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

            String userFolder = Paths
                .get(System.getProperty(PiretServerProperties.USERS_FOLDER_KEY), homeFolder)
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
    add(new Label("selectedFile", new PropertyModel<>(this, "selectedFileRecordLabel")));

    // figure out the user's folder location
    String username = ((PiretWebSession) AuthenticatedWebSession.get()).getUser();

    // if username is null, use the dummy folder
    Path userFolder = Paths.get(System.getProperty(PiretServerProperties.USERS_FOLDER_KEY),
        "dummy");

    try {
      if (null != username) {
        userFolder = Paths.get(System.getProperty(PiretServerProperties.USERS_FOLDER_KEY),
            WicketRNADb.getUser(username).getUser_folder());
        if (!Files.exists(userFolder)) {
          logger.info("creating the user folder: " + userFolder.toString());
          Files.createDirectories(userFolder);
        }
      }
    }
    catch (IOException e) {
      logger.error("an exception while creating the user folder: " + StackTrace.toString(e));
    }

    logger.debug("creatinga data provider");
    SortableFileRecordProvider dataProvider = new SortableFileRecordProvider(userFolder.toString());
    System.out.println(dataProvider.size());

    final DataView<FileRecord> dataView = new DataView<FileRecord>("fileTable", dataProvider) {
      private static final long serialVersionUID = 1L;

      @Override
      protected void populateItem(final Item<FileRecord> item) {
        FileRecord fr = item.getModelObject();
        item.add(new ActionPanel("actions", item.getModel()));
        item.add(new Link<Void>("toggleHighlite") {
          private static final long serialVersionUID = 1L;

          @Override
          public void onClick() {
            HighlitableDataItem<FileRecord> hitem = (HighlitableDataItem<FileRecord>) item;
            hitem.toggleHighlite();
          }
        });
        item.add(new Label("filename", String.valueOf(fr.getFileName())));
        item.add(new Label("timestamp", dateFormat.format(fr.getCreationTime())));
        item.add(new Label("filesize", fr.getFileSize()));

        item.add(
            AttributeModifier.replace("class", () -> (item.getIndex() % 2 == 1) ? "even" : "odd"));
      }

      @Override
      protected Item<FileRecord> newItem(String id, int index, IModel<FileRecord> model) {
        return new HighlitableDataItem<>(id, index, model);
      }
    };

    dataView.setItemsPerPage(8L);
    dataView.setItemReuseStrategy(ReuseIfModelsEqualStrategy.getInstance());

    add(new OrderByBorder<String>("orderByFileName", "fileName", dataProvider) {
      private static final long serialVersionUID = 1L;

      @Override
      protected void onSortChanged() {
        logger.info("sort order changed");
        dataView.setCurrentPage(0);
      }
    });

    add(new OrderByBorder<String>("orderByTimestamp", "timeStamp", dataProvider) {
      private static final long serialVersionUID = 1L;

      @Override
      protected void onSortChanged() {
        logger.info("sort order changed");
        dataView.setCurrentPage(0);
      }
    });

    PagingNavigator pagingNavigator = new PagingNavigator("navigator", dataView);

    add(dataView);
    add(pagingNavigator);

  }

  private static class HighlitableDataItem<T> extends Item<T> {
    private static final long serialVersionUID = 1L;
    private boolean highlite = false;

    /**
     * toggles highlite
     */
    public void toggleHighlite() {
      highlite = !highlite;
    }

    /**
     * Constructor
     * 
     * @param id
     * @param index
     * @param model
     */
    public HighlitableDataItem(String id, int index, IModel<T> model) {
      super(id, index, model);
      add(new AttributeModifier("style", "background-color:#80b6ed;") {
        private static final long serialVersionUID = 1L;

        @Override
        public boolean isEnabled(Component component) {
          return HighlitableDataItem.this.highlite;
        }
      });
    }
  }

  class ActionPanel extends Panel {
    private static final long serialVersionUID = 1L;

    /**
     * @param id component id
     * @param model model for contact
     */
    public ActionPanel(String id, IModel<FileRecord> model) {
      super(id, model);
      add(new ConfirmationLink<Void>("delete",
          "Do you want to delete " + model.getObject().getFileName()) {
        private static final long serialVersionUID = 1L;

        @Override
        public void onClick(AjaxRequestTarget target) {
          selected = (FileRecord) getParent().getDefaultModelObject();
          logger.info("selected for deletion: " + selected.getFileName());

        }
      });
    }
  }

  /**
   * @return string representation of selected contact property
   */
  public String getSelectedFileRecordLabel() {
    if (selected == null) {
      return "No File Selected";
    }
    else {
      return "Deleting " + selected.getFileName();
    }
  }

  /**
   * @return selected contact
   */
  public FileRecord getSelected() {
    return selected;
  }

  /**
   * sets selected contact
   * 
   * @param selected
   */
  public void setSelected(FileRecord selected) {
    addStateChange();
    this.selected = selected;
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
