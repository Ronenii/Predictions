package gui.app;

import gui.header.component.HeaderComponentController;
import gui.sub.menus.SubMenusController;
import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import manager.EngineAgent;

import java.util.ArrayList;
import java.util.EventListener;
import java.util.List;

public class AppController implements HasFileLoadedListeners {
    @FXML private GridPane headerComponent;
    @FXML private HeaderComponentController headerComponentController;
    @FXML private GridPane subMenus;
    @FXML private SubMenusController subMenusController;

    @FXML private GridPane notificationBarComponent;
    @FXML private NotificationBarComponentController notificationBarComponentController;

    @FXML private AnchorPane anchorNotification;

    public EngineAgent engineAgent;

    @FXML
    public void initialize() {
        if(headerComponentController != null && subMenusController != null && notificationBarComponentController != null) {
            headerComponentController.setMainController(this);
            subMenusController.setMainController(this);
            notificationBarComponentController.setMainController(this);
            engineAgent = new EngineAgent();
        }
    }

    public List<EventListener> getAllFileLoadedListeners(){
        List<EventListener> listeners = new ArrayList<>();
        listeners.add(headerComponentController);
        listeners.addAll(subMenusController.getAllFileLoadedListeners());
        return listeners;


    }

    /**
     * Shows the given string on the notification bar.
     */
    public void showNotification(String notification){
        anchorNotification.visibleProperty().set(true);
        notificationBarComponentController.setLblNotificationText(notification);
    }
}
