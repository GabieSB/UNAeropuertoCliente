/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.una.unaeropuertoclient.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.layout.HBox;
import org.una.unaeropuertoclient.model.AerolineaDto;
import org.una.unaeropuertoclient.model.AvionDto;
import org.una.unaeropuertoclient.service.AerolineaService;
import org.una.unaeropuertoclient.service.AvionService;
import org.una.unaeropuertoclient.utils.AppContext;
import static org.una.unaeropuertoclient.utils.ButtonWaitUtils.aModoEspera;
import static org.una.unaeropuertoclient.utils.ButtonWaitUtils.salirModoEspera;
import org.una.unaeropuertoclient.utils.Mensaje;
import org.una.unaeropuertoclient.utils.Respuesta;

/**
 * FXML Controller class
 *
 * @author Roberth :)
 */
public class EditorAvionesController extends Controller implements Initializable {

    @FXML
    public JFXTextField txtMatricula;
    @FXML
    public JFXComboBox<AerolineaDto> cbAerolinea;
    private AvionDto avion;
    private boolean editionMode;
    @FXML
    private HBox controlContainer;
    @FXML
    private JFXButton btnGuardar;

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
        txtMatricula.setText("");
        cbAerolinea.getItems().clear();
        cbAerolinea.setPromptText("Cargando...");
        cargarFuncionalidadesVentana();
    }

    @FXML
    public void OnClickSave(ActionEvent event) {
        if (!txtMatricula.getText().isBlank() && cbAerolinea.getValue() != null) {
            aModoEspera(btnGuardar);
            controlContainer.setDisable(true);
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
        if (AppContext.getInstance().get("EditAvion") != null) {
            editionMode = true;
            avion = (AvionDto) AppContext.getInstance().get("EditAvion");
            unChargeData();
        } else {
            editionMode = false;
            avion = new AvionDto();
        }
    }

    private void cargarFuncionalidadesVentana() {
        Platform.runLater(() -> {
            this.getStage().setOnCloseRequest(event -> {
                clearContextData();
            });
        });
        Thread th = new Thread(() -> {
            Platform.runLater(() -> {
                chargeActivAirlines();
                tryActivEditionMode();
            });
        });
        th.start();
    }

    private void unChargeData() {
        txtMatricula.setText(avion.getMatricula());
        cbAerolinea.getSelectionModel().select(avion.getAerolineasId());
    }

    private void clearContextData() {
        avion = null;
        AppContext.getInstance().delete("EditAvion");
        AppContext.getInstance().delete("GAvion");
    }

    private void saveChanges() {
        Thread th = new Thread(() -> {
            Respuesta resp;
            AvionService serv = new AvionService();
            chargeData();
            resp = editionMode ? serv.update(avion) : serv.create(avion);
            Platform.runLater(() -> {
                salirModoEspera(btnGuardar, "Guardar cambios");
                controlContainer.setDisable(false);
                if (resp.getEstado()) {
                    new Mensaje().show(Alert.AlertType.INFORMATION, "Todo bien por ahora", " Cambios se han registrado con éxito, puedes editar los datos guardados si deseas.");
                    avion = (AvionDto) resp.getResultado("data");
                    unChargeData();
                    editionMode = true;
                    refreshBack();
                } else {
                    new Mensaje().showModal(Alert.AlertType.WARNING, "Atención", this.getStage(), resp.getMensaje());
                }
            });
        });
        th.start();
    }

    private void chargeData() {
        avion.setMatricula(txtMatricula.getText().trim());
        avion.setAerolineasId(cbAerolinea.getValue());
    }

    private void refreshBack() {
        Thread th = new Thread(() -> {
            Platform.runLater(() -> {
                if (AppContext.getInstance().get("GAvion") != null) {
                    ((GestorAvionesController) AppContext.getInstance().get("GAvion")).onActionBuscar(new ActionEvent());
                }
            });
        });
        th.start();
    }

    private void chargeActivAirlines() {
        Respuesta resp = new AerolineaService().findByEstado(true);
        if (resp.getEstado()) {
            cbAerolinea.getItems().clear();
            cbAerolinea.getItems().addAll((List) resp.getResultado("data"));
            cbAerolinea.setPromptText("Aerolineas");
        } else {
            cbAerolinea.getItems().clear();
            cbAerolinea.setPromptText(resp.getMensaje());
        }
    }

}
