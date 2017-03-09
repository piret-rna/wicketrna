package net.seninp.wicketrna;

import java.io.Serializable;
import net.seninp.wicketrna.util.FileRecord;

/**
 * name is the wrapped object that could be your business object. the selected property is just here
 * to record whether the checkbox for it was selected.
 */
class FileNameWrapper implements Serializable {

  private static final long serialVersionUID = 1L;

  private String name;
  private Boolean selected = Boolean.FALSE;

  public FileNameWrapper(FileRecord f) {
    this.name = f.getFileName();
  }

  public Boolean getSelected() {
    return selected;
  }

  public void setSelected(Boolean selected) {
    this.selected = selected;
  }

  public String getName() {
    return name;
  }

  public void setName(String wrapped) {
    this.name = wrapped;
  }

  public String toString() {
    return name + ": " + selected;
  }
}
