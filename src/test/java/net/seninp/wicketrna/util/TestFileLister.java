package net.seninp.wicketrna.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.Test;
import net.seninp.wicketrna.files.FileLister;
import net.seninp.wicketrna.files.FileRecord;

public class TestFileLister {

  @Test
  public void test() {

    try {

      //
      // create a temp folder
      Path tmp = Files.createTempDirectory("wicketrnatest");

      //
      // populate test files
      Map<String, BasicFileAttributes> attrs = new HashMap<String, BasicFileAttributes>();
      for (int i = 0; i < 11; i++) {
        String fname = "testfile" + String.valueOf(i) + ".txt";
        Path fpath = new File(tmp.toString(), fname).toPath();
        Files.deleteIfExists(fpath);
        BufferedWriter out = new BufferedWriter(
            new OutputStreamWriter(new FileOutputStream(fpath.toString()), "UTF8"));
        for (int j = 0; j < i; j++) {
          out.write("0");
        }
        out.close();
        BasicFileAttributes attr = Files.readAttributes(fpath, BasicFileAttributes.class);
        attrs.put(fpath.getFileName().toString(), attr);
      }

      //
      // list files
      List<FileRecord> files = FileLister.listFiles(tmp.toString());
      for (FileRecord f : files) {
        BasicFileAttributes fAttr = attrs.get(f.getFileName());
        assertEquals(Long.valueOf(fAttr.size()), f.getFileSize());
        assertEquals(fAttr.creationTime().toMillis(), f.getCreationTime().getTime());
      }

      //
      // drop the folder
      for (FileRecord f : files) {
        Files.delete(Paths.get(tmp.toString(), f.getFileName()));
      }
      Files.delete(tmp);
    }
    catch (IOException e) {
      System.err.println(StackTrace.toString(e));
      fail("shouldnt fail by throwing exception");
    }
  }

}
