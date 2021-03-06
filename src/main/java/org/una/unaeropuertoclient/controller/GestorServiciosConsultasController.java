package org.una.unaeropuertoclient.controller;

import com.jfoenix.controls.*;
import javafx.application.Platform;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.Callback;
import org.una.unaeropuertoclient.model.*;
import org.una.unaeropuertoclient.service.BitacoraService;
import org.una.unaeropuertoclient.service.NotificacionService;
import org.una.unaeropuertoclient.service.ServicioMantenimientoService;
import org.una.unaeropuertoclient.utils.*;
import  static  org.una.unaeropuertoclient.utils.ButtonWaitUtils.*;
import java.net.URL;
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
    public JFXButton limpiarButton;
    public JFXButton btnTipoDeServicios;
    public HBox containerButtons;
    public VBox containerControls;
    ToggleGroup estadoPagoToggleGroup = new ToggleGroup();
    ToggleGroup estadoFinalizacionToggleGroup = new ToggleGroup();
    ToggleGroup estadoServicioToggleGroup = new ToggleGroup();


    public DatePicker dateInicio;
    public DatePicker dateFin;
    public JFXButton btnNuevo;
    boolean isGestor = false;
    boolean modoDevelop = false;

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
                            if(servicioMantenimientoDto.getActivo() && !modoDevelop ) {
                                JFXButton buttonSelected = (JFXButton) event.getSource();
                                aModoEspera(buttonSelected);
                                registrarNotificacion(servicioMantenimientoDto,buttonSelected);
                            }
                            else if(!servicioMantenimientoDto.getActivo() && !modoDevelop) mensaje.show(Alert.AlertType.WARNING, "", "El servicio mantenimiento que intenta anular ya esta inactivo");
                            else if(modoDevelop)  mensaje.show(Alert.AlertType.ERROR, "", "Se encuentra en modo desarrollo, no puede solicitar anulaciones");
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

    private void  setModoAuditor(){

        btnTipoDeServicios.setVisible(false);
        columAcciones.setVisible(false);
        btnNuevo.setVisible(false);

    }
    private void modoDevelop(){
       modoDevelop = true;
    }


    private  void setModoEspera(boolean estado){
        containerButtons.setDisable(estado);
        containerControls.setDisable(estado);
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
        FlowController.changeSuperiorTittle("Servicios de Mantenimiento de Aviones");
        FlowController.changeCodeScreenTittle("SG000");
        cargarModoSeleccionado();

       AppContext.getInstance().delete("servicioSeleccionado");


    }

    private void cargarModoSeleccionado(){
        int  modo = (int) AppContext.getInstance().get("mode");
        System.out.println("carga " + modo);
        switch (modo){
            case 2: setModoAuditor(); break;
            case 3: modoDevelop(); break;
        }

    }

    public void llenarTabla(){
        tableResultados.getItems().clear();
        tableResultados.setItems(FXCollections.observableList(serviciosResultados));
        addButtonToTable();
    }

    void registrarNotificacion(ServicioMantenimientoDto selected, JFXButton buttonSelected){


        Thread t = new Thread(()->{
            AuthenticationResponse authentication = (AuthenticationResponse) AppContext.getInstance().get("token");

            NotificacionDto notificacionDto = new NotificacionDto(true, selected.getId(),Timestamp.valueOf(LocalDateTime.now()), authentication.getUsuario().getAreasId());
            Respuesta respuesta = notificacionService.create(notificacionDto);

            Platform.runLater(()->{
                if(respuesta.getEstado()){
                    new BitacoraService().create("Solicitó anulación de el servicio mantenimiento con ID: " + selected.getId());
                    mensaje.show(Alert.AlertType.INFORMATION, "Información", "Se solicitó la anulacion del servicion con ID: " + selected.getId());
                }
                salirModoEspera(buttonSelected, "Anular");
                setModoEspera(false);
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
            salirModoEspera(btonBuscarParametro,"Buscar");
            setModoEspera(false);
        });
    }

    public void buscarPorParametroOnAction(ActionEvent actionEvent) {
       aModoEspera(btonBuscarParametro);
       setModoEspera(true);
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
