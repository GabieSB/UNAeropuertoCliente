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
import org.una.unaeropuertoclient.service.GastoReparacionService;
import org.una.unaeropuertoclient.service.NotificacionService;
import org.una.unaeropuertoclient.utils.*;

import java.net.URL;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.*;

public class GestorGastosConsultasController extends Controller implements Initializable {
    public TableColumn<GastoReparacionDto, String> columId;
    public TableColumn<GastoReparacionDto, String>  columFecha;
    public TableColumn<GastoReparacionDto, Boolean>  columEstado;
    public TableColumn<GastoReparacionDto, String>  columContrato;
    public TableColumn<GastoReparacionDto, String>  columDurabilidad;
    public TableColumn<GastoReparacionDto, String>  columPeriocidad;
    public TableColumn<GastoReparacionDto, String>  columTipo;
    public TableColumn<GastoReparacionDto, String>  columObservaciones;
    public TableColumn<GastoReparacionDto, Void>  columAcciones;
    public TableColumn<GastoReparacionDto, Boolean>  columActivo;
    public DatePicker dateInicio;
    public DatePicker dateFin;
    public JFXButton btnBuscarFecha;
    public JFXButton btonBuscarParametro1;
    public JFXComboBox comboxParametros;
    public JFXTextField txtValorBuscado;
    public JFXButton btonBuscarParametro;
    public JFXComboBox comboxParametros1;
    public JFXTextField txtDiasInicio;
    public JFXTextField txtDiasFinal;
    public TableView tableResultados;
    public JFXButton btonNuevo;
    List<GastoReparacionDto> gastoReparacionList = new ArrayList<>();
    GastoReparacionService service = new GastoReparacionService();
    NotificacionService notificacionService = new NotificacionService();
    Mensaje mensaje = new Mensaje();
    boolean isGestor = false;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        FlowController.changeSuperiorTittle("Gastos de Mantenimiento");
        iniciarComponentes();


    }

    private void iniciarComponentes(){

        columId.setCellValueFactory(x -> new SimpleStringProperty(x.getValue().getId().toString()));
        columContrato.setCellValueFactory(x -> new SimpleStringProperty(x.getValue().getNumeroContrato().toString()));
        columFecha.setCellValueFactory(x -> new SimpleStringProperty(x.getValue().getFechaRegistro().toString()));
        columEstado.setCellValueFactory(x -> new SimpleBooleanProperty(x.getValue().getEstadoPago()));
        columEstado.setCellFactory( tc -> new TableCell<>() {
            @Override
            protected void updateItem(Boolean item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty ? null :
                        item.booleanValue() ? "Pagado" : "Pendiente");
            }
        });

        columActivo.setCellValueFactory(x -> new SimpleBooleanProperty(x.getValue().getActivo()));
        columActivo.setCellFactory( tc -> new TableCell<>() {
            @Override
            protected void updateItem(Boolean item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty ? null :
                        item.booleanValue() ? "Activo" : "Inactivo");
            }
        });


        columTipo.setCellValueFactory(x -> new SimpleStringProperty(x.getValue().getTiposId().getNombre()));
        columDurabilidad.setCellValueFactory(x -> new SimpleStringProperty(x.getValue().getDuracion().toString()));
        columPeriocidad.setCellValueFactory(x -> new SimpleStringProperty(x.getValue().getPeriodicidad().toString()));
        columObservaciones.setCellValueFactory(x -> new SimpleStringProperty(x.getValue().getObservaciones()));
        llenarComboBox();
    }

    private void addButtonToTable() {

        Callback<TableColumn<GastoReparacionDto, Void>, TableCell<GastoReparacionDto, Void>> cellFactory = new Callback<>() {
            @Override
            public TableCell<GastoReparacionDto, Void> call(final TableColumn<GastoReparacionDto, Void> param) {
                final TableCell<GastoReparacionDto, Void> cell = new TableCell<>() {

                    private final JFXButton btn = new JFXButton("Modificar");

                    {
                        btn.setOnAction((ActionEvent event) -> {
                            GastoReparacionDto gastoSeleccionadoDto = getTableView().getItems().get(getIndex());

                            if(gastoSeleccionadoDto.getActivo()){
                                AppContext.getInstance().set("gastoSeleccionado", gastoSeleccionadoDto);
                                FlowController.getInstance().goView("RegistrarGastos");
                            }else{
                                mensaje.show(Alert.AlertType.WARNING, "", "El gasto mantenimiento que intenta modificar esta inactivo");
                            }

                        });
                    }
                    private final JFXButton btn2 = new JFXButton("Anular");
                    {
                        btn2.setOnAction((ActionEvent event) -> {
                            GastoReparacionDto gastoReparacionDto = getTableView().getItems().get(getIndex());
                            if(gastoReparacionDto.getActivo()) registrarNotificacion(gastoReparacionDto);
                            else  mensaje.show(Alert.AlertType.WARNING, "", "El gasto mantenimiento que intenta anular ya está inactivo");
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
    public void registrarNotificacion(GastoReparacionDto gastoReparacionDto){

        AuthenticationResponse authentication = (AuthenticationResponse) AppContext.getInstance().get("token");

        AreaDto areaDto = new AreaDto();
        areaDto.setId((long) 3);

        NotificacionDto notificacionDto = new NotificacionDto(true, Math.toIntExact(gastoReparacionDto.getId()), Timestamp.valueOf(LocalDateTime.now()), areaDto);

        Respuesta respuesta = notificacionService.create(notificacionDto);

        if(respuesta.getEstado()){
            mensaje.show(Alert.AlertType.INFORMATION, "Información", "Se solicitó la anulacion del servicion con ID: " + gastoReparacionDto.getId());
        }

    }
    public void llenarTabla(){
        tableResultados.getItems().clear();
        tableResultados.setItems(FXCollections.observableList(gastoReparacionList));
        if(isGestor) addButtonToTable();
        else columAcciones.setVisible(false);
    }

    private void  llenarComboBox(){
        ObservableList<String> options =
                FXCollections.observableArrayList(
                        "ID",
                        "Número de Contrato",
                        "Estado de Pago",
                        "Activo",
                        "Tipo de servicio"

                );
        comboxParametros.setItems(options);

        ObservableList<String> options2 =
                FXCollections.observableArrayList(
                        "Periocidad",
                        "Durabilidad"
                );
        comboxParametros1.setItems(options2);
        
    }
    public void buscarEntreDiasDurabilidadYPeriocidad(int selectedItem){
        Respuesta respuesta = null;
        switch (selectedItem){
            case 0: respuesta = service.buscarEntreDiasPeriocidad(txtDiasInicio.getText(), txtDiasFinal.getText()) ;break;
            case 1: respuesta = service.buscarEntreDiasDurabilidad(txtDiasInicio.getText(), txtDiasFinal.getText()); break;
        }

        if(respuesta != null && respuesta.getEstado()){
            gastoReparacionList = (List<GastoReparacionDto>) respuesta.getResultado("data");
            llenarTabla();
        }else{
            mensaje.show(Alert.AlertType.ERROR, "Información", "No se encontraron resultados :c");
        }


    }

    public void busquedaSegunParametro(int selectedItem, String valor) {

        Respuesta respuesta = null;
        switch (selectedItem) {
            case 0: {
                if(Validar.isLongNumber(valor)){
                    respuesta = service.buscarPorID(valor);
                }else   mensaje.show(Alert.AlertType.WARNING, "Dato incorrecto", "Parece que el valor ingresado no es un número");
                break;
            }
            case 1:
                if(Validar.isLongNumber(valor)){
                    respuesta = service.buscarPorNumeroContrato(Long.parseLong(valor));
                }else   mensaje.show(Alert.AlertType.WARNING, "Dato incorrecto", "Parece que el valor ingresado no es un número");
                break;
            case 2:
            {
                if (valor.contains("pagado") || valor.contains("pendiente")) {
                    valor = (valor.contains("pagado"))?"true":"false";
                    respuesta = service.buscarPorEstadoPago(Boolean.parseBoolean(valor));
                }else
                    mensaje.show(Alert.AlertType.INFORMATION, "ERROR DE DATO", "El valor búscado es incorrecto, debe ser pagado o pendiente");
                break;
            }
            case 3:
                if (valor.contains("activo") || valor.contains("inactivo")) {
                    valor = (valor.contains("inactivo"))?"false":"true";
                    respuesta = service.buscarPorEstado(Boolean.parseBoolean(valor));
                }else
                    mensaje.show(Alert.AlertType.INFORMATION, "Dato incorrecto", "El valor búscado debería ser activo o inactivo");
                break;

            case 4:respuesta = service.buscarPorTipoNombre(valor); break;

        }

        if(respuesta != null ){

            if(respuesta.getEstado()){
                if(selectedItem == 0 || selectedItem == 1) {
                    GastoReparacionDto service1 = (GastoReparacionDto) respuesta.getResultado("data");
                    gastoReparacionList.clear();
                    gastoReparacionList.add(service1);

                }else{
                    gastoReparacionList = (List<GastoReparacionDto>) respuesta.getResultado("data");
                }
                llenarTabla();
            }else{
                mensaje.show(Alert.AlertType.INFORMATION, "Respuesta", respuesta.getMensaje());
            }

        }

    }


    @Override
    public void initialize() {
        FlowController.changeSuperiorTittle("Gastos de Mantenimiento");
        AuthenticationResponse auth = (AuthenticationResponse) AppContext.getInstance().get("token");
        Optional<RolUsuarioDto> rol = auth.getRolUsuario().stream().filter(r -> r.getRolesId().getNombre().equals("GESTOR_MANTENIMIENTO_AEROPUERTO")).findFirst();
        if(rol.isPresent())
        {
            isGestor = true;
            btonNuevo.setVisible(true);
            System.out.println("Gestor");
        }
    }

    public void buscarPorFecha(ActionEvent actionEvent) {
        Respuesta  respuesta = service.buscarEntreFechas(dateInicio.getValue(), dateFin.getValue());
        if(respuesta.getEstado()){
            gastoReparacionList = (List<GastoReparacionDto>) respuesta.getResultado("data");
            llenarTabla();
        }else{

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

    public void buscarEntreDias(ActionEvent actionEvent) {
        int selectedItem = comboxParametros1.getSelectionModel().getSelectedIndex();

        if(comboxParametros1.getSelectionModel().isEmpty() && Validar.isLongNumber(txtDiasFinal.getText()) && Validar.isLongNumber(txtDiasInicio.getText()))
        buscarEntreDiasDurabilidadYPeriocidad(selectedItem);
        else   mensaje.show(Alert.AlertType.INFORMATION,"ERROR DE DATOS", "Parece que no has ingresado datos correctos.");
    }

    public void btnNuevo(ActionEvent actionEvent) {
        FlowController.getInstance().goView("RegistrarGastos");
    }
}
