package interfaces;

import controllers.ScreensController;
import javafx.event.ActionEvent;

public interface ControlledScreen {

    void setScreenParent(ScreensController screenParent);
    void changeScreen(ActionEvent event, String name);
}
