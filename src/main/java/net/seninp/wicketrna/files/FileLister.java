package net.seninp.wicketrna.util;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;

/**
 * Listing files in a folder.
 * 
 * @author psenin
 *
 */
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
  public static ArrayList<FileRecord> listFiles(String folder) {

    final ArrayList<FileRecord> res = new ArrayList<FileRecord>();
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
          res.add(new FileRecord(file.toAbsolutePath(), attrs.creationTime(), attrs.size()));
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

  public static FileRecord getFileRecord(String absolutePath) {
    Path file = Paths.get(absolutePath);
    BasicFileAttributes attrs;
    try {
      attrs = Files.readAttributes(file, BasicFileAttributes.class);
      return new FileRecord(file, attrs.creationTime(), attrs.size());
    }
    catch (IOException e) {
      System.err.println(StackTrace.toString(e));
    }
    return new FileRecord();
  }

}
