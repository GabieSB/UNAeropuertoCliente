package org.una.unaeropuertoclient.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXToggleButton;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.util.converter.LocalDateStringConverter;
import org.una.unaeropuertoclient.model.*;
import org.una.unaeropuertoclient.service.BitacoraService;
import org.una.unaeropuertoclient.service.GastoReparacionService;
import org.una.unaeropuertoclient.service.ProveedorService;
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
    public JFXComboBox<String> comboxTipo;
    public JFXToggleButton btnEstadoPago;
    public JFXComboBox<String> comboxProveedores;
    public JFXTextField txtCobro;
    public JFXButton guardarButton;
    public VBox container;
    List<TipoReparacionDto> tipoReparacion = new ArrayList<>();
    List<ProvedorDto> provedores = new ArrayList<>();
    boolean componentesIniciados = false;

    GastoReparacionDto gastoSeleccionado = null;

    TipoReparacionService  tipoReparacionService = new TipoReparacionService();
    GastoReparacionService gastoReparacionService = new GastoReparacionService();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        FlowController.changeSuperiorTittle("Registrar Gasto Mantenimiento");
      iniciarComponentes();
      cargarGastoModificar();
    }
    public void iniciarComponentes(){
        Thread thread = new Thread(this::llenarComboxTiposReparaciones);
        thread.start();
        Thread thread1 = new Thread(this::llenarComboxProveedores);
        thread1.start();

        dateRegistro.setValue(LocalDate.now());
        dateRegistro.setConverter(new LocalDateStringConverter(FormatStyle.FULL));
        componentesIniciados = true;
    }


    public void cargarGastoModificar(){
        gastoSeleccionado = (GastoReparacionDto) AppContext.getInstance().get("gastoSeleccionado");
        if(gastoSeleccionado!=null){
            guardarButton.setText("Modificar");
            FlowController.changeSuperiorTittle("Modicar Gasto Mantenimiento");
            if(!componentesIniciados) iniciarComponentes();
            cargarDatosGasto();
        }

    }

    private void cargarDatosGasto() {

        comboxTipo.getSelectionModel().select(gastoSeleccionado.getTiposId().getNombre());
        comboxProveedores.getSelectionModel().select(gastoSeleccionado.getProvedoresId().getNombre());
        dateRegistro.setValue(gastoSeleccionado.getFechaRegistro().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
        btnEstadoPago.setSelected(gastoSeleccionado.getEstadoPago());
        txtDuracion.setText(gastoSeleccionado.getDuracion().toString());
        txtPeriocidad.setText(gastoSeleccionado.getPeriodicidad().toString());
        txtNumeroContrato.setText(gastoSeleccionado.getNumeroContrato().toString());
        txtObservaciones.setText(gastoSeleccionado.getObservaciones());
        txtCobro.setText(gastoSeleccionado.getMonto().toString());

    }

    private void llenarComboxTiposReparaciones(){
        Respuesta respuesta = tipoReparacionService.getActivos();

        Platform.runLater(()->{
            if(respuesta.getEstado()){
                tipoReparacion = (List<TipoReparacionDto>) respuesta.getResultado("data");
                ObservableList<String> items = FXCollections.observableArrayList();
                tipoReparacion.forEach(k-> items.add(k.getNombre()));
                comboxTipo.getItems().addAll(items);
                comboxTipo.setPromptText("Seleccione un tipo de reparación");
            }else {
                comboxTipo.setPromptText(respuesta.getMensaje());
            }
        });

    }

    private void llenarComboxProveedores(){
        Respuesta respuesta = new ProveedorService().findActivos();

        Platform.runLater(()->{
            if(respuesta.getEstado()){
                provedores = (List<ProvedorDto>) respuesta.getResultado("data");
                ObservableList<String> items = FXCollections.observableArrayList();
                provedores.forEach(k-> items.add(k.getNombre()));
                comboxProveedores.getItems().addAll(items);
                comboxProveedores.setPromptText("Seleccione un proveedor");
            }else{
                comboxProveedores.setPromptText(respuesta.getMensaje());
            }
        });


    }



    private GastoReparacionDto crearGastoReparacionConDatosIngresados(){
        boolean sePuedeCrear;
        Optional<TipoReparacionDto> optTipo  = tipoReparacion.stream().filter(t -> t.getNombre().equals(comboxTipo.getSelectionModel().getSelectedItem())).findFirst();
        Optional<ProvedorDto> opProveedor  = provedores.stream().filter(t -> t.getNombre().equals(comboxProveedores.getSelectionModel().getSelectedItem())).findFirst();
        sePuedeCrear = isSePuedeCrear(true, optTipo, opProveedor);
        if(sePuedeCrear){
            AreaDto areaActual = new AreaDto();
            areaActual.setId((long) 3);
            return new GastoReparacionDto(dateRegistro.getValue(), btnEstadoPago.isSelected(),Long.parseLong(txtNumeroContrato.getText()), Integer.parseInt(txtDuracion.getText()),Integer.parseInt(txtPeriocidad.getText()), txtObservaciones.getText(), areaActual, optTipo.get(), opProveedor.get(), Float.parseFloat(txtCobro.getText()));
        }else return null;

    }

    private boolean isSePuedeCrear(boolean sePuedeCrear, Optional<TipoReparacionDto> optTipo, Optional<ProvedorDto> opProveedor) {
        if(optTipo.isEmpty())  {
            new Mensaje().show(Alert.AlertType.INFORMATION, "Información", "Parece que no has  seleccionado un tipo de  reparación");
            sePuedeCrear = false;
        }
        if(opProveedor.isEmpty()){
            new Mensaje().show(Alert.AlertType.INFORMATION, "Información", "Parece que no has  seleccionado un proveedor");
            sePuedeCrear = false;
        }
        if(!Validar.isLongNumber(txtNumeroContrato.getText())) {
            new Mensaje().show(Alert.AlertType.INFORMATION, "Información", "Parece que el valor ingresado en el número de contrato no es permitido, debería ser un número"); sePuedeCrear = false;}
        if(!Validar.isLongNumber(txtPeriocidad.getText())){
            new Mensaje().show(Alert.AlertType.INFORMATION, "Información", "Parece que el valor ingresado en los días de periocidad no es permitido, debería ser un número"); sePuedeCrear = false;}
        if(!Validar.isLongNumber(txtDuracion.getText())){
            new Mensaje().show(Alert.AlertType.INFORMATION, "Información", "Parece que el valor ingresado en los días de duración no es permitido, debería ser un número"); sePuedeCrear = false;}
        if(txtObservaciones.getText().isEmpty()){
            new Mensaje().show(Alert.AlertType.INFORMATION, "Información", "Parece que el campo de observacopn está en blanco. Este campo es necesario para realizar el registro"); sePuedeCrear = false;}
        if(!Validar.isFloatNumber(txtCobro.getText())){
            new Mensaje().show(Alert.AlertType.INFORMATION, "Información", "Parece que el valor ingresado en el cobro no es permitido, debería ser un número"); sePuedeCrear = false;}
        return sePuedeCrear;
    }


    @Override
    public void initialize() {
        FlowController.changeSuperiorTittle("Registrar Gasto Mantenimiento");
        iniciarComponentes();
        cargarGastoModificar();
        limpiar();
        int modo = (int) AppContext.getInstance().get("mode");
        if(modo == 3) modoDevelop();

    }

    private void modoDevelop() {
        dateRegistro.setDisable(true);
        txtCobro.setEditable(false);
        txtNumeroContrato.setEditable(false);
        txtPeriocidad.setEditable(false);
        txtDuracion.setEditable(false);
        txtObservaciones.setEditable(false);
        comboxTipo.setDisable(true);
        comboxProveedores.setDisable(true);
        guardarButton.setDisable(true);


    }

    public void guardarRegistro(ActionEvent actionEvent) {
        GastoReparacionDto gastoReparacionDto = crearGastoReparacionConDatosIngresados();
        if(gastoReparacionDto != null){
            ButtonWaitUtils.aModoEspera(guardarButton);
            container.setDisable(true);
            if(gastoSeleccionado == null){

                Thread t = new Thread(()-> registrarGastoReparacion(gastoReparacionDto));
                t.start();

            }else{

                Thread t = new Thread(()->  modificarGastoReparacion(gastoReparacionDto));
                t.start();
            }
        }



    }

    private void modificarGastoReparacion(GastoReparacionDto gastoReparacionDto) {
        Respuesta respuesta;
        gastoReparacionDto.setId(gastoSeleccionado.getId());
        respuesta = gastoReparacionService.update(gastoReparacionDto);
        Platform.runLater(()->{
            if(respuesta.getEstado()){
                AppContext.getInstance().delete("gastoSeleccionado");
                new BitacoraService().create("Modificó un gasto mantenimiento con número ID: "+gastoReparacionDto.getId());
                new Mensaje().show(Alert.AlertType.INFORMATION, "Información", "Gasto Mantenimiento modificado correctamente");
            }else{
                new Mensaje().show(Alert.AlertType.ERROR, "Información", respuesta.getMensaje());
            }
           ButtonWaitUtils.salirModoEspera(guardarButton, "Modificar");
            container.setDisable(false);
        });

    }
    private void limpiar(){
        txtCobro.clear();
        txtObservaciones.clear();
        txtDuracion.clear();
        txtPeriocidad.clear();
        txtNumeroContrato.clear();
        comboxProveedores.getSelectionModel().clearSelection();
        comboxTipo.getSelectionModel().clearSelection();
        dateRegistro.setValue(LocalDate.now());

    }

    private void registrarGastoReparacion(GastoReparacionDto gastoReparacionDto) {
        Respuesta respuesta;
        respuesta = gastoReparacionService.create(gastoReparacionDto);
        Platform.runLater(()->{
            if(respuesta.getEstado()){
                limpiar();
                new BitacoraService().create("Registró gasto mantenimiento con ID: " + ((GastoReparacionDto) respuesta.getResultado("data")).getId());
                new Mensaje().show(Alert.AlertType.INFORMATION, "Información", "Gasto Mantenimiento registrado correctamente");
            }else
                new Mensaje().show(Alert.AlertType.ERROR, "Información", respuesta.getMensaje());
            ButtonWaitUtils.salirModoEspera(guardarButton, "Registrar");
            container.setDisable(false);

        });

    }
}
