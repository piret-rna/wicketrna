package net.seninp.wicketrna;

import org.apache.wicket.devutils.debugbar.DebugBar;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import net.seninp.wicketrna.models.TimeStampModel;

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
    setVersioned(false);
    
    add(new DebugBar("debug"));

    //
    // place the App and DB info on the screen
    add(new Label("wicketversion", getApplication().getFrameworkSettings().getVersion()));
    add(new Label("hsqlversion", ((PiretApplication) getApplication()).getDBInfo()));
    add(new Label("timeStamp", new TimeStampModel()));

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
