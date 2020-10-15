package org.una.unaeropuertoclient;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.una.unaeropuertoclient.utils.AppContext;
import org.una.unaeropuertoclient.utils.FlowController;

import java.io.IOException;



/**
 * JavaFX App
 */
public class App extends Application {

    private static Scene scene;

    @Override
    public void start(Stage stage) throws IOException {
        FlowController.getInstance().InitializeFlow(stage, null);
        FlowController.getInstance().goViewInWindow("Login", Boolean.FALSE);
    }

    public static void main(String[] args) {

        AppContext.getInstance();
        launch();
    }

}
