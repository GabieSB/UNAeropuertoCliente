/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.una.unaeropuertoclient.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import org.una.unaeropuertoclient.model.AuthenticationResponse;
import org.una.unaeropuertoclient.model.UsuarioDto;
import org.una.unaeropuertoclient.service.UsuarioService;
import org.una.unaeropuertoclient.utils.ButtonWaitUtils;
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
    @FXML
    private JFXButton btnIngresar;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        txtCedula.setText("admin2");
        txtContrasenna.setText("Una2020");
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
            ingresar();
        } else {
            new Mensaje().showModal(Alert.AlertType.INFORMATION, "Observa con atención", this.getStage(), "Alguno de los campos ha se encuentra vacío");
        }
    }

    private void ingresar() {
        ButtonWaitUtils.aModoEspera(btnIngresar);
        Thread th = new Thread(() -> {
            Respuesta resp = usuarioService.logIn(txtCedula.getText(), txtContrasenna.getText());
            Platform.runLater(() -> {
                if (resp.getEstado()) {
                    ButtonWaitUtils.salirModoEspera(btnIngresar, "Ingesar");
                    FlowController.getInstance().goMain();
                    FlowController.getInstance().goView("MenuSuperior", "Top", null);
                    FlowController.getInstance().goView("MenuPrincipal");
                    UsuarioDto user = ((AuthenticationResponse) resp.getResultado("data")).getUsuario();
                    FlowController.changeUserNameTittle(user.getNombre() + " " + user.getApellidos());
                    this.getStage().close();
                } else {
                    ButtonWaitUtils.salirModoEspera(btnIngresar, "Ingesar");
                    new Mensaje().showModal(Alert.AlertType.WARNING, "Algo ha ocurrido", this.getStage(), resp.getMensaje());
                }
            });
        });
        th.start();
    }

}
