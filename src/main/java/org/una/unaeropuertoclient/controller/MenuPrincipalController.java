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
    public FlowPane flow;

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
        FlowController.changeCodeScreenTittle("GG200");
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
        rList.forEach(rolU -> {
            switch (rolU.getRolesId().getNombre()) {
                case "ADMINISTRADOR":
                    AppContext.getInstance().set("mode", 3);
                    flow.getChildren().add(getButtonFromList("Registrar Usuario", flowChildrenList));
                    flow.getChildren().add(getButtonFromList("Parámetros Sistema", flowChildrenList));
                    flow.getChildren().add(getButtonFromList("Modificar Usuario", flowChildrenList));
                    flow.getChildren().add(getButtonFromList("Control de vuelos", flowChildrenList));
                    flow.getChildren().add(getButtonFromList("Gestor Servicios", flowChildrenList));
                    flow.getChildren().add(getButtonFromList("Gestor Gastos", flowChildrenList));
                    flow.getChildren().add(getButtonFromList("Notificaciones", flowChildrenList));
                    flow.getChildren().add(getButtonFromList("Consultar Bitácoras", flowChildrenList));

                    break;
                case "GESTOR_CONTROL_VUELOS":
                    AppContext.getInstance().set("mode", 1);
                    flow.getChildren().add(getButtonFromList("Control de vuelos", flowChildrenList));
                    break;
                case "GESTOR_SERVICIOS_AERONAVES":
                    AppContext.getInstance().set("mode", 1);
                    flow.getChildren().add(getButtonFromList("Gestor Servicios", flowChildrenList));
                    break;
                case "GESTOR_MANTENIMIENTO_AEROPUERTO":
                    AppContext.getInstance().set("mode", 1);
                    flow.getChildren().add(getButtonFromList("Gestor Gastos", flowChildrenList));
                    break;
                case "GERENTE_SERVICIOS_AERONAVES":
                case "GERENTE_MANTENIMIENTO_AEROPUERTO":
                case "GERENTE_CONTROL_VUELO":
                    AppContext.getInstance().set("mode", 1);
                    flow.getChildren().add(getButtonFromList("Notificaciones", flowChildrenList));
                    break;
                case "AUDITOR_CONTROL_VUELOS":
                    AppContext.getInstance().set("mode", 2);
                    flow.getChildren().add(getButtonFromList("Reporte Vuelo", flowChildrenList));
                    flow.getChildren().add(getButtonFromList("Consultar Bitácoras", flowChildrenList));
                    flow.getChildren().add(getButtonFromList("Control de vuelos", flowChildrenList));
                    flow.getChildren().add(getButtonFromList("Notificaciones", flowChildrenList));
                    break;
                case "AUDITOR_MANTENIMIENTO_AEROPUERTO":
                    AppContext.getInstance().set("auditMode", true);
                    AppContext.getInstance().set("mode", 2);
                    flow.getChildren().add(getButtonFromList("Reporte Mantenimiento", flowChildrenList));
                    flow.getChildren().add(getButtonFromList("Gestor Gastos", flowChildrenList));
                    flow.getChildren().add(getButtonFromList("Consultar Bitácoras", flowChildrenList));
                    flow.getChildren().add(getButtonFromList("Notificaciones", flowChildrenList));
                    break;
                case "AUDITOR_SERVICIOS_AERONAVES":
                    AppContext.getInstance().set("mode", 2);
                    AppContext.getInstance().set("auditMode", true);
                    flow.getChildren().add(getButtonFromList("Reporte Servicio", flowChildrenList));
                    flow.getChildren().add(getButtonFromList("Gestor Servicios", flowChildrenList));
                    flow.getChildren().add(getButtonFromList("Consultar Bitácoras", flowChildrenList));
                    flow.getChildren().add(getButtonFromList("Notificaciones", flowChildrenList));
                    break;
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

    @FXML
    public void goModificarUsuario(ActionEvent actionEvent) {
        FlowController.getInstance().goView("BuscarUsuario");
    }

    @FXML
    public void btnReporteServicio(ActionEvent event) {
        FlowController.getInstance().goView("ReporteServicio");
    }

    @FXML
    void goReporteMantenimiento(ActionEvent event) {
        FlowController.getInstance().goView("DepartamentoReport");
    }

    @FXML
    private void goReporteVuelo(ActionEvent event) {
        FlowController.getInstance().goView("VueloReporte");
    }
}
