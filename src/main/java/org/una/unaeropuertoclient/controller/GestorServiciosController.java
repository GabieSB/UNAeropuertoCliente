package org.una.unaeropuertoclient.controller;

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
import org.una.unaeropuertoclient.model.AvionDto;
import org.una.unaeropuertoclient.model.CobroDto;
import org.una.unaeropuertoclient.model.ServicioMantenimientoDto;
import org.una.unaeropuertoclient.model.TipoServicioDto;
import org.una.unaeropuertoclient.service.AvionService;
import org.una.unaeropuertoclient.service.CobroService;
import org.una.unaeropuertoclient.service.ServicioMantenimientoService;
import org.una.unaeropuertoclient.service.TipoServicioService;
import org.una.unaeropuertoclient.utils.AppContext;
import org.una.unaeropuertoclient.utils.FlowController;
import org.una.unaeropuertoclient.utils.Mensaje;
import org.una.unaeropuertoclient.utils.Respuesta;

import java.net.URL;
import java.time.LocalDate;
import java.time.format.FormatStyle;
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
    List<TipoServicioDto> tiposServicios;
    TipoServicioService tipoServicioService = new TipoServicioService();
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        System.out.println("inicia en url");
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
    }
    private void llenarComboBoxTipos(){
        ObservableList<String> items = FXCollections.observableArrayList();
        tiposServicios.forEach(k-> items.add(k.getNombre()));
        comboxTipos.getItems().addAll(items);


    }
    @Override
    public void initialize() {
        AvionDto avionDto = (AvionDto) AppContext.getInstance().get("avionSeleccionado");
        if(avionDto!=null){
            txtAvion.setText(avionDto.getMatricula());
        }
    }

    private  ServicioMantenimientoDto getServicioMantenimiento(){
        Optional<TipoServicioDto> optTipo  = tiposServicios.stream().filter(t ->    t.getNombre().equals(comboxTipos.getSelectionModel().getSelectedItem())).findFirst();
        AvionService avionService = new AvionService();

        Respuesta respuesta =  avionService.getByMatricula(txtAvion.getText());
        AvionDto avion  = null;

        if(respuesta.getEstado()) avion = (AvionDto) respuesta.getResultado("data");



        ServicioMantenimientoDto  servicioMantenimiento  = new ServicioMantenimientoDto(dateServicio.getValue(),Long.parseLong(txtNumeroFactura.getText()), btnEstadoPago.isSelected(), btonFinalizado.isSelected(),avion, optTipo.get() );


        return  servicioMantenimiento;
    }

    @FXML
    public void registrarServicio(ActionEvent actionEvent) {
        CobroService cobroService = new CobroService();
        ServicioMantenimientoService  service = new ServicioMantenimientoService();
        ServicioMantenimientoDto  servicioMantenimientoDto = getServicioMantenimiento();
        Respuesta respuesta = service.create(servicioMantenimientoDto);
        if(respuesta.getEstado()){
            CobroDto cobroDto = new CobroDto(Float.parseFloat(txtCobro.getText()), "Cobro de servicio");
            ServicioMantenimientoDto servicioMantenimientoDto1 = (ServicioMantenimientoDto) respuesta.getResultado("data");
            servicioMantenimientoDto.setId(servicioMantenimientoDto1.getId());
            cobroDto.setServiciosMantenimientoId(servicioMantenimientoDto);
            Respuesta respuesta1 = cobroService.create(cobroDto);
            if(respuesta1.getEstado()){
                new Mensaje().show(Alert.AlertType.INFORMATION, "Información", "Servicio Registrado Exitosamente");
            }
        }

    }

    public void buscarAvion(ActionEvent actionEvent) {

        FlowController.getInstance().goView("BuscarAvion");
        System.out.println("regresa");
    }
}
