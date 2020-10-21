/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.una.unaeropuertoclient.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXToggleButton;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import org.una.unaeropuertoclient.model.NotificacionDto;
import org.una.unaeropuertoclient.model.RecolectarInformacionNotas;
import org.una.unaeropuertoclient.model.ServicioMantenimientoDto;
import org.una.unaeropuertoclient.service.NotificacionService;
import org.una.unaeropuertoclient.service.ServicioMantenimientoService;
import org.una.unaeropuertoclient.utils.Respuesta;

/**
 * FXML Controller class
 *
 * @author LordLalo
 */
public class AutorizarNotificacionesController extends Controller implements Initializable {

    @FXML
    public TableView<RecolectarInformacionNotas> tbvNotificaciones;

    @FXML
    public JFXToggleButton togNotificacion;
    @FXML
    private TableColumn<RecolectarInformacionNotas, String> tabId;
    @FXML
    private TableColumn<RecolectarInformacionNotas, String> tabNumeroFactura;
    @FXML
    private TableColumn<RecolectarInformacionNotas, String> tabTipoServicio;
    @FXML
    private TableColumn<RecolectarInformacionNotas, String> tabFechaServicio;
    private List<NotificacionDto> listNotificaciones = new ArrayList<>();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        tabId.setCellValueFactory(new PropertyValueFactory<>("idNota"));
        tabNumeroFactura.setCellValueFactory(new PropertyValueFactory<>("numeroFactura"));
        tabTipoServicio.setCellValueFactory(new PropertyValueFactory<>("matricula"));
        tabFechaServicio.setCellValueFactory(new PropertyValueFactory<>("fechaServico") );
    }

    @Override
    public void initialize() {
    }

    @FXML
    private void toggBuscar(ActionEvent event) {
        NotificacionService notificacionService = new NotificacionService();
        Respuesta resp = notificacionService.getNoficaciones(2);
        if (resp != null) {
            listNotificaciones = (List) resp.getResultado("data");
            for (NotificacionDto no : listNotificaciones) {
                ServicioMantenimientoService serv = new ServicioMantenimientoService();
                Respuesta r = serv.buscarPorID(no.getIdObjeto().toString());
                ServicioMantenimientoDto s = (ServicioMantenimientoDto) r.getResultado("data");
                RecolectarInformacionNotas recolectarInformacionNotas = new RecolectarInformacionNotas(no.getId().toString(), s.getTiposServiciosId().getNombre(), no.getFechaRegistro().toString(), no.getAreasId().getNombre(), s.getFechaServicio().toString(), s.getNumeroFactura().toString());
                tbvNotificaciones.getItems().add(recolectarInformacionNotas);
            }
        }

    }
}
