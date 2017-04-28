package net.seninp.wicketrna;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.authroles.authentication.AuthenticatedWebSession;
import org.apache.wicket.extensions.markup.html.repeater.data.sort.OrderByBorder;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.navigation.paging.PagingNavigator;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.parser.filter.WicketTagIdentifier;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.ReuseIfModelsEqualStrategy;
import org.apache.wicket.markup.repeater.data.DataView;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import net.seninp.wicketrna.db.WicketRNADb;
import net.seninp.wicketrna.files.FileRecord;
import net.seninp.wicketrna.logic.ConfirmationLink;
import net.seninp.wicketrna.logic.SortableFileRecordProvider;
import net.seninp.wicketrna.security.PiretWebSession;
import net.seninp.wicketrna.util.StackTrace;

public class FileManagementPanel extends Panel {

  private static final long serialVersionUID = -6725615122875891173L;

  private static final Logger logger = LogManager.getLogger(FileManagementPanel.class);

  private static final DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

  private FileRecord selected;

  /**
   * fix those DTD warnings.
   */
  static {
    WicketTagIdentifier.registerWellKnownTagName(PANEL);
  }

  @Override
  protected void onInitialize() {
    super.onInitialize();
    logger.info("on initialize called");
  }

  public FileManagementPanel(String id, IModel<String> model) {

    super(id, model);

    logger.info("constructor called");

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

    System.out.println("creatinga data provider");
    SortableFileRecordProvider dataProvider = new SortableFileRecordProvider(userFolder.toString());

    final DataView<FileRecord> dataView = new DataView<FileRecord>("oir", dataProvider) {
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
        System.out.println("sort order changed");
        dataView.setCurrentPage(0);
      }
    });

    add(new OrderByBorder<String>("orderByTimestamp", "timeStamp", dataProvider) {
      private static final long serialVersionUID = 1L;

      @Override
      protected void onSortChanged() {
        System.out.println("sort order changed");
        dataView.setCurrentPage(0);
      }
    });

    add(dataView);
    add(new PagingNavigator("navigator", dataView));

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

}
