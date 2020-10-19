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
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import org.una.unaeropuertoclient.model.AerolineaDto;

/**
 * FXML Controller class
 *
 * @author Roberth :)
 */
public class GestorAerolineasController extends Controller implements Initializable {

    @FXML
    public JFXTextField txtNombre;
    @FXML
    public TableView<AerolineaDto> tbAerolineas;
    @FXML
    public TableColumn<String, AerolineaDto> clNombre;
    @FXML
    public TableColumn<String, AerolineaDto> clEstado;

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

}
