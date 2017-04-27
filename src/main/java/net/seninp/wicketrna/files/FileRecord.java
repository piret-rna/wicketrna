package net.seninp.wicketrna.files;

import java.io.Serializable;
import java.nio.file.Path;
import java.nio.file.attribute.FileTime;
import java.util.Date;

public class FileRecord implements Serializable {

  private static final long serialVersionUID = -4599113548243588407L;

  private Path path;
  private Date creationTime;
  private Long fileSize;

  public FileRecord(Path path, FileTime fileTime, Long fileSize) {
    super();
    this.path = path;
    this.creationTime = new Date(fileTime.toMillis());
    this.fileSize = fileSize;
  }

  public FileRecord() {
    assert true;
  }

  public String getFileName() {
    return path.getFileName().toString();
  }

  public void setPath(Path path) {
    this.path = path;
  }

  public Path getPath() {
    return this.path;
  }

  public Date getCreationTime() {
    return creationTime;
  }

  public void setCreationTime(Date creationTime) {
    this.creationTime = creationTime;
  }

  public Long getFileSize() {
    return fileSize;
  }

  public void setFileSize(Long fileSize) {
    this.fileSize = fileSize;
  }

  @Override
  public String toString() {
    StringBuilder builder = new StringBuilder();
    builder.append("FileRecord [fname=").append(path).append(", creationTime=").append(creationTime)
        .append(", fileSize=").append(fileSize).append("]");
    return builder.toString();
  }

}
