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
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.layout.HBox;
import org.una.unaeropuertoclient.model.ParamSistemaDto;
import org.una.unaeropuertoclient.service.ParamSistemaServicio;
import static org.una.unaeropuertoclient.utils.ButtonWaitUtils.aModoEspera;
import static org.una.unaeropuertoclient.utils.ButtonWaitUtils.salirModoEspera;
import org.una.unaeropuertoclient.utils.FlowController;
import org.una.unaeropuertoclient.utils.Formato;
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
    @FXML
    private HBox controlsBase;

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        txtFormat();
    }
    
    @Override
    public void initialize() {
        FlowController.changeSuperiorTittle("Parámetros de sistema");
        cbxTiempoInactivo.getItems().clear();
        cbxVuelos.getItems().clear();
        desactivarComponentes();
        cargarDatosComboBox();
        cargarDatos();
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
        aModoEspera(btnEditar);
        Thread th = new Thread(() -> {
            ParamSistemaServicio paramSistemaServicio = new ParamSistemaServicio();
            Respuesta respuesta = paramSistemaServicio.getById();
            Platform.runLater(() -> {
                salirModoEspera(btnEditar, "Editar");
                if (respuesta.getEstado()) {
                    paramSistemaDto = (ParamSistemaDto) respuesta.getResultado("data");
                    txtRepresentante.setText(paramSistemaDto.getNombreRepresentante());
                    txtCorreo.setText(paramSistemaDto.getEmailAeropuerto());
                    txtTelefono.setText(paramSistemaDto.getTelefonoAeropuerto());
                    LocalTime ltApertura = LocalTime.of(paramSistemaDto.getAperturaOficina().getHours(), paramSistemaDto.getAperturaOficina().getMinutes(), paramSistemaDto.getAperturaOficina().getSeconds());
                    LocalTime ltCiere = LocalTime.of(paramSistemaDto.getCierreOficina().getHours(), paramSistemaDto.getCierreOficina().getMinutes(), paramSistemaDto.getCierreOficina().getSeconds());
                    tpkApertura.setValue(ltApertura);
                    tpkCierre.setValue(ltCiere);
                    cbxTiempoInactivo.setValue(paramSistemaDto.getTiempoInactividad());
                    cbxVuelos.setValue(paramSistemaDto.getVuelosHora());
                } else {
                    new Mensaje().showModal(Alert.AlertType.ERROR, "Falla al extraer los parametros del sistema", this.getStage(), respuesta.getMensaje());
                }
            });
        });
        th.start();
    }
    
    public void desactivarComponentes() {
        txtCorreo.setEditable(false);
        txtRepresentante.setEditable(false);
        txtTelefono.setEditable(false);
        cbxTiempoInactivo.setDisable(true);
        cbxVuelos.setDisable(true);
        tpkApertura.setDisable(true);
        tpkCierre.setDisable(true);
        btnGuardar.setDisable(true);
    }
    
    public void ActivarComponentes() {
        txtCorreo.setEditable(true);
        txtRepresentante.setEditable(true);
        txtTelefono.setEditable(true);
        cbxTiempoInactivo.setDisable(false);
        cbxVuelos.setDisable(false);
        tpkApertura.setDisable(false);
        tpkCierre.setDisable(false);
        btnGuardar.setDisable(false);
    }
    
    @FXML
    public void onActionGuardar(ActionEvent event) {
        if (camposTextLlenos() && combBoxYPikersLlenos()) {
            ParamSistemaDto prm = new ParamSistemaDto();
            cargarDatos(prm);
            controlsBase.setDisable(true);
            aModoEspera(btnGuardar);
            save(prm);
        }
        
    }
    
    private void save(ParamSistemaDto prm) {
        Thread th = new Thread(() -> {
            Respuesta res = new ParamSistemaServicio().update(prm);
            Platform.runLater(() -> {
                controlsBase.setDisable(false);
                salirModoEspera(btnGuardar, "Guardar");
                if (res.getEstado()) {
                    new Mensaje().showModal(Alert.AlertType.INFORMATION, "Correcto", this.getStage(), "Los parámetros fueron actualizados exitosamente");
                    btnGuardar.setDisable(false);
                } else {
                    new Mensaje().showModal(Alert.AlertType.ERROR, "Error", this.getStage(), res.getMensaje());
                }
            });
        });
        th.start();
    }
    
    private void cargarDatos(ParamSistemaDto p) {
        LocalDateTime ltApertura = LocalDateTime.of(LocalDate.now(), tpkApertura.getValue());
        LocalDateTime ltCierre = LocalDateTime.of(LocalDate.now(), tpkCierre.getValue());
        p.setAperturaOficina(Timestamp.valueOf(ltApertura));
        p.setCierreOficina(Timestamp.valueOf(ltCierre));
        p.setTelefonoAeropuerto(txtTelefono.getText());
        p.setEmailAeropuerto(txtCorreo.getText());
        p.setNombreRepresentante(txtRepresentante.getText());
        p.setVuelosHora(cbxVuelos.getValue());
        p.setTiempoInactividad(cbxTiempoInactivo.getValue());
        p.setId(1);
        p.setUbicacion(paramSistemaDto.getUbicacion());
    }
    
    public void cargarDatosComboBox() {
        cbxTiempoInactivo.getItems().addAll(5, 10, 20, 25, 30);
        Short timeList[] = {4, 5, 6, 10, 12};
        cbxVuelos.getItems().addAll(timeList);
    }
    
    public boolean camposTextLlenos() {
        if (txtCorreo.getText().isBlank() || txtRepresentante.getText().isBlank() || txtTelefono.getText().isBlank()) {
            new Mensaje().show(Alert.AlertType.WARNING, "Quizá olvidas algo", "Revisa los campos de texto, al menos uno se encuentra vacío");
            return false;
        }
        return true;
    }
    
    public boolean combBoxYPikersLlenos() {
        if (tpkApertura.getValue() == null || tpkCierre.getValue() == null) {
            new Mensaje().show(Alert.AlertType.WARNING, "Quizá olvidas algo", "Revisa los campos de hora, al menos uno se encuentra vacío");
            return false;
        } else if (cbxTiempoInactivo.getValue() == null || cbxVuelos.getValue() == null) {
            new Mensaje().show(Alert.AlertType.WARNING, "Quizá olvidas algo", "Revisa los campos desplegables, al menos uno se encuentra vacío");
            return false;
        }
        return true;
    }
    
    private void txtFormat() {
        txtCorreo.setTextFormatter(Formato.getInstance().maxLengthFormat(50));
        txtRepresentante.setTextFormatter(Formato.getInstance().maxLengthFormat(45));
        txtTelefono.setTextFormatter(Formato.getInstance().maxLengthFormat(25));
    }
}
