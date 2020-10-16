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
import javafx.scene.control.TableColumn;

/**
 * FXML Controller class
 *
 * @author LordLalo
 */
public class BuscarUsuarioController extends Controller implements Initializable {

    @FXML
    public JFXTextField txtNombre;
    @FXML
    public JFXTextField txtApellido;
    @FXML
    public JFXTextField txtCedula;
    @FXML
    public DatePicker dtpFechaInicial;
    @FXML
    public DatePicker dtpFechaFinal;
    @FXML
    public JFXCheckBox checkActivo;
    @FXML
    public JFXButton btnBuscar;
    @FXML
    public TableColumn<?, ?> tbcId;
    @FXML
    public TableColumn<?, ?> tblCedula;
    @FXML
    public TableColumn<?, ?> tbcNombre;
    @FXML
    public TableColumn<?, ?> tbcApellido;
    @FXML
    public TableColumn<?, ?> tbcFechaIngreso;
    @FXML
    public TableColumn<?, ?> tbcFechaModificacion;
    @FXML
    public TableColumn<?, ?> tbcEstado;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    @Override
    public void initialize() {
        
    }

    @FXML
    public void actionBuscarPersona(ActionEvent event) {
    }
    
}
