/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.una.unaeropuertoclient.controller;

import java.net.URL;
import java.util.ResourceBundle;
import com.jfoenix.controls.JFXButton;
import java.util.ArrayList;
import java.util.List;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.FlowPane;
import org.una.unaeropuertoclient.model.AuthenticationResponse;
import org.una.unaeropuertoclient.model.RolUsuarioDto;
import org.una.unaeropuertoclient.utils.AppContext;
import org.una.unaeropuertoclient.utils.FlowController;

/**
 * FXML Controller class
 *
 * @author roberth :)
 */
public class MenuPrincipalController extends Controller implements Initializable {

    @FXML
    public JFXButton btnRegistrarUsuario;
    @FXML
    public JFXButton btnGestorServicios;
    @FXML
    public JFXButton btnNotificaciones;
    @FXML
    private FlowPane flow;

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO

    }

    @Override
    public void initialize() {
        extraerOpcionesProhibidas();
        FlowController.changeSuperiorTittle("Menú principal");
    }

    @FXML
    public void goRegistrarUsuario(ActionEvent event) {
        FlowController.getInstance().goView("CrearUsuario");
    }

    @FXML
    public void goGestorServicio(ActionEvent event) {
        FlowController.getInstance().goView("GestorServicios");
    }

    @FXML
    public void goParametros(ActionEvent event) {
        FlowController.getInstance().goView("ParametrosSistema");
    }

    @FXML
    public void btnconsultarServicios(ActionEvent actionEvent) {
        FlowController.getInstance().goView("GestorServiciosConsultas");
    }

    @FXML
    public void OnClickAirModule(ActionEvent event) {
        FlowController.getInstance().goView("GestorVuelos");

    }

    @FXML
    public void goNotificaciones(ActionEvent event) {
        FlowController.getInstance().goView("AutorizarNotificaciones");
    }

    @FXML
    public void btnRegistrarGastos(ActionEvent actionEvent) {
        FlowController.getInstance().goView("RegistrarGastos");
    }

    @FXML
    public void btnconsultarGastos(ActionEvent actionEvent) {
        FlowController.getInstance().goView("GestorGastosConsultas");
    }

    @FXML
    public void btnConsultarBitacoras(ActionEvent actionEvent) {
        FlowController.getInstance().goView("BuscarBitacoras");
    }

    public void extraerOpcionesProhibidas() {
        List<RolUsuarioDto> rList = ((AuthenticationResponse) AppContext.getInstance().get("token")).getUsuario().getRolUsuarioList();
        List<Node> flowChildrenList = new ArrayList();
        flow.getChildren().forEach(node -> flowChildrenList.add(node));
        flow.getChildren().clear();
        AppContext.getInstance().set("auditMode", false);
        rList.forEach(rolU -> {
            switch (rolU.getRolesId().getNombre()) {
                case "ADMINISTRADOR":
                    flow.getChildren().add(getButtonFromList("Registrar Usuario", flowChildrenList));
                    flow.getChildren().add(getButtonFromList("Parametros Sistema", flowChildrenList));
                    flow.getChildren().add(getButtonFromList("Modificar Usuario", flowChildrenList));
                    break;
                case "GESTOR_CONTROL_VUELOS":
                    flow.getChildren().add(getButtonFromList("Control de vuelos", flowChildrenList));
                    break;
                case "GESTOR_SERVICIOS_AERONAVES":
                    flow.getChildren().add(getButtonFromList("Gestor Servicios", flowChildrenList));
                    break;
                case "GESTOR_MANTENIMIENTO_AEROPUERTO":
                    flow.getChildren().add(getButtonFromList("Gestor Gastos", flowChildrenList));
                    break;
                case "GERENTE_SERVICIOS_AERONAVES":
                    flow.getChildren().add(getButtonFromList("Notificaciones", flowChildrenList));
                    break;
                case "GERENTE_MANTENIMIENTO_AEROPUERTO":
                    flow.getChildren().add(getButtonFromList("Notificaciones", flowChildrenList));
                    break;
                case "GERENTE_CONTROL_VUELO":
                    flow.getChildren().add(getButtonFromList("Notificaciones", flowChildrenList));
                    break;
                case "AUDITOR_CONTROL_VUELOS":
                    flow.getChildren().add(getButtonFromList("Control de vuelos", flowChildrenList));
                    AppContext.getInstance().set("auditMode", true);
                    break;
//                case "GERENTE_CONTROL_VUELO":
//                    flow.getChildren().add(getButtonFromList("Gestor Gastos", flowChildrenList));
//                    break;
//                case "GERENTE_CONTROL_VUELO":
//                    flow.getChildren().add(getButtonFromList("Gestor Gastos", flowChildrenList));
//                    break;
//                case "GERENTE_CONTROL_VUELO":
//                    flow.getChildren().add(getButtonFromList("Gestor Gastos", flowChildrenList));
//                    break; ---> TODO
            }
        });
    }

    public Node getButtonFromList(String text, List<Node> childrenList) {
        for (Node butt : childrenList) {
            if (((Button) butt).getText().equals(text)) {
                return butt;
            }
        }
        return null;
    }

    public void goModificarUsuario(ActionEvent actionEvent) {
        FlowController.getInstance().goView("BuscarUsuario");
    }
}
