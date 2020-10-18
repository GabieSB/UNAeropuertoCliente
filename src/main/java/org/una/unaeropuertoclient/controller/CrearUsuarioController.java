/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.una.unaeropuertoclient.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import java.net.URL;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import org.una.unaeropuertoclient.model.AreaDto;
import org.una.unaeropuertoclient.model.RolDto;
import org.una.unaeropuertoclient.model.UsuarioDto;
import org.una.unaeropuertoclient.service.AreaService;
import org.una.unaeropuertoclient.service.RolService;
import org.una.unaeropuertoclient.service.UsuarioService;
import org.una.unaeropuertoclient.utils.FlowController;
import org.una.unaeropuertoclient.utils.Mensaje;
import org.una.unaeropuertoclient.utils.Respuesta;

/**
 * FXML Controller class
 *
 * @author LordLalo
 */
public class CrearUsuarioController extends Controller implements Initializable {

    @FXML
    public JFXTextField txtNombre;
    @FXML
    public JFXTextField txtApellido;
    @FXML
    public JFXTextField txtCedula;
    @FXML
    public JFXTextField txtContrasenna;
    @FXML
    public JFXTextField txtContrasennaConfirmacion;
    @FXML
    public JFXCheckBox checkActivo;
    @FXML
    public Label lblFechaModificacion;
    @FXML
    public JFXButton btnGuardo;
    @FXML
    public DatePicker dtpFechaNacimiento;
    @FXML
    public DatePicker dtpFechaRegistro;
    @FXML
    public JFXButton btnEditar;

    @FXML
    public JFXComboBox<String> cbxAreas;
    List<AreaDto> areaList = new ArrayList<AreaDto>();
    List<RolDto> rolList = new ArrayList<RolDto>();
    @FXML
    public VBox vbxRoles;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        cargarAreas();
        RolService rolService = new RolService();
        Respuesta respuesta = rolService.getAll();
        if (respuesta.getEstado()) {
            rolList = (List<RolDto>) respuesta.getResultado("data");
        } else {
            new Mensaje().showModal(Alert.AlertType.ERROR, "Falla al extraer areas", this.getStage(), respuesta.getMensaje());

        }
        for (RolDto rolDto : rolList) {
            JFXCheckBox jFXCheckBox = new JFXCheckBox();
            jFXCheckBox.setText(rolDto.getNombre());

            vbxRoles.getChildren().add(jFXCheckBox);
            System.out.println(rolDto.getNombre());
        }
//        System.out.printl((JFXCheckBox)vbxRoles.getClass());
//        (JFXCheckBox)vbxRoles.getChildren().get(0).toString();
    }

    @Override
    public void initialize() {
    }

    @FXML
    public void onActionGuardar(ActionEvent event) {
        AreaDto areaD = new AreaDto();
        for (AreaDto areaDto : areaList) {
            if (cbxAreas.getValue().toString().equals(areaDto.getNombre())) {
                areaD = areaDto;
            }
        }

        UsuarioDto usuarioDto = new UsuarioDto(txtCedula.getText(), txtNombre.getText(), txtApellido.getText(), txtContrasenna.getText(), Timestamp.valueOf(LocalDateTime.of(dtpFechaNacimiento.getValue(), LocalTime.MIN)), Timestamp.valueOf(LocalDateTime.of(dtpFechaRegistro.getValue(), LocalTime.MIN)), new Timestamp(System.currentTimeMillis()), checkActivo.isSelected(), areaD);
        UsuarioService usuarioService = new UsuarioService();
        usuarioService.create(usuarioDto);
    }

    @FXML
    public void goBuscar(ActionEvent event) {
        FlowController.getInstance().goView("BuscarUsuario");
    }

    public void cargarAreas() {
        AreaService areaService = new AreaService();

        Respuesta respuesta = areaService.getAll();
        if (respuesta.getEstado()) {
            areaList = (List<AreaDto>) respuesta.getResultado("data");
        } else {
            new Mensaje().showModal(Alert.AlertType.ERROR, "Falla al extraer areas", this.getStage(), respuesta.getMensaje());

        }
        for (AreaDto areaDto : areaList) {
            cbxAreas.getItems().add(areaDto.getNombre());
        }
    }

    @FXML
    public void d(ScrollEvent event) {
    }

}
