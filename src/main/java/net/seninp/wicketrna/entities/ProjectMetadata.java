package net.seninp.wicketrna.entities;

import java.io.Serializable;

public class ProjectMetadata implements Serializable {

  private static final long serialVersionUID = 1L;

  private String projectName;
  private String description;
  private String contributors;
  private String overallDesign;

  public String getProjectName() {
    return projectName;
  }

  public void setProjectName(String projectName) {
    this.projectName = projectName;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public String getContributors() {
    return contributors;
  }

  public void setContributors(String contributors) {
    this.contributors = contributors;
  }

  public String getOverallDesign() {
    return overallDesign;
  }

  public void setOverallDesign(String overallDesign) {
    this.overallDesign = overallDesign;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((contributors == null) ? 0 : contributors.hashCode());
    result = prime * result + ((description == null) ? 0 : description.hashCode());
    result = prime * result + ((overallDesign == null) ? 0 : overallDesign.hashCode());
    result = prime * result + ((projectName == null) ? 0 : projectName.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    ProjectMetadata other = (ProjectMetadata) obj;
    if (contributors == null) {
      if (other.contributors != null)
        return false;
    }
    else if (!contributors.equals(other.contributors))
      return false;
    if (description == null) {
      if (other.description != null)
        return false;
    }
    else if (!description.equals(other.description))
      return false;
    if (overallDesign == null) {
      if (other.overallDesign != null)
        return false;
    }
    else if (!overallDesign.equals(other.overallDesign))
      return false;
    if (projectName == null) {
      if (other.projectName != null)
        return false;
    }
    else if (!projectName.equals(other.projectName))
      return false;
    return true;
  }

  @Override
  public String toString() {
    StringBuilder builder = new StringBuilder();
    builder.append("ProjectMetadata [projectName=").append(projectName).append(", description=")
        .append(description).append(", contributors=").append(contributors)
        .append(", overallDesign=").append(overallDesign).append("]");
    return builder.toString();
  }

}
