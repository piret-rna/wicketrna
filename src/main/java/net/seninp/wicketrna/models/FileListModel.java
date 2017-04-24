package net.seninp.wicketrna.models;

import java.util.ArrayList;
import org.apache.wicket.model.LoadableDetachableModel;
import net.seninp.wicketrna.util.FileLister;
import net.seninp.wicketrna.util.FileRecord;

public class FileListModel extends LoadableDetachableModel<ArrayList<FileNameWrapper>> {

  private static final long serialVersionUID = 1L;
  private String userFolder;

  private ArrayList<FileNameWrapper> data = new ArrayList<FileNameWrapper>();

  public FileListModel(String userFolder) {
    super();
    this.userFolder = userFolder.substring(0);
  }

  @Override
  protected ArrayList<FileNameWrapper> load() {
    if (null == userFolder || userFolder.isEmpty()) {
      data = new ArrayList<FileNameWrapper>();
    }
    else {

      ArrayList<FileRecord> files = FileLister.listFiles(userFolder);

      data = new ArrayList<FileNameWrapper>();
      for (FileRecord f : files) {
        data.add(new FileNameWrapper(f));
      }
    }
    return data;
  }

}
