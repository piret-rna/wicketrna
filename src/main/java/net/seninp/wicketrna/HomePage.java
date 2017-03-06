package net.seninp.wicketrna;

import java.util.Date;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.mapper.parameter.PageParameters;

/**
 * Implements the landing homepage.
 * 
 * @author psenin
 *
 */
public class HomePage extends WebPage {
  private static final long serialVersionUID = 1L;

  /**
   * Constructor.
   * 
   * @param parameters
   */
  public HomePage(final PageParameters parameters) {
    super(parameters);

    // the timestamp model to print the current time on the screen
    Model<String> timeStampModel = new Model<String>() {
      private static final long serialVersionUID = 1L;

      @Override
      public String getObject() {
        return new Date().toString();
      }
    };

    // place the label with current wicket version and ct
    add(new Label("wicketversion", getApplication().getFrameworkSettings().getVersion()));
    add(new Label("hsqlversion", ((WicketApplication) getApplication()).getDBInfo()));
    add(new Label("timeStamp", timeStampModel));

    //
    // place the "Sign In" button on the screen
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
        setResponsePage(PiretPage.class);
      }
    };

    button2.setDefaultFormProcessing(false);
    form.add(button2);
    add(form);

  }
}
