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
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.view.JasperViewer;
import org.una.unaeropuertoclient.model.TipoReparacionDto;
import org.una.unaeropuertoclient.service.ReporteService;
import org.una.unaeropuertoclient.service.TipoReparacionService;
import org.una.unaeropuertoclient.utils.ButtonWaitUtils;
import org.una.unaeropuertoclient.utils.FlowController;
import org.una.unaeropuertoclient.utils.Mensaje;
import org.una.unaeropuertoclient.utils.Respuesta;

/**
 * FXML Controller class
 *
 * @author LordLalo
 */
public class DepartamentoReportController extends Controller implements Initializable {

    @FXML
    public JFXDatePicker dtpFechaFinal;
    @FXML
    public JFXDatePicker dtpFechaInicio;
    @FXML
    public JFXComboBox<String> cbxMantenimiento;
    @FXML
    private JFXButton btnGenerar;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        cargarServiciosMantenimiento();
    }

    @Override
    public void initialize() {
        FlowController.changeSuperiorTittle("Reporte Mantenimiento");
        FlowController.changeCodeScreenTittle("MR300");
        cargarServiciosMantenimiento();
    }

    @FXML
    public void generarReporte(ActionEvent event) {
        if (dtpFechaInicio.getValue() != null && dtpFechaFinal.getValue() != null && cbxMantenimiento.getValue() != null) {
            crearReporte();
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

    public void crearReporte() {
        ButtonWaitUtils.aModoEspera(btnGenerar);
        Thread th = new Thread(() -> {
            ReporteService reporteService = new ReporteService();
            Respuesta resp = reporteService.getReporteDepartamento(dtpFechaInicio.getValue(), dtpFechaFinal.getValue(), cbxMantenimiento.getValue());
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
                        System.err.println(e);
                        crearMensajeConnexionFallida();
                    }
                } else {
                    crearMensajeConnexionFallida();
                }
            });
        });
        th.start();
    }

    public void cargarServiciosMantenimiento() {
        cbxMantenimiento.getItems().clear();
        Thread th = new Thread(() -> {

            TipoReparacionService tpr = new TipoReparacionService();
            Respuesta respuesta = tpr.getAll();
            Platform.runLater(() -> {
                if (respuesta.getEstado()) {
                    List<TipoReparacionDto> listTipoServicio = new ArrayList<>();
                    listTipoServicio = (List<TipoReparacionDto>) respuesta.getResultado("data");
                    for (TipoReparacionDto tp : listTipoServicio) {
                        cbxMantenimiento.getItems().add(tp.getNombre());
                    }
                }
            });
        });
        th.start();
    }
}
