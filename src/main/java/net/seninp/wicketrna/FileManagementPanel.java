package net.seninp.wicketrna;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.Component;
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
import net.seninp.wicketrna.logic.SortableFileRecordProvider;
import net.seninp.wicketrna.util.FileRecord;

public class FileManagementPanel extends Panel {

  private static final long serialVersionUID = -6725615122875891173L;

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
  }

  public FileManagementPanel(String id, IModel<String> model) {

    super(id, model);

    // add(new Label("selectedLabel", new PropertyModel<>(this, "selectedContactLabel"))); 

    SortableFileRecordProvider dataProvider = new SortableFileRecordProvider();

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
        item.add(new Label("timestamp", fr.getFileSize()));
        item.add(new Label("filesize", fr.getCreationTime()));

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
        dataView.setCurrentPage(0);
      }
    });

    add(new OrderByBorder<String>("orderByTimestamp", "fileTimestamp", dataProvider) {
      private static final long serialVersionUID = 1L;

      @Override
      protected void onSortChanged() {
        dataView.setCurrentPage(0);
      }
    });

    add(dataView);
    add(new PagingNavigator("navigator", dataView));

  }

  /**
   * @return string representation of selected contact property
   */
  public String getSelectedContactLabel() {
    if (selected == null) {
      return "No File Selected";
    }
    else {
      return selected.getFileName() + " " + selected.getCreationTime();
    }
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
      add(new Link<Void>("select") {
        private static final long serialVersionUID = 1L;

        @Override
        public void onClick() {
          selected = (FileRecord) getParent().getDefaultModelObject();
        }
      });
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
