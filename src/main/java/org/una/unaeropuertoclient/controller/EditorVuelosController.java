/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.una.unaeropuertoclient.controller;

import com.jfoenix.controls.JFXComboBox;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import org.una.unaeropuertoclient.model.AerolineaDto;
import org.una.unaeropuertoclient.model.AvionDto;
import org.una.unaeropuertoclient.model.LugarDto;
import org.una.unaeropuertoclient.model.PistaDto;
import org.una.unaeropuertoclient.service.LugarService;
import org.una.unaeropuertoclient.service.PistaService;
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

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }

    @Override
    public void initialize() {
        cbAerolinea.setPromptText("Cargando...");
        cbAvion.setPromptText("Cargando...");
        cbPistaAterrisage.setPromptText("Cargando...");
        cbSitioSalida.setPromptText("Cargando...");
        cbSitioLlegada.setPromptText("Cargando...");
        chargeData();
        cargarFuncionalidadesVentana();
    }

    @FXML
    public void onActionCancel(ActionEvent event) {
        this.getStage().close();
    }

    @FXML
    public void OnActionClear(ActionEvent event) {
    }

    @FXML
    public void OnClickSave(ActionEvent event) {
    }

    private void cargarFuncionalidadesVentana() {
        Platform.runLater(() -> {
            this.getStage().setOnCloseRequest(event -> {
                //TODO
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

    private void chargeData() {
        Thread th = new Thread(() -> chargePistas());
        th.start();
        Thread th2 = new Thread(() -> chargeLugares());
        th2.start();

    }

    private void chargePistas() {
        Platform.runLater(() -> {
            Respuesta resp = new PistaService().findAll();
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
        Platform.runLater(() -> {
            Respuesta resp = new LugarService().findByEstado(true);
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

}
