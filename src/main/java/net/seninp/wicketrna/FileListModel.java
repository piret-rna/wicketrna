package net.seninp.wicketrna;

import java.util.ArrayList;
import org.apache.wicket.model.Model;
import net.seninp.wicketrna.util.FileLister;
import net.seninp.wicketrna.util.FileRecord;

public class FileListModel extends Model<ArrayList<FileNameWrapper>> {

  private static final long serialVersionUID = 1L;

  @Override
  public ArrayList<FileNameWrapper> getObject() {
    FileLister fl = new FileLister();
    ArrayList<FileRecord> files = fl.listFiles("/Users/psenin/piretfs/test/files");

    ArrayList<FileNameWrapper> data = new ArrayList<FileNameWrapper>();
    for (FileRecord f : files) {
      data.add(new FileNameWrapper(f));
    }

    return data;
  }
}
