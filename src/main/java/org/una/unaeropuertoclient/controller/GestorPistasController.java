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
import org.una.unaeropuertoclient.model.PistaDto;

/**
 * FXML Controller class
 *
 * @author Roberth :)
 */
public class GestorPistasController extends Controller implements Initializable {

    @FXML
    private JFXTextField txtNumeroPista;
    @FXML
    private JFXTextField txtLongitud;
    @FXML
    private TableView<PistaDto> tbAerolineas;
    @FXML
    private TableColumn<String, PistaDto> clNombre;
    @FXML
    private TableColumn<String, PistaDto> clEstado;

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
    private void onActionBuscar(ActionEvent event) {
    }

}
