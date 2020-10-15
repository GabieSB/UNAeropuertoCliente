package org.una.unaeropuertoclient.controller;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXToggleButton;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.DatePicker;
import org.una.unaeropuertoclient.model.AvionDto;
import org.una.unaeropuertoclient.model.ServicioMantenimientoDto;
import org.una.unaeropuertoclient.model.TipoServicioDto;
import org.una.unaeropuertoclient.service.AvionService;
import org.una.unaeropuertoclient.service.ServicioMantenimientoService;
import org.una.unaeropuertoclient.service.TipoServicioService;
import org.una.unaeropuertoclient.utils.Respuesta;

import java.net.URL;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class GestorServiciosController extends Controller implements Initializable {

    public JFXComboBox<String> comboxTipos;
    public JFXTextField txtNumeroFactura;
    public JFXTextField txtEstadoPago;
    public JFXTextField txtEstaFinalizado;
    public JFXTextField txtAvion;
    public JFXToggleButton btonFinalizado;
    public JFXToggleButton btnEstadoPago;
    public DatePicker dateServicio;
    List<TipoServicioDto> tiposServicios;
    TipoServicioService tipoServicioService = new TipoServicioService();
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Respuesta respuesta = tipoServicioService.getAll();
        if( respuesta.getEstado()){
            tiposServicios = (List<TipoServicioDto>) respuesta.getResultado("data");
            llenarComboBoxTipos();
        }

    }
    private void iniciarComponetes(){
        ObservableList<String> items = FXCollections.observableArrayList();
        tiposServicios.forEach(k-> items.add(k.getNombre()));
        comboxTipos.getItems().addAll(items);
        dateServicio.setValue(LocalDate.now());
        dateServicio.

    }
    private void llenarComboBoxTipos(){


    }
    @Override
    public void initialize() {

    }

    private  ServicioMantenimientoDto getServicioMantenimiento(){
        Optional<TipoServicioDto> optTipo  = tiposServicios.stream().filter(t ->    t.getNombre().equals(comboxTipos.getSelectionModel().getSelectedItem())).findFirst();
        AvionService avionService = new AvionService();
        Respuesta respuesta =  avionService.getById(txtAvion.getText());
        AvionDto avion  = null;
        if(respuesta.getEstado()) avion = (AvionDto) respuesta.getResultado("data");
        ServicioMantenimientoDto servicioMantenimiento = new ServicioMantenimientoDto(Long.parseLong(txtNumeroFactura.getText()), btnEstadoPago.isSelected(), btonFinalizado.isSelected(),avion, optTipo.get() );
        return  servicioMantenimiento;
    }

    @FXML
    public void registrarServicio(ActionEvent actionEvent) {

        ServicioMantenimientoService  service = new ServicioMantenimientoService();

        service.create(getServicioMantenimiento());

    }
}
