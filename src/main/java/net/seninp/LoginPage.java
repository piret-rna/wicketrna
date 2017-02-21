package net.seninp;

import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.form.PasswordTextField;
import org.apache.wicket.markup.html.form.StatelessForm;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.util.string.Strings;

public final class LoginPage extends WebPage {

  private static final long serialVersionUID = -5269975581193823397L;

  private String username;
  private String password;

  @Override
  protected void onInitialize() {
    super.onInitialize();

    StatelessForm<String> form = new StatelessForm<String>("form") {
      private static final long serialVersionUID = 1L;

      @Override
      protected void onSubmit() {
        if (Strings.isEmpty(username) || Strings.isEmpty(password))
          return;

        boolean authResult = SignInSession.get().signIn(username, password);

        if (authResult) {
          continueToOriginalDestination();
        }
      }
    };

    form.setDefaultModel(new CompoundPropertyModel<LoginPage>(this));

    form.add(new TextField<String>("username"));
    form.add(new PasswordTextField("password"));

    add(form);
  }
}