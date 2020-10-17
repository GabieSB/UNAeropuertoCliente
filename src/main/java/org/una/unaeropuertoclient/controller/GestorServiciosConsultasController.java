package org.una.unaeropuertoclient.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextField;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.util.Callback;
import org.una.unaeropuertoclient.model.ServicioMantenimientoDto;
import org.una.unaeropuertoclient.service.ServicioMantenimientoService;
import org.una.unaeropuertoclient.utils.Mensaje;
import org.una.unaeropuertoclient.utils.Respuesta;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class GestorServiciosConsultasController  extends Controller implements Initializable {
    public JFXComboBox comboxParametros;
    public JFXTextField txtValorBuscado;
    public JFXButton btonBuscarParametro;
    public JFXButton btnBuscarFecha;
    public TableView<ServicioMantenimientoDto> tableResultados;
    public TableColumn<ServicioMantenimientoDto, String> columId;
    public TableColumn<ServicioMantenimientoDto, String> columFecha;
    public TableColumn<ServicioMantenimientoDto, String> columFactura;
    public TableColumn<ServicioMantenimientoDto, String> columEstadoPago;
    public TableColumn<ServicioMantenimientoDto, String> columServicioFinalizado;
    public TableColumn<ServicioMantenimientoDto, String> columTipoServicio;
    public TableColumn<ServicioMantenimientoDto, String> columAvion;
    public TableColumn<ServicioMantenimientoDto, String> columEstadoServicio;
    public TableColumn<ServicioMantenimientoDto, Void> columAcciones;

    public DatePicker dateInicio;
    public DatePicker dateFin;


    //TableColumn<ServicioMantenimientoDto, Void> columAcciones;
    private List<ServicioMantenimientoDto> serviciosResultados = new ArrayList<>();
    ServicioMantenimientoService service = new ServicioMantenimientoService();
    Mensaje mensaje = new Mensaje();


    private void addButtonToTable() {

        Callback<TableColumn<ServicioMantenimientoDto, Void>, TableCell<ServicioMantenimientoDto, Void>> cellFactory = new Callback<>() {
            @Override
            public TableCell<ServicioMantenimientoDto, Void> call(final TableColumn<ServicioMantenimientoDto, Void> param) {
                final TableCell<ServicioMantenimientoDto, Void> cell = new TableCell<>() {

                    private final Button btn = new Button("Modificar");

                    {
                        btn.setOnAction((ActionEvent event) -> {
                            ServicioMantenimientoDto ServicioMantenimientoDto = getTableView().getItems().get(getIndex());
                            System.out.println("selectedServicioMantenimientoDto: " + ServicioMantenimientoDto.getId());
                        });
                    }
                    private final Button btn2 = new Button("Anular");
                    {
                        btn2.setOnAction((ActionEvent event) -> {
                            ServicioMantenimientoDto ServicioMantenimientoDto = getTableView().getItems().get(getIndex());
                            System.out.println("selectedServicioMantenimientoDto: " + ServicioMantenimientoDto.getId());
                        });
                    }
                    HBox hBox = new HBox(btn, btn2);
                    @Override
                    public void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            setGraphic(hBox);
                        }
                    }

                };
                return cell;
            }
        };

        columAcciones.setCellFactory(cellFactory);

       // tableResultados.getColumns().add(columAcciones);

    }

    private void  llenarComboBox(){
        ObservableList<String> options =
                FXCollections.observableArrayList(
                        "ID",
                        "Número Factura",
                        "Estado Pago",
                        "Estado Finalización",
                        "Activo",
                        "Tipo de servicio",
                        "Matrícula"
                );
        comboxParametros.setItems(options);
    }



    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        llenarComboBox();
        columId.setCellValueFactory(x -> new SimpleStringProperty(x.getValue().getId().toString()));
        columFactura.setCellValueFactory(x -> new SimpleStringProperty(x.getValue().getNumeroFactura().toString()));
        columServicioFinalizado.setCellValueFactory(x -> new SimpleStringProperty(x.getValue().getEstaFinalizacion().toString()));
        columEstadoServicio.setCellValueFactory(x -> new SimpleStringProperty(x.getValue().getActivo().toString()));
        columEstadoPago.setCellValueFactory(x -> new SimpleStringProperty(x.getValue().getEstadoPago().toString()));
        columAvion.setCellValueFactory(x -> new SimpleStringProperty(x.getValue().getAvionesId().getMatricula()));
        columTipoServicio.setCellValueFactory(x -> new SimpleStringProperty(x.getValue().getTiposServiciosId().getNombre()));
    }

    @Override
    public void initialize() {

    }

    public void llenarTabla(){
        tableResultados.getItems().clear();
        tableResultados.setItems(FXCollections.observableList(serviciosResultados));
        addButtonToTable();
    }



    public void buscarPorFecha(ActionEvent actionEvent) {
        Respuesta  respuesta = service.buscarEntreFechas(dateInicio.getValue(), dateFin.getValue());
        if(respuesta.getEstado()){
            serviciosResultados = (List<ServicioMantenimientoDto>) respuesta.getResultado("data");
            llenarTabla();
        }else{

        }
    }

    public void busquedaSegunParametro(int selectedItem, String valor) {

        Respuesta respuesta = null;
        switch (selectedItem) {
            case 0:  respuesta = service.buscarPorID(valor); break;
            case 1:  respuesta = service.buscarPorNumeroFactura(Long.parseLong(valor)); break;
            case 2:
            {
                if (valor.equalsIgnoreCase("true") || valor.equalsIgnoreCase("false"))
                    respuesta = service.buscarPorEstadoPago(Boolean.parseBoolean(valor));
                else
                    mensaje.show(Alert.AlertType.INFORMATION, "ERROR DE DATO", "El valor búscado es incorrecto, debe ser true o false");
                break;
            }

            case 3:{
                if (valor.equalsIgnoreCase("true") || valor.equalsIgnoreCase("false"))
                    respuesta = service.buscarPorEstadoFinalizacion(Boolean.parseBoolean(valor));
                else
                    mensaje.show(Alert.AlertType.INFORMATION, "ERROR DE DATO", "El valor búscado es incorrecto, debe ser true o false");
                break;
            }
            case 4:
            {
                if (valor.equalsIgnoreCase("true") || valor.equalsIgnoreCase("false"))
                    respuesta = service.buscarPorEstado(Boolean.parseBoolean(valor));
                else
                    mensaje.show(Alert.AlertType.INFORMATION, "ERROR DE DATO", "El valor búscado es incorrecto, debe ser true o false");
                break;
            }
            case 5: respuesta = service.buscarPorTipoNombre(valor); break;
            case 6: respuesta = service.buscarPorMatricula(valor); break;
        }


        if(respuesta != null && respuesta.getEstado()){
            if(selectedItem == 0) {
                ServicioMantenimientoDto service1 = (ServicioMantenimientoDto) respuesta.getResultado("data");
                serviciosResultados.clear();
                serviciosResultados.add(service1);

            }else{
                serviciosResultados = (List<ServicioMantenimientoDto>) respuesta.getResultado("data");
            }

            llenarTabla();
        }else{
            mensaje.show(Alert.AlertType.ERROR, "ERROR", "Algo salío mal :c");
        }

    }

    public void buscarPorParametro(ActionEvent actionEvent) {
        int selectedItem = comboxParametros.getSelectionModel().getSelectedIndex();
        String valor = txtValorBuscado.getText();

        if(!valor.isEmpty()&& !comboxParametros.getSelectionModel().isEmpty()){
            busquedaSegunParametro(selectedItem, valor);

        }else{
            mensaje.show(Alert.AlertType.INFORMATION,"ERROR DE DATOS", "Debe seleccionar el parámetro e ingresar un valor a consultar");
        }


    }
}
