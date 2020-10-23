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
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.util.Callback;
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
    private List<ServicioMantenimientoDto> listServicio = new ArrayList<>();
    @FXML
    private TableColumn<RecolectarInformacionNotas, String> tabMatricula;
    @FXML
    private TableColumn<RecolectarInformacionNotas, Void> tabOpciones;
    @FXML
    private TableColumn<RecolectarInformacionNotas, String> tabFechaSolicitud;
    @FXML
    private TableColumn<RecolectarInformacionNotas, Boolean> tabEstado;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        String tab;
        int n=2;
        if(n==2)
        {
         tab="numeroFactura";
        }else{
          tab="numeroContrato";
        }
        tabId.setCellValueFactory(new PropertyValueFactory<>("idNota"));
        tabNumeroFactura.setCellValueFactory(new PropertyValueFactory<>(tab));
        tabTipoServicio.setCellValueFactory(new PropertyValueFactory<>("tipoServicio"));
        tabFechaServicio.setCellValueFactory(new PropertyValueFactory<>("fechaServico"));
        tabMatricula.setCellValueFactory(new PropertyValueFactory<>("matricula"));
        tabEstado.setCellValueFactory(new PropertyValueFactory<>("estado"));
        tabFechaSolicitud.setCellValueFactory(new PropertyValueFactory<>("fecha"));
        if (togNotificacion.isSelected()) {
            cargarDatos(true,n);
        } else {
            cargarDatos(false,n);
        }
    }

    @Override
    public void initialize() {
        //  cargarDatos();
    }

    @FXML
    private void toggBuscar(ActionEvent event) {
        if (togNotificacion.isSelected()) {
            cargarDatos(true,2);
        } else {
            cargarDatos(false,2);
        }

    }

    private void cargarDatos(boolean estado,int n) {
        listNotificaciones.clear();
        tbvNotificaciones.getItems().clear();
        Thread th = new Thread(() -> {

            NotificacionService notificacionService = new NotificacionService();
            //       Respuesta resp = notificacionService.getNoficaciones(2);
            Respuesta resp = notificacionService.getIDandEstado(2, estado);
            Platform.runLater(() -> {
                if (resp != null) {
                    listNotificaciones = (List) resp.getResultado("data");
                    for (NotificacionDto notificacionDto : listNotificaciones) {
                        ServicioMantenimientoService serv = new ServicioMantenimientoService();
                        Respuesta r = serv.buscarPorID(notificacionDto.getIdObjeto().toString());
                        if(n==2){
                        ServicioMantenimientoDto servicioMantenimientoDto = (ServicioMantenimientoDto) r.getResultado("data");
                        listServicio.add(servicioMantenimientoDto);
                        RecolectarInformacionNotas recolectarInformacionNotas = new RecolectarInformacionNotas(notificacionDto.getId(), servicioMantenimientoDto.getAvionesId().getMatricula(), servicioMantenimientoDto.getFechaServicio().toString(), notificacionDto.getAreasId().toString(), servicioMantenimientoDto.getFechaServicio().toString(), servicioMantenimientoDto.getNumeroFactura().toString(), servicioMantenimientoDto.getTiposServiciosId().getNombre(), notificacionDto.isActivo());
                        tbvNotificaciones.getItems().add(recolectarInformacionNotas);
                        }
                    }
                    addButtonToTable();
                }
            });
        });
        th.start();

    }

    private void addButtonToTable() {

        Callback<TableColumn<RecolectarInformacionNotas, Void>, TableCell<RecolectarInformacionNotas, Void>> cellFactory = new Callback<>() {
            @Override
            public TableCell<RecolectarInformacionNotas, Void> call(final TableColumn<RecolectarInformacionNotas, Void> param) {
                final TableCell<RecolectarInformacionNotas, Void> cell = new TableCell<>() {

                    public JFXButton acceptar = new JFXButton("Acceptar");

                    {
                        acceptar.setOnAction((ActionEvent event) -> {
                            RecolectarInformacionNotas recolectarInformacionNotas = getTableView().getItems().get(getIndex());
                            acceptar(recolectarInformacionNotas);

                        });
                    }
                    public JFXButton denegar = new JFXButton("Denegar");

                    {
                        denegar.setStyle("-fx-background-color:#b71c1c;");
                        denegar.setOnAction((ActionEvent event) -> {
                            RecolectarInformacionNotas recolectarInformacionNota = getTableView().getItems().get(getIndex());
                            desactivar(recolectarInformacionNota);
                        });
                    }

                    public HBox v = new HBox(acceptar, denegar);

                    {
                        v.setSpacing(2);
                    }

                    @Override
                    public void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            setGraphic(v);
                        }
                    }

                };
                return cell;
            }
        };

        tabOpciones.setCellFactory(cellFactory);

        // tableResultados.getColumns().add(columAcciones);
    }

    private void desactivar(RecolectarInformacionNotas recolectarInformacionNota) {
        for (NotificacionDto notf : listNotificaciones) {

            if (notf.getId() == recolectarInformacionNota.getIdNota()) {
                NotificacionDto n = notf;
                n.setActivo(false);
                ServicioMantenimientoService serv = new ServicioMantenimientoService();
                NotificacionService notificacionService = new NotificacionService();
                Respuesta res = notificacionService.update(n);
            }
        }
        tbvNotificaciones.getItems().clear();
        listNotificaciones.clear();
        cargarDatos(true,2);
    }

    private void acceptar(RecolectarInformacionNotas recolectarInformacionNota) {

        for (NotificacionDto notf : listNotificaciones) {
            if (notf.getId() == recolectarInformacionNota.getIdNota()) {
                NotificacionDto n = notf;
                n.setActivo(false);
                ServicioMantenimientoService serv = new ServicioMantenimientoService();
                for (ServicioMantenimientoDto se : listServicio) {
                    if (n.getIdObjeto()== se.getId().longValue()) {
                        se.setActivo(false);
                        serv.update(se);
                    }
                }
                NotificacionService notificacionService = new NotificacionService();
                Respuesta res = notificacionService.update(n);
            }
        }
        tbvNotificaciones.getItems().clear();
        listNotificaciones.clear();
        cargarDatos(true,2);
    }
}
