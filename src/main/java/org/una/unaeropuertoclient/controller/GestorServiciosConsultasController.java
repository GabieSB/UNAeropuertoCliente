package org.una.unaeropuertoclient.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.util.Callback;
import org.una.unaeropuertoclient.model.*;
import org.una.unaeropuertoclient.service.NotificacionService;
import org.una.unaeropuertoclient.service.ServicioMantenimientoService;
import org.una.unaeropuertoclient.utils.*;
import java.net.URL;
import java.util.*;
import java.sql.Timestamp;
import java.time.LocalDateTime;
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
    public TableColumn<ServicioMantenimientoDto, Boolean> columEstadoPago;
    public TableColumn<ServicioMantenimientoDto,  Boolean> columServicioFinalizado;
    public TableColumn<ServicioMantenimientoDto, String> columTipoServicio;
    public TableColumn<ServicioMantenimientoDto, String> columAvion;
    public TableColumn<ServicioMantenimientoDto, Boolean> columEstadoServicio;
    public TableColumn<ServicioMantenimientoDto, Void> columAcciones;
    public TableColumn<ServicioMantenimientoDto, String> columMonto;

    public DatePicker dateInicio;
    public DatePicker dateFin;
    public JFXButton btnNuevo;
    boolean isGestor = false;

    private List<ServicioMantenimientoDto> serviciosResultados = new ArrayList<>();
    ServicioMantenimientoService service = new ServicioMantenimientoService();
    NotificacionService notificacionService = new NotificacionService();
    Mensaje mensaje = new Mensaje();


    private void addButtonToTable() {

        Callback<TableColumn<ServicioMantenimientoDto, Void>, TableCell<ServicioMantenimientoDto, Void>> cellFactory = new Callback<>() {
            @Override
            public TableCell<ServicioMantenimientoDto, Void> call(final TableColumn<ServicioMantenimientoDto, Void> param) {
                final TableCell<ServicioMantenimientoDto, Void> cell = new TableCell<>() {

                    private final JFXButton btn = new JFXButton("Modificar");

                    {
                        btn.setOnAction((ActionEvent event) -> {
                            ServicioMantenimientoDto servicioMantenimientoDto = getTableView().getItems().get(getIndex());
                            if(servicioMantenimientoDto.getActivo()){
                                AppContext.getInstance().set("servicioSeleccionado", servicioMantenimientoDto);
                                FlowController.getInstance().goView("GestorServicios");
                            }else{
                                mensaje.show(Alert.AlertType.WARNING, "", "El servicio mantenimiento que intenta modificar esta inactivo");
                            }

                        });
                    }
                    private final JFXButton btn2 = new JFXButton("Anular");
                    {
                        btn2.setOnAction((ActionEvent event) -> {
                            ServicioMantenimientoDto servicioMantenimientoDto = getTableView().getItems().get(getIndex());
                            if(servicioMantenimientoDto.getActivo()) registrarNotificacion(servicioMantenimientoDto);
                            else mensaje.show(Alert.AlertType.WARNING, "", "El servicio mantenimiento que intenta anular ya esta inactivo");
                        });

                        btn2.setStyle(" -fx-background-color: #d63152;\n" +
                                "    -fx-font-size: 12px;\n" +
                                "    -fx-border-radius: 0.2;\n" +
                                "    -jfx-button-type:RAISED;\n" +
                                "    -fx-text-fill:  #E0E0E0;");
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

    private void cargarPropertiesTable(){
        columId.setCellValueFactory(x -> new SimpleStringProperty(x.getValue().getId().toString()));
        columFactura.setCellValueFactory(x -> new SimpleStringProperty(x.getValue().getNumeroFactura().toString()));
        columFecha.setCellValueFactory(x -> new SimpleStringProperty(x.getValue().getFechaServicioFormateada()));
        columEstadoServicio.setCellValueFactory(x -> new SimpleBooleanProperty(x.getValue().getActivo()));
        columEstadoServicio.setCellFactory( tc -> new TableCell<>() {
            @Override
            protected void updateItem(Boolean item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty ? null :
                        item.booleanValue() ? "Activo" : "Inactivo");
            }
        });
        columEstadoPago.setCellValueFactory(x -> new SimpleBooleanProperty(x.getValue().getEstadoPago()));
        columEstadoPago.setCellFactory( tc -> new TableCell<>() {
            @Override
            protected void updateItem(Boolean item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty ? null :
                        item.booleanValue() ? "Pagado" : "Pendiente");
            }
        });
        columAvion.setCellValueFactory(x -> new SimpleStringProperty(x.getValue().getAvionesId().getMatricula()));
        columTipoServicio.setCellValueFactory(x -> new SimpleStringProperty(x.getValue().getTiposServiciosId().getNombre()));
        columMonto.setCellValueFactory(x -> new SimpleStringProperty(x.getValue().getCobroList().get(0).getMonto().toString()));
        columServicioFinalizado.setCellValueFactory(x -> new SimpleBooleanProperty(x.getValue().getEstaFinalizacion()));
        columServicioFinalizado.setCellFactory( tc -> new TableCell<>() {
            @Override
            protected void updateItem(Boolean item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty ? null :
                        item.booleanValue() ? "Finalizado" : "Pendiente");
            }
        });
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        llenarComboBox();
        cargarPropertiesTable();




    }

    @Override
    public void initialize() {
        AuthenticationResponse auth = (AuthenticationResponse) AppContext.getInstance().get("token");
        Optional<RolUsuarioDto> rol = auth.getRolUsuario().stream().filter(r -> r.getRolesId().getNombre().equals("GESTOR_SERVICIOS_AERONAVES")).findFirst();
        if(rol.isPresent())
        {
            isGestor = true;
            btnNuevo.setVisible(true);
            System.out.println("Gestor");
        }
        FlowController.changeSuperiorTittle("Servicios de Mantenimiento de Aviones");

    }

    public void llenarTabla(){
        tableResultados.getItems().clear();
        tableResultados.setItems(FXCollections.observableList(serviciosResultados));
        if(isGestor) addButtonToTable();
        else columAcciones.setVisible(false);
    }

    void registrarNotificacion(ServicioMantenimientoDto selected){

        AuthenticationResponse authentication = (AuthenticationResponse) AppContext.getInstance().get("token");

//        NotificacionDto notificacionDto = new NotificacionDto(true, Math.toIntExact(selected.getId()), new Date(), authentication.getUsuario().getAreasId());
  NotificacionDto notificacionDto = new NotificacionDto(true, selected.getId(),Timestamp.valueOf(LocalDateTime.now()), authentication.getUsuario().getAreasId());
        Respuesta respuesta = notificacionService.create(notificacionDto);

        if(respuesta.getEstado()){
            mensaje.show(Alert.AlertType.INFORMATION, "Información", "Se solicitó la anulacion del servicion con ID: " + selected.getId());
        }
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

        if(valor.isEmpty()){

        }
        switch (selectedItem) {
            case 0: {
                if(Validar.isLongNumber(valor)){  respuesta = service.buscarPorID(valor); }
                else  mensaje.show(Alert.AlertType.INFORMATION, "ERROR DE DATO", "El valor búscado es incorrecto, debe ser un número");
                break;
            }
            case 1:
                if(Validar.isLongNumber(valor)) respuesta = service.buscarPorNumeroFactura(Long.parseLong(valor));
                else mensaje.show(Alert.AlertType.INFORMATION, "ERROR DE DATO", "El valor búscado es incorrecto, debe ser un número");
                break;
            case 2:
            {
                if (valor.contains("pagado") || valor.contains("pendiente")){
                    valor = (valor.contains("pagado"))? "true":"false";
                    respuesta = service.buscarPorEstadoPago(Boolean.parseBoolean(valor));
                }

                else
                    mensaje.show(Alert.AlertType.INFORMATION, "ERROR DE DATO", "El valor búscado es incorrecto, debe ser pagado o pendiente");
                break;
            }

            case 3:{
                if (valor.contains("finalizado") || valor.contains("pendiente")){
                    valor = (valor.contains("finalizado"))? "true":"false";
                    respuesta = service.buscarPorEstadoFinalizacion(Boolean.parseBoolean(valor));
                }

                else
                    mensaje.show(Alert.AlertType.INFORMATION, "ERROR DE DATO", "El valor búscado es incorrecto, debe ser finalizado o pendiente");
                break;
            }
            case 4:
            {
                if (valor.contains("activo")|| valor.contains("inactivo")){
                    valor = (valor.contains("inactivo"))? "false":"true";
                    respuesta = service.buscarPorEstado(Boolean.parseBoolean(valor));}
                else
                    mensaje.show(Alert.AlertType.INFORMATION, "ERROR DE DATO", "El valor búscado es incorrecto, debe ser activo o inactivo");
                break;
            }
            case 5: respuesta = service.buscarPorTipoNombre(valor); break;
            case 6: respuesta = service.buscarPorMatricula(valor); break;
        }


        if(respuesta != null ){
            if(respuesta.getEstado()){
                if(selectedItem == 0) {
                    ServicioMantenimientoDto service1 = (ServicioMantenimientoDto) respuesta.getResultado("data");
                    serviciosResultados.clear();
                    serviciosResultados.add(service1);

                }else{
                    serviciosResultados = (List<ServicioMantenimientoDto>) respuesta.getResultado("data");
                }

                llenarTabla();
            }else{
                mensaje.show(Alert.AlertType.INFORMATION, "Respuesta", respuesta.getMensaje());
            }

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

    public void btnNuevo(ActionEvent actionEvent) {
        FlowController.getInstance().goView("GestorServicios");
    }
}
