/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.una.unaeropuertoclient.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXTextField;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import org.una.unaeropuertoclient.utils.FlowController;

/**
 * FXML Controller class
 *
 * @author LordLalo
 */
public class CrearUsuarioController extends Controller implements Initializable {

    
    @FXML
    public JFXTextField txtNombre;
    @FXML
    public JFXTextField txtApellido;
    @FXML
    public JFXTextField txtCedula;
    @FXML
    public JFXTextField txtContrasenna;
    @FXML
    public JFXTextField txtContrasennaConfirmacion;
    public DatePicker dtkFechaNacimiento;
    public DatePicker dtkFechaIngreso;
    @FXML
    public JFXCheckBox checkActivo;
    @FXML
    public Label lblFechaModificacion;
    @FXML
    public JFXButton btnGuardo;
    @FXML
    public DatePicker dtpFechaNacimiento;
    @FXML
    public DatePicker dtpFechaRegistro;
    @FXML
    public JFXButton btnEditar;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO0
    }

    @Override
    public void initialize() {
    }

    @FXML
    public void onActionGuardar(ActionEvent event) {
    }

    @FXML
    public void goBuscar(ActionEvent event) {
           FlowController.getInstance().goView("BuscarUsuario");
    }

}
