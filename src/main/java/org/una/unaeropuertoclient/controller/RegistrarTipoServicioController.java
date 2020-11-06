package org.una.unaeropuertoclient.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TabPane;
import org.una.unaeropuertoclient.model.TipoServicioDto;
import org.una.unaeropuertoclient.service.TipoServicioService;
import org.una.unaeropuertoclient.utils.AppContext;
import org.una.unaeropuertoclient.utils.Mensaje;
import org.una.unaeropuertoclient.utils.Respuesta;

import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class RegistrarTipoServicioController extends Controller implements Initializable {
    
    public JFXTextField txtDescripcionEditar;
    public JFXTextField txtNombreEditar;
    public JFXComboBox comboTipoServicio;
    public JFXTextField txtNombreRegistrar;
    public JFXTextField txtDescripcionRegistrar;
    public TabPane tabServicios;
    public JFXButton modificarButton;
    public JFXButton registrarButton;
    private TipoServicioDto tipoServicioSeleccionadoEditar;
    private List<TipoServicioDto> tiposServicios = null;
    TipoServicioService tipoServicioService = new TipoServicioService();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }


    @Override
    public void initialize() {

    }

    private void cargarServicios() {
        Respuesta respuesta = tipoServicioService.getAll();

        Platform.runLater(()->{
            if( respuesta.getEstado()){
                tiposServicios = (List<TipoServicioDto>) respuesta.getResultado("data");
                ObservableList<String> items = FXCollections.observableArrayList();
                tiposServicios.forEach(k-> items.add(k.getNombre()));
                comboTipoServicio.getItems().clear();
                comboTipoServicio.getItems().addAll(items);
                comboTipoServicio.setPromptText("Tipo de servicio");

                comboTipoServicio.setPromptText("Tipo de servicio");

            }else{
                comboTipoServicio.setPromptText(respuesta.getMensaje());
            }
        });

        System.out.println("Termina");
    }

    private boolean datosValidosRegistrar(){

        if(txtNombreRegistrar.getText().isEmpty() || txtDescripcionRegistrar.getText().isEmpty()){
            new Mensaje().show(Alert.AlertType.ERROR, "Información", "Se requieren que todos los campos estén completos para poder registrar el servicio");
            return false;
        }
        return true;
    }

    private boolean datosValidosEditar(){

        if(txtNombreEditar.getText().isEmpty() || txtDescripcionEditar.getText().isEmpty() || tipoServicioSeleccionadoEditar ==null){
            new Mensaje().show(Alert.AlertType.ERROR, "Información", "Se requieren que todos los campos estén completos para poder editar el servicio");
            return false;
        }
        return true;
    }

    public void registrarButtonOnAction(ActionEvent actionEvent) {
        if(datosValidosRegistrar()){
            TipoServicioDto servicioDto = new TipoServicioDto(txtNombreRegistrar.getText(), txtDescripcionRegistrar.getText());
            registrarButton.setText("Registrando");
            registrarButton.setDisable(true);
            Thread thread = new Thread(()-> solicitarRegistro(servicioDto));
            thread.start();
        }

    }


    private void cargarSelectedServicio(){

        Optional<TipoServicioDto> optTipo  = tiposServicios.stream().filter(t ->    t.getNombre().equals(comboTipoServicio.getSelectionModel().getSelectedItem())).findFirst();
        if(optTipo.isPresent()) {
            tipoServicioSeleccionadoEditar = optTipo.get();

            txtNombreEditar.setText(tipoServicioSeleccionadoEditar.getNombre());
            txtDescripcionEditar.setText(tipoServicioSeleccionadoEditar.getDescripcion());
        }
    }

    private void limpiarEditar(){
        txtDescripcionEditar.setText("");
        txtNombreEditar.setText("");
        comboTipoServicio.getSelectionModel().clearSelection();
    }
    private void limpiarRegistrar(){
        txtDescripcionRegistrar.setText("");
        txtNombreRegistrar.setText("");
    }

    public void editarButtonOnAction(ActionEvent actionEvent) {
        if(datosValidosEditar()){

            TipoServicioDto servicioDto = new TipoServicioDto(txtNombreEditar.getText(), txtDescripcionEditar.getText());
            servicioDto.setId(tipoServicioSeleccionadoEditar.getId());

            modificarButton.setText("Modificando...");
            modificarButton.setDisable(true);
            Thread thread = new Thread(()->solicitarModificacion(servicioDto));
            thread.start();


        }
    }

    private void solicitarModificacion( TipoServicioDto servicioDto){
        Respuesta respuesta = new TipoServicioService().update(servicioDto);

        Platform.runLater(()->{
            if(respuesta.getEstado()){
                tiposServicios.remove(tipoServicioSeleccionadoEditar);
                tiposServicios.add(servicioDto);
                cargarComboServicios();
                limpiarEditar();


                new Mensaje().show(Alert.AlertType.INFORMATION, "Información", "Se modificó el servicio con éxito");
            }else{
                new Mensaje().show(Alert.AlertType.ERROR, "Información", respuesta.getMensaje());
            }
            modificarButton.setText("Modificar");
            modificarButton.setDisable(false);
        });

    }

    private void solicitarRegistro(TipoServicioDto servicioDto){
        Respuesta respuesta = new TipoServicioService().create(servicioDto);

        Platform.runLater(()->{
            if(respuesta.getEstado()){
                limpiarRegistrar();
                new Mensaje().show(Alert.AlertType.INFORMATION, "Información", "Se registró el servicio con éxito");
            }else{
                new Mensaje().show(Alert.AlertType.ERROR, "Información", respuesta.getMensaje());
            }
            registrarButton.setText("Registrar");
            registrarButton.setDisable(false);
        });

    }

    private void  cargarComboServicios(){


        comboTipoServicio.getItems().clear();
        ObservableList<String> items = FXCollections.observableArrayList();
        tiposServicios.forEach(k-> items.add(k.getNombre()));
        comboTipoServicio.getItems().addAll(items);

    }

    public void tabSelected(Event event) {
        System.out.println("1");
        comboTipoServicio.setPromptText("Cargando...");
        if(tabServicios.getSelectionModel().isSelected(1) && tiposServicios == null){
            System.out.println("2");
            Thread th = new Thread(() -> cargarServicios());
            th.start();
            System.out.println("Se sale");
        }
    }

    public void onActionComboBox(ActionEvent actionEvent) {
        cargarSelectedServicio();

    }
}
