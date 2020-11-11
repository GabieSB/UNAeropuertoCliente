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
import org.una.unaeropuertoclient.model.LugarDto;
import org.una.unaeropuertoclient.service.LugarService;
import org.una.unaeropuertoclient.utils.AppContext;
import static org.una.unaeropuertoclient.utils.ButtonWaitUtils.aModoEspera;
import static org.una.unaeropuertoclient.utils.ButtonWaitUtils.salirModoEspera;
import org.una.unaeropuertoclient.utils.Formato;
import org.una.unaeropuertoclient.utils.Mensaje;
import org.una.unaeropuertoclient.utils.Respuesta;
//

/**
 * FXML Controller class
 *
 * @author Roberth :)
 */
public class EditorLugaresController extends Controller implements Initializable {

    @FXML
    public JFXTextField txtNombre;
    private boolean editionMode;
    private LugarDto lugar;
    @FXML
    private HBox controlContainer;
    @FXML
    private JFXButton btnGuardar;
    private boolean modoAuditor;

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
        modoAuditor = (boolean) AppContext.getInstance().get("auditMode");
        btnGuardar.setDisable(modoAuditor);
        txtNombre.setText("");
        tryActivEditionMode();
        cargarFuncionalidadesVentana();
    }

    @FXML
    public void OnClickSave(ActionEvent event) {
        if (!txtNombre.getText().isBlank()) {
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
        if (AppContext.getInstance().get("EditLugar") != null) {
            editionMode = true;
            lugar = (LugarDto) AppContext.getInstance().get("EditLugar");
            unChargeData();
        } else {
            editionMode = false;
            lugar = new LugarDto();
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
        txtNombre.setText(lugar.getNombre());
    }

    private void clearContextData() {
        lugar = null;
        AppContext.getInstance().delete("EditLugar");
        AppContext.getInstance().delete("GLugar");
    }

    private void saveChanges() {
        Thread th = new Thread(() -> {
            Respuesta resp;
            LugarService serv = new LugarService();
            chargeData();
            resp = editionMode ? serv.update(lugar) : serv.create(lugar);
            Platform.runLater(() -> {
                salirModoEspera(btnGuardar, "Guardar cambios");
                controlContainer.setDisable(false);
                if (resp.getEstado()) {
                    new Mensaje().show(Alert.AlertType.INFORMATION, "Todo bien por ahora", " Cambios se han registrado con éxito, puedes editar los datos guardados si deseas.");
                    lugar = (LugarDto) resp.getResultado("data");
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
        lugar.setNombre(txtNombre.getText().trim());
    }

    private void refreshBack() {
        Thread th = new Thread(() -> {
            Platform.runLater(() -> {
                if (AppContext.getInstance().get("GLugar") != null) {
                    ((GestorLugaresController) AppContext.getInstance().get("GLugar")).onActionBuscar(new ActionEvent());
                }
            });
        });
        th.start();
    }

    private void txtFormat() {
        txtNombre.setTextFormatter(Formato.getInstance().letrasYNumerosFormat(60));
    }

}
