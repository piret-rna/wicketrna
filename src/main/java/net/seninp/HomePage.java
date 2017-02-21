package net.seninp;

import java.util.Date;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.request.mapper.parameter.PageParameters;

public class HomePage extends WebPage {
  private static final long serialVersionUID = 1L;

  public HomePage(final PageParameters parameters) {
    super(parameters);

    add(new Label("version", getApplication().getFrameworkSettings().getVersion()));
    add(new Label("timeStamp", new Date().toString()));

    // Model<String> timeStampModel = new Model<String>() {
    // private static final long serialVersionUID = 1L;
    //
    // @Override
    // public String getObject() {
    // return new Date().toString();
    // }
    // };
    //
    // add(new Label("timeStamp", timeStampModel));

    Form<String> form = new Form<String>("form") {
      private static final long serialVersionUID = 1L;

      protected void onSubmit() {
        info("Form.onSubmit executed");
      }
    };

    Button button2 = new Button("sign_in_button") {
      private static final long serialVersionUID = 1L;

      public void onSubmit() {
        info("button2.onSubmit executed");
        setResponsePage(LoginPage.class);
      }
    };

    button2.setDefaultFormProcessing(false);
    form.add(button2);

    add(form);

  }
}
