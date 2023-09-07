package gui.header.component;

import engine2ui.simulation.prview.PreviewData;
import gui.app.AppController;
import gui.header.component.queue.manager.QueueManagerComponentController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;
import jaxb.event.FileLoadedEvent;
import java.io.File;
import java.util.EventListener;
import java.util.List;

public class HeaderComponentController implements FileLoadedEvent {
    private AppController mainController;
    @FXML
    private Button loadFileBTN;

    @FXML
    private GridPane QueueManagerComponent;

    @FXML private QueueManagerComponentController queueManagerComponentController;

    @FXML
    private TextField pathTF;
    @FXML
    private Label predictionLabel;

    private String currentLoadedFilePath;

    public void setMainController(AppController mainController) {
        this.mainController = mainController;
        currentLoadedFilePath = "";
    }

    public void initialize() {
        if(queueManagerComponentController != null) {
            queueManagerComponentController.setMainController(this);
        }
    }

    /**
     * Opens a file choose dialog (FCD) and tries to load the file given.
     */
    @FXML
    void loadFileButtonActionListener(ActionEvent event) {
        /// Initial FCD settings
        FileChooser fileChooser = new FileChooser();

        // Set the title of the dialog
        fileChooser.setTitle("Open settings file");

        // Set the starting dir of the dialog to be desktop
        fileChooser.setInitialDirectory(new File(System.getProperty("user.home"), "Desktop"));

        File selectedFile = fileChooser.showOpenDialog(null);

        if(selectedFile != null){
            loadFile(selectedFile);
        }
    }

    /**
     * Tries to load a simulation file. If successful sets it as the header text field text
     * And notifies the user that the load succeeded. Otherwise notifies that the load failed
     * and displays the errors.
     */
    private void loadFile(File fileToLoad){
        try {
            mainController.engineAgent.loadSimulationFromFile(fileToLoad);
            pathTF.setText(fileToLoad.getPath());
            currentLoadedFilePath = fileToLoad.getPath();
            mainController.showNotification("File has been loaded successfully!");
        }catch (IllegalArgumentException e){
            mainController.showNotification(e.getMessage());
        }
    }

    @FXML
    void loadFileTextFieldListener(ActionEvent event) {
        if (!pathTF.getText().equals(currentLoadedFilePath)) {
            File file = new File(pathTF.getText());

            loadFile(file);
        }
    }

    @FXML
    void resetTextOnMouseEnteredTextFieldListener(MouseEvent event) {
        resetTextFieldIfNotFocused();
    }

    @FXML
    void resetTextOnMouseExitTextFieldListener(MouseEvent event) {
        resetTextFieldIfNotFocused();
    }

    private void resetTextFieldIfNotFocused() {
        if (!pathTF.isFocused()) {
            pathTF.setText(currentLoadedFilePath);
        }
    }

    @Override
    public void onFileLoaded(PreviewData previewData) {

    }
}