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
import org.una.unaeropuertoclient.model.AerolineaDto;
import org.una.unaeropuertoclient.service.AerolineaService;
import org.una.unaeropuertoclient.utils.AppContext;
import static org.una.unaeropuertoclient.utils.ButtonWaitUtils.aModoEspera;
import static org.una.unaeropuertoclient.utils.ButtonWaitUtils.salirModoEspera;
import org.una.unaeropuertoclient.utils.FlowController;
import org.una.unaeropuertoclient.utils.Mensaje;
import org.una.unaeropuertoclient.utils.Respuesta;

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
    public TableColumn<AerolineaDto, String> clNombre;
    @FXML
    public TableColumn<AerolineaDto, String> clEstado;
    @FXML
    private TableColumn<AerolineaDto, Void> clAcciones;
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
            Respuesta resp = new AerolineaService().findByNombre(txtNombre.getText());
            Platform.runLater(() -> {
                salirModoEspera(btnBuscar, "Buscar");
                controlsContainer.setDisable(false);
                if (resp.getEstado()) {
                    tbAerolineas.getItems().clear();
                    tbAerolineas.getItems().addAll((List) resp.getResultado("data"));
                } else {
                    new Mensaje().showModal(Alert.AlertType.WARNING, "Atención", this.getStage(), resp.getMensaje());
                }
            });
        });
        th.start();
    }

    @FXML
    public void OnClickNuevo(ActionEvent event) {
        FlowController.getInstance().goViewInWindowModal("EditorAerolineas", FlowController.getInstance().getStage(), false);
    }

    private void prepareTable() {
        tbAerolineas.setPlaceholder(new Label("No hay aerolineas para mostrar por el momento"));
        activateResponsiveConfig();
        clNombre.setCellValueFactory(x -> new SimpleStringProperty(x.getValue().getNombre()));
        clEstado.setCellValueFactory(x -> new SimpleStringProperty((x.getValue().getActiov()) ? "En operación" : "Operación suspendida"));
        addTableAcction();
    }

    private void activateResponsiveConfig() {
        clEstado.prefWidthProperty().bind(tbAerolineas.widthProperty().divide(5));
        clNombre.prefWidthProperty().bind(tbAerolineas.widthProperty().divide(5));
        clAcciones.prefWidthProperty().bind(tbAerolineas.widthProperty().divide(5));
    }

    private void addTableAcction() {
        GestorAerolineasController thisController = this;
        Callback<TableColumn<AerolineaDto, Void>, TableCell<AerolineaDto, Void>> cellFactory = (final TableColumn<AerolineaDto, Void> param) -> {
            final TableCell<AerolineaDto, Void> cell = new TableCell<>() {
                private final JFXButton butt = new JFXButton("Editar");

                {
                    butt.setOnAction((ActionEvent event) -> {
                        AerolineaDto pista = getTableView().getItems().get(getIndex());
                        AppContext.getInstance().set("EditAeroL", pista);
                        AppContext.getInstance().set("GAero", thisController);
                        FlowController.getInstance().goViewInWindowModal("EditorAerolineas", FlowController.getInstance().getStage(), false);
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

    private void clearScreen() {
        tbAerolineas.getItems().clear();
        txtNombre.setText("");
    }

}
