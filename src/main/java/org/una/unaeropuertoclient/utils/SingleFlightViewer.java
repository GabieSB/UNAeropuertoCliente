/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.una.unaeropuertoclient.utils;

import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import org.una.unaeropuertoclient.model.LugarDto;
import org.una.unaeropuertoclient.model.VueloDto;

/**
 *
 * @author roberth :)
 */
public class SingleFlightViewer extends AnchorPane {

    private final VueloDto vuelo;
    private Label nombreVuelo;
    private Label hora;

    public SingleFlightViewer(VueloDto vuelo, LugarDto ubucacionAeroP) {
        this.vuelo = vuelo;
        colocarTitulos(vuelo, ubucacionAeroP);
    }

    private void colocarTitulos(VueloDto vuelo1, LugarDto ubucacionAeroP) {
        this.nombreVuelo = new Label(vuelo1.getNombreVuelo());
        this.hora = new Label(vuelo1.getExecutionDateTime(ubucacionAeroP).toLocalTime().toString());
        this.nombreVuelo.setLayoutY(10);
        this.hora.setLayoutY(25);
        this.nombreVuelo.setLayoutX(1);
        this.hora.setLayoutX(1);
        this.setMinWidth(41);
        this.nombreVuelo.prefWidthProperty().bind(this.widthProperty());
        this.getChildren().addAll(nombreVuelo, hora);
    }

    public VueloDto getVuelo() {
        return vuelo;
    }
}
