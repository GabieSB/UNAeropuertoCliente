/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.una.unaeropuertoclient.controller;

import com.jfoenix.controls.JFXTextField;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import org.una.unaeropuertoclient.model.LugarDto;
import org.una.unaeropuertoclient.utils.FlowController;

/**
 * FXML Controller class
 *
 * @author Roberth :)
 */
public class GestorLugaresController extends Controller implements Initializable {

    @FXML
    public JFXTextField txtNombre;
    @FXML
    public TableView<LugarDto> tbAerolineas;
    @FXML
    public TableColumn<String, LugarDto> clNombre;
    @FXML
    public TableColumn<String, LugarDto> clEstado;

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
    }

    @FXML
    public void onActionBuscar(ActionEvent event) {
    }

    @FXML
    public void OnClickNuevo(ActionEvent event) {
        FlowController.getInstance().goViewInWindowModal("EditorLugares", FlowController.getInstance().getStage(), false);
    }

}
