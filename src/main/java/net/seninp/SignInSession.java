package net.seninp;

import org.apache.wicket.authroles.authentication.AuthenticatedWebSession;
import org.apache.wicket.authroles.authorization.strategies.role.Roles;
import org.apache.wicket.request.Request;

public final class SignInSession extends AuthenticatedWebSession {
  /** Trivial user representation */
  private String user;

  /**
   * Constructor
   * 
   * @param request
   */
  protected SignInSession(Request request) {
    super(request);
  }

  /**
   * Checks the given username and password, returning a User object if if the username and password
   * identify a valid user.
   * 
   * @param username The username
   * @param password The password
   * @return True if the user was authenticated
   */
  @Override
  public final boolean authenticate(final String username, final String password) {
    final String WICKET = "wicket";

    if (user == null) {
      // Trivial password "db"
      if (WICKET.equalsIgnoreCase(username) && WICKET.equalsIgnoreCase(password)) {
        user = username;
      }
    }

    return user != null;
  }

  /**
   * @return User
   */
  public String getUser() {
    return user;
  }

  /**
   * @param user New user
   */
  public void setUser(final String user) {
    this.user = user;
  }

  /**
   * @see org.apache.wicket.authentication.AuthenticatedWebSession#getRoles()
   */
  @Override
  public Roles getRoles() {
    return null;
  }
}
