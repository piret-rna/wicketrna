package net.seninp.wicketrna.util;

import java.nio.file.attribute.FileTime;
import java.util.Date;

public class FileRecord {

  private String filename;
  private Date creationTime;
  private Long fileSize;

  public FileRecord(String path, FileTime fileTime, Long fileSize) {
    super();
    this.filename = path;
    this.creationTime = new Date(fileTime.toMillis());
    this.fileSize = fileSize;
  }

  public String getFileName() {
    return filename;
  }

  public void setFileName(String fname) {
    this.filename = fname;
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
    builder.append("FileRecord [fname=").append(filename).append(", creationTime=")
        .append(creationTime).append(", fileSize=").append(fileSize).append("]");
    return builder.toString();
  }

}
