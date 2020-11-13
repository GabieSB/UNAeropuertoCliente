/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.una.unaeropuertoclient.controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import org.una.unaeropuertoclient.utils.AppContext;
import org.una.unaeropuertoclient.utils.FlowController;
import org.una.unaeropuertoclient.utils.Mensaje;

/**
 * FXML Controller class
 *
 * @author roberth :)
 */
public class MenuSuperiorController extends Controller implements Initializable {

    @FXML
    public Label lblUsuarioLogeado;
    @FXML
    public Label lblPantallaActual;
    public Label lblCodigoPantallas;

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        AppContext.getInstance().set("MenuSuperior", this);
    }

    @Override
    public void initialize() {
    }

    @FXML
    public void OnActionGoBack(ActionEvent event) {
        FlowController.getInstance().goBack();
    }

    public void changeUserNameTitle(String name) {
        lblUsuarioLogeado.setText(name);
    }

    public void changeScreenNameTitle(String name) {
        lblPantallaActual.setText(name);
    }

    public void changeScreenCode(String name) {
        lblCodigoPantallas.setText(name);
    }

    @FXML
    private void onClickCerrarSesion(ActionEvent event) {
        if (new Mensaje().showConfirmation("Atención", this.getStage(), "Al aceptar "
                + "este recuadro la sesión actual se terminará y se perderán los cambios que no "
                + "halla guardado", "¿Desea cerrar sesión?")) {
            AppContext.getInstance().reset();
            FlowController.getInstance().reset();
            this.getStage().close();
            FlowController.getInstance().goViewInWindow("Login", Boolean.FALSE);
        }
    }

    @FXML
    private void onClickSalir(ActionEvent event) {
        if (new Mensaje().showConfirmation("Atención", this.getStage(), "Al aceptar "
                + "este recuadro el programa se cerrará y se perderán los cambios que no "
                + "halla guardado", "¿Desea cerrar esta aplicación?")) {
            this.getStage().close();
        }
    }

}
