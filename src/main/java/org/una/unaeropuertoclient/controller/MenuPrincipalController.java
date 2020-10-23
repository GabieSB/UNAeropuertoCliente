/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.una.unaeropuertoclient.controller;

import java.net.URL;
import java.util.ResourceBundle;
import com.jfoenix.controls.JFXButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
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
    @FXML
    public JFXButton btnNotificaciones;

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

    

    @FXML
    public void btnconsultarServicios(ActionEvent actionEvent) {
        FlowController.getInstance().goView("GestorServiciosConsultas");
    }
    @FXML
    public void OnClickAirModule(ActionEvent event) {
        FlowController.getInstance().goView("GestorVuelos");
       
    }

    @FXML
    public void goNotificaciones(ActionEvent event) {
       FlowController.getInstance().goView("AutorizarNotificaciones"); 
    }

    public void btnRegistrarGastos(ActionEvent actionEvent) {
        FlowController.getInstance().goView("RegistrarGastos");
    }

    public void btnconsultarGastos(ActionEvent actionEvent) {
        FlowController.getInstance().goView("GestorGastosConsultas");
    }

    public void btnConsultarBitacoras(ActionEvent actionEvent) {
        FlowController.getInstance().goView("BuscarBitacoras");
    }
}
