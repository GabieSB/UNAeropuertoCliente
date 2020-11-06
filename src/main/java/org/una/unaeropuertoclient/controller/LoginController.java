/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.una.unaeropuertoclient.controller;

import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import org.una.unaeropuertoclient.model.AuthenticationResponse;
import org.una.unaeropuertoclient.model.UsuarioDto;
import org.una.unaeropuertoclient.service.UsuarioService;
import org.una.unaeropuertoclient.utils.FlowController;
import org.una.unaeropuertoclient.utils.Mensaje;
import org.una.unaeropuertoclient.utils.Respuesta;

/**
 * FXML Controller class
 *
 * @author Roberth :)
 */
public class LoginController extends Controller implements Initializable {

    @FXML
    public JFXTextField txtCedula;
    @FXML
    public JFXPasswordField txtContrasenna;

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    UsuarioService usuarioService = new UsuarioService();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }

    @Override
    public void initialize() {
    }

    @FXML
    public void onActionSalir(ActionEvent event) {
        this.getStage().close();
    }

    @FXML
    public void onActionIngresar(ActionEvent event) {

        if (!txtCedula.getText().isBlank() && !txtContrasenna.getText().isBlank()) {
            Respuesta resp = usuarioService.logIn(txtCedula.getText(), txtContrasenna.getText());
            if (resp.getEstado()) {
                FlowController.getInstance().goMain();
                FlowController.getInstance().goView("MenuSuperior", "Top", null);
                FlowController.getInstance().goView("MenuPrincipal");
                UsuarioDto user = ((AuthenticationResponse) resp.getResultado("data")).getUsuario();
                FlowController.changeUserNameTittle(user.getNombre() + " " + user.getApellidos());
                this.getStage().close();
            } else {
                new Mensaje().showModal(Alert.AlertType.WARNING, "Algo ha ocurrido", this.getStage(), resp.getMensaje());
            }
        } else {
            new Mensaje().showModal(Alert.AlertType.INFORMATION, "Observa con atención", this.getStage(), "Alguno de los campos ha se encuentra vacío");
        }
    }

}
