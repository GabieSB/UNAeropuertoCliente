package org.una.unaeropuertoclient.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.util.Callback;
import org.una.unaeropuertoclient.model.TipoReparacionDto;
import org.una.unaeropuertoclient.service.TipoReparacionService;
import org.una.unaeropuertoclient.service.TipoReparacionService;
import org.una.unaeropuertoclient.utils.Mensaje;
import org.una.unaeropuertoclient.utils.Respuesta;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class EditorTiposReparacionesController  extends Controller implements Initializable {
    public JFXButton modicarButton;
    public Label nombreTipoReparacioneditarLabel;
    public Button buscarTodosButton;
    public JFXButton buscarNombreButton;
    public JFXTextField txtNombreTipoReparacionBuscar;
    public TableView<TipoReparacionDto> tableTipoReparaciones;
    public TableColumn<TipoReparacionDto, String> columNombre;
    public JFXTextField txtNombreRegistrar;
    public JFXButton buttonRegistrar;
    public JFXTextField txtNombreModifcar;
    public TableColumn<TipoReparacionDto, Void> columAcciones;
    private List<TipoReparacionDto> proveedores;
    private TipoReparacionDto tipoReparacionSelected = null;

    public void buscarNombreButtonOnAction(ActionEvent actionEvent) {
        tableTipoReparaciones.getItems().clear();
        tableTipoReparaciones.setPlaceholder(new Label("Cargando..."));
        if(!txtNombreTipoReparacionBuscar.getText().isEmpty()){
            tableTipoReparaciones.getItems().clear();
            Thread thread = new Thread(()-> finByNombre(txtNombreTipoReparacionBuscar.getText()));
            thread.start();
        }else {
            new Mensaje().showInformation("Parece que no se ha introducido el valor a consultar");
        }


    }

    public void buscarTodosButtonOnAction(ActionEvent actionEvent) {

        tableTipoReparaciones.getItems().clear();
        tableTipoReparaciones.setPlaceholder(new Label("Cargando..."));
        Thread thread = new Thread(()-> cargarActivosFromServer());
        thread.start();
    }

    public void modicarButtonOnAction(ActionEvent actionEvent) {

        if(!txtNombreModifcar.getText().isEmpty() && tipoReparacionSelected!=null){
            modicarButton.setText("Modificando...");
            modicarButton.setDisable(true);

            Thread t = new Thread(()->modificar(new TipoReparacionDto(tipoReparacionSelected.getId(), txtNombreModifcar.getText())));

            t.start();
        }else {
            new Mensaje().showInformation("Debe seleccionar un proveedor e ingresar el nuevo nombre");
        }


    }

    public void registrarButtonOnAction(ActionEvent actionEvent) {

        if(!txtNombreRegistrar.getText().isEmpty()){
            buttonRegistrar.setText("Registrando...");
            buttonRegistrar.setDisable(true);

            Thread t = new Thread(()->registrar(new TipoReparacionDto(txtNombreRegistrar.getText())));

            t.start();
        }else {
            new Mensaje().showInformation("Se debe completar el espacio del nombread");
        }

    }




    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    @Override
    public void initialize() {
        cargarTablePropiedades();
    }

    private void cargarActivosFromServer(){

        Respuesta respuesta = new TipoReparacionService().getActivos();

        Platform.runLater(()->{
            if(respuesta.getEstado()){
                proveedores = (List<TipoReparacionDto>) respuesta.getResultado("data");
                mostrarDatosEnTable();

            }else {
                new Mensaje().show(Alert.AlertType.ERROR, "Error", respuesta.getMensaje());
            }
        });

    }
    private void limpiarEditar(){
        txtNombreModifcar.setText("");
        nombreTipoReparacioneditarLabel.setText("");
    }
    private void finByNombre(String nombre){

        Respuesta respuesta = new TipoReparacionService().findByNombre(nombre);

        Platform.runLater(()->{
            if(respuesta.getEstado()){
                proveedores = (List<TipoReparacionDto>) respuesta.getResultado("data");
                mostrarDatosEnTable();

            }
        });

    }


    private void modificar(TipoReparacionDto tipoReparacionDto){
        Respuesta respuesta = new TipoReparacionService().update(tipoReparacionDto);

        Platform.runLater(()->{
            if(respuesta.getEstado()){
                new Mensaje().showInformation("TipoReparacion modificado con éxito.");

                limpiarEditar();
            }else {
                new Mensaje().show(Alert.AlertType.ERROR, "Error al modificar el TipoReparacion", respuesta.getMensaje() );
            }

            modicarButton.setText("Modificar");
            modicarButton.setDisable(false);
        });


    }

    private void registrar(TipoReparacionDto tipoReparacionDto){
        Respuesta respuesta = new TipoReparacionService().create(tipoReparacionDto);

        Platform.runLater(()->{
            if(respuesta.getEstado()){
                new Mensaje().showInformation("TipoReparacion registrado con éxito.");
                txtNombreRegistrar.setText("");

            }else {
                new Mensaje().show(Alert.AlertType.ERROR, "Error al registrar el TipoReparacion", respuesta.getMensaje() );
            }

            buttonRegistrar.setText("Registrar");
            buttonRegistrar.setDisable(false);
        });


    }

    private void  mostrarDatosEnTable(){
        tableTipoReparaciones.getItems().clear();
        tableTipoReparaciones.getItems().setAll(FXCollections.observableList(proveedores));
        addButtonToTable();
    }

    private void cargarTablePropiedades(){
        tableTipoReparaciones.setPlaceholder(new Label("Realize una búsqueda para mostrar proveedores"));
        columNombre.setCellValueFactory(x -> new SimpleStringProperty(x.getValue().getNombre()));
    }

    private void addButtonToTable() {

        Callback<TableColumn<TipoReparacionDto, Void>, TableCell<TipoReparacionDto, Void>> cellFactory = new Callback<>() {
            @Override
            public TableCell<TipoReparacionDto, Void> call(final TableColumn<TipoReparacionDto, Void> param) {
                final TableCell<TipoReparacionDto, Void> cell = new TableCell<>() {

                    private final JFXButton btn = new JFXButton("Seleccionar");

                    {
                        btn.setOnAction((ActionEvent event) -> {
                            TipoReparacionDto tipoReparacionDto = getTableView().getItems().get(getIndex());
                            nombreTipoReparacioneditarLabel.setText(tipoReparacionDto.getNombre());
                            tipoReparacionSelected = tipoReparacionDto;

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

        columAcciones.setCellFactory(cellFactory);

    }

}