package org.una.unaeropuertoclient.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.util.Callback;
import org.una.unaeropuertoclient.model.TipoReparacionDto;
import org.una.unaeropuertoclient.service.TipoReparacionService;
import org.una.unaeropuertoclient.utils.AppContext;
import org.una.unaeropuertoclient.utils.Mensaje;
import org.una.unaeropuertoclient.utils.Respuesta;
import  static  org.una.unaeropuertoclient.utils.ButtonWaitUtils.*;

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
    public AnchorPane controlsEditarContainer;
    public AnchorPane controlRegistrarContainer;
    private List<TipoReparacionDto> reparaciones;
    private TipoReparacionDto tipoReparacionSelected = null;

    public void buscarNombreButtonOnAction(ActionEvent actionEvent) {
        tableTipoReparaciones.getItems().clear();
        tableTipoReparaciones.setPlaceholder(new Label("Cargando..."));

        if(!txtNombreTipoReparacionBuscar.getText().isEmpty()){
            tableTipoReparaciones.getItems().clear();
            aModoEspera(buscarNombreButton);
            controlsEditarContainer.setDisable(true);
            Thread thread = new Thread(()-> finByNombre(txtNombreTipoReparacionBuscar.getText()));
            thread.start();
        }else {
            new Mensaje().showInformation("Parece que no se ha introducido el valor a consultar");
        }


    }

    public void buscarTodosButtonOnAction(ActionEvent actionEvent) {

        tableTipoReparaciones.getItems().clear();
        tableTipoReparaciones.setPlaceholder(new Label("Cargando..."));
        aModoEspera(buscarTodosButton);
        controlsEditarContainer.setDisable(true);

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
            new Mensaje().showInformation("Debe seleccionar un tipo de reparación e ingresar el nuevo nombre");
        }


    }

    public void registrarButtonOnAction(ActionEvent actionEvent) {

        if(!txtNombreRegistrar.getText().isEmpty()){
            aModoEspera(buttonRegistrar);
            controlRegistrarContainer.setDisable(true);

            Thread t = new Thread(()->registrar(new TipoReparacionDto(txtNombreRegistrar.getText())));

            t.start();
        }else {
            new Mensaje().showInformation("Se debe completar el espacio del nombre");
        }

    }




    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    @Override
    public void initialize() {
        cargarTablePropiedades();
        int modoSeleccionado = (int) AppContext.getInstance().get("mode");
        if(modoSeleccionado == 3) modoDeveloper();

    }

    private void modoDeveloper(){
        controlsEditarContainer.setDisable(true);
        controlRegistrarContainer.setDisable(true);
    }

    private void cargarActivosFromServer(){

        Respuesta respuesta = new TipoReparacionService().getActivos();

        Platform.runLater(()->{
            if(respuesta.getEstado()){
                reparaciones = (List<TipoReparacionDto>) respuesta.getResultado("data");
                mostrarDatosEnTable();

            }else {
                new Mensaje().show(Alert.AlertType.ERROR, "Error", respuesta.getMensaje());
            }
            salirModoEspera(buscarTodosButton, "Buscar Todos");
            controlsEditarContainer.setDisable(false);
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
                reparaciones = (List<TipoReparacionDto>) respuesta.getResultado("data");
                mostrarDatosEnTable();

            }
            salirModoEspera(buscarNombreButton, "Buscar Nombre");
            controlsEditarContainer.setDisable(false);
        });

    }


    private void modificar(TipoReparacionDto tipoReparacionDto){
        Respuesta respuesta = new TipoReparacionService().update(tipoReparacionDto);

        Platform.runLater(()->{
            if(respuesta.getEstado()){
                new Mensaje().showInformation("Tipo de Reparacion modificado con éxito.");

                limpiarEditar();
            }else {
                new Mensaje().show(Alert.AlertType.ERROR, "Error al modificar el Tipo de Reparacion", respuesta.getMensaje() );
            }

            salirModoEspera(modicarButton, "Modificar");
            controlRegistrarContainer.setDisable(false);
        });


    }

    private void registrar(TipoReparacionDto tipoReparacionDto){
        Respuesta respuesta = new TipoReparacionService().create(tipoReparacionDto);

        Platform.runLater(()->{
            if(respuesta.getEstado()){
                new Mensaje().showInformation("Tipo de reparacion registrado con éxito.");
                txtNombreRegistrar.setText("");

            }else {
                new Mensaje().show(Alert.AlertType.ERROR, "Error al registrar el Tipo Reparacion", respuesta.getMensaje() );
            }

            salirModoEspera(buttonRegistrar, "Registrar");
            controlRegistrarContainer.setDisable(false);
        });


    }

    private void  mostrarDatosEnTable(){
        tableTipoReparaciones.getItems().clear();
        tableTipoReparaciones.getItems().setAll(FXCollections.observableList(reparaciones));
        addButtonToTable();
    }

    private void cargarTablePropiedades(){
        tableTipoReparaciones.setPlaceholder(new Label("Realice una búsqueda para mostrar tipos de reparaciones"));
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
