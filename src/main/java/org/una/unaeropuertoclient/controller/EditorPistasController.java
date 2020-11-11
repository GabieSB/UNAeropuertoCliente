/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.una.unaeropuertoclient.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.layout.HBox;
import org.una.unaeropuertoclient.model.PistaDto;
import org.una.unaeropuertoclient.service.PistaService;
import org.una.unaeropuertoclient.utils.AppContext;
import static org.una.unaeropuertoclient.utils.ButtonWaitUtils.aModoEspera;
import static org.una.unaeropuertoclient.utils.ButtonWaitUtils.salirModoEspera;
import org.una.unaeropuertoclient.utils.FlowController;
import org.una.unaeropuertoclient.utils.Formato;
import org.una.unaeropuertoclient.utils.Mensaje;
import org.una.unaeropuertoclient.utils.Respuesta;

/**
 * FXML Controller class
 *
 * @author Roberth :)
 */
public class EditorPistasController extends Controller implements Initializable {

    @FXML
    public JFXTextField txtNumeroPista;
    @FXML
    public JFXTextField txtLongitud;
    private boolean editionMode;
    private PistaDto pista;
    @FXML
    private HBox controlContainer;
    @FXML
    private JFXButton btnGuardar;
    private int accesMode;

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        txtFormat();
    }

    @Override
    public void initialize() {
        accesMode = (int) AppContext.getInstance().get("mode");
        btnGuardar.setDisable(accesMode != 1);
        txtLongitud.setText("");
        txtNumeroPista.setText("");
        if (accesMode < 2) {
            tryActivEditionMode();
        }
        cargarFuncionalidadesVentana();
    }

    @FXML
    public void OnClickSave(ActionEvent event) {
        if (isValidData()) {
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

    public boolean isValidData() {
        return !txtLongitud.getText().isBlank() && !txtNumeroPista.getText().isBlank();
    }

    public void cargarFuncionalidadesVentana() {
        Platform.runLater(() -> {
            this.getStage().setOnCloseRequest(event -> {
                clearContextData();
            });
        });
    }

    public void tryActivEditionMode() {
        if (AppContext.getInstance().get("EditPista") != null) {
            editionMode = true;
            pista = (PistaDto) AppContext.getInstance().get("EditPista");
            unChargeData();
        } else {
            editionMode = false;
            pista = new PistaDto();
        }
    }

    private void clearContextData() {
        pista = null;
        FlowController.getInstance().eliminarDeCache("EditorPistas");
        AppContext.getInstance().delete("EditPista");
        AppContext.getInstance().delete("GPist");
    }

    public void chargeData() {
        pista.setLongitud(Float.valueOf(txtLongitud.getText().trim()));
        pista.setNumeroPista(txtNumeroPista.getText().trim());
    }

    public void unChargeData() {
        txtLongitud.setText(pista.getLongitud().toString());
        txtNumeroPista.setText(pista.getNumeroPista());
    }

    public void saveChanges() {
        Thread th = new Thread(() -> {
            Respuesta resp;
            PistaService serv = new PistaService();
            chargeData();
            resp = editionMode ? serv.update(pista) : serv.create(pista);
            Platform.runLater(() -> {
                salirModoEspera(btnGuardar, "Guardar cambios");
                controlContainer.setDisable(false);
                if (resp.getEstado()) {
                    new Mensaje().show(Alert.AlertType.INFORMATION, "Todo bien por ahora", " Cambios se han registrado con éxito, puedes editar los datos guardados si deseas.");
                    pista = (PistaDto) resp.getResultado("data");
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

    public void refreshBack() {
        Thread th = new Thread(() -> {
            Platform.runLater(() -> {
                if (AppContext.getInstance().get("GPist") != null) {
                    ((GestorPistasController) AppContext.getInstance().get("GPist")).onActionBuscar(new ActionEvent());
                }
            });
        });
        th.start();
    }

    private void txtFormat() {
        txtNumeroPista.setTextFormatter(Formato.getInstance().letrasYNumerosFormat(10));
        txtLongitud.setTextFormatter(Formato.getInstance().twoDecimalFormat());
    }

}
