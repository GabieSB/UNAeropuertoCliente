package org.una.unaeropuertoclient.controller;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXToggleButton;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ToggleButton;
import javafx.util.converter.LocalDateStringConverter;
import org.una.unaeropuertoclient.model.*;
import org.una.unaeropuertoclient.service.AvionService;
import org.una.unaeropuertoclient.service.GastoReparacionService;
import org.una.unaeropuertoclient.service.TipoReparacionService;
import org.una.unaeropuertoclient.utils.*;

import java.net.URL;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.FormatStyle;
import java.util.*;

public class RegistrarGastosController  extends Controller implements Initializable {
    public DatePicker dateRegistro;
    public JFXTextField txtNumeroContrato;
    public JFXTextField txtDuracion;
    public JFXTextField txtPeriocidad;
    public JFXTextField txtObservaciones;
    public JFXComboBox comboxTipo;
    public JFXToggleButton btnEstadoPago;
    List<TipoReparacionDto> tipoReparacion = new ArrayList<>();
    boolean componentesIniciados = false;

    GastoReparacionDto gastoSeleccionado = null;

    TipoReparacionService  tipoReparacionService = new TipoReparacionService();
    GastoReparacionService gastoReparacionService = new GastoReparacionService();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        FlowController.changeSuperiorTittle("Registrar Gasto Mantenimiento");
      iniciarComponentes();

      if(gastoSeleccionado==null){
          cargarGastoModificar();
      }
    }
    public void iniciarComponentes(){
        llenarCombox();
        dateRegistro.setValue(LocalDate.now());
        dateRegistro.setConverter(new LocalDateStringConverter(FormatStyle.FULL));
        componentesIniciados = true;
    }


    public void cargarGastoModificar(){
        gastoSeleccionado = (GastoReparacionDto) AppContext.getInstance().get("gastoSeleccionado");
        if(gastoSeleccionado!=null){
            if(!componentesIniciados) iniciarComponentes();
            cargarDatosGasto();
        }

    }

    private void cargarDatosGasto() {

        comboxTipo.getSelectionModel().select(gastoSeleccionado.getTiposId().getNombre());
        dateRegistro.setValue(gastoSeleccionado.getFechaRegistro().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
        btnEstadoPago.setSelected(gastoSeleccionado.getEstadoPago());
        txtDuracion.setText(gastoSeleccionado.getDuracion().toString());
        txtPeriocidad.setText(gastoSeleccionado.getPeriodicidad().toString());
        txtNumeroContrato.setText(gastoSeleccionado.getNumeroContrato().toString());
        txtObservaciones.setText(gastoSeleccionado.getObservaciones());

    }

    void llenarCombox(){
        Respuesta respuesta = tipoReparacionService.getActivos();
        if(respuesta.getEstado()){
            tipoReparacion = (List<TipoReparacionDto>) respuesta.getResultado("data");
            ObservableList<String> items = FXCollections.observableArrayList();
            tipoReparacion.forEach(k-> items.add(k.getNombre()));
            comboxTipo.getItems().addAll(items);
        }
    }



    private GastoReparacionDto crearGastoReparacionConDatosIngresados(){
        boolean sePuedeCrear = true;
        Optional<TipoReparacionDto> optTipo  = tipoReparacion.stream().filter(t -> t.getNombre().equals(comboxTipo.getSelectionModel().getSelectedItem())).findFirst();

        if(!optTipo.isPresent())  {
            new Mensaje().show(Alert.AlertType.INFORMATION, "Información", "Parece que no has  seleccionado un tipo de  reparación");
            sePuedeCrear = false;
        }
        if(!Validar.isLongNumber(txtNumeroContrato.getText())) {
            new Mensaje().show(Alert.AlertType.INFORMATION, "Información", "Parece que el valor ingresado en el número de contrato no es permitido, debería ser un número"); sePuedeCrear = false;}
        if(!Validar.isLongNumber(txtPeriocidad.getText())){
            new Mensaje().show(Alert.AlertType.INFORMATION, "Información", "Parece que el valor ingresado en los días de periocidad no es permitido, debería ser un número"); sePuedeCrear = false;}
        if(!Validar.isLongNumber(txtDuracion.getText())){
            new Mensaje().show(Alert.AlertType.INFORMATION, "Información", "Parece que el valor ingresado en los días de duración no es permitido, debería ser un número"); sePuedeCrear = false;}
        if(txtObservaciones.getText().isEmpty()){
            new Mensaje().show(Alert.AlertType.INFORMATION, "Información", "Parece que el campo de observacopn está en blanco. Este campo es necesario para realizar el registro"); sePuedeCrear = false;};

        if(sePuedeCrear){
            AuthenticationResponse auth = (AuthenticationResponse) AppContext.getInstance().get("token");
            GastoReparacionDto  gastoReparacion  = new GastoReparacionDto(dateRegistro.getValue(), btnEstadoPago.isSelected(),Long.parseLong(txtNumeroContrato.getText()), Integer.parseInt(txtDuracion.getText()),Integer.parseInt(txtPeriocidad.getText()), txtObservaciones.getText(), auth.getUsuario().getAreasId(), optTipo.get());
            return  gastoReparacion;
        }else return null;

    }
    @Override
    public void initialize() {
        FlowController.changeSuperiorTittle("Registrar Gasto Mantenimiento");
        if(gastoSeleccionado==null){
            cargarGastoModificar();
        }
    }

    public void guardarRegistro(ActionEvent actionEvent) {
        Respuesta respuesta;
        GastoReparacionDto gastoReparacionDto = crearGastoReparacionConDatosIngresados();
        if(gastoReparacionDto != null){
            if(gastoSeleccionado == null){

                respuesta = gastoReparacionService.create(gastoReparacionDto);
                if(respuesta.getEstado()){
                    new Mensaje().show(Alert.AlertType.INFORMATION, "Información", "Gasto Mantenimiento registrado correctamente");
                }
            }else{
                gastoReparacionDto.setId(gastoSeleccionado.getId());
                respuesta = gastoReparacionService.update(gastoReparacionDto);
                if(respuesta.getEstado()){
                    new Mensaje().show(Alert.AlertType.INFORMATION, "Información", "Gasto Mantenimiento modificado correctamente");
                }else{
                    new Mensaje().show(Alert.AlertType.WARNING, "Información", respuesta.getMensaje());
                }
            }
        }



    }
}
