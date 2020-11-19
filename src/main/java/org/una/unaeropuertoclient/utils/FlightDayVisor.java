/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.una.unaeropuertoclient.utils;

import java.time.LocalDate;
import java.util.List;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;

/**
 *
 * @author roberth :)
 */
public class FlightDayVisor extends AnchorPane {

    private final LocalDate fecha;
    private List<SingleFlightViewer> vuelosDelDía;

    public FlightDayVisor(int weekPosition, LocalDate initialWeekDate) {
        adaptarVisor();
        this.fecha = initialWeekDate.plusDays(weekPosition);
        establecercolor(weekPosition);
    }

    private void establecercolor(int wikPosition) {
        getStyleClass().add((wikPosition % 2 == 0) ? "flight-Day-Visor-DiaPar" : "flight-Day-Visor-DiaInPar");
    }

    private void adaptarVisor() {
        this.setPrefHeight(60);
        this.setMaxHeight(60);
        this.setPrefWidth(6000);
    }

    public LocalDate getFecha() {
        return fecha;
    }

}
