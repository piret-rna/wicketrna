package net.seninp.wicketrna;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import org.apache.wicket.Component;
import org.apache.wicket.authroles.authentication.AuthenticatedWebApplication;
import org.apache.wicket.authroles.authentication.AuthenticatedWebSession;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.StatelessLink;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.RepeatingView;
import org.apache.wicket.model.Model;
import net.seninp.wicketrna.db.WicketRNADb;
import net.seninp.wicketrna.security.PiretWebSession;

public final class PiretPage extends WebPage {

  private static final long serialVersionUID = 2799448818773645768L;

  private static final String HOME = "home";
  private static final String UPLOAD = "upload";
  private static final String RUN = "run";
  private static final String PROJECTS = "projects";

  private static final List<MainMenuLink> mainMenuLinks = Arrays.asList(new MainMenuLink[] {
      new MainMenuLink("Home", PiretPage.HOME), new MainMenuLink("Manage files", PiretPage.UPLOAD),
      new MainMenuLink("Run PiReT pipeline", PiretPage.RUN),
      new MainMenuLink("Projects", PiretPage.PROJECTS) });

  @Override
  protected void onConfigure() {

    super.onConfigure();

    AuthenticatedWebApplication app = (AuthenticatedWebApplication) AuthenticatedWebApplication
        .get();

    if (!AuthenticatedWebSession.get().isSignedIn()) {
      app.restartResponseAtSignInPage();
    }

  }

  @Override
  protected void onInitialize() {

    super.onInitialize();

    // the timestamp model to print the current time on the screen
    Model<String> timeStampModel = new Model<String>() {
      private static final long serialVersionUID = 1L;

      @Override
      public String getObject() {
        return new Date().toString();
      }
    };

    // the username model
    Model<String> userNameModel = new Model<String>() {
      private static final long serialVersionUID = 1L;

      @Override
      public String getObject() {
        return ((PiretWebSession) AuthenticatedWebSession.get()).getUser();
      }
    };

    // the userfolder model
    Model<String> userFolderModel = new Model<String>() {
      private static final long serialVersionUID = 1L;

      @Override
      public String getObject() {
        String username = ((PiretWebSession) AuthenticatedWebSession.get()).getUser();
        return WicketRNADb.getUser(username).getUser_folder();
      }
    };

    final Panel homePanel = new HomePanel("home_panel", new DummyHomePanelModel());
    add(homePanel);
    homePanel.setVisible(false);

    final Panel fileUplodPanel = new FileUploadPanel("fileupload_panel", new DummyHomePanelModel());
    add(fileUplodPanel);
    fileUplodPanel.setVisible(false);

    final Panel fileManagementPanel = new FileManagementPanel("filemanagement_panel",
        new DummyHomePanelModel());
    add(fileManagementPanel);
    fileManagementPanel.setVisible(false);

    final Panel pipelinePanel = new PipelinePanel("pipeline_panel", new DummyHomePanelModel());
    add(pipelinePanel);
    pipelinePanel.setVisible(false);

    final Panel projectsPanel = new ProjectsPanel("projects_panel", new DummyHomePanelModel());
    add(projectsPanel);
    projectsPanel.setVisible(false);

    add(new Label("username", userNameModel));
    add(new Label("userfolder", userFolderModel));
    add(new Label("timeStamp", timeStampModel));

    //
    // left side menu
    RepeatingView view = new RepeatingView("list_items") {
      private static final long serialVersionUID = 1L;
      private String activeSelection = HOME; // put the homepanel on the screen by default
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

            // is it home panel?
            if (HOME.equals(linky.getActionKey())) {
              if (null != activeComponent && !(activeComponent instanceof HomePanel)) {
                activeComponent.setVisible(false);
              }
              homePanel.setVisible(true);
              activeComponent = homePanel;
            }

            // is it fileupload panel?
            if (UPLOAD.equals(linky.getActionKey())) {
              if (null != activeComponent && !(activeComponent instanceof FileManagementPanel)) {
                activeComponent.setVisible(false);
                fileManagementPanel.setVisible(false);
              }
              fileUplodPanel.setVisible(true);
              fileManagementPanel.setVisible(true);
              activeComponent = fileUplodPanel;
            }

            // is it pipeline panel?
            if (RUN.equals(linky.getActionKey())) {
              if (null != activeComponent && !(activeComponent instanceof PipelinePanel)) {
                activeComponent.setVisible(false);
              }
              pipelinePanel.setVisible(true);
              activeComponent = pipelinePanel;
            }

            // is it projects panel?
            if (PROJECTS.equals(linky.getActionKey())) {
              if (null != activeComponent && !(activeComponent instanceof ProjectsPanel)) {
                activeComponent.setVisible(false);
              }
              projectsPanel.setVisible(true);
              activeComponent = projectsPanel;
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