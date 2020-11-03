/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.una.unaeropuertoclient.controller;

import com.jfoenix.controls.JFXComboBox;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
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
import javafx.scene.layout.VBox;
import javafx.util.Pair;
import org.una.unaeropuertoclient.model.AerolineaDto;
import org.una.unaeropuertoclient.model.AvionDto;
import org.una.unaeropuertoclient.model.LugarDto;
import org.una.unaeropuertoclient.model.ParamSistemaDto;
import org.una.unaeropuertoclient.model.PistaDto;
import org.una.unaeropuertoclient.model.VueloDto;
import org.una.unaeropuertoclient.service.AerolineaService;
import org.una.unaeropuertoclient.service.AvionService;
import org.una.unaeropuertoclient.service.LugarService;
import org.una.unaeropuertoclient.service.ParamSistemaServicio;
import org.una.unaeropuertoclient.service.PistaService;
import org.una.unaeropuertoclient.service.VueloService;
import org.una.unaeropuertoclient.utils.AppContext;
import org.una.unaeropuertoclient.utils.FlowController;
import org.una.unaeropuertoclient.utils.Mensaje;
import org.una.unaeropuertoclient.utils.Respuesta;
import static org.una.unaeropuertoclient.utils.VuelosUtilis.*;

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
    @FXML
    private VBox vbSalidaYLlegada;
    private boolean editionMode;
    private VueloDto vuelo;
    private List<ComboBox> cbList;
    private String oldFlyName;
    private AerolineaDto oldAerline;
    private ParamSistemaDto paramSistem;
    private boolean isDangerous = false;

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
        vbSalidaYLlegada.setDisable(true);
        cbAerolinea.setPromptText("Cargando...");
        cbAvion.setPromptText("Aviones(Vacío)");
        cbAvion.setDisable(true);
        cbPistaAterrisage.setPromptText("Cargando...");
        cbSitioSalida.setPromptText("Cargando...");
        cbSitioLlegada.setPromptText("Cargando...");
        chargeExternalData();
        tryActivEditionMode();
    }

    @FXML
    public void onActionCancel(ActionEvent event) {
        FlowController.getInstance().eliminarDeCache("EditorVuelos");
        clearContextData();
        paramSistem = null;
        this.getStage().close();
    }

    @FXML
    public void OnActionClear(ActionEvent event) {
        cbList.forEach(cb -> cb.setValue(null));
        vuelo = null;
        AppContext.getInstance().delete("EditVuelo");
        tryActivEditionMode();
        lblNombreVuelo.setText("Nombre de vuelo:");
        dpFechaLlegada.setValue(null);
        dpFechaSalida.setValue(null);

    }

    @FXML
    public void OnClickSave(ActionEvent event) {
        if (isAllFull()) {
            if (sonCorrectosLugaresDeSalidaYLlegada()) {
                if (sonFechasCorrectas() && esDuracionCorrecta()) {
                    if (validarContratiemposVuelo()) {
                        saveChanges();
                    }
                }
            }
        } else {
            new Mensaje().showModal(Alert.AlertType.WARNING, "Observa con atención", this.getStage(), "Quizá has dejado algún espacio sin rellenar.");
        }
    }

    private void cargarFuncionalidadesVentana() {
        cbEsadoVuelo.getItems().addAll("Programado", "En vuelo", "Finalizado");
        Platform.runLater(() -> {
            this.getStage().setOnCloseRequest(event -> {
                clearContextData();
                paramSistem = null;
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
        Thread th1 = new Thread(() -> chargePistas());
        th1.start();
        Thread th2 = new Thread(() -> chargeLugares());
        th2.start();
        Thread th3 = new Thread(() -> chargeAerolinas());
        th3.start();
        Thread th4 = new Thread(() -> chargeParamSistema());
        th4.start();
    }

    private void chargePistas() {
        Respuesta resp = new PistaService().findAll();
        Platform.runLater(() -> {
            if (resp.getEstado()) {
                List<PistaDto> pList = (List<PistaDto>) resp.getResultado("data");
                pList.removeIf(elemnt -> elemnt.equals(cbPistaAterrisage.getValue()));
                cbPistaAterrisage.getItems().addAll(pList);
                cbPistaAterrisage.setPromptText("Pistas");
            } else {
                cbPistaAterrisage.setPromptText(resp.getMensaje());
            }
        });
    }

    public void chargeParamSistema() {
        Respuesta resp = new ParamSistemaServicio().getById();
        Platform.runLater(() -> {
            if (resp.getEstado()) {
                paramSistem = (ParamSistemaDto) resp.getResultado("data");
                vbSalidaYLlegada.setDisable(false);
            } else {
                new Mensaje().showModal(Alert.AlertType.ERROR, "Error", this.getStage(), "No ha sido "
                        + "posible cargar los datos del aeropuerto, tal como hora de apertura y de "
                        + "cierre, debido a esto se ha cancelado la posibilidad de crear o modificar vuelos.");
            }
        });
    }

    private void chargeLugares() {
        Respuesta resp = new LugarService().findByEstado(true);
        Platform.runLater(() -> {
            if (resp.getEstado()) {
                List<LugarDto> pList = (List<LugarDto>) resp.getResultado("data");
                pList.removeIf(elemnt -> elemnt.equals(cbSitioSalida.getValue()));
                cbSitioSalida.getItems().addAll(pList);
                cbSitioSalida.setPromptText("Lugar de salida");
                if (cbSitioSalida.getValue() != null) {
                    pList.add(cbSitioSalida.getValue());
                }
                pList.removeIf(elemnt -> elemnt.equals(cbSitioLlegada.getValue()));
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
            if (resp.getEstado()) {
                List<AerolineaDto> aeroList = (List<AerolineaDto>) resp.getResultado("data");
                aeroList.removeIf(elemnt -> elemnt.equals(cbAerolinea.getValue()));
                cbAerolinea.setPromptText("Aerolinas");
                cbAerolinea.getItems().addAll(aeroList);
            } else {
                cbAerolinea.setPromptText(resp.getMensaje());
            }
        });
    }

    public void chargeAviones(boolean clearItems) {
        Thread th = new Thread(() -> {
            Platform.runLater(() -> {
                cbAvion.setDisable(false);
                cbAvion.setPromptText("Cargando...");
            });
            if (cbAerolinea.getValue() != null) {
                Respuesta resp = new AvionService().filter("", cbAerolinea.getValue().getNombre());
                Platform.runLater(() -> {
                    if (clearItems) {
                        cbAvion.getItems().clear();
                    }
                    if (resp.getEstado()) {
                        List<AvionDto> pList = (List<AvionDto>) resp.getResultado("data");
                        if (!clearItems) {
                            pList.removeIf(elemnt -> elemnt.equals(cbAvion.getValue()));
                        }
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
            cbAerolinea.getItems().add(vuelo.getAvionesId().getAerolineasId());
            cbAvion.getItems().add(vuelo.getAvionesId());
            cbSitioLlegada.getItems().add(vuelo.getLugarLlegada());
            cbSitioSalida.getItems().add(vuelo.getLugarSalida());
            cbPistaAterrisage.getItems().add(vuelo.getPistasId());
            copyUnmodificableFlyData();
            chargeData();
            chargeAviones(false);
        } else {
            editionMode = false;
            cbEsadoVuelo.getSelectionModel().select("Programado");
            cbEsadoVuelo.setDisable(true);
            vuelo = new VueloDto();
        }
    }

    @FXML
    public void OnActionChargeAviones(ActionEvent event) {
        createNombreVuelo();
        chargeAviones(true);
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

    private boolean isAllFull() {
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
            copyUnmodificableFlyData();
            chargeData();
            editionMode = true;
            refreshBack();
        } else {
            new Mensaje().showModal(Alert.AlertType.WARNING, "Atención", this.getStage(), resp.getMensaje());
        }
    }

    private void chargeData() {
        cbAerolinea.getSelectionModel().select(vuelo.getAvionesId().getAerolineasId());
        cbAvion.getSelectionModel().select(vuelo.getAvionesId());
        lblNombreVuelo.setText("Vuelo: " + vuelo.getNombreVuelo());
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
        if (!editionMode) {
            th.start();
        } else if (cbAerolinea.getValue().equals(oldAerline)) {
            vuelo.setNombreVuelo(oldFlyName);
            lblNombreVuelo.setText("Vuelo: " + vuelo.getNombreVuelo());
        } else {
            th.start();
        }

    }

    public void copyUnmodificableFlyData() {
        oldAerline = vuelo.getAvionesId().getAerolineasId();
        oldFlyName = vuelo.getNombreVuelo();
    }

    public boolean validarContratiemposVuelo() {
        LocalDateTime start = dpFechaSalida.getValue().atTime(Integer.valueOf(cbHoraSalida.getValue()),
                Integer.valueOf(cbMinutosSalida.getValue()));
        LocalDateTime end = dpFechaLlegada.getValue().atTime(Integer.valueOf(cbHoraLlegada.getValue()),
                Integer.valueOf(cbMinutosLlegada.getValue()));
        LocalDateTime exeDateTime = LocalDateTime.now();
        if (cbSitioLlegada.getValue().equals(paramSistem.getUbicacion())) {
            exeDateTime = dpFechaLlegada.getValue().atTime(Integer.valueOf(cbHoraLlegada.getValue()),
                    Integer.valueOf(cbMinutosLlegada.getValue()));
        } else {
            exeDateTime = dpFechaSalida.getValue().atTime(Integer.valueOf(cbHoraSalida.getValue()),
                    Integer.valueOf(cbMinutosSalida.getValue()));
        }
        Respuesta resp = new AvionService().validarContratiemposVuelo(start, end, vuelo.getId(), cbAvion.getValue().getId(), exeDateTime);
        if (resp.getEstado()) {
            Pair<String, String> pair = (Pair) resp.getResultado("data");
            if ("NoCorrect".equals(pair.getKey())) {
                new Mensaje().showModal(Alert.AlertType.WARNING, "Atención", this.getStage(), pair.getValue());
                return false;
            } else if ("Dangerous".equals(pair.getKey())) {
                isDangerous = true;
                return new Mensaje().showConfirmation("Peligro", this.getStage(), pair.getValue(), "¿Desea permitir este vuelo igualmente?");
            }
            return true;
        }
        new Mensaje().showModal(Alert.AlertType.ERROR, "Error", this.getStage(), resp.getMensaje());
        return false;
    }

    public boolean sonCorrectosLugaresDeSalidaYLlegada() {
        if (cbSitioSalida.getValue().equals(cbSitioLlegada.getValue())) {
            new Mensaje().showModal(Alert.AlertType.WARNING, "Atención", this.getStage(), "El sitio de salida y de llegada no pueden ser los iguales");
            return false;
        } else if (!(cbSitioSalida.getValue().equals(paramSistem.getUbicacion()) || cbSitioLlegada.getValue().equals(paramSistem.getUbicacion()))) {
            new Mensaje().showModal(Alert.AlertType.WARNING, "Atención", this.getStage(), "Este vuelo parece no despegar ni aterrizar en este aeropuerto. Revisa de nuevo el sitio de salida y el de llegada.");
            return false;
        }
        return true;
    }

    public boolean sonFechasCorrectas() {
        if (dpFechaSalida.getValue().isBefore(LocalDate.now().minusDays(1))
                || dpFechaLlegada.getValue().isBefore(LocalDate.now().minusDays(1))) {
            return new Mensaje().showConfirmation("Fecha confusa", this.getStage(), "La fecha de "
                    + "este vuelo es de antes de ayer, lo que podría ser un error al selecciona la fecha.",
                    "¿Desea guardar los cambios a pesar de todo?");
        }
        return true;
    }

    public boolean esDuracionCorrecta() {
        if (Math.abs(dpFechaSalida.getValue().until(dpFechaLlegada.getValue()).getDays()) > 1) {
            return new Mensaje().showConfirmation("Duración de vuelo", this.getStage(), "Este vuelo tiene una "
                    + "duración de más de 24 horas, lo cual podría deberse a un error al digitar las fechas.",
                    "¿Desea guardar los cambios a pesar de todo?");
        }
        return true;
    }
}
