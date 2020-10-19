/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.una.unaeropuertoclient.controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Tab;
import javafx.scene.layout.AnchorPane;
import org.una.unaeropuertoclient.utils.FlowController;

/**
 * FXML Controller class
 *
 * @author Roberth :)
 */
public class AdvanceVuelosConfigController extends Controller implements Initializable {

    @FXML
    public Tab tabAerolineas;
    @FXML
    public Tab tabAviones;
    @FXML
    public Tab tabPistas;
    @FXML
    public Tab tabLugares;
    @FXML
    public AnchorPane apAerolineas;
    @FXML
    public AnchorPane apAviones;
    @FXML
    public AnchorPane apPistas;
    @FXML
    public AnchorPane apLugares;

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
        FlowController.changeSuperiorTittle("Opciones avanzadas de control aéreo");
    }

}
