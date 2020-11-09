package org.una.unaeropuertoclient.controller;

import com.jfoenix.controls.*;
import javafx.application.Platform;
import javafx.beans.binding.Binding;
import javafx.beans.binding.Bindings;
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
import org.una.unaeropuertoclient.service.BitacoraService;
import org.una.unaeropuertoclient.service.NotificacionService;
import org.una.unaeropuertoclient.service.ServicioMantenimientoService;
import org.una.unaeropuertoclient.utils.*;
import java.net.URL;
import java.sql.SQLOutput;
import java.util.*;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class GestorServiciosConsultasController  extends Controller implements Initializable {
    public JFXTextField txtValorBuscado;
    public JFXButton btonBuscarParametro;
    public TableView<ServicioMantenimientoDto> tableResultados;
    public TableColumn<ServicioMantenimientoDto, String> columFecha;
    public TableColumn<ServicioMantenimientoDto, String> columFactura;
    public TableColumn<ServicioMantenimientoDto, String> columEstadoPago;
    public TableColumn<ServicioMantenimientoDto, String> columServicioFinalizado;
    public TableColumn<ServicioMantenimientoDto, String> columTipoServicio;
    public TableColumn<ServicioMantenimientoDto, String> columAvion;
    public TableColumn<ServicioMantenimientoDto, String> columEstadoServicio;
    public TableColumn<ServicioMantenimientoDto, Void> columAcciones;
    public TableColumn<ServicioMantenimientoDto, String> columMonto;
    public RadioButton finalizacionFinalizadaRadioButton;
    public RadioButton finalizacionPendienteRadioButton;
    public RadioButton estadoActivoRadioButton;
    public RadioButton estadoInactivoRadioButton;
    public RadioButton pagoPagadoRadioButton;
    public RadioButton pagoPendienteRadioButton;
    public JFXTextField txtNumeroFactura;
    public JFXTextField txtTipoServicio;
    public JFXTextField txtMatricula;
    public JFXToggleButton pagoToggleButton;
    public JFXToggleButton finalizacionToggleButton;
    public JFXToggleButton servicioToggleButton;
    ToggleGroup estadoPagoToggleGroup = new ToggleGroup();
    ToggleGroup estadoFinalizacionToggleGroup = new ToggleGroup();
    ToggleGroup estadoServicioToggleGroup = new ToggleGroup();


    public DatePicker dateInicio;
    public DatePicker dateFin;
    public JFXButton btnNuevo;
    boolean isGestor = false;

    private List<ServicioMantenimientoDto> serviciosResultados = new ArrayList<>();
    ServicioMantenimientoService service = new ServicioMantenimientoService();
    NotificacionService notificacionService = new NotificacionService();
    Mensaje mensaje = new Mensaje();


    private void  unirRadioButtons(){
        estadoActivoRadioButton.setToggleGroup(estadoServicioToggleGroup);
        estadoInactivoRadioButton.setToggleGroup(estadoServicioToggleGroup);
        pagoPagadoRadioButton.setToggleGroup(estadoPagoToggleGroup);
        pagoPendienteRadioButton.setToggleGroup(estadoPagoToggleGroup);
        finalizacionFinalizadaRadioButton.setToggleGroup(estadoFinalizacionToggleGroup);
        finalizacionPendienteRadioButton.setToggleGroup(estadoFinalizacionToggleGroup);
    }
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

    private  void bindToggleButtonsConRadioButtons(){
        estadoActivoRadioButton.visibleProperty().bind(servicioToggleButton.selectedProperty());
        estadoInactivoRadioButton.visibleProperty().bind(servicioToggleButton.selectedProperty());
        finalizacionPendienteRadioButton.visibleProperty().bind(finalizacionToggleButton.selectedProperty());
        finalizacionFinalizadaRadioButton.visibleProperty().bind(finalizacionToggleButton.selectedProperty());
        pagoPendienteRadioButton.visibleProperty().bind(pagoToggleButton.selectedProperty());
        pagoPagadoRadioButton.visibleProperty().bind(pagoToggleButton.selectedProperty());
    }

    private void cargarPropertiesTable(){
        tableResultados.setPlaceholder(new Label("Realice una consulta para mostrar resultados"));
        activateResponsiveConfig();
        columFactura.setCellValueFactory(x -> new SimpleStringProperty(x.getValue().getNumeroFactura().toString()));
        columFecha.setCellValueFactory(x -> new SimpleStringProperty(x.getValue().getFechaServicioFormateada()));
        columEstadoServicio.setCellValueFactory(x -> new SimpleStringProperty(x.getValue().getActivoPalabra()));
        columEstadoPago.setCellValueFactory(x -> new SimpleStringProperty(x.getValue().getEstadoPagoPalabra()));
        columAvion.setCellValueFactory(x -> new SimpleStringProperty(x.getValue().getAvionesId().getMatricula()));
        columTipoServicio.setCellValueFactory(x -> new SimpleStringProperty(x.getValue().getTiposServiciosId().getNombre()));
        columMonto.setCellValueFactory(x -> new SimpleStringProperty(x.getValue().getCobro()));
        columServicioFinalizado.setCellValueFactory(x -> new SimpleStringProperty(x.getValue().getEstadoFinalizacionPalabra()));
    }

    private void activateResponsiveConfig() {

        columFactura.prefWidthProperty().bind(tableResultados.widthProperty().divide(10));
        columFecha.prefWidthProperty().bind(tableResultados.widthProperty().divide(9));
        columEstadoServicio.prefWidthProperty().bind(tableResultados.widthProperty().divide(12));
        columEstadoPago.prefWidthProperty().bind(tableResultados.widthProperty().divide(12));
        columAvion.prefWidthProperty().bind(tableResultados.widthProperty().divide(15));
        columTipoServicio.prefWidthProperty().bind(tableResultados.widthProperty().divide(8));
        columMonto.prefWidthProperty().bind(tableResultados.widthProperty().divide(13));
        columServicioFinalizado.prefWidthProperty().bind(tableResultados.widthProperty().divide(10));
        columAcciones.prefWidthProperty().bind(tableResultados.widthProperty().divide(8));
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        unirRadioButtons();
        bindToggleButtonsConRadioButtons();
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


        Thread t = new Thread(()->{
            AuthenticationResponse authentication = (AuthenticationResponse) AppContext.getInstance().get("token");

            NotificacionDto notificacionDto = new NotificacionDto(true, selected.getId(),Timestamp.valueOf(LocalDateTime.now()), authentication.getUsuario().getAreasId());
            Respuesta respuesta = notificacionService.create(notificacionDto);

            Platform.runLater(()->{
                if(respuesta.getEstado()){
                    new BitacoraService().create("Se solicitó la anulación de el servicio mantenimiento con ID: " + selected.getId());
                    mensaje.show(Alert.AlertType.INFORMATION, "Información", "Se solicitó la anulacion del servicion con ID: " + selected.getId());
                }
            });

        });

        t.start();


    }

    public void busquedaSegunParametro() {
        String activo, pago, finalizacion, fechaInicio, fechaFinal;
        if(servicioToggleButton.isSelected()) activo = estadoActivoRadioButton.isSelected()? "true":"false";
        else activo = "none";
        if(pagoToggleButton.isSelected()) pago= pagoPagadoRadioButton.isSelected()? "true":"false";
        else pago = "none";
        if(finalizacionToggleButton.isSelected()) finalizacion= finalizacionFinalizadaRadioButton.isSelected()? "true":"false";
        else finalizacion = "none";
        if(dateInicio.getValue() == null) fechaInicio = "none";
        else fechaInicio = dateInicio.getValue().toString();
        if(dateFin.getValue() == null) fechaFinal = "none";
        else fechaFinal = dateFin.getValue().toString();



       Respuesta respuesta =  service.filter(txtMatricula.getText(), txtTipoServicio.getText(), txtNumeroFactura.getText(),activo, pago, finalizacion, fechaInicio, fechaFinal );

        Platform.runLater(()->{
            if(respuesta.getEstado()){
                serviciosResultados = (List<ServicioMantenimientoDto>) respuesta.getResultado("data");
                llenarTabla();
            }else {
                new Mensaje().show(Alert.AlertType.ERROR, "Error al consultar", respuesta.getMensaje());
            }
            btonBuscarParametro.setText("Buscar");
            btonBuscarParametro.setDisable(false);
        });
    }

    public void buscarPorParametroOnAction(ActionEvent actionEvent) {
        btonBuscarParametro.setText("Buscando...");
        btonBuscarParametro.setDisable(true);
        Thread t = new Thread(()-> busquedaSegunParametro());
        t.start();


    }

    public void btnNuevo(ActionEvent actionEvent) {
        FlowController.getInstance().goView("GestorServicios");
    }

    public void avanzadoButtonOnAction(ActionEvent actionEvent) {
        FlowController.getInstance().goViewInWindowModal("RegistrarTipoServicio",this.getStage(), false);
    }



    public void limpiarButtonOnAction(ActionEvent actionEvent) {
        txtMatricula.clear();
        txtNumeroFactura.clear();
        txtTipoServicio.clear();
        dateInicio.setValue(null);
        dateFin.setValue(null);
        servicioToggleButton.selectedProperty().set(false);
        pagoToggleButton.selectedProperty().set(false);
        finalizacionToggleButton.selectedProperty().set(false);

    }
}
