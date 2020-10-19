/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.una.unaeropuertoclient.controller;

import java.net.URL;
import java.net.http.HttpResponse;
import java.sql.Timestamp;
import java.util.ResourceBundle;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.jfoenix.controls.JFXButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.input.MouseEvent;
import org.una.unaeropuertoclient.utils.RequestHTTP;
import org.una.unaeropuertoclient.model.AuthenticationResponse;
import org.una.unaeropuertoclient.model.BitacoraDto;
import org.una.unaeropuertoclient.utils.AppContext;
import org.una.unaeropuertoclient.utils.FlowController;

/**
 * FXML Controller class
 *
 * @author roberth :)
 */
public class MenuPrincipalController extends Controller implements Initializable {

    @FXML
    public JFXButton btnRegistrarUsuario;
    @FXML
    public JFXButton btnGestorServicios;

    /**
     * Initializes the controller class.
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO


    }

    @Override
    public void initialize() {
    }

    @FXML
    public void goRegistrarUsuario(ActionEvent event) {
        FlowController.getInstance().goView("CrearUsuario");
    }

    @FXML
    public void goGestorServicio(ActionEvent event) {
        FlowController.getInstance().goView("GestorServicios");
    }

    

    @FXML
    public void goParametros(ActionEvent event) {
          FlowController.getInstance().goView("ParametrosSistema");
    }

    

    public void btnconsultarServicios(ActionEvent actionEvent) {
        FlowController.getInstance().goView("GestorServiciosConsultas");
    }
    @FXML
    public void OnClickAirModule(ActionEvent event) {
        FlowController.getInstance().goView("GestorVuelos");
    }

}
