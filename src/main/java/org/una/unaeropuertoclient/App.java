package org.una.unaeropuertoclient;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.una.unaeropuertoclient.utils.AppContext;
import org.una.unaeropuertoclient.utils.FlowController;

import java.io.IOException;
import org.una.unaeropuertoclient.utils.Mensaje;
/**
 * JavaFX App
 */
public class App extends Application {

    private static Scene scene;

    @Override
    public void start(Stage stage) throws IOException {
        stage.setOnCloseRequest(event -> {
            if (!new Mensaje().showConfirmation("Atención", stage, "Al aceptar "
                    + "este recuadro la sesión actual se terminará y se perderán los cambios que no "
                    + "haya guardado", "¿Desea cerrar esta aplicación?")) {
                event.consume();
            }
        });
        FlowController.getInstance().InitializeFlow(stage, null);
        FlowController.getInstance().goViewInWindow("Login", Boolean.FALSE);
    }

    public static void main(String[] args) {
        AppContext.getInstance();
        launch();
    }

}
