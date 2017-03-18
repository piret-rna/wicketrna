package net.seninp.wicketrna;

import java.io.Serializable;
import java.nio.file.Path;
import net.seninp.wicketrna.util.FileRecord;

/**
 * It's the wrapped object that could be your business object. the selected property is just here to
 * record whether the checkbox for it was selected.
 */
class FileNameWrapper implements Serializable {

  private static final long serialVersionUID = 1L;

  private String name;
  private Boolean selected = Boolean.FALSE;

  private String absolutePath;

  public FileNameWrapper(FileRecord f) {
    this.name = f.getFileName();
    this.absolutePath = f.getPath().toString();
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

  public String getPath() {
    return absolutePath;
  }

  public void setPath(Path path) {
    this.absolutePath = path.toString();
  }

  public String toString() {
    return name + ": " + selected;
  }
}
