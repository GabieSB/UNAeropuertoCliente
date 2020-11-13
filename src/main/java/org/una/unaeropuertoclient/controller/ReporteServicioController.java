/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.una.unaeropuertoclient.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDatePicker;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.net.URL;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.view.JasperViewer;
import org.una.unaeropuertoclient.model.TipoServicioDto;
import org.una.unaeropuertoclient.service.ReporteService;
import org.una.unaeropuertoclient.service.TipoReparacionService;
import org.una.unaeropuertoclient.service.TipoServicioService;
import org.una.unaeropuertoclient.utils.ButtonWaitUtils;
import org.una.unaeropuertoclient.utils.FlowController;
import org.una.unaeropuertoclient.utils.Mensaje;
import org.una.unaeropuertoclient.utils.Respuesta;

/**
 * FXML Controller class
 *
 * @author LordLalo
 */
public class ReporteServicioController extends Controller implements Initializable {

    @FXML
    public JFXComboBox<String> cbxServicio;
    @FXML
    public JFXDatePicker dtpFechaInicio;
    @FXML
    public JFXDatePicker dtpFechaFinal;
    List<TipoServicioDto> tps = new ArrayList<>();
    @FXML
    public JFXButton btnGenerar;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        listaServicios();
    }

    @Override
    public void initialize() {
        FlowController.changeSuperiorTittle("Reportes Servicios");
        FlowController.changeCodeScreenTittle("SR300");
        listaServicios();
    }

    @FXML
    public void onActionGenerarReporte(ActionEvent event) {
        if (cbxServicio.getValue() != null && dtpFechaInicio.getValue() != null && dtpFechaFinal.getValue() != null) {
            generarReporte();
        } else {
            crearMensaje();
        }

    }

    private void crearMensaje() {
        Mensaje mensaje = new Mensaje();
        mensaje.show(Alert.AlertType.WARNING, "Informacion Incompleta", "Se debe se completar la información de todos los campos");
    }

    private void crearMensajeConnexionFallida() {
        Mensaje mensaje = new Mensaje();
        mensaje.show(Alert.AlertType.WARNING, "Informacion Incompleta", "Problemas con la conexión");
    }

    private void generarReporte() {
        ButtonWaitUtils.aModoEspera(btnGenerar);
        Thread th = new Thread(() -> {

            ReporteService reporteService = new ReporteService();
            Respuesta resp = reporteService.getReporteServicio(dtpFechaInicio.getValue(), dtpFechaFinal.getValue(), cbxServicio.getValue());
            Platform.runLater(() -> {
                if (resp.getEstado()) {
                    try {
                        ButtonWaitUtils.salirModoEspera(btnGenerar, "Generar");
                        String r = (String) resp.getResultado("data");
                        byte[] base64 = Base64.getDecoder().decode(r);
                        InputStream in = new ByteArrayInputStream(base64);
                        ObjectInputStream obin = new ObjectInputStream(in);
                        JasperPrint jasperPrint = new JasperPrint();
                        jasperPrint = (JasperPrint) obin.readObject();
                        JasperViewer j = new JasperViewer(jasperPrint, false);
                        j.setTitle("Reporte Servicio");
                        j.setVisible(true);
                        j.show();
                    } catch (Exception e) {
                        crearMensajeConnexionFallida();
                    }
                } else {
                    crearMensajeConnexionFallida();
                }
            });
        });
        th.start();
    }

    void listaServicios() {
        tps.clear();
        cbxServicio.getItems().clear();
        Thread th = new Thread(() -> {
            TipoServicioService tipoServicioService = new TipoServicioService();
            Respuesta resp = tipoServicioService.getAll();
            Platform.runLater(() -> {
                if (resp.getEstado()) {
                    tps = (List<TipoServicioDto>) resp.getResultado("data");
                    for (TipoServicioDto t : tps) {
                        cbxServicio.getItems().add(t.getNombre());
                    }
                }
            });
        });
        th.start();
    }
}
