package org.una.unaeropuertoclient.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import org.una.unaeropuertoclient.model.BitacoraDto;
import org.una.unaeropuertoclient.service.BitacoraService;
import org.una.unaeropuertoclient.utils.*;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.fxml.FXML;

public class BuscarBitacoraController extends Controller implements Initializable {

    public DatePicker dateInicio;
    public DatePicker dateFin;
    public JFXButton btnBuscarFecha;
    public TableColumn<BitacoraDto, String> columId;
    public TableColumn<BitacoraDto, String> columFecha;
    public TableColumn<BitacoraDto, String> columAccion;
    public TableColumn<BitacoraDto, String> columUsuario;
    public JFXTextField txtValorBuscado;
    public JFXButton btonBuscarParametro;
    public JFXComboBox comboxParametros;
    public TableView tableResultados;
    public VBox containerRoot;

    BitacoraService bitacoraService = new BitacoraService();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        iniciarComponentes();
    }

    public void iniciarComponentes() {
        ObservableList<String> items = FXCollections.observableArrayList();
        FlowController.changeSuperiorTittle("BITÁCORAS");
        FlowController.changeCodeScreenTittle("BG000");
        items.addAll("ID", "CÉDULA USUARIO");
        comboxParametros.getItems().addAll(items);
        cargarPropiedadesTable();
    }

    private void cargarPropiedadesTable() {
        tableResultados.setPlaceholder(new Label("Realice una consulta para mostrar resultados"));
        columId.setCellValueFactory(x -> new SimpleStringProperty(x.getValue().getId().toString()));
        columFecha.setCellValueFactory(x -> new SimpleStringProperty(x.getValue().getFechaModificacion().toString()));
        columAccion.setCellValueFactory(x -> new SimpleStringProperty(x.getValue().getTipoCambio()));
        columUsuario.setCellValueFactory(x -> new SimpleStringProperty(x.getValue().getUsuariosId().getNombre()));
    }

    @SuppressWarnings("null")
    public void busquedaSegunCategoria() {
        int categoria = comboxParametros.getSelectionModel().getSelectedIndex();
        String valor = txtValorBuscado.getText();

        if (!valor.isEmpty()) {
            Respuesta respuesta = null;
            switch (categoria) {
                case 0:
                    if (Validar.isLongNumber(valor)) {
                        respuesta = bitacoraService.buscarPorID(Long.parseLong(valor));
                    } else {
                        new Mensaje().show(Alert.AlertType.INFORMATION, "Información", "Parece que has ingresado un valor incorrecto. El ID debería ser un número");
                    }
                    break;
                case 1:
                    respuesta = bitacoraService.buscarPorUserCedula(valor);
                    break;
                case 2:
                    respuesta = bitacoraService.buscarPorTipoCambio(valor);
                    break;
                default:
                    Platform.runLater(() -> new Mensaje().show(Alert.AlertType.WARNING, "Atención", "Selecciona un tipo de búsqueda."));
                    break;
            }
            tableResultados.getItems().clear();
            if (respuesta != null && respuesta.getEstado()) {
                if (categoria == 0) {
                    tableResultados.getItems().add((BitacoraDto) respuesta.getResultado("data"));
                } else {
                    tableResultados.getItems().addAll((List<BitacoraDto>) respuesta.getResultado("data"));
                }
            } else {
                new Mensaje().showModal(Alert.AlertType.ERROR, "Atención", this.getStage(), respuesta.getMensaje());
            }
        } else {
            new Mensaje().showModal(Alert.AlertType.WARNING, "Información", this.getStage(), "Parece que el espacio de valor a consultar esta vacío");
        }
    }

    @Override
    public void initialize() {
        FlowController.changeSuperiorTittle("Bitácoras");

    }

    @FXML
    public void buscarPorFecha(ActionEvent actionEvent) {
        ButtonWaitUtils.aModoEspera(btnBuscarFecha);
        Thread th = new Thread(() -> {
            Respuesta respuesta = bitacoraService.buscarEntreFechas(dateInicio.getValue(), dateFin.getValue());
            Platform.runLater(() -> {
                ButtonWaitUtils.salirModoEspera(btnBuscarFecha, "Buscar por fechas");
                if (respuesta.getEstado()) {
                    tableResultados.getItems().addAll((List)respuesta.getResultado("data"));
                } else {
                    new Mensaje().showModal(Alert.AlertType.WARNING, "Atención", this.getStage(), respuesta.getMensaje());
                }
            });
        });
        th.start();
    }

    public void buscarPorParametro(ActionEvent actionEvent) {
        busquedaSegunCategoria();
    }
}
