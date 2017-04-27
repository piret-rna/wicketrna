package net.seninp.wicketrna.files;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import org.apache.wicket.extensions.markup.html.repeater.util.SortParam;
import net.seninp.wicketrna.util.StackTrace;

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
          res.add(
              new FileRecord(file.toAbsolutePath().toString(), attrs.creationTime(), attrs.size()));
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

  public static ArrayList<FileRecord> listFiles(String folder, SortParam<String> sort) {
    ArrayList<FileRecord> list = listFiles(folder);
    if (null == sort) {
      return list;
    }
    if (sort.getProperty().equalsIgnoreCase("fileName")) {
      Collections.sort(list, new Comparator<FileRecord>() {
        @Override
        public int compare(final FileRecord lhs, FileRecord rhs) {
          if (sort.isAscending()) {
            return lhs.getFileName().compareTo(rhs.getFileName());
          }
          else {
            return rhs.getFileName().compareTo(lhs.getFileName());
          }
        }
      });
    }
    else if (sort.getProperty().equalsIgnoreCase("timestamp")) {
      Collections.sort(list, new Comparator<FileRecord>() {
        @Override
        public int compare(final FileRecord lhs, FileRecord rhs) {
          if (sort.isAscending()) {
            return lhs.getCreationTime().compareTo(rhs.getCreationTime());
          }
          else {
            return rhs.getCreationTime().compareTo(lhs.getCreationTime());
          }
        }
      });
    }
    return list;
  }

  /**
   * Queries file system for additional file info.
   * 
   * @param absolutePath
   * @return
   */
  public static FileRecord getFileRecord(String absolutePath) {
    Path file = Paths.get(absolutePath);
    BasicFileAttributes attrs;
    try {
      attrs = Files.readAttributes(file, BasicFileAttributes.class);
      return new FileRecord(file.toString(), attrs.creationTime(), attrs.size());
    }
    catch (IOException e) {
      System.err.println(StackTrace.toString(e));
    }
    return new FileRecord();
  }

}
