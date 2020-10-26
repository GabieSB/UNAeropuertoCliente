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
import java.util.stream.Collectors;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.util.Callback;
import org.una.unaeropuertoclient.model.AuthenticationResponse;
import org.una.unaeropuertoclient.model.GastoReparacionDto;
import org.una.unaeropuertoclient.model.NotificacionDto;
import org.una.unaeropuertoclient.model.RecolectorInfoNotas;
import org.una.unaeropuertoclient.model.ServicioMantenimientoDto;
import org.una.unaeropuertoclient.model.VueloDto;
import org.una.unaeropuertoclient.service.GastoReparacionService;
import org.una.unaeropuertoclient.service.NotificacionService;
import org.una.unaeropuertoclient.service.ServicioMantenimientoService;
import org.una.unaeropuertoclient.service.VueloService;
import org.una.unaeropuertoclient.utils.AppContext;
import org.una.unaeropuertoclient.utils.Mensaje;
import org.una.unaeropuertoclient.utils.Respuesta;

/**
 * FXML Controller class
 *
 * @author LordLalo
 */
public class AutorizarNotificacionesController extends Controller implements Initializable {

    @FXML
    public TableView<RecolectorInfoNotas> tbvNotificaciones;
    @FXML
    public JFXToggleButton togNotificacion;
    @FXML
    private TableColumn<RecolectorInfoNotas, String> tabId;
    @FXML
    private TableColumn<RecolectorInfoNotas, String> tabNumeroFactura;
    @FXML
    private TableColumn<RecolectorInfoNotas, String> tabTipoServicio;
    @FXML
    private TableColumn<RecolectorInfoNotas, String> tabFechaServicio;
    private List<NotificacionDto> listNotificaciones = new ArrayList<>();
    private final List<ServicioMantenimientoDto> listServicio = new ArrayList<>();
    private final List<GastoReparacionDto> listGastoReparacion = new ArrayList<>();
    private final List<VueloDto> listVuelo = new ArrayList<>();
    @FXML
    private TableColumn<RecolectorInfoNotas, String> tabMatricula;
    @FXML
    private TableColumn<RecolectorInfoNotas, Void> tabOpciones;
    @FXML
    private TableColumn<RecolectorInfoNotas, String> tabFechaSolicitud;
    @FXML
    private TableColumn<RecolectorInfoNotas, Boolean> tabEstado;
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

    private void cargarDatos(boolean estado, int numeroAerea) {
        listNotificaciones.clear();
        tbvNotificaciones.getItems().clear();
        Thread th = new Thread(() -> {
            NotificacionService notificacionService = new NotificacionService();
            Respuesta resp = notificacionService.getIDandEstado(numeroAerea, estado);
            Platform.runLater(() -> {
                if (resp.getEstado()) {
                    listNotificaciones = (List) resp.getResultado("data");
                    List<Long> idList = listNotificaciones.stream().map(nota -> nota.getIdObjeto()).collect(Collectors.toList());
                    switch (numeroAerea) {
                        case 1:
                            Respuesta vResp = new VueloService().findByIdUsingIdParam(idList);
                            if (vResp.getEstado()) {
                                ((List<VueloDto>) vResp.getResultado("data")).forEach(vuelo -> {
                                    listVuelo.add(vuelo);
                                    RecolectorInfoNotas colectorInfoNotas = new RecolectorInfoNotas(vuelo, listNotificaciones);
                                    tbvNotificaciones.getItems().add(colectorInfoNotas);
                                });
                            }
                            break;
                        case 2:
                            break;
                        default:
                            break;
                    }

//                    for (NotificacionDto notificacionDto : listNotificaciones) {
//                        if (n == 2) {
//                            ServicioMantenimientoService serv = new ServicioMantenimientoService();
//                            Respuesta r = serv.buscarPorID(notificacionDto.getIdObjeto().toString());
//                            ServicioMantenimientoDto servicioMantenimientoDto = (ServicioMantenimientoDto) r.getResultado("data");
//                            listServicio.add(servicioMantenimientoDto);
//                            RecolectorInfoNotas recolectarInformacionNotas = new RecolectorInfoNotas(notificacionDto.getId(), servicioMantenimientoDto.getAvionesId().getMatricula(), servicioMantenimientoDto.getFechaServicio().toString(), notificacionDto.getAreasId().toString(), servicioMantenimientoDto.getFechaServicio().toString(), servicioMantenimientoDto.getNumeroFactura().toString(), servicioMantenimientoDto.getTiposServiciosId().getNombre(), notificacionDto.isActivo());
//                            tbvNotificaciones.getItems().add(recolectarInformacionNotas);
//                        } else {
//                            if (n == 3) {
//                                GastoReparacionService gastoReparacionService = new GastoReparacionService();
//                                Respuesta r = gastoReparacionService.buscarPorID(notificacionDto.getIdObjeto().toString());
//                                GastoReparacionDto gasto = (GastoReparacionDto) r.getResultado("data");
//                                listGastoReparacion.add(gasto);
//                                RecolectorInfoNotas recolectarInformacionNotas = new RecolectorInfoNotas(notificacionDto.getId(), gasto.getNumeroContrato().toString(), notificacionDto.getFechaRegistro().toString(), notificacionDto.getAreasId().toString(), gasto.getFechaRegistro().toString(), gasto.getTiposId().getNombre(), gasto.getObservaciones(), notificacionDto.isActivo());
//                                tbvNotificaciones.getItems().add(recolectarInformacionNotas);
//                            } else {
//                                VueloService vueloService = new VueloService();
//                                Respuesta r = vueloService.buscarPorID(notificacionDto.getIdObjeto().toString());
//                                VueloDto vuelo = (VueloDto) r.getResultado("data");
//                                listVuelo.add(vuelo);
//                                RecolectorInfoNotas recolectarInformacionNotas = new RecolectorInfoNotas(notificacionDto.getId(), vuelo.getNombreVuelo(), notificacionDto.getFechaRegistro().toString(), notificacionDto.getAreasId().toString(), vuelo.getSitioYFechaLLegada(), vuelo.getAvionesId().getMatricula(), vuelo.getSitioYFechaSalida(), notificacionDto.isActivo());
//                                tbvNotificaciones.getItems().add(recolectarInformacionNotas);
//                            }
//                        }
//                    }
                    addButtonToTable();
                }
            });
        });
        th.start();
    }

