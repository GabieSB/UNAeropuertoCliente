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
import org.una.unaeropuertoclient.model.AuthenticationResponse;
import org.una.unaeropuertoclient.model.GastoReparacionDto;
import org.una.unaeropuertoclient.model.NotificacionDto;
import org.una.unaeropuertoclient.model.RecolectarInformacionNotas;
import org.una.unaeropuertoclient.model.ServicioMantenimientoDto;
import org.una.unaeropuertoclient.model.VueloDto;
import org.una.unaeropuertoclient.service.AvionService;
import org.una.unaeropuertoclient.service.GastoReparacionService;
import org.una.unaeropuertoclient.service.NotificacionService;
import org.una.unaeropuertoclient.service.ServicioMantenimientoService;
import org.una.unaeropuertoclient.service.VueloService;
import org.una.unaeropuertoclient.utils.AppContext;
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
    private List<GastoReparacionDto> listGastoReparacion = new ArrayList<>();
    private List<VueloDto> listVuelo = new ArrayList<>();
    @FXML
    private TableColumn<RecolectarInformacionNotas, String> tabMatricula;
    @FXML
    private TableColumn<RecolectarInformacionNotas, Void> tabOpciones;
    @FXML
    private TableColumn<RecolectarInformacionNotas, String> tabFechaSolicitud;
    @FXML
    private TableColumn<RecolectarInformacionNotas, Boolean> tabEstado;
    int numeroArea;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        AuthenticationResponse aux = (AuthenticationResponse) AppContext.getInstance().get("token");
        numeroArea = Integer.parseInt(aux.getUsuario().getAreasId().getId().toString());
        if (numeroArea == 3) {
            tabMatricula.setText("Nº Contrado");
            tabNumeroFactura.setText("Tipo");
            tabTipoServicio.setText("Observaciones");
        } else {
            if (numeroArea == 2) {
                tabMatricula.setText("Nombre Vuelo");
                tabNumeroFactura.setText("Matricula");
                tabTipoServicio.setText("Salida");
                tabFechaServicio.setText("Destino");
            }
        }
        tabId.setCellValueFactory(new PropertyValueFactory<>("idNota"));
        tabNumeroFactura.setCellValueFactory(new PropertyValueFactory<>("numeroFactura"));
        tabTipoServicio.setCellValueFactory(new PropertyValueFactory<>("tipoServicio"));
        tabFechaServicio.setCellValueFactory(new PropertyValueFactory<>("fechaServico"));
        tabMatricula.setCellValueFactory(new PropertyValueFactory<>("matricula"));
        tabEstado.setCellValueFactory(new PropertyValueFactory<>("estado"));
        tabFechaSolicitud.setCellValueFactory(new PropertyValueFactory<>("fecha"));
        if (togNotificacion.isSelected()) {
            cargarDatos(true, numeroArea);
        } else {
            cargarDatos(false, numeroArea);
        }
    }

    @Override
    public void initialize() {
        //  cargarDatos();
    }

    @FXML
    private void toggBuscar(ActionEvent event) {
        if (togNotificacion.isSelected()) {
            cargarDatos(true, numeroArea);
        } else {
            cargarDatos(false, numeroArea);
        }

    }

    private void cargarDatos(boolean estado, int n) {
        listNotificaciones.clear();
        tbvNotificaciones.getItems().clear();
        Thread th = new Thread(() -> {

            NotificacionService notificacionService = new NotificacionService();
            //       Respuesta resp = notificacionService.getNoficaciones(2);
            Respuesta resp = notificacionService.getIDandEstado(n, estado);
            Platform.runLater(() -> {
                if (resp != null) {
                    listNotificaciones = (List) resp.getResultado("data");
                    for (NotificacionDto notificacionDto : listNotificaciones) {
                        if (n == 2) {
                            ServicioMantenimientoService serv = new ServicioMantenimientoService();
                            Respuesta r = serv.buscarPorID(notificacionDto.getIdObjeto().toString());
                            ServicioMantenimientoDto servicioMantenimientoDto = (ServicioMantenimientoDto) r.getResultado("data");
                            listServicio.add(servicioMantenimientoDto);
                            RecolectarInformacionNotas recolectarInformacionNotas = new RecolectarInformacionNotas(notificacionDto.getId(), servicioMantenimientoDto.getAvionesId().getMatricula(), servicioMantenimientoDto.getFechaServicio().toString(), notificacionDto.getAreasId().toString(), servicioMantenimientoDto.getFechaServicio().toString(), servicioMantenimientoDto.getNumeroFactura().toString(), servicioMantenimientoDto.getTiposServiciosId().getNombre(), notificacionDto.isActivo());
                            tbvNotificaciones.getItems().add(recolectarInformacionNotas);
                        } else {
                            if (n == 3) {
                                GastoReparacionService gastoReparacionService = new GastoReparacionService();
                                Respuesta r = gastoReparacionService.buscarPorID(notificacionDto.getIdObjeto().toString());
                                GastoReparacionDto gasto = (GastoReparacionDto) r.getResultado("data");
                                listGastoReparacion.add(gasto);
                                RecolectarInformacionNotas recolectarInformacionNotas = new RecolectarInformacionNotas(notificacionDto.getId(), gasto.getNumeroContrato().toString(), notificacionDto.getFechaRegistro().toString(), notificacionDto.getAreasId().toString(), gasto.getFechaRegistro().toString(), gasto.getTiposId().getNombre(), gasto.getObservaciones(), notificacionDto.isActivo());
                                tbvNotificaciones.getItems().add(recolectarInformacionNotas);
                            } else {
                                VueloService vueloService = new VueloService();
                                Respuesta r = vueloService.buscarPorID(notificacionDto.getIdObjeto().toString());
                                VueloDto vuelo = (VueloDto) r.getResultado("data");
                                listVuelo.add(vuelo);
                                RecolectarInformacionNotas recolectarInformacionNotas = new RecolectarInformacionNotas(notificacionDto.getId(), vuelo.getNombreVuelo(), notificacionDto.getFechaRegistro().toString(), notificacionDto.getAreasId().toString(), vuelo.getSitioYFechaLLegada(), vuelo.getAvionesId().getMatricula(), vuelo.getSitioYFechaSalida(), notificacionDto.isActivo());
                                tbvNotificaciones.getItems().add(recolectarInformacionNotas);
                            }
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
        listGastoReparacion.clear();
        listServicio.clear();
        cargarDatos(true, numeroArea);
    }

    private void acceptar(RecolectarInformacionNotas recolectarInformacionNota) {
        if (numeroArea == 1) {
            for (NotificacionDto notf : listNotificaciones) {
                if (notf.getId() == recolectarInformacionNota.getIdNota()) {
                    NotificacionDto n = notf;
                    n.setActivo(false);
                    VueloService vueloService = new VueloService();
                    for (VueloDto se : listVuelo) {
                        if (n.getIdObjeto() == se.getId().longValue()) {
                            se.setEstado((byte) 3);
                            vueloService.update(se);
                        }
                    }
                    NotificacionService notificacionService = new NotificacionService();
                    Respuesta res = notificacionService.update(n);
                }
            }
        }
        for (NotificacionDto notf : listNotificaciones) {
            if (numeroArea == 2) {
                if (notf.getId() == recolectarInformacionNota.getIdNota()) {
                    NotificacionDto n = notf;
                    n.setActivo(false);
                    ServicioMantenimientoService serv = new ServicioMantenimientoService();
                    for (ServicioMantenimientoDto se : listServicio) {
                        if (n.getIdObjeto() == se.getId().longValue()) {
                            se.setActivo(false);
                            serv.update(se);
                        }
                    }
                    NotificacionService notificacionService = new NotificacionService();
                    Respuesta res = notificacionService.update(n);
                }
            }
        }
        if (numeroArea == 3) {
            for (NotificacionDto notf : listNotificaciones) {
                if (notf.getId() == recolectarInformacionNota.getIdNota()) {
                    NotificacionDto n = notf;
                    n.setActivo(false);
                    GastoReparacionService serv = new GastoReparacionService();
                    for (GastoReparacionDto se : listGastoReparacion) {
                        if (n.getIdObjeto() == se.getId().longValue()) {
                            se.setActivo(false);
                            serv.update(se);
                        }
                    }
                    NotificacionService notificacionService = new NotificacionService();
                    Respuesta res = notificacionService.update(n);
                }
            }
        }
        tbvNotificaciones.getItems().clear();
        listNotificaciones.clear();
        listGastoReparacion.clear();
        listServicio.clear();
        cargarDatos(true, numeroArea);
    }
}
