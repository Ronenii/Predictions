package gui.execution.inputs.env.var;

import engine2ui.simulation.prview.PreviewData;
import gui.execution.inputs.InputsController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import jaxb.event.FileLoadedEvent;

public class EnvironmentVariablesComponentController implements FileLoadedEvent {

    private InputsController mainController;
    @FXML
    private TextField valueTF;

    @FXML
    private Button setBTN;

    @FXML
    private Label explanationLabel;

    @FXML
    private ListView<?> envVarsLV;

    @FXML
    private Label envVarLabel;

    public void setMainController(InputsController mainController) {
        this.mainController = mainController;
    }

    @FXML
    void setButtonActionListener(ActionEvent event) {

    }

    @FXML
    void valueTextFieldActionListener(ActionEvent event) {

    }

    @Override
    public void onFileLoaded(PreviewData previewData) {

    }
}