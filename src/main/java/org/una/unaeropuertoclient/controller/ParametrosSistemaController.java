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
import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import org.una.unaeropuertoclient.model.ParamSistemaDto;
import org.una.unaeropuertoclient.service.ParamSistemaServicio;
import org.una.unaeropuertoclient.utils.Mensaje;
import org.una.unaeropuertoclient.utils.Respuesta;

/**
 * FXML Controller class
 *
 * @author LordLalo
 */
public class ParametrosSistemaController extends Controller implements Initializable {

    @FXML
    public JFXTextField txtRepresentante;
    @FXML
    public JFXTextField txtCorreo;
    @FXML
    public JFXTextField txtTelefono;
    @FXML
    private JFXTimePicker tpkApertura;
    @FXML
    public JFXComboBox<Integer> cbxTiempoInactivo;
    @FXML
    private JFXTimePicker tpkCierre;
    @FXML
    public JFXComboBox<Short> cbxVuelos;
    @FXML
    public JFXButton btnEditar;
    @FXML
    public JFXButton btnGuardar;
    public ParamSistemaDto paramSistemaDto = new ParamSistemaDto();
    private boolean activar = false;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        desactivarComponentes();
        CargarDatosComboBox();
        cargarDatos();

    }

    @Override
    public void initialize() {
    }

    @FXML
    public void editarParametros(ActionEvent event) {
        if (!activar) {
            ActivarComponentes();
            activar = true;
        } else {
            desactivarComponentes();
            activar = false;
        }
    }

    public void cargarDatos() {
        ParamSistemaServicio paramSistemaServicio = new ParamSistemaServicio();
        Respuesta respuesta = paramSistemaServicio.getById();
        if (respuesta.getEstado()) {
            paramSistemaDto = (ParamSistemaDto) respuesta.getResultado("data");
        } else {
            new Mensaje().showModal(Alert.AlertType.ERROR, "Falla al extraer los parametros del sistema", this.getStage(), respuesta.getMensaje());

        }
        txtRepresentante.setText(paramSistemaDto.getNombreRepresentante());
        txtCorreo.setText(paramSistemaDto.getEmailAeropuerto());
        txtTelefono.setText(paramSistemaDto.getTelefonoAeropuerto());
        LocalTime ltApertura = LocalTime.of(paramSistemaDto.getAperturaOficina().getHours(), paramSistemaDto.getAperturaOficina().getMinutes(), paramSistemaDto.getAperturaOficina().getSeconds());
        LocalTime ltCiere = LocalTime.of(paramSistemaDto.getCierreOficina().getHours(), paramSistemaDto.getCierreOficina().getMinutes(), paramSistemaDto.getCierreOficina().getSeconds());
        tpkApertura.setValue(ltApertura);
        tpkCierre.setValue(ltCiere);
        cbxTiempoInactivo.setValue(paramSistemaDto.getTiempoInactividad());
        cbxVuelos.setValue(paramSistemaDto.getVuelosHora());

    }

    public void desactivarComponentes() {
        txtCorreo.setDisable(true);
        txtRepresentante.setDisable(true);
        txtTelefono.setDisable(true);
        cbxTiempoInactivo.setDisable(true);
        cbxVuelos.setDisable(true);
        tpkApertura.setDisable(true);
        tpkCierre.setDisable(true);
        btnGuardar.setDisable(true);
    }

    public void ActivarComponentes() {
        txtCorreo.setDisable(false);
        txtRepresentante.setDisable(false);
        txtTelefono.setDisable(false);
        cbxTiempoInactivo.setDisable(false);
        cbxVuelos.setDisable(false);
        tpkApertura.setDisable(false);
        tpkCierre.setDisable(false);
        btnGuardar.setDisable(false);
    }

    @FXML
    public void onActionGuardar(ActionEvent event) {
        LocalDateTime ltApertura= LocalDateTime.of(LocalDate.now(), tpkApertura.getValue());
        LocalDateTime ltCierre = LocalDateTime.of(LocalDate.now(), tpkCierre.getValue());
        ParamSistemaDto p = new ParamSistemaDto();
        p.setAperturaOficina(Timestamp.valueOf(ltApertura));
        p.setCierreOficina(Timestamp.valueOf(ltCierre));
        p.setTelefonoAeropuerto(txtTelefono.getText());
        p.setEmailAeropuerto(txtCorreo.getText());
        p.setNombreRepresentante(txtRepresentante.getText());
        p.setVuelosHora(cbxVuelos.getValue());
        p.setTiempoInactividad(cbxTiempoInactivo.getValue());
        p.setId(1);
        ParamSistemaServicio paramSistemaServicio = new ParamSistemaServicio();
        Respuesta res = paramSistemaServicio.update(p);
        if (res.getEstado()) {
            new Mensaje().showModal(Alert.AlertType.INFORMATION, "Correcto", this.getStage(), "Datos Guardados de forma ");
            btnGuardar.setDisable(false);
        }
    }

    public void CargarDatosComboBox() {
        List<Integer> tiempo = new ArrayList<Integer>();
        tiempo.addAll(Arrays.asList(5, 10, 20, 25, 30));
        tiempo.forEach(tiem -> {
            cbxTiempoInactivo.getItems().add(tiem);
        });
        List<Short> vuelosHo = new ArrayList();
        for (Short i = 1; i < 13; i++) {
            Short q = i;
            vuelosHo.add(q);

        }
        vuelosHo.addAll(vuelosHo);
        vuelosHo.forEach(vuel -> {
            cbxVuelos.getItems().add(vuel);
        });
    }
}
