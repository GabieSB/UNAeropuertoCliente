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
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.HBox;
import javafx.util.Callback;
import org.una.unaeropuertoclient.model.PistaDto;
import org.una.unaeropuertoclient.service.PistaService;
import org.una.unaeropuertoclient.utils.AppContext;
import org.una.unaeropuertoclient.utils.FlowController;
import org.una.unaeropuertoclient.utils.Mensaje;
import org.una.unaeropuertoclient.utils.Respuesta;
import static org.una.unaeropuertoclient.utils.ButtonWaitUtils.*;
import org.una.unaeropuertoclient.utils.Formato;

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
    public TableView<PistaDto> tbPistas;
    @FXML
    public TableColumn<PistaDto, String> clNombre;
    @FXML
    public TableColumn<PistaDto, String> clLongitud;
    @FXML
    public TableColumn<PistaDto, Void> clAcciones;
    @FXML
    private HBox controlsContainer;
    @FXML
    private JFXButton btnBuscar;

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        txtFormat();
        prepareTable();
    }

    @Override
    public void initialize() {
        clearScreen();
    }

    @FXML
    public void onActionBuscar(ActionEvent event) {
        aModoEspera(btnBuscar);
        controlsContainer.setDisable(true);
        buscar();
    }

    private void buscar() {
        Thread th = new Thread(() -> {
            Respuesta resp = new PistaService().filter(txtNumeroPista.getText(), txtLongitud.getText());
            Platform.runLater(() -> {
                salirModoEspera(btnBuscar, "Buscar");
                controlsContainer.setDisable(false);
                if (resp.getEstado()) {
                    tbPistas.getItems().clear();
                    tbPistas.getItems().addAll((List) resp.getResultado("data"));
                } else {
                    new Mensaje().showModal(Alert.AlertType.WARNING, "Atención", this.getStage(), resp.getMensaje());
                }
            });
        });
        th.start();
    }

    @FXML
    public void OnClickNuevo(ActionEvent event) {
        FlowController.getInstance().goViewInWindowModal("EditorPistas", FlowController.getInstance().getStage(), false);
    }

    private void activateResponsiveConfig() {
        clAcciones.prefWidthProperty().bind(tbPistas.widthProperty().divide(10));
        clNombre.prefWidthProperty().bind(tbPistas.widthProperty().divide(5));
        clLongitud.prefWidthProperty().bind(tbPistas.widthProperty().divide(5));
    }

    private void prepareTable() {
        tbPistas.setPlaceholder(new Label("No hay pistas para mostrar por el momento"));
        activateResponsiveConfig();
        clNombre.setCellValueFactory(x -> new SimpleStringProperty(x.getValue().getNumeroPista()));
        clLongitud.setCellValueFactory(x -> new SimpleStringProperty(x.getValue().getLongitud().toString() + "m"));
        addTableAcction();
    }

    private void clearScreen() {
        tbPistas.getItems().clear();
        txtLongitud.setText("");
        txtNumeroPista.setText("");
    }

    private void addTableAcction() {
        GestorPistasController thisController = this;
        Callback<TableColumn<PistaDto, Void>, TableCell<PistaDto, Void>> cellFactory = (final TableColumn<PistaDto, Void> param) -> {
            final TableCell<PistaDto, Void> cell = new TableCell<>() {
                private final JFXButton butt = new JFXButton("Editar");

                {
                    butt.setOnAction((ActionEvent event) -> {
                        PistaDto pista = getTableView().getItems().get(getIndex());
                        AppContext.getInstance().set("EditPista", pista);
                        AppContext.getInstance().set("GPist", thisController);
                        FlowController.getInstance().goViewInWindowModal("EditorPistas", FlowController.getInstance().getStage(), false);
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

    private void txtFormat() {
        txtLongitud.setTextFormatter(Formato.getInstance().integerFormat());
        txtNumeroPista.setTextFormatter(Formato.getInstance().letrasYNumerosFormat(35));
    }

}
