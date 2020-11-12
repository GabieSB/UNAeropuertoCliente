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
import org.una.unaeropuertoclient.model.AerolineaDto;
import org.una.unaeropuertoclient.model.TipoVueloDto;
import org.una.unaeropuertoclient.service.AerolineaService;
import org.una.unaeropuertoclient.service.ReporteService;
import org.una.unaeropuertoclient.service.TipoVueloService;
import org.una.unaeropuertoclient.utils.ButtonWaitUtils;
import static org.una.unaeropuertoclient.utils.ButtonWaitUtils.salirModoEspera;
import org.una.unaeropuertoclient.utils.Mensaje;
import org.una.unaeropuertoclient.utils.Respuesta;

/**
 * FXML Controller class
 *
 * @author LordLalo
 */
public class VueloReporteController extends Controller implements Initializable {

    @FXML
    public JFXDatePicker dtpFechaInicio;
    @FXML
    public JFXDatePicker dtpFechaFinal;
    @FXML
    public JFXComboBox<String> cbxAerolinea;
    @FXML
    public JFXComboBox<String> cbxTipo;
    @FXML
    private JFXButton btnGenerar;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        llenarAerolines();
        llenarTipoVuelo();
    }

    @Override
    public void initialize() {

    }

    @FXML
    public void generarReporte(ActionEvent event) {
        if (dtpFechaInicio.getValue() != null && dtpFechaFinal.getValue() != null && cbxAerolinea.getValue() != null && cbxTipo.getValue() != null) {
            crearReporte();
        } else {
            crearMensaje();
        }
    }

    private void crearMensaje() {
        Mensaje mensaje = new Mensaje();
        mensaje.show(Alert.AlertType.WARNING, "Informacion Incompleta", "Se debe se completar la información de todos los campos");
    }

    private void crearReporte() {
        ButtonWaitUtils.aModoEspera(btnGenerar);
        Thread th = new Thread(() -> {

            ReporteService reporS = new ReporteService();
            Respuesta respuesta = reporS.getReporteVuelo(dtpFechaInicio.getValue(), dtpFechaFinal.getValue(), cbxAerolinea.getValue(), cbxTipo.getValue());
            Platform.runLater(() -> {
                salirModoEspera(btnGenerar, "Generar");
                if (respuesta.getEstado()) {
                    try {
                        String r = (String) respuesta.getResultado("data");
                        byte[] base64 = Base64.getDecoder().decode(r);
                        InputStream in = new ByteArrayInputStream(base64);
                        ObjectInputStream obin = new ObjectInputStream(in);
                        JasperPrint jasperPrint = new JasperPrint();
                        jasperPrint = (JasperPrint) obin.readObject();
                        JasperViewer j = new JasperViewer(jasperPrint, false);
                        j.setTitle("Reporte Vuelo");
                        j.setVisible(true);
                        j.show();
                    } catch (Exception e) {
                        System.err.println(e);
                    }
                }else{
                    new Mensaje().show(Alert.AlertType.WARNING, "Atención", respuesta.getMensaje());
                }
            });
        });
        th.start();
    }

    private void llenarAerolines() {
        cbxAerolinea.getItems().clear();
        Respuesta respuesta = new Respuesta();
        AerolineaService aerolineaService = new AerolineaService();
        respuesta = aerolineaService.findByEstado(true);
        List<AerolineaDto> listAerolinea = new ArrayList<>();
        if (respuesta.getEstado()) {
            listAerolinea = (List<AerolineaDto>) respuesta.getResultado("data");
            for (AerolineaDto aerolineaDto : listAerolinea) {
                cbxAerolinea.getItems().add(aerolineaDto.getNombre());
            }
        }
    }

    private void llenarTipoVuelo() {
        cbxTipo.getItems().clear();
        Respuesta respuesta = new Respuesta();
        TipoVueloService tipoVuelo = new TipoVueloService();
        respuesta = tipoVuelo.findByEstado(true);
        List<TipoVueloDto> listaTipoVuelo = new ArrayList<>();
        if (respuesta.getEstado()) {
            listaTipoVuelo = (List<TipoVueloDto>) respuesta.getResultado("data");
            for (TipoVueloDto tipoV : listaTipoVuelo) {
                cbxTipo.getItems().add(tipoV.getNombre());
            }

        }

    }

}
