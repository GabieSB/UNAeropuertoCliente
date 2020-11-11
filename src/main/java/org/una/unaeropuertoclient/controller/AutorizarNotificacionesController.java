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
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
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
import static org.una.unaeropuertoclient.utils.ButtonWaitUtils.aModoEspera;
import static org.una.unaeropuertoclient.utils.ButtonWaitUtils.salirModoEspera;
import org.una.unaeropuertoclient.utils.FlowController;
import org.una.unaeropuertoclient.utils.Mensaje;
import org.una.unaeropuertoclient.utils.Respuesta;

/**
 * FXML Controller class
 *
 * @author roberth :)
 */
public class AutorizarNotificacionesController extends Controller implements Initializable {

    @FXML
    private Label lblTableTittle;
    @FXML
    public TableView<RecolectorInfoNotas> tbvNotificaciones;
    @FXML
    public JFXToggleButton togNotificacion;
    @FXML
    private TableColumn<RecolectorInfoNotas, String> columnId;
    @FXML
    private TableColumn<RecolectorInfoNotas, String> clNumber2;
    @FXML
    private TableColumn<RecolectorInfoNotas, String> clNumer3;
    @FXML
    private TableColumn<RecolectorInfoNotas, String> clNumber4;
    @FXML
    private TableColumn<RecolectorInfoNotas, String> clNumber1;
    @FXML
    private TableColumn<RecolectorInfoNotas, Void> tabOpciones;
    @FXML
    private TableColumn<RecolectorInfoNotas, String> tabFechaSolicitud;
    @FXML
    private TableColumn<RecolectorInfoNotas, String> tabEstado;
    private long numeroArea;
    private List<NotificacionDto> listNotificaciones = new ArrayList<>();
    private final List<ServicioMantenimientoDto> listServicio = new ArrayList<>();
    private final List<GastoReparacionDto> listGastoReparacion = new ArrayList<>();
    private final List<VueloDto> listVuelo = new ArrayList<>();
    private int accesMode;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        tbvNotificaciones.setPlaceholder(new Label("Estás al día, no hay nuevas solicitudes de inhabilitación que mostrar"));
    }

    @Override
    public void initialize() {
        accesMode = (int) AppContext.getInstance().get("mode");
        AuthenticationResponse aux = (AuthenticationResponse) AppContext.getInstance().get("token");
        numeroArea = aux.getUsuario().getAreasId().getId();
        FlowController.changeSuperiorTittle("Notificaciones (peticiones de inhabilitación)");
        FlowController.changeCodeScreenTittle("NG200");
        prepareTable();
        togNotificacion.setDisable(accesMode != 2);
        togNotificacion.setSelected(true);
        toggBuscar(new ActionEvent());
    }

    @FXML
    private void toggBuscar(ActionEvent event) {
        if (accesMode == 2) {
            if (togNotificacion.isSelected()) {
                cargarDatos(true);
            } else {
                cargarDatos(false);
            }
        }
    }

    private void prepareTable() {
        if (numeroArea == 1) {
            clNumber1.setText("Vuelo");
            clNumber2.setText("Avión");
            clNumer3.setText("Salida");
            clNumber4.setText("Destino");
            clNumber1.prefWidthProperty().bind(tbvNotificaciones.widthProperty().divide(10));
            clNumber2.prefWidthProperty().bind(tbvNotificaciones.widthProperty().divide(10));
            clNumer3.prefWidthProperty().bind(tbvNotificaciones.widthProperty().divide(4.5));
            clNumber4.prefWidthProperty().bind(tbvNotificaciones.widthProperty().divide(4.5));
        } else if (numeroArea == 2) {
            clNumber1.setText("Matrícula");
            clNumber2.setText("N° factura");
            clNumer3.setText("Tipo de servicio");
            clNumber4.setText("Fecha de servico");
            clNumber1.prefWidthProperty().bind(tbvNotificaciones.widthProperty().divide(12));
            clNumber2.prefWidthProperty().bind(tbvNotificaciones.widthProperty().divide(12));
            clNumer3.prefWidthProperty().bind(tbvNotificaciones.widthProperty().divide(6));
            clNumber4.prefWidthProperty().bind(tbvNotificaciones.widthProperty().divide(5));
        } else {
            clNumber1.setText("Nº contrato");
            clNumber2.setText("Tipo de reparación");
            clNumer3.setText("Observaciones");
            clNumber4.setText("Fecha registro reparación");
            clNumber1.prefWidthProperty().bind(tbvNotificaciones.widthProperty().divide(12));
            clNumber2.prefWidthProperty().bind(tbvNotificaciones.widthProperty().divide(6));
            clNumer3.prefWidthProperty().bind(tbvNotificaciones.widthProperty().divide(4));
            clNumber4.prefWidthProperty().bind(tbvNotificaciones.widthProperty().divide(8));
        }
        columnId.prefWidthProperty().bind(tbvNotificaciones.widthProperty().divide(20));
        tabOpciones.prefWidthProperty().bind(tbvNotificaciones.widthProperty().divide(8));
        tabEstado.prefWidthProperty().bind(tbvNotificaciones.widthProperty().divide(18));
        tabFechaSolicitud.prefWidthProperty().bind(tbvNotificaciones.widthProperty().divide(8));
        columnId.setCellValueFactory(new PropertyValueFactory("idNota"));
        clNumber2.setCellValueFactory(new PropertyValueFactory("numeroFactura"));
        clNumer3.setCellValueFactory(new PropertyValueFactory("tipoServicio"));
        clNumber4.setCellValueFactory(new PropertyValueFactory("fechaServico"));
        clNumber1.setCellValueFactory(new PropertyValueFactory("matricula"));
        tabEstado.setCellValueFactory(x -> new SimpleStringProperty(x.getValue().getEstadoEnFormatoHumano()));
        tabFechaSolicitud.setCellValueFactory(new PropertyValueFactory("fecha"));
    }

    private void cargarDatos(boolean estado) {
        listNotificaciones.clear();
        tbvNotificaciones.getItems().clear();
        lblTableTittle.setText(estado ? "Mostrando solo notificaciones pendientes" : "Mostrando solo notificaciones atendidas");
        aModoEspera(togNotificacion);
        Thread th = new Thread(() -> {
            NotificacionService notificacionService = new NotificacionService();
            Respuesta resp = notificacionService.getIDandEstado(numeroArea, estado);
            Platform.runLater(() -> {
                salirModoEspera(togNotificacion, "");
                if (resp.getEstado()) {
                    listNotificaciones = (List) resp.getResultado("data");
                    List<Long> idList = listNotificaciones.stream().map(nota -> nota.getIdObjeto()).collect(Collectors.toList());
                    if (numeroArea == 1) {
                        Respuesta vResp = new VueloService().findByIdUsingIdParam(idList);
                        if (vResp.getEstado()) {
                            ((List<VueloDto>) vResp.getResultado("data")).forEach(vuelo -> {
                                listVuelo.add(vuelo);
                                RecolectorInfoNotas colectorInfoNotas = new RecolectorInfoNotas(vuelo, listNotificaciones);
                                tbvNotificaciones.getItems().add(colectorInfoNotas);
                            });
                        }
                    } else if (numeroArea == 2) {
                        Respuesta vResp = new ServicioMantenimientoService().findByIdUsingIdParam(idList);
                        if (vResp.getEstado()) {
                            ((List<ServicioMantenimientoDto>) vResp.getResultado("data")).forEach(servM -> {
                                listServicio.add(servM);
                                RecolectorInfoNotas colectorInfoNotas = new RecolectorInfoNotas(servM, listNotificaciones);
                                tbvNotificaciones.getItems().add(colectorInfoNotas);
                            });
                        }
                    } else {
                        Respuesta vResp = new GastoReparacionService().findByIdUsingListParam(idList);
                        if (vResp.getEstado()) {
                            ((List<GastoReparacionDto>) vResp.getResultado("data")).forEach(gastoR -> {
                                listGastoReparacion.add(gastoR);
                                RecolectorInfoNotas colectorInfoNotas = new RecolectorInfoNotas(gastoR, listNotificaciones);
                                tbvNotificaciones.getItems().add(colectorInfoNotas);
                            });
                        }
                    }
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
                    acceptar.setDisable(accesMode != 2);
                    acceptar.setOnAction((ActionEvent event) -> {
                        RecolectorInfoNotas recolectorInfoNotas = getTableView().getItems().get(getIndex());
                        if (recolectorInfoNotas.isEstado()) {
                            acceptar(recolectorInfoNotas);
                        } else {
                            new Mensaje().show(Alert.AlertType.WARNING, "No permitido",
                                    "Esta acción no está permitida en notificaciones que ya han sido atendidas");
                        }

                    });
                }
                public JFXButton denegar = new JFXButton("Denegar");

                {
                    denegar.setDisable(accesMode != 2);
                    denegar.setId("dangerous-button-efect");
                    denegar.setOnAction((ActionEvent event) -> {
                        RecolectorInfoNotas recolectorInfoNotas = getTableView().getItems().get(getIndex());
                        if (recolectorInfoNotas.isEstado()) {
                            desactivar(recolectorInfoNotas);
                        } else {
                            new Mensaje().show(Alert.AlertType.WARNING, "No permitido",
                                    "Esta acción no está permitida en notificaciones que ya han sido atendidas");
                        }
                    });
                }
                public HBox hb = new HBox(acceptar, denegar);

                {
                    hb.setSpacing(2);
                    hb.setAlignment(Pos.CENTER);
                }

                @Override
                public void updateItem(Void item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty) {
                        setGraphic(null);
                    } else {
                        setGraphic(hb);
                    }
                }
            };
            return cell;
        };
        tabOpciones.setCellFactory(cellFactory);
    }

    private void desactivar(RecolectorInfoNotas recolectarInformacionNota) {
        for (NotificacionDto notifi : listNotificaciones) {
            if (notifi.getId().equals(recolectarInformacionNota.getIdNota())) {
                notifi.setActivo(false);
                Respuesta res = new NotificacionService().update(notifi);
                if (res.getEstado()) {
                    tbvNotificaciones.getItems().clear();
                    listNotificaciones.clear();
                    listGastoReparacion.clear();
                    listServicio.clear();
                    cargarDatos(true);
                    break;
                } else {
                    new Mensaje().showModal(Alert.AlertType.WARNING, "Atención", this.getStage(), res.getMensaje());
                    break;
                }
            }
        }
    }

    private void acceptar(RecolectorInfoNotas colectorInfoNotas) {
        for (NotificacionDto notf : listNotificaciones) {
            if (numeroArea == 1) {
                if (notf.getId().equals(colectorInfoNotas.getIdNota())) {
                    notf.setActivo(false);
                    for (VueloDto vuel : listVuelo) {
                        if (notf.getIdObjeto().equals(vuel.getId())) {
                            vuel.setEstado((byte) 3);
                            Respuesta resp = new VueloService().update(vuel);
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
                    for (ServicioMantenimientoDto serv : listServicio) {
                        if (notf.getIdObjeto().equals(serv.getId())) {
                            serv.setActivo(false);
                            Respuesta resp = new ServicioMantenimientoService().update(serv);
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
        cargarDatos(true);
    }
}
