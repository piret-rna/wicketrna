package net.seninp.wicketrna.util;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;

public class FileLister {

  public FileLister() {
    // constructor
    super();
  }

  /**
   * List all the files in the folder.
   * 
   * @param folder the folder path.
   * @return the list of all file records.
   */
  public List<FileRecord> listFiles(String folder) {
    
    final List<FileRecord> res = new ArrayList<FileRecord>();
    Path path = FileSystems.getDefault().getPath(folder);
    
    try {
      Files.walkFileTree(path, new FileVisitor<Path>() {
        @Override
        public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs)
            throws IOException {
          return FileVisitResult.CONTINUE;
        }

        @Override
        public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
          // here you have the files to process
          res.add(new FileRecord(file.getFileName().toString(), attrs.creationTime(), attrs.size()));
          return FileVisitResult.CONTINUE;
        }

        @Override
        public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException {
          return FileVisitResult.TERMINATE;
        }

        @Override
        public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
          return FileVisitResult.CONTINUE;
        }
      });
    }
    catch (IOException e) {
      System.err.println(StackTrace.toString(e));
    }
    return res;
  }
}
