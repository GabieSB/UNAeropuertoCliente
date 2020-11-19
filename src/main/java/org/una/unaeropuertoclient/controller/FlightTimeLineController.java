/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.una.unaeropuertoclient.controller;

import com.jfoenix.controls.JFXButton;
import java.net.URL;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.una.unaeropuertoclient.utils.FlightDayVisor;
import org.una.unaeropuertoclient.utils.FlowController;
import org.una.unaeropuertoclient.utils.Mensaje;

/**
 * FXML Controller class
 *
 * @author roberth :)
 */
public class FlightTimeLineController extends Controller implements Initializable {

    @FXML
    private DatePicker dpFecha;
    @FXML
    private JFXButton btnBuscar;
    @FXML
    private JFXButton btnSave;
    @FXML
    private VBox vbPortaVuelos;
    @FXML
    private Label lblLunes;
    @FXML
    private Label lblMartes;
    @FXML
    private Label lblMiercoles;
    @FXML
    private Label lblJueves;
    @FXML
    private Label lblViernes;
    @FXML
    private Label lblSabado;
    @FXML
    private Label lblDomingo;
    @FXML
    private HBox hbHoras;
    @FXML
    private Label lblPortavuelosTittle;
    @FXML
    private VBox vbPlanificador;
    private final List<Label> flagsDateLabels = new ArrayList();

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        flagsDateLabels.addAll(Arrays.asList(lblLunes, lblMartes, lblMiercoles, lblJueves,
                lblJueves, lblViernes, lblSabado, lblDomingo));
    }

    @Override
    public void initialize() {
        FlowController.changeSuperiorTittle("Planificador de vuelos");
        FlowController.changeCodeScreenTittle("VE000");
        clearAll();
        dpFecha.setValue(LocalDate.now());
        buscar(new ActionEvent());
    }

    private void clearAll() {
        vbPortaVuelos.getChildren().removeIf(obj -> !(obj instanceof Label));
        vbPortaVuelos.setAlignment(Pos.CENTER);
        lblPortavuelosTittle.setText("Coloca uno o varios\nvuelos aquí para\nmoverlos a otras\nsemanas");
    }

    @FXML
    private void buscar(ActionEvent event) {
        if (dpFecha.getValue() != null) {
            vbPlanificador.getChildren().removeIf(node -> !(node instanceof HBox));
            for (int i = 0; i < 7; i++) {
                FlightDayVisor visor = new FlightDayVisor(i, dpFecha.getValue().with((DayOfWeek.MONDAY)));
                flagsDateLabels.get(i).setText(String.valueOf(visor.getFecha().getDayOfMonth()));
                vbPlanificador.getChildren().add(visor);
            }
        } else {
            new Mensaje().show(Alert.AlertType.WARNING, "Atención", "Antes debes escoger una fecha");
        }
    }

    @FXML
    private void onClickSaveChanges(ActionEvent event) {
    }

}
