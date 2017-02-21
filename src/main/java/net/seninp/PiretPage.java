package net.seninp;

import org.apache.wicket.authroles.authentication.AuthenticatedWebApplication;
import org.apache.wicket.authroles.authentication.AuthenticatedWebSession;
import org.apache.wicket.markup.html.WebPage;

public final class PiretPage extends WebPage {

  private static final long serialVersionUID = 2799448818773645768L;

  @Override
  protected void onConfigure() {
    AuthenticatedWebApplication app = (AuthenticatedWebApplication) AuthenticatedWebApplication
        .get();

    if (!AuthenticatedWebSession.get().isSignedIn())
      app.restartResponseAtSignInPage();
  }

  @Override
  protected void onInitialize() {
    super.onInitialize();
    // add(new Link("goToHomePage") {
    //
    // @Override
    // public void onClick() {
    // setResponsePage(getApplication().getHomePage());
    // }
    // });
    //
    // add(new Link("logOut") {
    //
    // @Override
    // public void onClick() {
    // AuthenticatedWebSession.get().invalidate();
    // setResponsePage(getApplication().getHomePage());
    // }
    // });
  }

}