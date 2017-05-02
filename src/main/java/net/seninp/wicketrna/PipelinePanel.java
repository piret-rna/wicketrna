package net.seninp.wicketrna;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.parser.filter.WicketTagIdentifier;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import net.seninp.wicketrna.entities.FileSelection;
import net.seninp.wicketrna.entities.ProjectMetadata;

public class PipelinePanel extends Panel {

  private static final long serialVersionUID = -6725615122840221173L;

  static {
    WicketTagIdentifier.registerWellKnownTagName(PANEL);
  }

  public PipelinePanel(String id, IModel<String> model) {
    super(id, model);

    // project metadata
    ProjectMetadata projectMetadata = new ProjectMetadata();
    Form<String> form = new Form<String>("form");

    form.add(new TextField<String>("projectname",
        new PropertyModel<String>(projectMetadata, "projectName")));
    form.add(new TextField<String>("description",
        new PropertyModel<String>(projectMetadata, "description")));
    form.add(new TextField<String>("contributors",
        new PropertyModel<String>(projectMetadata, "contributors")));
    form.add(new TextField<String>("overall_design",
        new PropertyModel<String>(projectMetadata, "overallDesign")));

    // files selection
    FileSelection inputFilesSelection = new FileSelection();
    form.add(new TextField<String>("input_files",
        new PropertyModel<String>(inputFilesSelection, "selectedFiles")));
    form.add(new Button("select_input_files", Model.of("Select files")) {
      private static final long serialVersionUID = 1L;

      public void onSubmit() {
        System.out.println("File select pushed!\n" + inputFilesSelection.getSelectedFiles());
      }
    });

    // the form submitter
    Button formSubmitter;
    form.add(formSubmitter = new Button("submit", Model.of("Validate and Run new project")) {
      private static final long serialVersionUID = 1L;

      public void onSubmit() {
        System.out.println("Submit pushed!\n" + projectMetadata.toString());
      }
    });

    form.setDefaultButton(formSubmitter);
    add(form);

    this.add(new Label("label", model.getObject()));
  }

}
