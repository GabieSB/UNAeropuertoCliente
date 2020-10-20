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
import org.una.unaeropuertoclient.model.AvionDto;
import org.una.unaeropuertoclient.utils.FlowController;

/**
 * FXML Controller class
 *
 * @author Roberth :)
 */
public class GestorAvionesController extends Controller implements Initializable {

    @FXML
    public JFXTextField txtAerolinea;
    @FXML
    public JFXTextField txtMatricula;
    @FXML
    public TableView<AvionDto> tbAerolineas;
    @FXML
    public TableColumn<String, AvionDto> clMatricula;
    @FXML
    public TableColumn<String, AvionDto> clAerolinea;

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
    public void onActionClear(ActionEvent event) {
    }

    @FXML
    public void OnClickNuevo(ActionEvent event) {
        FlowController.getInstance().goViewInWindowModal("EditorAviones", FlowController.getInstance().getStage(), false);
    }

}
