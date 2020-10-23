/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.una.unaeropuertoclient.controller;

import com.jfoenix.controls.JFXComboBox;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import org.una.unaeropuertoclient.model.AerolineaDto;
import org.una.unaeropuertoclient.model.AvionDto;
import org.una.unaeropuertoclient.model.LugarDto;
import org.una.unaeropuertoclient.model.PistaDto;
import org.una.unaeropuertoclient.model.VueloDto;
import org.una.unaeropuertoclient.service.AerolineaService;
import org.una.unaeropuertoclient.service.AvionService;
import org.una.unaeropuertoclient.service.LugarService;
import org.una.unaeropuertoclient.service.PistaService;
import org.una.unaeropuertoclient.service.VueloService;
import org.una.unaeropuertoclient.utils.AppContext;
import org.una.unaeropuertoclient.utils.FlowController;
import org.una.unaeropuertoclient.utils.Mensaje;
import org.una.unaeropuertoclient.utils.Respuesta;

/**
 * FXML Controller class
 *
 * @author Roberth :)
 */
public class EditorVuelosController extends Controller implements Initializable {

    @FXML
    public JFXComboBox<AerolineaDto> cbAerolinea;
    @FXML
    public JFXComboBox<AvionDto> cbAvion;
    @FXML
    public JFXComboBox<String> cbEsadoVuelo;
    @FXML
    public Label lblNombreVuelo;
    @FXML
    public JFXComboBox<LugarDto> cbSitioSalida;
    @FXML
    public DatePicker dpFechaSalida;
    @FXML
    public JFXComboBox<LugarDto> cbSitioLlegada;
    @FXML
    public DatePicker dpFechaLlegada;
    @FXML
    public JFXComboBox<String> cbHoraSalida;
    @FXML
    public JFXComboBox<String> cbMinutosSalida;
    @FXML
    public JFXComboBox<String> cbHoraLlegada;
    @FXML
    public JFXComboBox<String> cbMinutosLlegada;
    @FXML
    private JFXComboBox<PistaDto> cbPistaAterrisage;
    private boolean editionMode;
    private VueloDto vuelo;
    private List<ComboBox> cbList;

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        cbList = Arrays.asList(cbAerolinea, cbAvion, cbPistaAterrisage,
                cbSitioLlegada, cbSitioSalida, cbEsadoVuelo, cbMinutosSalida,
                cbHoraSalida, cbMinutosLlegada, cbHoraLlegada);
        cargarFuncionalidadesVentana();
    }

    @Override
    public void initialize() {
        cbAerolinea.setPromptText("Cargando...");
        cbAvion.setPromptText("Aviones(Vacío)");
        cbAvion.setDisable(true);
        cbPistaAterrisage.setPromptText("Cargando...");
        cbSitioSalida.setPromptText("Cargando...");
        cbSitioLlegada.setPromptText("Cargando...");
        tryActivEditionMode();
        chargeExternalData();
    }

    @FXML
    public void onActionCancel(ActionEvent event) {
        FlowController.getInstance().eliminarDeCache("EditorVuelos");
        this.getStage().close();
    }

    @FXML
    public void OnActionClear(ActionEvent event) {
        cbList.forEach(cb -> cb.setValue(null));
        clearContextData();
        tryActivEditionMode();
        lblNombreVuelo.setText("Nombre de vuelo:");
        dpFechaLlegada.setValue(null);
        dpFechaSalida.setValue(null);
        
    }

    @FXML
    public void OnClickSave(ActionEvent event) {
        if (isValidData()) {
            saveChanges();
        } else {
            new Mensaje().showModal(Alert.AlertType.WARNING, "Observa con atención", this.getStage(), "Quizá has dejado algún espacio sin rellenar.");
        }
    }

    private void cargarFuncionalidadesVentana() {
        cbEsadoVuelo.getItems().addAll("Programado", "En vuelo", "Finalizado");
        Platform.runLater(() -> {
            this.getStage().setOnCloseRequest(event -> {
                FlowController.getInstance().eliminarDeCache("EditorVuelos");
            });
        });
        for (Integer h = 0; h < 24; h++) {
            cbHoraSalida.getItems().add(h.toString());
            cbHoraLlegada.getItems().add(h.toString());
        }
        for (Integer m = 0; m < 60; m++) {
            cbMinutosSalida.getItems().add(((m < 10) ? "0" : "") + m.toString());
            cbMinutosLlegada.getItems().add(((m < 10) ? "0" : "") + m.toString());
        }
    }

    private void chargeExternalData() {
        Thread th = new Thread(() -> chargePistas());
        th.start();
        Thread th2 = new Thread(() -> chargeLugares());
        th2.start();
        Thread th3 = new Thread(() -> chargeAerolinas());
        th3.start();
    }

    private void chargePistas() {
        Respuesta resp = new PistaService().findAll();
        Platform.runLater(() -> {
            cbPistaAterrisage.getItems().clear();
            if (resp.getEstado()) {
                List<PistaDto> pList = (List<PistaDto>) resp.getResultado("data");
                cbPistaAterrisage.getItems().addAll(pList);
                cbPistaAterrisage.setPromptText("Pistas");
            } else {
                cbPistaAterrisage.setPromptText(resp.getMensaje());
            }
        });
    }

    private void chargeLugares() {
        Respuesta resp = new LugarService().findByEstado(true);
        Platform.runLater(() -> {
            cbSitioLlegada.getItems().clear();
            cbSitioSalida.getItems().clear();
            if (resp.getEstado()) {
                List<LugarDto> pList = (List<LugarDto>) resp.getResultado("data");
                cbSitioSalida.getItems().addAll(pList);
                cbSitioSalida.setPromptText("Lugar de salida");
                cbSitioLlegada.getItems().addAll(pList);
                cbSitioLlegada.setPromptText("Lugar de llegada");
            } else {
                cbSitioLlegada.setPromptText(resp.getMensaje());
                cbSitioSalida.setPromptText(resp.getMensaje());
            }
        });
    }

    private void chargeAerolinas() {
        Respuesta resp = new AerolineaService().findByEstado(true);
        Platform.runLater(() -> {
            cbAerolinea.getItems().clear();
            if (resp.getEstado()) {
                List<AerolineaDto> aeroList = (List<AerolineaDto>) resp.getResultado("data");
                cbAerolinea.setPromptText("Aerolinas");
                cbAerolinea.getItems().addAll(aeroList);
            } else {
                cbAerolinea.setPromptText(resp.getMensaje());
            }
        });
    }

    @FXML
    public void chargeAviones(ActionEvent event) {
        createNombreVuelo();
        Thread th = new Thread(() -> {
            Platform.runLater(() -> {
                cbAvion.setDisable(false);
                cbAvion.setPromptText("Cargando...");
            });
            if (cbAerolinea.getValue() != null) {
                Respuesta resp = new AvionService().filter("", cbAerolinea.getValue().getNombre());
                Platform.runLater(() -> {
                    cbAvion.getItems().clear();
                    if (resp.getEstado()) {
                        List<AvionDto> pList = (List<AvionDto>) resp.getResultado("data");
                        cbAvion.getItems().addAll(pList);
                        cbAvion.setPromptText("Aviones");
                    } else {
                        cbAvion.setPromptText(resp.getMensaje());
                    }
                });
            } else {
                Platform.runLater(() -> {
                    cbAvion.setPromptText("Aviones(Vacío)");
                    cbAvion.setDisable(true);
                });
            }
        });
        th.start();
    }

    private void tryActivEditionMode() {
        if (AppContext.getInstance().get("EditVuelo") != null) {
            editionMode = true;
            vuelo = (VueloDto) AppContext.getInstance().get("EditVuelo");
            unChargeData();
        } else {
            editionMode = false;
            cbEsadoVuelo.getSelectionModel().select("Programado");
            cbEsadoVuelo.setDisable(true);
            vuelo = new VueloDto();
        }
    }

    private void unChargeData() {
       vuelo.setAvionesId(cbAvion.getValue());
        vuelo.setLugarLlegada(cbSitioLlegada.getValue());
        vuelo.setLugarSalida(cbSitioSalida.getValue());
        vuelo.setPistasId(cbPistaAterrisage.getValue());
        vuelo.setStateAsWord(cbEsadoVuelo.getValue());
        vuelo.setHoraLlegada(toDate(dpFechaLlegada, cbHoraLlegada.getValue(), cbMinutosLlegada.getValue()));
        vuelo.setHoraSalida(toDate(dpFechaSalida, cbHoraSalida.getValue(), cbMinutosSalida.getValue()));
    }

    private boolean isValidData() {
        if (dpFechaLlegada.getValue() != null && dpFechaLlegada.getValue() != null) {
            if (cbAerolinea.getValue() != null && cbAvion != null) {
                if (cbMinutosLlegada.getValue() != null && cbHoraLlegada.getValue() != null) {
                    if (cbMinutosSalida.getValue() != null && cbHoraSalida.getValue() != null) {
                        if (cbEsadoVuelo.getValue() != null && cbPistaAterrisage.getValue() != null) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    private void saveChanges() {
        Respuesta resp;
        VueloService serv = new VueloService();
        unChargeData();
        resp = editionMode ? serv.update(vuelo) : serv.create(vuelo);
        if (resp.getEstado()) {
            new Mensaje().show(Alert.AlertType.INFORMATION, "Todo bien por ahora", " Cambios se han registrado con éxito, puedes editar los datos guardados si deseas.");
            vuelo = (VueloDto) resp.getResultado("data");
            chargeData();
            editionMode = true;
            refreshBack();
        } else {
            new Mensaje().showModal(Alert.AlertType.WARNING, "Atención", this.getStage(), resp.getMensaje());
        }
    }

    private void chargeData() {
        lblNombreVuelo.setText("Vuelo: " + vuelo.getNombreVuelo());
        cbAvion.getSelectionModel().select(vuelo.getAvionesId());
        cbSitioLlegada.getSelectionModel().select(vuelo.getLugarLlegada());
        cbSitioSalida.getSelectionModel().select(vuelo.getLugarSalida());
        cbPistaAterrisage.getSelectionModel().select(vuelo.getPistasId());
        cbEsadoVuelo.getSelectionModel().select(vuelo.getStateAsWord());
        LocalDateTime ldt = toLocalDateTime(vuelo.getHoraLlegada());
        dpFechaLlegada.setValue(ldt.toLocalDate());
        cbHoraLlegada.getSelectionModel().select(ldt.getHour());
        cbMinutosLlegada.getSelectionModel().select(ldt.getMinute());
        ldt = toLocalDateTime(vuelo.getHoraSalida());
        dpFechaSalida.setValue(ldt.toLocalDate());
        cbHoraSalida.getSelectionModel().select(ldt.getHour());
        cbMinutosSalida.getSelectionModel().select(ldt.getMinute());
    }

    private void refreshBack() {
        if (AppContext.getInstance().get("GVuelo") != null) {
            ((GestorVuelosController) AppContext.getInstance().get("GVuelo")).onActionBuscar(new ActionEvent());
        }
    }

    private void clearContextData() {
        vuelo = null;
        AppContext.getInstance().delete("EditVuelo");
        AppContext.getInstance().delete("GVuelo");
    }

    private Date toDate(DatePicker dp, String hours, String minuts) {
        LocalDateTime locaDT = dp.getValue().atTime(Integer.valueOf(hours), Integer.valueOf(minuts));
        return Date.from(locaDT.atZone(ZoneId.systemDefault()).toInstant());
    }

    public LocalDateTime toLocalDateTime(Date dateToConvert) {
        return dateToConvert.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
    }

    public void createNombreVuelo() {
        Thread th = new Thread(() -> {
            Respuesta resp = new VueloService().countVuelosByAerolinea(cbAerolinea.getValue());
            Platform.runLater(() -> {
                if (resp.getEstado()) {
                    Long consecutivo = (Long) resp.getResultado("data");
                    consecutivo++;
                    vuelo.setNombreVuelo(cbAerolinea.getValue().getNombre() + "-" + consecutivo.toString());
                    lblNombreVuelo.setText("Vuelo: " + vuelo.getNombreVuelo());
                }
            });
        });
        th.start();
    }

}
