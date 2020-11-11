package org.una.unaeropuertoclient.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXToggleButton;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.converter.LocalDateStringConverter;
import org.una.unaeropuertoclient.model.*;
import static org.una.unaeropuertoclient.utils.ButtonWaitUtils.*;
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
    public Label titleLabel;
    public HBox editarButtonOnAction;
    public VBox container;
    List<TipoServicioDto> tiposServicios;
    TipoServicioService tipoServicioService = new TipoServicioService();
    ServicioMantenimientoService  service = new ServicioMantenimientoService();
    CobroService cobroService = new CobroService();
    ServicioMantenimientoDto servicioSelecionado  = null;
    Boolean componentesIniciados = false;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        FlowController.changeSuperiorTittle("Registrar Servicio Mantenimiento de Aviones");
      iniciarComponetes();

    }
    private void iniciarComponetes(){
        comboxTipos.setPromptText("Cargando...");
        Thread th = new Thread(() -> llenarComboBoxTipos());
        th.start();
        dateServicio.setConverter(new LocalDateStringConverter(FormatStyle.FULL));
        componentesIniciados = true;
    }
    public void llenarComboBoxTipos(){
        Respuesta respuesta = tipoServicioService.getAll();

        Platform.runLater(()->{
            if( respuesta.getEstado()){
                tiposServicios = (List<TipoServicioDto>) respuesta.getResultado("data");
                ObservableList<String> items = FXCollections.observableArrayList();
                tiposServicios.forEach(k-> items.add(k.getNombre()));
                comboxTipos.getItems().clear();
                comboxTipos.getItems().addAll(items);
                comboxTipos.setPromptText("Tipo de servicio");

            }else{
                comboxTipos.setPromptText(respuesta.getMensaje());
            }
        });

    }
    @Override
    public void initialize() {
        FlowController.changeSuperiorTittle("Registrar Servicio Mantenimiento de Aviones");
        FlowController.changeCodeScreenTittle("SG000");

        int modoSeleccionado = (int) AppContext.getInstance().get("mode");
        if(modoSeleccionado == 3) modoDevelop();
        else {
            AvionDto avionDto = (AvionDto) AppContext.getInstance().get("avionSeleccionado");
            if(avionDto!=null){
                txtAvion.setText(avionDto.getMatricula());
            }

            if(servicioSelecionado==null){
                cargarServicioAModificar();
            }
        }
    }

    private void modoDevelop() {
        container.setDisable(true);
    }

    public void cargarServicioAModificar(){
        FlowController.changeSuperiorTittle("Modificar Servicio Mantenimiento de Aviones");
        titleLabel.setText("MODIFICAR SERVICIO");
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
        if(dateServicio.getValue()==null){
            new Mensaje().show(Alert.AlertType.INFORMATION, "Información", "Parece que no has ingresado la fecha del servicio"); sePuedeCrear = false;}



        if (sePuedeCrear) {
            ServicioMantenimientoDto  servicioMantenimiento  = new ServicioMantenimientoDto(dateServicio.getValue(),Long.parseLong(txtNumeroFactura.getText()), btnEstadoPago.isSelected(), btonFinalizado.isSelected(),avion, optTipo.get() );
            servicioMantenimiento.setActivo(true);
            return servicioMantenimiento;
        }else return  null;

    }

    @FXML
    public void registrarServicio(ActionEvent actionEvent) {
        aModoEspera(btonRegistrar);
        container.setDisable(true);
        if(servicioSelecionado==null) {

            Thread t = new Thread(this::registrarServicio);
            t.start();
        }
        else {
            Thread t = new Thread(this::modificarServicio);
            t.start();

        }
    }
    public void registrarServicio(){
        ServicioMantenimientoDto  servicioMantenimientoDto = crearServicioMantenimientoConDatosIngresados();
        if(servicioMantenimientoDto!=null) {
            Platform.runLater(() -> {
                Respuesta respuesta = service.create(servicioMantenimientoDto);
                if (respuesta.getEstado()) {
                    CobroDto cobroDto = new CobroDto(Float.parseFloat(txtCobro.getText()), "Cobro de servicio");
                    ServicioMantenimientoDto servicioMantenimientoDto1 = (ServicioMantenimientoDto) respuesta.getResultado("data");
                    servicioMantenimientoDto.setId(servicioMantenimientoDto1.getId());
                    cobroDto.setServiciosMantenimientoId(servicioMantenimientoDto);
                    Respuesta respuesta1 = cobroService.create(cobroDto);
                    if (respuesta1.getEstado()) new Mensaje().show(Alert.AlertType.INFORMATION, "Información", "Servicio Registrado Exitosamente");
                    else new Mensaje().show(Alert.AlertType.ERROR, "Error de registro de cobro", respuesta1.getMensaje());

                } else {
                     new Mensaje().show(Alert.AlertType.ERROR, "Error de registro de Servicio de Aeronave", respuesta.getMensaje());
                }
                container.setDisable(false);
                salirModoEspera(btonRegistrar, "REGISTRAR");
                limpiar();

            });
        }
    }

    public void modificarServicio(){
        ServicioMantenimientoDto  servicioMantenimientoDto = crearServicioMantenimientoConDatosIngresados();
        if(servicioMantenimientoDto!=null){
            servicioMantenimientoDto.setId(servicioSelecionado.getId());
            Respuesta respuesta = service.update(servicioMantenimientoDto);

            Platform.runLater(()->{
                if(respuesta.getEstado()){
                    CobroDto cobroDto =  new CobroDto(Float.parseFloat(txtCobro.getText()), "Cobro de servicio");
                    cobroDto.setServiciosMantenimientoId(servicioMantenimientoDto);
                    Respuesta respuesta1;
                    if(servicioSelecionado.getCobroList() == null || servicioSelecionado.getCobroList().isEmpty()){
                        respuesta1 = cobroService.create(cobroDto);
                    }else {
                        cobroDto.setId(servicioSelecionado.getCobroList().get(0).getId());
                        respuesta1 = cobroService.update(cobroDto);
                    }

                    if(respuesta1.getEstado()){
                        new Mensaje().show(Alert.AlertType.INFORMATION, "Información", "Servicio Modificado Exitosamente");
                    }else{
                        new Mensaje().show(Alert.AlertType.ERROR, "Información", "Hubo un error al modificar el cobro del servicio");
                    }
                }else  new Mensaje().show(Alert.AlertType.ERROR, "Información", "Hubo un error al modificar el servicio");

                container.setDisable(false);
                salirModoEspera(btonRegistrar, "MODIFICAR");
            });

        }

    }


    public void buscarAvionOnAction(ActionEvent actionEvent) {
        FlowController.getInstance().goView("BuscarAvion");
    }

}
