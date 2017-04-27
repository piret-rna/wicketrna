/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.seninp.wicketrna.logic;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import org.apache.wicket.extensions.markup.html.repeater.data.sort.SortOrder;
import org.apache.wicket.extensions.markup.html.repeater.data.table.filter.IFilterStateLocator;
import org.apache.wicket.extensions.markup.html.repeater.util.SortableDataProvider;
import org.apache.wicket.model.IModel;
import net.seninp.wicketrna.files.FileLister;
import net.seninp.wicketrna.files.FileRecord;

/**
 * implementation of IDataProvider for file records that keeps track of sort information.
 * 
 * @author igor, seninp
 * 
 */
public class SortableFileRecordProvider extends SortableDataProvider<FileRecord, String>
    implements IFilterStateLocator<FileRecordFilter> {

  private static final long serialVersionUID = 1L;

  private FileRecordFilter fileNameFilter = new FileRecordFilter();

  private String userFolder;

  /**
   * constructor
   * 
   * @param userFolder the user's folder we are working with.
   */
  public SortableFileRecordProvider(String userFolder) {
    this.userFolder = userFolder;
    setSort("timestamp", SortOrder.DESCENDING);
    System.out.println("size -- > " + size());
  }

  @Override
  public Iterator<FileRecord> iterator(long first, long count) {
    List<FileRecord> filesFound = FileLister.listFiles(this.userFolder.toString(), getSort());
    return filterFileRecords(filesFound).subList((int) first, (int) (first + count)).iterator();
  }

  private List<FileRecord> filterFileRecords(List<FileRecord> filesFound) {
    System.out.println("Sort order: " + getSort());
    ArrayList<FileRecord> result = new ArrayList<>();
    Date dateFrom = fileNameFilter.getDateFrom();
    Date dateTo = fileNameFilter.getDateTo();

    for (FileRecord fileRecord : filesFound) {
      Date fileCreationDate = fileRecord.getCreationTime();

      if (dateFrom != null && fileCreationDate.before(dateFrom)) {
        continue;
      }

      if (dateTo != null && fileCreationDate.after(dateTo)) {
        continue;
      }

      result.add(fileRecord);
    }

    return result;
  }

  @Override
  public long size() {
    int size = filterFileRecords(FileLister.listFiles(this.userFolder.toString(), getSort()))
        .size();
    System.out.println("called size: " + size);
    return size;
  }

  @Override
  public IModel<FileRecord> model(FileRecord object) {
    return new DetachableFileRecordModel(object);
  }

  @Override
  public FileRecordFilter getFilterState() {
    return fileNameFilter;
  }

  @Override
  public void setFilterState(FileRecordFilter state) {
    System.out.println("filter state " + state);
    fileNameFilter = state;
  }
}
