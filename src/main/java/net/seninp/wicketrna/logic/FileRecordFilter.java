package net.seninp.wicketrna.logic;

import java.io.Serializable;
import java.util.Date;

public class FileRecordFilter implements Serializable {

  private static final long serialVersionUID = 1L;

  private Date dateFrom;
  private Date dateTo;

  public Date getDateFrom() {
    return dateFrom;
  }

  public void setDateFrom(Date dateFrom) {
    this.dateFrom = dateFrom;
  }

  public Date getDateTo() {
    return dateTo;
  }

  public void setDateTo(Date dateTo) {
    this.dateTo = dateTo;
  }

}
