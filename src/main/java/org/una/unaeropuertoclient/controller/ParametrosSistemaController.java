/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.una.unaeropuertoclient.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXTimePicker;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import org.una.unaeropuertoclient.model.ParamSistemaDto;
import org.una.unaeropuertoclient.service.ParamSistemaServicio;
import org.una.unaeropuertoclient.service.RolService;
import org.una.unaeropuertoclient.utils.Mensaje;
import org.una.unaeropuertoclient.utils.Respuesta;

/**
 * FXML Controller class
 *
 * @author LordLalo
 */
public class ParametrosSistemaController  extends Controller implements Initializable {

    @FXML
    public JFXTextField txtRepresentante;
    @FXML
    public JFXTextField txtCorreo;
    @FXML
    public JFXTextField txtTelefono;
    @FXML
    public JFXTimePicker tpkApertura;
    @FXML
    public JFXComboBox<?> cbxTiempoInactivo;
    @FXML
    private JFXTimePicker tpkCierre;
    @FXML
    private JFXComboBox<?> cbxVuelos;
    @FXML
    public JFXButton btnEditar;
public ParamSistemaDto paramSistemaDto=new ParamSistemaDto();
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        ParamSistemaServicio paramSistemaServicio = new ParamSistemaServicio();
        Respuesta respuesta = paramSistemaServicio.getById();
        if (respuesta.getEstado()) {
            paramSistemaDto = (ParamSistemaDto) respuesta.getResultado("data");
        } else {
            new Mensaje().showModal(Alert.AlertType.ERROR, "Falla al extraer los parametros del sistema", this.getStage(), respuesta.getMensaje());

        }
        txtRepresentante.setText(paramSistemaDto.getNombreRepresentante());
    }    

    @Override
    public void initialize() {
    }

    @FXML
    public void editarParametros(ActionEvent event) {
        
    }
    
}
