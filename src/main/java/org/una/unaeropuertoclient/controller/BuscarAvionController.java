package org.una.unaeropuertoclient.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXTreeTableView;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import org.una.unaeropuertoclient.model.AvionDto;
import org.una.unaeropuertoclient.service.AvionService;
import org.una.unaeropuertoclient.utils.AppContext;
import org.una.unaeropuertoclient.utils.FlowController;
import org.una.unaeropuertoclient.utils.Mensaje;
import org.una.unaeropuertoclient.utils.Respuesta;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class BuscarAvionController extends Controller implements Initializable {


    public JFXTextField txtValofBusqueda;
    public JFXComboBox comboBusqueda;
    public JFXButton btnBuscar;
    public TableView<AvionDto> tableResultados;
    public TableColumn<AvionDto, String> columId;
    public TableColumn<AvionDto, String> columMatricula;
    public TableColumn<AvionDto, String> columAerolinea;
    public List<AvionDto> avionesResultado = new ArrayList<>();


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        carcarComponentes();

    }

    public void carcarComponentes(){

        ObservableList<String> items = FXCollections.observableArrayList();
        items.addAll("ID", "NÚMERO MATRÍCULA", "NOMBRE AEROLÍNEA");
        comboBusqueda.getItems().addAll(items);

        // CARGAR TABLA
        columId.setCellValueFactory(x -> new SimpleStringProperty(x.getValue().getId().toString()));
        columMatricula.setCellValueFactory(x -> new SimpleStringProperty(x.getValue().getMatricula()));
        columAerolinea.setCellValueFactory(x -> new SimpleStringProperty(x.getValue().getAerolineasId().getNombre()));

    }

    @Override
    public void initialize() {

    }

    public void llenarTabla(){
        tableResultados.setItems(FXCollections.observableList(avionesResultado));
    }



    public void busquedaSegunCategoria(){
        int categoria = comboBusqueda.getSelectionModel().getSelectedIndex();
        AvionService avionService = new AvionService();

        Respuesta respuesta = null;

        if(categoria == 0){
            respuesta = avionService.getById(txtValofBusqueda.getText());
        }else if(categoria == 1){
            respuesta= avionService.getByMatriculaLike(txtValofBusqueda.getText());
        }else if(categoria == 2){
            respuesta = avionService.getByAerolinaNombre(txtValofBusqueda.getText());
        }

        if(respuesta != null && respuesta.getEstado()){
            if(categoria == 0) avionesResultado.add((AvionDto) respuesta.getResultado("data"));
            else avionesResultado = (List<AvionDto>) respuesta.getResultado("data");

            if(avionesResultado.size() == 0) new Mensaje().show(Alert.AlertType.INFORMATION, "Informacion", "No hubo resulatos");
            else llenarTabla();

        }else{
            new Mensaje().showModal(Alert.AlertType.ERROR, "Falla al realizar la búsqueda", this.getStage(), respuesta.getMensaje());

        }

    }

    public void buscarAvion(ActionEvent actionEvent) {
        busquedaSegunCategoria();
    }

    public void avionSelected(MouseEvent mouseEvent) {
        if(mouseEvent.getClickCount() == 2){
            int rowSelected = tableResultados.getSelectionModel().getSelectedIndex();
            AvionDto avionSeleccionado = tableResultados.getItems().get(rowSelected);
            System.out.println(avionSeleccionado.getMatricula());
            AppContext.getInstance().set("avionSeleccionado", avionSeleccionado);

            FlowController.getInstance().goBack();

        }
    }
}
