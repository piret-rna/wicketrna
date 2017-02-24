package net.seninp;

import java.util.Arrays;
import java.util.List;
import org.apache.wicket.Component;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.StatelessLink;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.RepeatingView;

public final class PiretPage extends WebPage {

  private static final long serialVersionUID = 2799448818773645768L;

  private static final String HOME = "home";
  private static final String UPLOAD = "upload";
  private static final String RUN = "run";
  private static final String PROJECTS = "projects";

  private static final List<MainMenuLink> mainMenuLinks = Arrays.asList(new MainMenuLink[] {
      new MainMenuLink("Home", PiretPage.HOME), new MainMenuLink("Upload files", PiretPage.UPLOAD),
      new MainMenuLink("Run PiReT pipeline", PiretPage.RUN),
      new MainMenuLink("Projects", PiretPage.PROJECTS) });

  @Override
  protected void onInitialize() {

    super.onInitialize();

    final Panel homePanel = new PiretHomePanel("homepanel", new PiretHomePageModel());

    add(homePanel);
    homePanel.setVisible(false);
    homePanel.setOutputMarkupPlaceholderTag(true);

    //
    // left side menu
    RepeatingView view = new RepeatingView("list_items") {
      private static final long serialVersionUID = 1L;
      private String activeSelection;
      private Component activeComponent;

      protected void onPopulate() {
        removeAll();
        for (MainMenuLink linky : mainMenuLinks) {
          StatelessLink<String> link = new StatelessLink<String>(linky.getActionKey()) {
            private static final long serialVersionUID = 1L;

            @Override
            public void onClick() {
              activeSelection = this.getId();
              System.out.println("selected id: " + this.getId());
            }
          };
          if (linky.getActionKey().equalsIgnoreCase(activeSelection)) {
            link.add(new AttributeAppender("class", " active"));
            if (HOME.equals(linky.getActionKey())) {
              if (null != activeComponent && !(activeComponent instanceof PiretHomePanel)) {
                activeComponent.setVisible(false);
              }
              homePanel.setVisible(true);
              activeComponent = homePanel;
            }
          }

          this.add(link);
          link.add(new Label("name", linky.getDisplayName()));
        }
      }

    };

    this.add(view);

  }

}