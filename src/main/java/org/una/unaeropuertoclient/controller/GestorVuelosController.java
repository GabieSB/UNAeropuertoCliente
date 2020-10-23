/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.una.unaeropuertoclient.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.util.Callback;
import org.una.unaeropuertoclient.model.VueloDto;
import org.una.unaeropuertoclient.service.VueloService;
import org.una.unaeropuertoclient.utils.AppContext;
import org.una.unaeropuertoclient.utils.FlowController;
import org.una.unaeropuertoclient.utils.Mensaje;
import org.una.unaeropuertoclient.utils.Respuesta;

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
    @FXML
    public TableColumn<VueloDto, Void> clAcciones;

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        prepareTable();
    }

    @Override
    public void initialize() {
        FlowController.changeSuperiorTittle("Módulo de vuelos");
        clearScreen();
    }

    @FXML
    public void onActionNuevo(ActionEvent event) {
        FlowController.getInstance().goViewInWindowModal("EditorVuelos", this.getStage(), false);
    }

    @FXML
    public void onActionBuscar(ActionEvent event) {
        Thread th = new Thread(() -> {
            Respuesta resp = new VueloService().filter(txtAerolinea.getText(),
                    txtFlyName.getText(), txtMatriculaAvion.getText(), txtLlegada.getText(),
                    txtSalida.getText(), dpDesde.getValue(), dpHasta.getValue());
            Platform.runLater(() -> {
                if (resp.getEstado()) {
                    tbVuelos.getItems().clear();
                    tbVuelos.getItems().addAll((List) resp.getResultado("data"));
                } else {
                    new Mensaje().showModal(Alert.AlertType.WARNING, "Atención", this.getStage(), resp.getMensaje());
                }
            });
        });
        th.start();
    }

    @FXML
    public void onActionLimpiar(ActionEvent event) {
        clearScreen();
    }

    private void activateResponsiveConfig() {
        clSalida.prefWidthProperty().bind(tbVuelos.widthProperty().divide(5));
        clLlegada.prefWidthProperty().bind(tbVuelos.widthProperty().divide(5));
        clAerolinea.prefWidthProperty().bind(tbVuelos.widthProperty().divide(9));
        clAvion.prefWidthProperty().bind(tbVuelos.widthProperty().divide(11));
        clNombre.prefWidthProperty().bind(tbVuelos.widthProperty().divide(9));
        clEstado.prefWidthProperty().bind(tbVuelos.widthProperty().divide(13));
        clAcciones.prefWidthProperty().bind(tbVuelos.widthProperty().divide(13));
    }

    private void configureDataRepresentation() {
        clSalida.setCellValueFactory(x -> new SimpleStringProperty(x.getValue().getSitioYFechaSalida()));
        clLlegada.setCellValueFactory(x -> new SimpleStringProperty(x.getValue().getSitioYFechaLLegada()));
        clAerolinea.setCellValueFactory(x -> new SimpleStringProperty(x.getValue().getAvionesId().getAerolineasId().getNombre()));
        clAvion.setCellValueFactory(x -> new SimpleStringProperty(x.getValue().getAvionesId().getMatricula()));
        clNombre.setCellValueFactory(x -> new SimpleStringProperty(x.getValue().getNombreVuelo()));
        clEstado.setCellValueFactory(x -> new SimpleStringProperty(x.getValue().getStateAsWord()));
    }

    private void chargeTodayData() {
        Thread th = new Thread(() -> {
            Respuesta resp = new VueloService().findVuelosDelDia();
            Platform.runLater(() -> {
                if (resp.getEstado()) {
                    tbVuelos.getItems().clear();
                    tbVuelos.getItems().addAll((List) resp.getResultado("data"));
                }
            });
        });
        th.start();
    }

    @FXML
    public void onClickAdvanceSettings(ActionEvent event) {
        FlowController.getInstance().goView("AdvanceVuelosConfig");
    }

    private void prepareTable() {
        activateResponsiveConfig();
        configureDataRepresentation();
        addTableAcction();
    }

    private void clearScreen() {
        txtAerolinea.setText("");
        txtFlyName.setText("");
        txtLlegada.setText("");
        txtSalida.setText("");
        txtFlyName.setText("");
        txtMatriculaAvion.setText("");
        dpDesde.setValue(null);
        dpHasta.setValue(null);
        chargeTodayData();
    }

    private void addTableAcction() {
        GestorVuelosController thisController = this;
        Callback<TableColumn<VueloDto, Void>, TableCell<VueloDto, Void>> cellFactory = (final TableColumn<VueloDto, Void> param) -> {
            final TableCell<VueloDto, Void> cell = new TableCell<>() {
                private final JFXButton butt = new JFXButton("Editar");

                {
                    butt.setOnAction((ActionEvent event) -> {
                        VueloDto pista = getTableView().getItems().get(getIndex());
                        AppContext.getInstance().set("EditVuelo", pista);
                        AppContext.getInstance().set("GVuelo", thisController);
                        FlowController.getInstance().goViewInWindowModal("EditorVuelos", FlowController.getInstance().getStage(), false);
                    });
                }

                @Override
                public void updateItem(Void item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty) {
                        setGraphic(null);
                    } else {
                        setGraphic(butt);
                    }
                }
            };
            return cell;
        };
        clAcciones.setCellFactory(cellFactory);
    }

}
