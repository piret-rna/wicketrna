package net.seninp.wicketrna.files;

import java.nio.file.Paths;
import java.nio.file.attribute.FileTime;
import java.util.Date;
import org.apache.wicket.util.io.IClusterable;

public class FileRecord implements IClusterable {

  private static final long serialVersionUID = -4599113548243588407L;

  private String path;
  private String fileName;
  private Date creationTime;
  private Long fileSize;
  
  private String timestamp; // for sorting

  public FileRecord() {
    assert true;
  }

  public FileRecord(String path, FileTime fileTime, Long fileSize) {
    super();
    this.path = path;
    this.fileName = Paths.get(path).getFileName().toString();
    this.creationTime = new Date(fileTime.toMillis());
    this.timestamp = Long.valueOf(fileTime.toMillis()).toString();
    this.fileSize = fileSize;
  }

  public String getPath() {
    return path;
  }

  public void setPath(String path) {
    this.path = path;
  }

  public String getFileName() {
    return fileName;
  }

  public void setFileName(String fileName) {
    this.fileName = fileName;
  }

  public Date getCreationTime() {
    return creationTime;
  }

  public void setCreationTime(Date creationTime) {
    this.creationTime = creationTime;
  }

  public String getTimestamp() {
    return timestamp;
  }

  public void setTimestamp(String timestamp) {
    this.timestamp = timestamp;
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
    builder.append("FileRecord [path=").append(path).append(", creationTime=").append(creationTime)
        .append(", fileSize=").append(fileSize).append("]");
    return builder.toString();
  }

}
