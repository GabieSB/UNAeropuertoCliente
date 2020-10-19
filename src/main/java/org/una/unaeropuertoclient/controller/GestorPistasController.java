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
    public JFXTextField txtNumeroPista;
    @FXML
    public JFXTextField txtLongitud;
    @FXML
    public TableView<PistaDto> tbAerolineas;
    @FXML
    public TableColumn<String, PistaDto> clNombre;
    @FXML
    public TableColumn<String, PistaDto> clEstado;

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
    }

}
