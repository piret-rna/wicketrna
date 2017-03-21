package net.seninp.wicketrna;

import java.util.ArrayList;
import org.apache.wicket.model.LoadableDetachableModel;
import net.seninp.wicketrna.util.FileLister;
import net.seninp.wicketrna.util.FileRecord;

public class FileListModel extends LoadableDetachableModel<ArrayList<FileNameWrapper>> {

  private static final long serialVersionUID = 1L;

  @Override
  protected ArrayList<FileNameWrapper> load() {
    
    FileLister fl = new FileLister();
    ArrayList<FileRecord> files = fl.listFiles("/Users/psenin/piretfs/test/files");

    ArrayList<FileNameWrapper> data = new ArrayList<FileNameWrapper>();
    for (FileRecord f : files) {
      data.add(new FileNameWrapper(f));
    }

    return data;
  }
}
