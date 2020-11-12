/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.una.unaeropuertoclient.controller;

import com.jfoenix.controls.*;

import java.net.URL;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.VBox;
import org.una.unaeropuertoclient.model.*;
import org.una.unaeropuertoclient.service.*;
import org.una.unaeropuertoclient.utils.AppContext;
import static org.una.unaeropuertoclient.utils.ButtonWaitUtils.*;

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
    public JFXPasswordField txtContrasenna;
    @FXML
    public JFXPasswordField txtContrasennaConfirmacion;
    @FXML
    public JFXCheckBox checkActivo;
    @FXML
    public Label lblFechaModificacion;
    @FXML
    public JFXButton btnGuardo;
    @FXML
    public JFXComboBox<String> cbxAreas;
    public Label txtTitulo;
    public JFXButton cambiarContrasenaButton;
    public JFXButton modificarButton;
    public VBox container;
    @FXML
    private JFXComboBox<String> cbxRoles;
    @FXML
    private DatePicker dtpFechaNacimiento;
    List<AreaDto> areaList = new ArrayList<>();
    List<RolDto> rolList = new ArrayList<>();
    boolean cambiarContrasena;
    UsuarioDto usuarioSeleccionado = null ;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {


    }

    @Override
    public void initialize() {
        FlowController.changeSuperiorTittle("Registrar Usuario");
        FlowController.changeCodeScreenTittle("UG100");

        if(rolList.isEmpty()) cargarRoles();
        DatosEdicion();

    }


    @FXML
    public void onActionGuardar(ActionEvent event) {

        if(isValidInformation(true)){
           container.setDisable(true);
            aModoEspera(btnGuardo);
            Optional<RolDto> rolDto = rolList.stream().filter(r -> cbxRoles.getValue().equals(r.getNombre())).findFirst();
            UsuarioDto usuarioDto = new UsuarioDto(txtCedula.getText(), txtNombre.getText(), txtApellido.getText(), txtContrasenna.getText(), Timestamp.valueOf(LocalDateTime.of(dtpFechaNacimiento.getValue(), LocalTime.MIN)), checkActivo.isSelected(), getAreaByRole(cbxRoles.getValue()));
            Thread thread = new Thread(()->{

                registrarUsuario(rolDto, usuarioDto);

            });

            thread.start();

        }

    }

    private void limpiar(){
        txtContrasenna.clear();
        txtContrasennaConfirmacion.clear();
        txtCedula.clear();
        txtApellido.clear();
        txtNombre.clear();
        cbxRoles.getSelectionModel().clearSelection();
        dtpFechaNacimiento.setValue(null);
    }

    private void registrarUsuario(Optional<RolDto> rolDto, UsuarioDto usuarioDto) {
        Respuesta respuesta = new UsuarioService().create(usuarioDto);
        Platform.runLater(()->{
            if (respuesta.getEstado()) {
                UsuarioDto usuarioCreado = (UsuarioDto) respuesta.getResultado("data");
                RolUsuarioDto rolUsuario = new RolUsuarioDto(Timestamp.valueOf(LocalDateTime.now()), Timestamp.valueOf(LocalDateTime.now()), true, rolDto.get(), usuarioCreado);
                Respuesta repuestaRol = new RolUsuarioService().create(rolUsuario);
                if(repuestaRol.getEstado()){
                    limpiar();
                    new Mensaje().showInformation("Se ha creado el usuario con éxito");
                    new BitacoraService().create("Registró usuario con ID: " + ((UsuarioDto) respuesta.getResultado("data")).getId());
                }else {
                    new Mensaje().show(Alert.AlertType.ERROR, "Error al asignar un rol", "No se ha podido asignar el rol al usuario");
                }

            }else {
                new Mensaje().show(Alert.AlertType.ERROR, "Error de información", respuesta.getMensaje());
            }
           container.setDisable(false);
            salirModoEspera(btnGuardo, "Guardar");

        });
    }

    private boolean isValidInformation(boolean evaluarContrasena){
        String mensajeErrores = ""; boolean isValid = true;

        if (cbxRoles.getSelectionModel().getSelectedItem()==null) {
            mensajeErrores+="Se necesita un rol para completar el registro\n";
            isValid = false;
        }
        if(txtNombre.getText().isEmpty() || txtApellido.getText().isEmpty()){
            mensajeErrores+="Se necesita que los campos de nombre y apellido estén completos para realizar el registro\n";
            isValid = false;
        }
        if(txtCedula.getText().isEmpty()){
            mensajeErrores+="Se necesita una cédula para completar el registro\n";
            isValid = false;
        }
        if(dtpFechaNacimiento.getValue() == null){
            mensajeErrores+="Se necesita una fecha de nacimiento para completar el registro\n";
            isValid = false;
        }
        if(evaluarContrasena){
            if(txtContrasenna.getText().isEmpty() || txtContrasennaConfirmacion.getText().isEmpty() && isValid){
                mensajeErrores+="Se necesita una contraseña y su confirmación para completar el registro\n";
                isValid = false;
            }else if(!(txtContrasenna.getText().equals(txtContrasennaConfirmacion.getText()) && isValid)){
                mensajeErrores+="La contraseña no coincide con su confirmación\n";
                isValid = false;
            }else if(!isValidPassword(txtContrasenna.getText())) isValid = false;
        }


        if(!isValid) new Mensaje().show(Alert.AlertType.ERROR, "Error de información", mensajeErrores);
        else if(usuarioSeleccionado==null) {

            boolean isCedulaDisponible = (boolean) new UsuarioService().isCedulaRegistrada(txtCedula.getText()).getResultado("data");
            if(!isCedulaDisponible) new Mensaje().show(Alert.AlertType.ERROR, "Error de información", "Ya se encuentra un usuario registrado con la cédula ingresada");
            isValid = isCedulaDisponible;
        }


        return  isValid;
    }


    public  boolean isValidPassword(String password)
    {
        String mensajeErrores = "";
        boolean isValid = true;
        String numbers = "(.*[0-9].*)";
        String upperCaseChars = "(.*[A-Z].*)";
        String lowerCaseChars = "(.*[a-z].*)";

        if (password.length() > 15 || password.length() < 6)
        {
            mensajeErrores += "La contraseña debe tener más de 6 caracteres y menos de 15\n";
            isValid = false;
        }

        if (!password.matches(upperCaseChars ))
        {
            mensajeErrores += "La contraseña debe tener al menos una letra mayúscula\n";
            isValid = false;
        }
        if (!password.matches(lowerCaseChars ))
        {
            mensajeErrores += "La contraseña debe tener al menos una letra minúscula\n";
            isValid = false;
        }

        if (!password.matches(numbers ))
        {
            mensajeErrores += "La contraseña debe tener al menos un número\n";
            isValid = false;
        }
        if(!isValid) new Mensaje().show(Alert.AlertType.ERROR, "Error de información", mensajeErrores);
        return isValid;
    }

    private AreaDto getAreaByRole(String role){
        AreaDto areaDto = new AreaDto();
        if(role.equals("GESTOR_CONTROL_VUELOS") || role.equals("GERENTE_CONTROL_VUELO") || role.equals("AUDITOR_CONTROL_VUELOS")) areaDto.setId((long) 1);
        if(role.equals("GESTOR_SERVICIOS_AERONAVES") || role.equals("GERENTE_SERVICIOS_AERONAVES") || role.equals("AUDITOR_SERVICIOS_AERONAVES")) areaDto.setId((long) 2);
        if(role.equals("GESTOR_MANTENIMIENTO_AEROPUERTO") || role.equals("GERENTE_MANTENIMIENTO_AEROPUERTO") || role.equals("AUDITOR_MANTENIMIENTO_AEROPUERTO")) areaDto.setId((long) 2);
        if(role.equals("ADMINISTRADOR")) areaDto.setId((long) 4);
        return areaDto;
    }

    public void cargarRoles() {
        Thread th = new Thread(() -> {
            Respuesta respuesta = new RolService().getAll();
            Platform.runLater(() -> {
                if (respuesta.getEstado()) {
                    rolList = (List<RolDto>) respuesta.getResultado("data");
                    rolList.forEach(r -> cbxRoles.getItems().add(r.getNombre()));
                    cbxRoles.setPromptText("Seleccione un rol");
                } else {
                    new Mensaje().showModal(Alert.AlertType.ERROR, "Falla al extraer areas", this.getStage(), respuesta.getMensaje());

                }

            });
        });
        th.start();
    }

    public void cargarAreas() {
        Thread th = new Thread(() -> {

            AreaService areaService = new AreaService();
            Respuesta respuesta = areaService.getAll();
            Platform.runLater(() -> {
                if (respuesta != null) {
                    areaList = (List<AreaDto>) respuesta.getResultado("data");
                } else {
                    new Mensaje().showModal(Alert.AlertType.ERROR, "Falla al extraer areas", this.getStage(), respuesta.getMensaje());

                }
                for (AreaDto areaDto : areaList) {
                    cbxAreas.getItems().add(areaDto.getNombre());
                }

            });
        }
        );
        th.start();
//
    }

    public void DatosEdicion() {
        if (AppContext.getInstance().get("usuarioEdit") != null) {
            FlowController.changeSuperiorTittle("Modificar Usuario");
            FlowController.changeCodeScreenTittle("UG200");
            UsuarioDto usuarioDto = (UsuarioDto) AppContext.getInstance().get("usuarioEdit");
            usuarioSeleccionado = usuarioDto;
            modificarButton.setVisible(true);
            txtTitulo.setText("Modificar Usuario");
            txtNombre.setText(usuarioDto.getNombre());
            txtCedula.setText(usuarioDto.getCedula());
            txtApellido.setText(usuarioDto.getApellidos());
            lblFechaModificacion.setVisible(true);
            lblFechaModificacion.setText("Fecha Modificacion:" + usuarioDto.getFechaModificacion());
            dtpFechaNacimiento.setValue(usuarioDto.getFechaNacimiento().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
            checkActivo.setSelected(usuarioDto.getActivo());
            txtContrasenna.setPromptText("Click para cambiar contraseña");
            txtContrasennaConfirmacion.setVisible(false);
            btnGuardo.setVisible(false);
            cbxRoles.getSelectionModel().select(usuarioDto.getRolNombre());

        }
    }

    @FXML
    public void d(ScrollEvent event) {
    }

    public void cambiarContraseniaOnAction(ActionEvent actionEvent) {

        if(!cambiarContrasena){
            txtContrasennaConfirmacion.setVisible(true);
            txtContrasenna.setVisible(true);
            cambiarContrasena = true;
            cambiarContrasenaButton.setVisible(false);
        }

    }

    public void modificarButtonOnAction(ActionEvent actionEvent) {

        if(isValidInformation(!txtContrasenna.getText().isEmpty())){
           container.setDisable(true);
           aModoEspera(modificarButton);
            Thread thread = new Thread(this::modificarUsuario);

            thread.start();

        }

    }

    private void modificarUsuario() {
        Optional<RolDto> rolDto = rolList.stream().filter(r -> cbxRoles.getValue().equals(r.getNombre())).findFirst();

        UsuarioDto usuarioDto = new UsuarioDto(txtCedula.getText(), txtNombre.getText(), txtApellido.getText(), txtContrasenna.getText(), Timestamp.valueOf(LocalDateTime.of(dtpFechaNacimiento.getValue(), LocalTime.MIN)), checkActivo.isSelected(), getAreaByRole(cbxRoles.getValue()));
        usuarioDto.setId(usuarioSeleccionado.getId());
        Respuesta respuesta = new UsuarioService().update(usuarioDto);
        Platform.runLater(()->{

            if (respuesta.getEstado()) {
                RolUsuarioDto rolUsuario = new RolUsuarioDto(Timestamp.valueOf(LocalDateTime.now()), Timestamp.valueOf(LocalDateTime.now()), true, rolDto.get(), usuarioDto);
                rolUsuario.setId(usuarioSeleccionado.getRolUsuarioList().get(0).getId());
                Respuesta repuestaRol = new RolUsuarioService().update(rolUsuario);
                if(repuestaRol.getEstado()){
                    new BitacoraService().create("Modificó usuario con ID: " + usuarioDto.getId());
                    new Mensaje().showInformation("Se ha modificado  el usuario con éxito");
                }else {
                    new Mensaje().show(Alert.AlertType.ERROR, "Error al modificar el rol", "No se ha podido asignar el rol al usuario");
                }

            }else {
                new Mensaje().show(Alert.AlertType.ERROR, "Error de información", respuesta.getMensaje());
            }
            container.setDisable(false);
            salirModoEspera(modificarButton, "Modificar");
        });
    }

    public void textContrasenaOnAction(ActionEvent actionEvent) {

        if(usuarioSeleccionado!=null){
            txtContrasennaConfirmacion.setVisible(true);
            txtContrasenna.setPromptText("Nueva contraseña");
        }
    }

    public void textContrasenaOnClick(MouseEvent mouseEvent) {

        if(usuarioSeleccionado!=null){
            txtContrasennaConfirmacion.setVisible(true);
            txtContrasenna.setPromptText("Nueva contraseña");
        }
    }
}
