/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.una.unaeropuertoclient.controller;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTimePicker;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import org.una.unaeropuertoclient.model.AerolineaDto;
import org.una.unaeropuertoclient.model.AvionDto;
import org.una.unaeropuertoclient.model.LugarDto;
import org.una.unaeropuertoclient.utils.Mensaje;

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

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        cargarFuncionalidadesVentana();
    }

    @Override
    public void initialize() {
    }

    @FXML
    public void onActionCancel(ActionEvent event) {
        if (autorizaCerrarVentana()) {
            this.getStage().close();
        }
    }

    @FXML
    public void OnActionClear(ActionEvent event) {
    }

    @FXML
    public void OnClickSave(ActionEvent event) {
    }

    private boolean autorizaCerrarVentana() {
        return new Mensaje().showConfirmation("Atención", this.getStage(), "¿Realmente desea cancelar la edición de este vuelo?");
    }

    private void cargarFuncionalidadesVentana() {
        Platform.runLater(() -> {
            this.getStage().setOnCloseRequest(event -> {
                if (!autorizaCerrarVentana()) {
                    event.consume();
                }
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

}
