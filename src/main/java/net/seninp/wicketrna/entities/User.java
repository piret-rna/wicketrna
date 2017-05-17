package net.seninp.wicketrna.entities;

import java.io.Serializable;

/**
 * The piret user object.
 * 
 * @author psenin
 *
 */
public class User implements Serializable {

  private static final long serialVersionUID = 1L;

  private Integer id;
  private String userName;

  private String firstName;
  private String lastName;
  private String affiliation;
  private String email;

  private String salt;
  private String user_folder;
  private String key_values;

  public User(String userName, String firstName, String lastName, String affiliation, String email,
      String salt, String user_folder, String key_values) {
    super();
    this.userName = userName;
    this.firstName = firstName;
    this.lastName = lastName;
    this.affiliation = affiliation;
    this.email = email;
    this.salt = salt;
    this.user_folder = user_folder;
    this.key_values = key_values;
  }

  public User() {
    super();
    // TODO Auto-generated constructor stub
  }

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public String getUserName() {
    return userName;
  }

  public void setUserName(String userName) {
    this.userName = userName;
  }

  public String getFirstName() {
    return firstName;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public String getLastName() {
    return lastName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  public String getAffiliation() {
    return affiliation;
  }

  public void setAffiliation(String affiliation) {
    this.affiliation = affiliation;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getSalt() {
    return salt;
  }

  public void setSalt(String salt) {
    this.salt = salt;
  }

  public String getUser_folder() {
    return user_folder;
  }

  public void setUser_folder(String user_folder) {
    this.user_folder = user_folder;
  }

  public String getKey_values() {
    return key_values;
  }

  public void setKey_values(String key_values) {
    this.key_values = key_values;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((email == null) ? 0 : email.hashCode());
    result = prime * result + ((id == null) ? 0 : id.hashCode());
    result = prime * result + ((userName == null) ? 0 : userName.hashCode());
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
    User other = (User) obj;
    if (email == null) {
      if (other.email != null)
        return false;
    }
    else if (!email.equals(other.email))
      return false;
    if (id == null) {
      if (other.id != null)
        return false;
    }
    else if (!id.equals(other.id))
      return false;
    if (userName == null) {
      if (other.userName != null)
        return false;
    }
    else if (!userName.equals(other.userName))
      return false;
    return true;
  }

  @Override
  public String toString() {
    StringBuilder builder = new StringBuilder();
    builder.append("User [id=").append(id).append(", userName=").append(userName)
        .append(", firstName=").append(firstName).append(", lastName=").append(lastName)
        .append(", affiliation=").append(affiliation).append(", email=").append(email)
        .append(", salt=").append(salt).append(", user_folder=").append(user_folder)
        .append(", key_values=").append(key_values).append("]");
    return builder.toString();
  }

}
