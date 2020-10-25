package org.una.unaeropuertoclient.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXToggleButton;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
import javafx.util.converter.LocalDateStringConverter;
import org.una.unaeropuertoclient.model.*;
import org.una.unaeropuertoclient.service.AvionService;
import org.una.unaeropuertoclient.service.CobroService;
import org.una.unaeropuertoclient.service.ServicioMantenimientoService;
import org.una.unaeropuertoclient.service.TipoServicioService;
import org.una.unaeropuertoclient.utils.*;

import java.net.URL;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.FormatStyle;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class GestorServiciosController extends Controller implements Initializable {

    public JFXComboBox<String> comboxTipos;
    public JFXTextField txtNumeroFactura;
    public JFXTextField txtAvion;
    public JFXToggleButton btonFinalizado;
    public JFXToggleButton btnEstadoPago;
    public DatePicker dateServicio;
    public JFXTextField txtCobro;
    public JFXToggleButton btonActivo;
    public JFXButton btonRegistrar;
    List<TipoServicioDto> tiposServicios;
    TipoServicioService tipoServicioService = new TipoServicioService();
    ServicioMantenimientoService  service = new ServicioMantenimientoService();
    CobroService cobroService = new CobroService();
    ServicioMantenimientoDto servicioSelecionado  = null;
    Boolean componentesIniciados = false;
    boolean isGestor;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        FlowController.changeSuperiorTittle("Registrar Servicio Mantenimiento de Aviones");
      iniciarComponetes();

    }
    private void iniciarComponetes(){

        Respuesta respuesta = tipoServicioService.getAll();
        if( respuesta.getEstado()){
            tiposServicios = (List<TipoServicioDto>) respuesta.getResultado("data");
            llenarComboBoxTipos();
        }

        dateServicio.setValue(LocalDate.now());
        dateServicio.setConverter(new LocalDateStringConverter(FormatStyle.FULL));
        componentesIniciados = true;
    }
    private void llenarComboBoxTipos(){
        ObservableList<String> items = FXCollections.observableArrayList();
        tiposServicios.forEach(k-> items.add(k.getNombre()));
        comboxTipos.getItems().addAll(items);


    }
    @Override
    public void initialize() {
        FlowController.changeSuperiorTittle("Registrar Servicio Mantenimiento de Aviones");
        AvionDto avionDto = (AvionDto) AppContext.getInstance().get("avionSeleccionado");
        if(avionDto!=null){
            txtAvion.setText(avionDto.getMatricula());
        }

        if(servicioSelecionado==null){
           cargarServicioAModificar();
        }

    }

    public void cargarServicioAModificar(){
        servicioSelecionado = (ServicioMantenimientoDto) AppContext.getInstance().get("servicioSeleccionado");
        if(servicioSelecionado!=null){
            if(!componentesIniciados) iniciarComponetes();
            btonRegistrar.setText("MODIFICAR");
            cargarDatosServicio();
        }
    }

    public void cargarDatosServicio(){
        dateServicio.setValue(servicioSelecionado.getFechaServicio().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
        txtNumeroFactura.setText(servicioSelecionado.getNumeroFactura().toString());
        btonFinalizado.setSelected(servicioSelecionado.getEstaFinalizacion());
        btnEstadoPago.setSelected(servicioSelecionado.getEstadoPago());
//        btonActivo.setSelected(servicioSelecionado.getActivo());
        txtAvion.setText(servicioSelecionado.getAvionesId().getMatricula());
        comboxTipos.getSelectionModel().select(servicioSelecionado.getTiposServiciosId().getNombre());
        txtCobro.setText(servicioSelecionado.getCobroList().get(0).getMonto().toString());


    }

    private  void limpiar(){

        dateServicio.setValue(LocalDate.now());
        txtNumeroFactura.setText("");
        btonFinalizado.setSelected(false);
        btnEstadoPago.setSelected(false);
        btonActivo.setSelected(false);
        txtAvion.setText("");

    }

    private  ServicioMantenimientoDto crearServicioMantenimientoConDatosIngresados(){
        Optional<TipoServicioDto> optTipo  = tiposServicios.stream().filter(t ->    t.getNombre().equals(comboxTipos.getSelectionModel().getSelectedItem())).findFirst();
        AvionService avionService = new AvionService();
        Respuesta respuesta =  avionService.getByMatricula(txtAvion.getText());
        AvionDto avion  = null;
        if(respuesta.getEstado()) avion = (AvionDto) respuesta.getResultado("data");
        boolean sePuedeCrear = true;
        if(optTipo.isEmpty())  {
            new Mensaje().show(Alert.AlertType.INFORMATION, "Información", "Parece que no has  seleccionado un tipo de servicio");
            sePuedeCrear = false;
        }
        if(!Validar.isLongNumber(txtNumeroFactura.getText())) {
            new Mensaje().show(Alert.AlertType.INFORMATION, "Información", "Parece que el valor ingresado en el número de factura no es permitido, debería ser un número"); sePuedeCrear = false;}
        if(avion==null) {
            new Mensaje().show(Alert.AlertType.INFORMATION, "Información", "Parece que ha habido problemas al registrar la matrícula del avión"); sePuedeCrear = false;}


        if (sePuedeCrear) {
            ServicioMantenimientoDto  servicioMantenimiento  = new ServicioMantenimientoDto(dateServicio.getValue(),Long.parseLong(txtNumeroFactura.getText()), btnEstadoPago.isSelected(), btonFinalizado.isSelected(),avion, optTipo.get() );
            servicioMantenimiento.setActivo(true);
            return servicioMantenimiento;
        }else return  null;

    }

    @FXML
    public void registrarServicio(ActionEvent actionEvent) {

        if(servicioSelecionado==null) registrarServicio();
        else  modificarServicio();
    }
    public void registrarServicio(){
        ServicioMantenimientoDto  servicioMantenimientoDto = crearServicioMantenimientoConDatosIngresados();
        if(servicioMantenimientoDto!=null){
            Respuesta respuesta = service.create(servicioMantenimientoDto);
            if(respuesta.getEstado()){
                CobroDto cobroDto = new CobroDto(Float.parseFloat(txtCobro.getText()), "Cobro de servicio");
                ServicioMantenimientoDto servicioMantenimientoDto1 = (ServicioMantenimientoDto) respuesta.getResultado("data");
                servicioMantenimientoDto.setId(servicioMantenimientoDto1.getId());
                cobroDto.setServiciosMantenimientoId(servicioMantenimientoDto);
                Respuesta respuesta1 = cobroService.create(cobroDto);
                System.out.println("antes de cobro");
                if(respuesta1.getEstado()){
                    new Mensaje().show(Alert.AlertType.INFORMATION, "Información", "Servicio Registrado Exitosamente");
                }
            }
        }

    }

    public void modificarServicio(){
        ServicioMantenimientoDto  servicioMantenimientoDto = crearServicioMantenimientoConDatosIngresados();
        if(servicioMantenimientoDto!=null){
            servicioMantenimientoDto.setId(servicioSelecionado.getId());
            Respuesta respuesta = service.update(servicioMantenimientoDto);
            if(respuesta.getEstado()){
                CobroDto cobroDto =  new CobroDto(Float.parseFloat(txtCobro.getText()), "Cobro de servicio");
                cobroDto.setId(servicioSelecionado.getCobroList().get(0).getId());
                cobroDto.setServiciosMantenimientoId(servicioMantenimientoDto);
                Respuesta respuesta1 = cobroService.update(cobroDto);
                if(respuesta1.getEstado()){
                    new Mensaje().show(Alert.AlertType.INFORMATION, "Información", "Servicio Modificado Exitosamente");
                }else{
                    new Mensaje().show(Alert.AlertType.ERROR, "Información", "Hubo un error en registrar el servicio");
                }
            }
        }

    }


    public void buscarAvion(ActionEvent actionEvent) {
        FlowController.getInstance().goView("BuscarAvion");
    }
}
