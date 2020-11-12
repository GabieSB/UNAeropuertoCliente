package org.una.unaeropuertoclient.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import org.una.unaeropuertoclient.model.BitacoraDto;
import org.una.unaeropuertoclient.service.BitacoraService;
import org.una.unaeropuertoclient.utils.*;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class BuscarBitacoraController  extends Controller implements Initializable {
    public DatePicker dateInicio;
    public DatePicker dateFin;
    public JFXButton btnBuscarFecha;
    public TableColumn<BitacoraDto, String> columId;
    public TableColumn<BitacoraDto, String> columFecha;
    public TableColumn<BitacoraDto, String> columAccion;
    public TableColumn<BitacoraDto, String> columUsuario;
    public JFXTextField txtValorBuscado;
    public JFXButton btonBuscarParametro;
    public JFXComboBox comboxParametros;
    public TableView tableResultados;
    public VBox containerRoot;

    List<BitacoraDto> bitacorasResultado = new ArrayList<>();
    
    BitacoraService bitacoraService = new BitacoraService();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        iniciarComponentes();
    }

    public void iniciarComponentes(){
        ObservableList<String> items = FXCollections.observableArrayList();
        FlowController.changeSuperiorTittle("BITÁCORAS");
        FlowController.changeCodeScreenTittle("BG000");


        items.addAll("ID", "CÉDULA USUARIO", "ACCIÓN");
        comboxParametros.getItems().addAll(items);

        cargarPropiedadesTable();

    }

    private void cargarPropiedadesTable() {
        tableResultados.setPlaceholder(new Label("Realice una consulta para mostrar resultados"));
        columId.setCellValueFactory(x -> new SimpleStringProperty(x.getValue().getId().toString()));
        columFecha.setCellValueFactory(x -> new SimpleStringProperty(x.getValue().getFechaModificacion().toString()));
        columAccion.setCellValueFactory(x -> new SimpleStringProperty(x.getValue().getTipoCambio()));
        columUsuario.setCellValueFactory(x -> new SimpleStringProperty(x.getValue().getUsuariosId().getNombre()));
    }

    public void busquedaSegunCategoria(){
        int categoria = comboxParametros.getSelectionModel().getSelectedIndex();
        String valor = txtValorBuscado.getText();
        
        Respuesta respuesta = null;

        if(!valor.isEmpty()){
            if(categoria == 0){
                if(Validar.isLongNumber(valor)){
                    respuesta = bitacoraService.buscarPorID(Long.parseLong(valor));
                }else  new Mensaje().show(Alert.AlertType.INFORMATION, "Información", "Parece que has ingresado un valor incorrecto. El ID debería ser un número");

            }else if(categoria == 1){
                respuesta= bitacoraService.buscarPorUserCedula(valor);
            }else if(categoria == 2){
                respuesta = bitacoraService.buscarPorTipoCambio(valor);
            }

            if(respuesta != null && respuesta.getEstado()){
                if(categoria == 0) bitacorasResultado.add((BitacoraDto) respuesta.getResultado("data"));
                else bitacorasResultado = (List<BitacoraDto>) respuesta.getResultado("data");

                if(bitacorasResultado.size() == 0) new Mensaje().show(Alert.AlertType.INFORMATION, "Informacion", "No hubo resulatos");
                else llenarTabla();

            }else{
                new Mensaje().showModal(Alert.AlertType.ERROR, "Falla al realizar la búsqueda", this.getStage(), "No se pudieron obtener resultados");

            }
        }else new Mensaje().showModal(Alert.AlertType.WARNING, "Información", this.getStage(), "Parece que el espacio de valor a consultar esta vacío");


    }

    public void llenarTabla(){
        tableResultados.getItems().clear();
        tableResultados.setItems(FXCollections.observableList(bitacorasResultado));
    }


    @Override
    public void initialize() {
        FlowController.changeSuperiorTittle("Bitácoras");

    }

    public void buscarPorFecha(ActionEvent actionEvent) {

        Respuesta  respuesta = bitacoraService.buscarEntreFechas(dateInicio.getValue(), dateFin.getValue());
        if(respuesta.getEstado()){
            bitacorasResultado = (List<BitacoraDto>) respuesta.getResultado("data");
            llenarTabla();
        }else{

        }
    }

    public void buscarPorParametro(ActionEvent actionEvent) {
        busquedaSegunCategoria();
    }
}
