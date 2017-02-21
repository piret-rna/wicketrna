package net.seninp;

import org.apache.wicket.authroles.authentication.AuthenticatedWebApplication;
import org.apache.wicket.authroles.authentication.AuthenticatedWebSession;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.link.Link;

public final class PiretPage extends WebPage {

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
    add(new Link("goToHomePage") {

      @Override
      public void onClick() {
        setResponsePage(getApplication().getHomePage());
      }
    });

    add(new Link("logOut") {

      @Override
      public void onClick() {
        AuthenticatedWebSession.get().invalidate();
        setResponsePage(getApplication().getHomePage());
      }
    });
  }

}