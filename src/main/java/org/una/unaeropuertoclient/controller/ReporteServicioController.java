/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.una.unaeropuertoclient.controller;

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
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.view.JasperViewer;
import org.una.unaeropuertoclient.model.TipoServicioDto;
import org.una.unaeropuertoclient.service.ReporteService;
import org.una.unaeropuertoclient.service.TipoReparacionService;
import org.una.unaeropuertoclient.service.TipoServicioService;
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
        listaServicios();
    }

    @FXML
    public void onActionGenerarReporte(ActionEvent event) {
        generarReporte();
    }

    private void generarReporte() {
        String fe = Timestamp.valueOf(LocalDateTime.of(dtpFechaInicio.getValue(), LocalTime.MIN)).toString();
        System.out.print(fe);
        ReporteService reporteService = new ReporteService();
        Respuesta resp = new Respuesta();
        resp = reporteService.getReporteServicio(dtpFechaInicio.getValue(), dtpFechaFinal.getValue(),cbxServicio.getValue());
        if (resp.getEstado()) {
            try {

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
            }
        }
    }

    void listaServicios() {
        tps.clear();
        cbxServicio.getItems().clear();
        TipoServicioService tipoServicioService = new TipoServicioService();
        Respuesta resp = tipoServicioService.getAll();
        if (resp.getEstado()) {

            tps = (List<TipoServicioDto>) resp.getResultado("data");
            for (TipoServicioDto t : tps) {
                cbxServicio.getItems().add(t.getNombre());
            }
        }

    }
}
