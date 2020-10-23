/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.una.unaeropuertoclient.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXTextField;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.util.Callback;
import org.una.unaeropuertoclient.model.UsuarioDto;
import org.una.unaeropuertoclient.service.UsuarioService;
import org.una.unaeropuertoclient.utils.AppContext;
import org.una.unaeropuertoclient.utils.FlowController;
import org.una.unaeropuertoclient.utils.Mensaje;
import org.una.unaeropuertoclient.utils.Respuesta;

/**
 * FXML Controller class
 *
 * @author LordLalo
 */
public class BuscarUsuarioController extends Controller implements Initializable {

    @FXML
    public JFXTextField txtNombre;
    @FXML
    public JFXTextField txtApellido;
    @FXML
    public JFXTextField txtCedula;
    @FXML
    public DatePicker dtpFechaInicial;
    @FXML
    public DatePicker dtpFechaFinal;
    @FXML
    public JFXCheckBox checkActivo;
    @FXML
    public JFXButton btnBuscar;
    @FXML
    public TableColumn<UsuarioDto, String> tbcId;
    @FXML
    public TableColumn<UsuarioDto, String> tblCedula;
    @FXML
    public TableColumn<UsuarioDto, String> tbcNombre;
    @FXML
    public TableColumn<UsuarioDto, String> tbcApellido;
    @FXML
    public TableColumn<UsuarioDto, String> tbcFechaIngreso;
    @FXML
    public TableColumn<UsuarioDto, String> tbcFechaModificacion;
    @FXML
    public TableColumn<UsuarioDto, String> tbcEstado;
    List<UsuarioDto> usuarioList = new ArrayList<>();
    @FXML
    public TableView<UsuarioDto> tblUsuarios;
    public ObservableList<UsuarioDto> usuariosTabla;
    @FXML
    public JFXButton btnLimpiar;
    @FXML
    public TableColumn<UsuarioDto, Void> tblEditar;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        checkActivo.setSelected(true);
        tbcId.setCellValueFactory(x -> new SimpleStringProperty(x.getValue().getId().toString()));
        tbcNombre.setCellValueFactory(x -> new SimpleStringProperty(x.getValue().getNombre()));
        tbcApellido.setCellValueFactory(x -> new SimpleStringProperty(x.getValue().getApellidos()));
        tblCedula.setCellValueFactory(x -> new SimpleStringProperty(x.getValue().getCedula()));
        tbcFechaIngreso.setCellValueFactory(x -> new SimpleStringProperty(x.getValue().getFechaIngreso().toString()));
        tbcFechaModificacion.setCellValueFactory(x -> new SimpleStringProperty(x.getValue().getFechaModificacion().toString()));
        tbcEstado.setCellValueFactory(x -> new SimpleStringProperty(x.getValue().getActivo().toString()));
        addButtonToTable();
    }

    @Override
    public void initialize() {

    }

    @FXML
    public void actionBuscarPersona(ActionEvent event) {
        String pNombre = "none";
        String pApellido = "none";
        String pCedula = "none";
        String pFechaIni = "";
        String pFechaFinal = "";
        boolean pFechaIngresadas = false;
        if (!txtNombre.getText().isBlank()) {
            pNombre = txtNombre.getText();
        }
        if (!txtApellido.getText().isBlank()) {
            pApellido = txtApellido.getText();
        }
        if (!txtCedula.getText().isBlank()) {
            pCedula = txtCedula.getText();
        }
        if (dtpFechaInicial.getValue() != null && dtpFechaFinal.getValue() != null) {
            pFechaIni = dtpFechaInicial.getValue().toString();
            pFechaFinal = dtpFechaFinal.getValue().toString();
            pFechaIngresadas = true;
        }
        UsuarioService usuarioService = new UsuarioService();
        Respuesta respuesta = usuarioService.getById(pNombre, pApellido, pCedula, pFechaIni, pFechaFinal, checkActivo.isSelected(), pFechaIngresadas);

        if (respuesta.getEstado()) {
            usuarioList = (List<UsuarioDto>) respuesta.getResultado("data");
            usuariosTabla = FXCollections.observableArrayList((List) respuesta.getResultado("data"));
            tblUsuarios.setItems(usuariosTabla);
        } else {
            new Mensaje().showModal(Alert.AlertType.ERROR, "Falla al extraer los usuarios", this.getStage(), respuesta.getMensaje());

        }
    }

    @FXML
    public void limpiarBtn(ActionEvent event) {
        dtpFechaFinal.getEditor().clear();
        dtpFechaInicial.getEditor().clear();
        txtNombre.setText("");
        txtCedula.setText("");
        txtApellido.setText("");
        checkActivo.setSelected(true);
        Label la = new Label();

    }

    private void addButtonToTable() {

        Callback<TableColumn<UsuarioDto, Void>, TableCell<UsuarioDto, Void>> cellFactory = new Callback<>() {
            @Override
            public TableCell<UsuarioDto, Void> call(final TableColumn<UsuarioDto, Void> param) {
                final TableCell<UsuarioDto, Void> cell = new TableCell<>() {

                    private final JFXButton btn = new JFXButton("Editar");

                    {
                        btn.setOnAction((ActionEvent event) -> {
                            UsuarioDto usuarioDto = getTableView().getItems().get(getIndex());
                            System.out.println(usuarioDto.getNombre());
                            AppContext.getInstance().set("usuarioEdit", usuarioDto);
                            FlowController.getInstance().goBack();
                            
                        });
                    }

                    @Override
                    public void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            setGraphic(btn);
                        }
                    }

                };
                return cell;
            }
        };

        tblEditar.setCellFactory(cellFactory);

        // tableResultados.getColumns().add(columAcciones);
    }
}
