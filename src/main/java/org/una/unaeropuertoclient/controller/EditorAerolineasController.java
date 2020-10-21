/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.una.unaeropuertoclient.controller;

import com.jfoenix.controls.JFXTextField;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import org.una.unaeropuertoclient.model.AerolineaDto;
import org.una.unaeropuertoclient.model.GastoReparacionDto;
import org.una.unaeropuertoclient.model.LugarDto;
import org.una.unaeropuertoclient.service.AerolineaService;
import org.una.unaeropuertoclient.service.LugarService;
import org.una.unaeropuertoclient.utils.AppContext;
import org.una.unaeropuertoclient.utils.Mensaje;
import org.una.unaeropuertoclient.utils.Respuesta;

/**
 * FXML Controller class
 *
 * @author Roberth :)
 */
public class EditorAerolineasController extends Controller implements Initializable {

    @FXML
    public JFXTextField txtNombre;
    private boolean editionMode;
    private AerolineaDto aerolinea;

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    @Override
    public void initialize() {
        txtNombre.setText("");
        tryActivEditionMode();
        cargarFuncionalidadesVentana();
    }

    @FXML
    public void OnClickSave(ActionEvent event) {
        if (!txtNombre.getText().isBlank()) {
            saveChanges();
        } else {
            new Mensaje().showModal(Alert.AlertType.WARNING, "Observa con atención", this.getStage(), "Quizá has dejado algún espacio sin rellenar.");
        }
    }

    @FXML
    public void OnClickCancel(ActionEvent event) {
        clearContextData();
        this.getStage().close();
    }

    private void tryActivEditionMode() {
        if (AppContext.getInstance().get("EditAeroL") != null) {
            editionMode = true;
            aerolinea = (AerolineaDto) AppContext.getInstance().get("EditAeroL");
            unChargeData();
        } else {
            editionMode = false;
            aerolinea = new AerolineaDto();
        }
    }

    private void cargarFuncionalidadesVentana() {
        Platform.runLater(() -> {
            this.getStage().setOnCloseRequest(event -> {
                clearContextData();
            });
        });
    }

    private void unChargeData() {
        txtNombre.setText(aerolinea.getNombre());
    }

    private void clearContextData() {
        aerolinea = null;
        AppContext.getInstance().delete("EditAeroL");
        AppContext.getInstance().delete("GAero");
    }

    private void saveChanges() {
        Respuesta resp;
        AerolineaService serv = new AerolineaService();
        chargeData();
        resp = editionMode ? serv.update(aerolinea) : serv.create(aerolinea);
        if (resp.getEstado()) {
            new Mensaje().show(Alert.AlertType.INFORMATION, "Todo bien por ahora", " Cambios se han registrado con éxito, puedes editar los datos guardados si deseas.");
            aerolinea = (AerolineaDto) resp.getResultado("data");
            unChargeData();
            editionMode = true;
            refreshBack();
        } else {
            new Mensaje().showModal(Alert.AlertType.WARNING, "Atención", this.getStage(), resp.getMensaje());
        }
    }

    private void chargeData() {
        aerolinea.setNombre(txtNombre.getText().trim());
    }

    private void refreshBack() {
        Thread th = new Thread(() -> {
            Platform.runLater(() -> {
                if (AppContext.getInstance().get("GAero") != null) {
                    ((GestorAerolineasController) AppContext.getInstance().get("GAero")).onActionBuscar(new ActionEvent());
                }
            });
        });
        th.start();
    }

}
