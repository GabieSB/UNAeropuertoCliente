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
import javafx.application.Platform;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.concurrent.Worker;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.una.unaeropuertoclient.model.ParamSistemaDto;
import org.una.unaeropuertoclient.model.VueloDto;
import org.una.unaeropuertoclient.service.ParamSistemaServicio;
import org.una.unaeropuertoclient.service.VueloService;
import org.una.unaeropuertoclient.utils.ButtonWaitUtils;
import org.una.unaeropuertoclient.utils.FlightDayVisor;
import org.una.unaeropuertoclient.utils.FlowController;
import org.una.unaeropuertoclient.utils.Mensaje;
import org.una.unaeropuertoclient.utils.Respuesta;

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
    private List<FlightDayVisor> visorsList;
    private ParamSistemaDto paramSist;
    @FXML
    private HBox hbBarraBusqueda;

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        flagsDateLabels.addAll(Arrays.asList(lblLunes, lblMartes, lblMiercoles, lblJueves,
                lblViernes, lblSabado, lblDomingo));
    }

    @Override
    public void initialize() {
        FlowController.changeSuperiorTittle("Planificador de vuelos");
        FlowController.changeCodeScreenTittle("VE000");
        clearAll();
        dpFecha.setValue(LocalDate.now());
        chargeInitialData();
    }

    private void clearAll() {
        hbBarraBusqueda.setDisable(true);
        vbPlanificador.setDisable(true);
        btnSave.setDisable(true);
        vbPlanificador.getChildren().removeIf(node -> !(node instanceof HBox));
        vbPortaVuelos.getChildren().removeIf(obj -> !(obj instanceof Label));
        vbPortaVuelos.setAlignment(Pos.CENTER);
        lblPortavuelosTittle.setText("Coloca uno o varios\nvuelos aquí para\nmoverlos a otras\nsemanas");
    }

    @FXML
    private void buscar(ActionEvent event) {
        if (paramSist != null) {
            if (dpFecha.getValue() != null) {
                vbPlanificador.getChildren().removeIf(node -> !(node instanceof HBox));
                visorsList = new ArrayList();
                for (int i = 0; i < 7; i++) {
                    FlightDayVisor visor = new FlightDayVisor(i, dpFecha.getValue().with(DayOfWeek.MONDAY));
                    flagsDateLabels.get(i).setText(String.valueOf(visor.getFecha().getDayOfMonth()));
                    visorsList.add(visor);
                    vbPlanificador.getChildren().add(visor);
                }
                getVuelosEntreFechas();
            } else {
                new Mensaje().show(Alert.AlertType.WARNING, "Atención", "Antes debes escoger una fecha");
            }
        } else {
            new Mensaje().show(Alert.AlertType.ERROR, "Error", "No se puede realizar la búsqueda, no se cuenta con la ubicación de aeropuerto");
        }
    }

    public void getVuelosEntreFechas() {
        ButtonWaitUtils.aModoEspera(btnBuscar);
        Thread th = new Thread(() -> {
            Respuesta resp = new VueloService().findEntreFechas(dpFecha.getValue().with(DayOfWeek.MONDAY), dpFecha.getValue().with(DayOfWeek.SUNDAY));
            Platform.runLater(() -> {
                ButtonWaitUtils.salirModoEspera(btnBuscar, "Buscar");
                if (resp.getEstado()) {
                    List<VueloDto> results = (List) resp.getResultado("data");
                    visorsList.forEach(visor -> {
                        visor.tomarVuelosCorrespondientes(results, paramSist.getUbicacion(), paramSist.getVuelosHora());
                    });
                } else {
                    new Mensaje().showModal(Alert.AlertType.WARNING, "Atención", this.getStage(), resp.getMensaje());
                }
            });
        });
        th.start();
    }

    @FXML
    private void onClickSaveChanges(ActionEvent event) {
    }

    private void getParamSistema() {
        Respuesta resp = new ParamSistemaServicio().getById();
        Platform.runLater(() -> {
            if (resp.getEstado()) {
                hbBarraBusqueda.setDisable(false);
                vbPlanificador.setDisable(false);
                btnSave.setDisable(false);
                this.paramSist = (ParamSistemaDto) resp.getResultado("data");
            } else {
                new Mensaje().showModal(Alert.AlertType.WARNING, "Atención", this.getStage(), "No ha sido "
                        + "posible cargar la ubicación del aeropuerto, por lo que se puede utilizar esta pantalla."
                        + " Si el problema persiste considere contactar con el el encargado del sistema.");
            }
        });
    }

    public void chargeInitialData() {
        Thread th = new Thread(() -> {
            getParamSistema();
            visorsList = new ArrayList();
            Platform.runLater(() -> {
                for (int i = 0; i < 7; i++) {
                    FlightDayVisor visor = new FlightDayVisor(i, dpFecha.getValue().with(DayOfWeek.MONDAY));
                    flagsDateLabels.get(i).setText(String.valueOf(visor.getFecha().getDayOfMonth()));
                    visorsList.add(visor);
                    vbPlanificador.getChildren().add(visor);
                }
                getVuelosEntreFechas();
            });
        });
        th.start();
    }

}
