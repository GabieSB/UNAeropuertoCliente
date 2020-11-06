package org.una.unaeropuertoclient.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.util.Callback;
import org.una.unaeropuertoclient.model.GastoReparacionDto;
import org.una.unaeropuertoclient.model.ProvedorDto;
import org.una.unaeropuertoclient.service.ProveedorService;
import org.una.unaeropuertoclient.utils.AppContext;
import org.una.unaeropuertoclient.utils.FlowController;
import org.una.unaeropuertoclient.utils.Mensaje;
import org.una.unaeropuertoclient.utils.Respuesta;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class EditorProvedoresContoller  extends Controller implements Initializable {
    public JFXButton modicarButton;
    public Label nombreProvedoreditarLabel;
    public Button buscarTodosButton;
    public JFXButton buscarNombreButton;
    public JFXTextField txtNombreProveedorBuscar;
    public TableView<ProvedorDto> tableProveedores;
    public TableColumn<ProvedorDto, String> columNombre;
    public JFXTextField txtNombreRegistrar;
    public JFXTextField txtDescripcionRegistrar;
    public JFXButton buttonRegistrar;
    public JFXTextField txtNombreModifcar;
    public TableColumn<ProvedorDto, Void> columAcciones;
    private List<ProvedorDto> proveedores;
    private ProvedorDto provedorSelected = null;

    public void buscarNombreButtonOnAction(ActionEvent actionEvent) {
        tableProveedores.getItems().clear();
        tableProveedores.setPlaceholder(new Label("Cargando..."));
        if(!txtNombreProveedorBuscar.getText().isEmpty()){
            tableProveedores.getItems().clear();
            Thread thread = new Thread(()-> finByNombre(txtNombreProveedorBuscar.getText()));
            thread.start();
        }else {
            new Mensaje().showInformation("Parece que no se ha introducido el valor a consultar");
        }


    }

    public void buscarTodosButtonOnAction(ActionEvent actionEvent) {

        tableProveedores.getItems().clear();
        tableProveedores.setPlaceholder(new Label("Cargando..."));
        Thread thread = new Thread(()-> cargarActivosFromServer());
        thread.start();
    }

    public void modicarButtonOnAction(ActionEvent actionEvent) {

        if(!txtNombreModifcar.getText().isEmpty() && provedorSelected!=null){
            modicarButton.setText("Modificando...");
            modicarButton.setDisable(true);

            Thread t = new Thread(()->modificar(new ProvedorDto(provedorSelected.getId(), txtNombreModifcar.getText())));

            t.start();
        }else {
            new Mensaje().showInformation("Debe seleccionar un proveedor e ingresar el nuevo nombre");
        }


    }

    public void registrarButtonOnAction(ActionEvent actionEvent) {

        if(!txtNombreRegistrar.getText().isEmpty()){
            buttonRegistrar.setText("Registrando...");
            buttonRegistrar.setDisable(true);

            Thread t = new Thread(()->registrar(new ProvedorDto(txtNombreRegistrar.getText())));

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

        Respuesta respuesta = new ProveedorService().findActivos();

        Platform.runLater(()->{
            if(respuesta.getEstado()){
                proveedores = (List<ProvedorDto>) respuesta.getResultado("data");
                mostrarDatosEnTable();

            }else {
                new Mensaje().show(Alert.AlertType.ERROR, "Error", respuesta.getMensaje());
            }
        });

    }
    private void limpiarEditar(){
        txtNombreModifcar.setText("");
        nombreProvedoreditarLabel.setText("");
    }
    private void finByNombre(String nombre){

        Respuesta respuesta = new ProveedorService().findByNombre(nombre);

        Platform.runLater(()->{
            if(respuesta.getEstado()){
                proveedores = (List<ProvedorDto>) respuesta.getResultado("data");
                mostrarDatosEnTable();

            }
        });

    }


    private void modificar(ProvedorDto provedorDto){
        Respuesta respuesta = new ProveedorService().update(provedorDto);

        Platform.runLater(()->{
            if(respuesta.getEstado()){
               new Mensaje().showInformation("Proveedor modificado con éxito.");

                limpiarEditar();
            }else {
                new Mensaje().show(Alert.AlertType.ERROR, "Error al modificar el Proveedor", respuesta.getMensaje() );
            }

            modicarButton.setText("Modificar");
            modicarButton.setDisable(false);
        });


    }

    private void registrar(ProvedorDto provedorDto){
        Respuesta respuesta = new ProveedorService().create(provedorDto);

        Platform.runLater(()->{
            if(respuesta.getEstado()){
                new Mensaje().showInformation("Proveedor registrado con éxito.");
                txtNombreRegistrar.setText("");

            }else {
                new Mensaje().show(Alert.AlertType.ERROR, "Error al registrar el Proveedor", respuesta.getMensaje() );
            }

            buttonRegistrar.setText("Registrar");
            buttonRegistrar.setDisable(false);
        });


    }

    private void  mostrarDatosEnTable(){
        tableProveedores.getItems().clear();
        tableProveedores.getItems().setAll(FXCollections.observableList(proveedores));
        addButtonToTable();
    }

    private void cargarTablePropiedades(){
        tableProveedores.setPlaceholder(new Label("Realize una búsqueda para mostrar proveedores"));
        columNombre.setCellValueFactory(x -> new SimpleStringProperty(x.getValue().getNombre()));
    }

    private void addButtonToTable() {

        Callback<TableColumn<ProvedorDto, Void>, TableCell<ProvedorDto, Void>> cellFactory = new Callback<>() {
            @Override
            public TableCell<ProvedorDto, Void> call(final TableColumn<ProvedorDto, Void> param) {
                final TableCell<ProvedorDto, Void> cell = new TableCell<>() {

                    private final JFXButton btn = new JFXButton("Seleccionar");

                    {
                        btn.setOnAction((ActionEvent event) -> {
                            ProvedorDto provedorDto = getTableView().getItems().get(getIndex());
                            nombreProvedoreditarLabel.setText(provedorDto.getNombre());
                            provedorSelected = provedorDto;

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
