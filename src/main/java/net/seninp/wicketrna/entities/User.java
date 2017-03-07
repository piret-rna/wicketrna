package net.seninp.wicketrna.entities;

public class User {

  private Integer id;
  private String username;
  private String salt;
  private String email;
  private String user_folder;
  private String key_values;

  public User(Integer id, String username, String salt, String email, String user_folder,
      String key_values) {
    super();
    this.id = id;
    this.username = username;
    this.salt = salt;
    this.email = email;
    this.user_folder = user_folder;
    this.key_values = key_values;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((email == null) ? 0 : email.hashCode());
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
    return true;
  }

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getSalt() {
    return salt;
  }

  public void setSalt(String salt) {
    this.salt = salt;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
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

}
