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
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

/**
 * FXML Controller class
 *
 * @author Roberth :)
 */
public class GestorVuelosController extends Controller implements Initializable {

    @FXML
    public JFXTextField txtFlyName;
    @FXML
    public JFXTextField txtAerolinea;
    @FXML
    public JFXTextField txtMatriculaAvion;
    @FXML
    public JFXTextField txtSalida;
    @FXML
    public JFXTextField txtLlegada;
    @FXML
    public DatePicker dpDesde;
    @FXML
    public DatePicker dpHasta;
    @FXML
    public JFXButton btnEliminar;
    @FXML
    public Label txtVuelosDe;
    @FXML
    public TableView<?> tbVuelos;
    @FXML
    public TableColumn<?, ?> clNombre;
    @FXML
    public TableColumn<?, ?> clAerolinea;
    @FXML
    public TableColumn<?, ?> clAvion;
    @FXML
    public TableColumn<?, ?> clSalida;
    @FXML
    public TableColumn<?, ?> clLlegada;

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
    public void onActionNuevo(ActionEvent event) {
    }

    @FXML
    public void onActionBuscar(ActionEvent event) {
    }

    @FXML
    public void onActionLimpiar(ActionEvent event) {
    }

    @FXML
    public void onActionEliminar(ActionEvent event) {
    }

}
