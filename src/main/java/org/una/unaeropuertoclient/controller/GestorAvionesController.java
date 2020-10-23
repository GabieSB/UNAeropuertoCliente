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
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.util.Callback;
import org.una.unaeropuertoclient.model.AvionDto;
import org.una.unaeropuertoclient.service.AvionService;
import org.una.unaeropuertoclient.utils.AppContext;
import org.una.unaeropuertoclient.utils.FlowController;
import org.una.unaeropuertoclient.utils.Mensaje;
import org.una.unaeropuertoclient.utils.Respuesta;

/**
 * FXML Controller class
 *
 * @author Roberth :)
 */
public class GestorAvionesController extends Controller implements Initializable {

    @FXML
    public JFXTextField txtAerolinea;
    @FXML
    public JFXTextField txtMatricula;
    @FXML
    public TableView<AvionDto> tbAerolineas;
    @FXML
    public TableColumn<AvionDto, String> clMatricula;
    @FXML
    public TableColumn<AvionDto, String> clAerolinea;
    @FXML
    private TableColumn<AvionDto, Void> clAcciones;

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
        Respuesta resp = new AvionService().filter(txtMatricula.getText(), txtAerolinea.getText());
        if (resp.getEstado()) {
            tbAerolineas.getItems().clear();
            tbAerolineas.getItems().addAll((List) resp.getResultado("data"));
        } else {
            new Mensaje().showModal(Alert.AlertType.WARNING, "Atención", this.getStage(), resp.getMensaje());
        }
    }

    @FXML
    public void onActionClear(ActionEvent event) {
        clearScreen();
    }

    @FXML
    public void OnClickNuevo(ActionEvent event) {
        FlowController.getInstance().goViewInWindowModal("EditorAviones", FlowController.getInstance().getStage(), false);
    }

    private void prepareTable() {
        activateResponsiveConfig();
        clAerolinea.setCellValueFactory(x -> new SimpleStringProperty(x.getValue().getAerolineasId().getNombre()));
        clMatricula.setCellValueFactory(x -> new SimpleStringProperty(x.getValue().getMatricula()));
        addTableAcction();
    }

    private void activateResponsiveConfig() {
        clAcciones.prefWidthProperty().bind(tbAerolineas.widthProperty().divide(10));
        clAerolinea.prefWidthProperty().bind(tbAerolineas.widthProperty().divide(5));
        clMatricula.prefWidthProperty().bind(tbAerolineas.widthProperty().divide(5));
    }

    private void addTableAcction() {
        GestorAvionesController thisController = this;
        Callback<TableColumn<AvionDto, Void>, TableCell<AvionDto, Void>> cellFactory = (final TableColumn<AvionDto, Void> param) -> {
            final TableCell<AvionDto, Void> cell = new TableCell<>() {
                private final JFXButton butt = new JFXButton("Editar");

                {
                    butt.setOnAction((ActionEvent event) -> {
                        AvionDto avion = getTableView().getItems().get(getIndex());
                        AppContext.getInstance().set("EditAvion", avion);
                        AppContext.getInstance().set("GAvion", thisController);
                        FlowController.getInstance().goViewInWindowModal("EditorAviones", FlowController.getInstance().getStage(), false);
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
        txtAerolinea.setText("");
        txtMatricula.setText("");
    }

}
