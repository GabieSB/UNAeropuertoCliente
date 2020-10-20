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
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import org.una.unaeropuertoclient.model.VueloDto;
import org.una.unaeropuertoclient.utils.FlowController;

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
    public TableView<VueloDto> tbVuelos;
    @FXML
    public TableColumn<VueloDto, String> clNombre;
    @FXML
    public TableColumn<VueloDto, String> clAerolinea;
    @FXML
    public TableColumn<VueloDto, String> clAvion;
    @FXML
    public TableColumn<VueloDto, String> clSalida;
    @FXML
    public TableColumn<VueloDto, String> clLlegada;
    @FXML
    public TableColumn<VueloDto, String> clEstado;
    private ObservableList<VueloDto> tableList;

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        activateResponsiveConfig();
        configureDataRepresentation();
    }

    @Override
    public void initialize() {
        FlowController.changeSuperiorTittle("Módulo de vuelos");
    }

    @FXML
    public void onActionNuevo(ActionEvent event) {
        FlowController.getInstance().goViewInWindowModal("EditorVuelos", this.getStage(), false);
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

    private void activateResponsiveConfig() {
        clSalida.prefWidthProperty().bind(tbVuelos.widthProperty().divide(4));
        clLlegada.prefWidthProperty().bind(tbVuelos.widthProperty().divide(4));
        clAerolinea.prefWidthProperty().bind(tbVuelos.widthProperty().divide(8));
        clAvion.prefWidthProperty().bind(tbVuelos.widthProperty().divide(10));
        clNombre.prefWidthProperty().bind(tbVuelos.widthProperty().divide(8));
        clEstado.prefWidthProperty().bind(tbVuelos.widthProperty().divide(12));
    }

    private void configureDataRepresentation() {
        clSalida.setCellValueFactory(x -> new SimpleStringProperty(x.getValue().getSitioYFechaSalida()));
        clLlegada.setCellValueFactory(x -> new SimpleStringProperty(x.getValue().getSitioYFechaLLegada()));
        clAerolinea.setCellValueFactory(x -> new SimpleStringProperty(x.getValue().getAvionesId().getAerolineasId().getNombre()));
        clAvion.setCellValueFactory(x -> new SimpleStringProperty(x.getValue().getAvionesId().getMatricula()));
        clNombre.setCellValueFactory(x -> new SimpleStringProperty(x.getValue().getNombreVuelo()));
        clEstado.setCellValueFactory(x -> new SimpleStringProperty(x.getValue().getStateAsWord()));
    }
    
    private void chargeTodayData(){
       // admin1
    }

    @FXML
    public void onClickAdvanceSettings(ActionEvent event) {
        FlowController.getInstance().goView("AdvanceVuelosConfig");
    }

}