    private void addButtonToTable() {
        Callback<TableColumn<RecolectorInfoNotas, Void>, TableCell<RecolectorInfoNotas, Void>> cellFactory = (final TableColumn<RecolectorInfoNotas, Void> param) -> {
            final TableCell<RecolectorInfoNotas, Void> cell = new TableCell<>() {
                public JFXButton acceptar = new JFXButton("Acceptar");

                {
                    acceptar.setOnAction((ActionEvent event) -> {
                        RecolectorInfoNotas recolectorInfoNotas = getTableView().getItems().get(getIndex());
                        acceptar(recolectorInfoNotas);
                    });
                }
                public JFXButton denegar = new JFXButton("Denegar");

                {
                    denegar.setStyle("-fx-background-color:#b71c1c;");
                    denegar.setOnAction((ActionEvent event) -> {
                        RecolectorInfoNotas recolectarInformacionNota = getTableView().getItems().get(getIndex());
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
        };
        tabOpciones.setCellFactory(cellFactory);
    }

    private void desactivar(RecolectorInfoNotas recolectarInformacionNota) {
        for (NotificacionDto notf : listNotificaciones) {

            if (notf.getId().equals(recolectarInformacionNota.getIdNota())) {
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

    private void acceptar(RecolectorInfoNotas colectorInfoNotas) {
        for (NotificacionDto notf : listNotificaciones) {
            if (numeroArea == 1) {
                if (notf.getId().equals(colectorInfoNotas.getIdNota())) {
                    notf.setActivo(false);
                    for (VueloDto se : listVuelo) {
                        if (notf.getIdObjeto().equals(se.getId())) {
                            se.setEstado((byte) 3);
                            Respuesta resp = new VueloService().update(se);
                            if (resp.getEstado()) {
                                Respuesta res = new NotificacionService().update(notf);
                                if (!res.getEstado()) {
                                    new Mensaje().showModal(Alert.AlertType.WARNING, "Atención", this.getStage(), res.getMensaje());
                                }
                            } else {
                                new Mensaje().showModal(Alert.AlertType.WARNING, "Atención", this.getStage(), resp.getMensaje());
                            }
                        }
                    }
                }
            }
            if (numeroArea == 2) {
                if (notf.getId().equals(colectorInfoNotas.getIdNota())) {
                    notf.setActivo(false);
                    for (ServicioMantenimientoDto se : listServicio) {
                        if (notf.getIdObjeto().equals(se.getId())) {
                            se.setActivo(false);
                            Respuesta resp = new ServicioMantenimientoService().update(se);
                            if (resp.getEstado()) {
                                Respuesta res = new NotificacionService().update(notf);
                                if (!res.getEstado()) {
                                    new Mensaje().showModal(Alert.AlertType.WARNING, "Atención", this.getStage(), res.getMensaje());
                                }
                            } else {
                                new Mensaje().showModal(Alert.AlertType.WARNING, "Atención", this.getStage(), resp.getMensaje());
                            }
                        }
                    }
                }
            }
            if (numeroArea == 3) {
                if (notf.getId().equals(colectorInfoNotas.getIdNota())) {
                    notf.setActivo(false);
                    for (GastoReparacionDto se : listGastoReparacion) {
                        if (notf.getIdObjeto().equals(se.getId())) {
                            se.setActivo(false);
                            Respuesta resp = new GastoReparacionService().update(se);
                            if (resp.getEstado()) {
                                Respuesta res = new NotificacionService().update(notf);
                                if (!res.getEstado()) {
                                    new Mensaje().showModal(Alert.AlertType.WARNING, "Atención", this.getStage(), res.getMensaje());
                                }
                            } else {
                                new Mensaje().showModal(Alert.AlertType.WARNING, "Atención", this.getStage(), resp.getMensaje());
                            }
                        }
                    }
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
