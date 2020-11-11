/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.una.unaeropuertoclient.utils;

import javafx.scene.control.Button;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.una.unaeropuertoclient.App;

/**
 *
 * @author roberth :)
 */
public class ButtonWaitUtils {

    public static void aModoEspera(Button button) {
        if (AppContext.getInstance().get("waitGif") == null) {
            ImageView img = new ImageView(new Image(App.class.getResource("pics/wait2.gif").toString()));
            img.setFitHeight(20);
            img.setFitWidth(20);
            AppContext.getInstance().set("waitGif", img);
        }
        button.setDisable(true);
        button.setText("espera...");
        button.setGraphic((ImageView) AppContext.getInstance().get("waitGif"));
    }

    public static void salirModoEspera(Button button, String OldTittle) {
        button.setDisable(false);
        button.setText(OldTittle);
        button.setGraphic(null);
    }

    public static void aModoEspera(ToggleButton button) {
        button.setDisable(true);
        button.setText("espera...");
    }

    public static void salirModoEspera(ToggleButton button, String OldTittle) {
        button.setDisable(false);
        button.setText(OldTittle);
    }

}
