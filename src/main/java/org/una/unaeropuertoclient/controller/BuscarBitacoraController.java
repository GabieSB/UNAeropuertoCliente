package org.una.unaeropuertoclient.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import org.una.unaeropuertoclient.model.BitacoraDto;
import org.una.unaeropuertoclient.service.BitacoraService;
import org.una.unaeropuertoclient.utils.FlowController;
import org.una.unaeropuertoclient.utils.Mensaje;
import org.una.unaeropuertoclient.utils.Respuesta;
import org.una.unaeropuertoclient.utils.Validar;

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

    List<BitacoraDto> bitacorasResultado = new ArrayList<>();
    
    BitacoraService bitacoraService = new BitacoraService();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        iniciarComponentes();
    }

    public void iniciarComponentes(){
        ObservableList<String> items = FXCollections.observableArrayList();
        items.addAll("ID", "CÉDULA USUARIO", "ACCIÓN");
        comboxParametros.getItems().addAll(items);

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
                }

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

        }


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
