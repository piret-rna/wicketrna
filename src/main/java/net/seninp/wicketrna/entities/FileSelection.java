package net.seninp.wicketrna.entities;

import java.io.Serializable;
import java.util.List;

public class FileSelection implements Serializable {

  private static final long serialVersionUID = 1L;

  private List<String> selectedFiles;

  public String getSelectedFiles() {
    return toString(selectedFiles);
  }

  private String toString(List<String> selectedFiles2) {
    StringBuilder sb = new StringBuilder();
    for (String s : selectedFiles) {
      sb.append(s);
      sb.append("; ");
    }
    if (sb.length() > 2) {
      sb.delete(sb.length() - 2, sb.length());
    }
    return sb.toString();
  }

  public void setSelectedFiles(List<String> selectedFiles) {
    this.selectedFiles = selectedFiles;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((selectedFiles == null) ? 0 : selectedFiles.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    FileSelection other = (FileSelection) obj;
    if (selectedFiles == null) {
      if (other.selectedFiles != null)
        return false;
    }
    else if (!selectedFiles.equals(other.selectedFiles))
      return false;
    return true;
  }

  @Override
  public String toString() {
    StringBuilder builder = new StringBuilder();
    builder.append("FileSelection [").append(toString(this.selectedFiles)).append("]");
    return builder.toString();
  }

}
