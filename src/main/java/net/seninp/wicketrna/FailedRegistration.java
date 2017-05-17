package net.seninp.wicketrna;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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
public class FailedRegistration extends WebPage {

  private static final long serialVersionUID = 1L;

  private static final Logger logger = LogManager.getLogger(FailedRegistration.class);

  /**
   * Constructor.
   * 
   * @param parameters
   */
  public FailedRegistration(final PageParameters parameters) {

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
    };

    Button signinButton = new Button("back_home") {
      private static final long serialVersionUID = 1L;

      public void onSubmit() {
        logger.info("signin button executed");
        setResponsePage(HomePage.class);
      }
    };

    signinButton.setDefaultFormProcessing(false);
    form.add(signinButton);
    add(form);

  }
}
