package net.seninp.wicketrna.security;

import org.apache.wicket.authroles.authentication.AuthenticatedWebSession;
import org.apache.wicket.authroles.authorization.strategies.role.Roles;
import org.apache.wicket.request.Request;
import net.seninp.wicketrna.db.WicketRNADb;

public final class PiretWebSession extends AuthenticatedWebSession {

  private static final long serialVersionUID = -715683341720577958L;

  /** Trivial user representation */
  private String userName;

  /**
   * Constructor
   * 
   * @param request
   */
  public PiretWebSession(Request request) {
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
    if (WicketRNADb.authinticate(username, password)) {
      userName = username;
    }
    return userName != null;
  }

  /**
   * @return User
   */
  public String getUser() {
    return userName;
  }

  /**
   * @param user New user
   */
  public void setUser(final String user) {
    this.userName = user;
  }

  /**
   * @see org.apache.wicket.authentication.AuthenticatedWebSession#getRoles()
   */
  @Override
  public Roles getRoles() {
    return null;
  }
}
