package org.una.unaeropuertoclient.controller;

import com.jfoenix.controls.*;
import javafx.application.Platform;
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
    public TableColumn<GastoReparacionDto, String>  columEstado;
    public TableColumn<GastoReparacionDto, String>  columContrato;
    public TableColumn<GastoReparacionDto, String>  columDurabilidad;
    public TableColumn<GastoReparacionDto, String>  columPeriocidad;
    public TableColumn<GastoReparacionDto, String>  columTipo;
    public TableColumn<GastoReparacionDto, String>  columObservaciones;
    public TableColumn<GastoReparacionDto, Void>  columAcciones;
    public TableColumn<GastoReparacionDto, String>  columActivo;
    public DatePicker dateInicio;
    public DatePicker dateFin;
    public TableView<GastoReparacionDto> tableResultados;
    public JFXButton btonNuevo;
    public JFXTextField txtnumeroContrato;
    public JFXTextField txtProveedor;
    public JFXTextField txtTipo;
    public JFXToggleButton estadoToggleButton;
    public JFXRadioButton estadoInactivoRadioButton;
    public JFXRadioButton estadoActivoRadioButton;
    public JFXToggleButton estadoPagoToggleButton;
    public JFXRadioButton pagadoRadioButton;
    public JFXRadioButton pendienteRadioButton;
    public JFXButton limpiarButton;
    public JFXButton buscarButton;
    public TableColumn<GastoReparacionDto, String> columProveedor;
    public JFXTextField txtPeriocidadDesde;
    public JFXTextField txtPeriocidadHasta;
    public JFXTextField txtDuracionDesde;
    public JFXTextField txtDuracionHasta;
    public TableColumn<GastoReparacionDto, String>  columMonto;
    List<GastoReparacionDto> gastoReparacionList = new ArrayList<>();
    GastoReparacionService service = new GastoReparacionService();
    NotificacionService notificacionService = new NotificacionService();
    Mensaje mensaje = new Mensaje();
    boolean isGestor = false;
    ToggleGroup estadoMantenimientoToggleGroup = new ToggleGroup() ;
    ToggleGroup estadoPagoToggleGroup = new ToggleGroup();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        FlowController.changeSuperiorTittle("Gastos de Mantenimiento");
        iniciarComponentes();


    }

    private void iniciarComponentes(){

        setPropiedadesTable();
        unirRadioButtons();
        bindToggleButtonsConRadioButtons();
    }

    private void setPropiedadesTable(){
        tableResultados.setPlaceholder(new Label("Realice una consulta para mostrar resultados"));
        activateResponsiveConfig();
        columContrato.setCellValueFactory(x -> new SimpleStringProperty(x.getValue().getNumeroContrato().toString()));
        columFecha.setCellValueFactory(x -> new SimpleStringProperty(x.getValue().getFechaServicioFormateada()));
        columEstado.setCellValueFactory(x -> new SimpleStringProperty(x.getValue().getEstadoPagoPalabra()));
        columActivo.setCellValueFactory(x -> new SimpleStringProperty(x.getValue().getActivoPalabra()));
        columTipo.setCellValueFactory(x -> new SimpleStringProperty(x.getValue().getTiposId().getNombre()));
        columDurabilidad.setCellValueFactory(x -> new SimpleStringProperty(x.getValue().getDuracion().toString()));
        columPeriocidad.setCellValueFactory(x -> new SimpleStringProperty(x.getValue().getPeriodicidad().toString()));
        columObservaciones.setCellValueFactory(x -> new SimpleStringProperty(x.getValue().getObservaciones()));
        columProveedor.setCellValueFactory(x -> new SimpleStringProperty(x.getValue().getProvedorNombre()));
        columMonto.setCellValueFactory(x -> new SimpleStringProperty(x.getValue().getMonto().toString()));
    }
    private void activateResponsiveConfig() {

        columContrato.prefWidthProperty().bind(tableResultados.widthProperty().divide(13));
        columFecha.prefWidthProperty().bind(tableResultados.widthProperty().divide(10));
        columActivo.prefWidthProperty().bind(tableResultados.widthProperty().divide(13));
        columEstado.prefWidthProperty().bind(tableResultados.widthProperty().divide(12));
        columTipo.prefWidthProperty().bind(tableResultados.widthProperty().divide(8));
        columMonto.prefWidthProperty().bind(tableResultados.widthProperty().divide(13));
        columDurabilidad.prefWidthProperty().bind(tableResultados.widthProperty().divide(15));
        columPeriocidad.prefWidthProperty().bind(tableResultados.widthProperty().divide(15));
        columAcciones.prefWidthProperty().bind(tableResultados.widthProperty().divide(8));
    }

    private void addButtonToTable() {

        Callback<TableColumn<GastoReparacionDto, Void>, TableCell<GastoReparacionDto, Void>> cellFactory = new Callback<>() {
            @Override
            public TableCell<GastoReparacionDto, Void> call(final TableColumn<GastoReparacionDto, Void> param) {
                return new TableCell<>() {

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
                    final HBox hBox = new HBox(btn, btn2);
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
            }
        };

        columAcciones.setCellFactory(cellFactory);

    }
    public void registrarNotificacion(GastoReparacionDto gastoReparacionDto){

        AreaDto areaDto = new AreaDto();
        areaDto.setId((long) 3);

        NotificacionDto notificacionDto = new NotificacionDto(true, gastoReparacionDto.getId(), Timestamp.valueOf(LocalDateTime.now()), areaDto);

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


    private void  unirRadioButtons(){
        estadoActivoRadioButton.setToggleGroup(estadoMantenimientoToggleGroup);
        estadoInactivoRadioButton.setToggleGroup(estadoMantenimientoToggleGroup);
        pagadoRadioButton.setToggleGroup(estadoPagoToggleGroup);
        pendienteRadioButton.setToggleGroup(estadoPagoToggleGroup);

    }
    private  void bindToggleButtonsConRadioButtons(){
        estadoActivoRadioButton.visibleProperty().bind(estadoToggleButton.selectedProperty());
        estadoInactivoRadioButton.visibleProperty().bind(estadoToggleButton.selectedProperty());
        pendienteRadioButton.visibleProperty().bind(estadoPagoToggleButton.selectedProperty());
        pagadoRadioButton.visibleProperty().bind(estadoPagoToggleButton.selectedProperty());
    }

    private void limpiar(){
       txtnumeroContrato.clear();
       txtProveedor.clear();
       txtTipo.clear();
       dateInicio.setValue(null);
       dateFin.setValue(null);
       estadoToggleButton.selectedProperty().set(false);
       estadoPagoToggleButton.selectedProperty().set(false);
       txtPeriocidadHasta.clear();
       txtPeriocidadDesde.clear();
       txtDuracionDesde.clear();
       txtPeriocidadHasta.clear();

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



    public void buscarPorParametro() {

        Respuesta respuesta = getRespuestaConsulta();

        Platform.runLater(()->{
            if(respuesta.getEstado()){
                gastoReparacionList = (List<GastoReparacionDto>) respuesta.getResultado("data");
                llenarTabla();
            }else {
                new Mensaje().show(Alert.AlertType.ERROR, "Error al consultar", respuesta.getMensaje());
            }
            buscarButton.setText("Buscar");
            buscarButton.setDisable(false);
        });
    }

    private Respuesta getRespuestaConsulta() {
        String activo, pago, fechaInicio, fechaFinal, diasDurabilidadInicic, diasDurabilidadFin, diasPeriocidadInicio, diasPeriocidadFin;
        if(estadoToggleButton.isSelected()) activo = estadoActivoRadioButton.isSelected()? "true":"false";
        else activo = "none";
        if(estadoPagoToggleButton.isSelected()) pago= pagadoRadioButton.isSelected()? "true":"false";
        else pago = "none";
        if(dateInicio.getValue() == null) fechaInicio = "none";
        else fechaInicio = dateInicio.getValue().toString();
        if(dateFin.getValue() == null) fechaFinal = "none";
        else fechaFinal = dateFin.getValue().toString();
        diasDurabilidadInicic =  !txtDuracionDesde.getText().isBlank() &&Validar.isLongNumber(txtDuracionDesde.getText())? txtDuracionDesde.getText():"none";
        diasDurabilidadFin =  !txtDuracionHasta.getText().isBlank() &&Validar.isLongNumber(txtDuracionHasta.getText())? txtDuracionHasta.getText():"none";
        diasPeriocidadInicio =  !txtPeriocidadDesde.getText().isBlank() &&Validar.isLongNumber(txtPeriocidadDesde.getText())? txtPeriocidadDesde.getText():"none";
        diasPeriocidadFin =  !txtPeriocidadHasta.getText().isBlank() &&Validar.isLongNumber(txtPeriocidadHasta.getText())? txtPeriocidadHasta.getText():"none";


        Respuesta respuesta =  service.filter(txtnumeroContrato.getText(), txtTipo.getText(), txtProveedor.getText(),activo, pago, fechaInicio, fechaFinal, diasDurabilidadInicic, diasDurabilidadFin, diasPeriocidadInicio,diasPeriocidadFin );
        return respuesta;
    }


    public void btnNuevo(ActionEvent actionEvent) {
        FlowController.getInstance().goView("RegistrarGastos");
    }

    public void provedoresButtonOnAction(ActionEvent actionEvent) {
        FlowController.getInstance().goViewInWindowModal("EditorProvedores", this.getStage(), false);
    }

    public void tiposDeReparacionButtonOnAction(ActionEvent actionEvent) {
        FlowController.getInstance().goViewInWindowModal("EditorTipoReparaciones", this.getStage(), false);
    }

    public void limpiarOnAction(ActionEvent actionEvent) {
        limpiar();
    }

    public void buscarOnAction(ActionEvent actionEvent) {
        buscarButton.setText("Buscando...");
        buscarButton.setDisable(true);
        Thread t = new Thread(()-> buscarPorParametro());
        t.start();

    }
}
