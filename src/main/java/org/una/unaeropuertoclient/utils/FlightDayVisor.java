/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.una.unaeropuertoclient.utils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javafx.scene.layout.AnchorPane;
import org.una.unaeropuertoclient.model.LugarDto;
import org.una.unaeropuertoclient.model.VueloDto;

/**
 *
 * @author roberth :)
 */
public class FlightDayVisor extends AnchorPane {

    private final LocalDate fecha;
    private final List<SingleFlightViewer> vuelosDelDía;

    public FlightDayVisor(int weekPosition, LocalDate initialWeekDate) {
        adaptarVisor();
        vuelosDelDía = new ArrayList();
        this.fecha = initialWeekDate.plusDays(weekPosition);
        establecercolor(weekPosition);
    }

    private void establecercolor(int wikPosition) {
        getStyleClass().add((wikPosition % 2 == 0) ? "flight-Day-Visor-DiaPar" : "flight-Day-Visor-DiaInPar");
    }

    private void adaptarVisor() {
        this.setPrefHeight(60);
        this.setMaxHeight(60);
        this.setPrefWidth(12000);
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void tomarVuelosCorrespondientes(List<VueloDto> vuelos, LugarDto airPortLocation, int vuelosHora) {
        vuelos.forEach((flight) -> {
            if (flight.getExecutionDateTime(airPortLocation).toLocalDate().equals(this.fecha)) {
                this.vuelosDelDía.add(new SingleFlightViewer(flight, airPortLocation));
            }
        });
        acomodarYGraficarVuelos(vuelosHora, airPortLocation);
    }

    private void acomodarYGraficarVuelos(int vuelosHora, LugarDto airPortLocation) {
        this.vuelosDelDía.forEach(viewer -> {
            viewer.setPrefHeight(50);
            viewer.setPrefWidth(500 / vuelosHora);
            viewer.setMaxWidth(500 / vuelosHora); //500 es el resultado de dividir 12000px en 24 partes.

            viewer.setLayoutY(5);
            viewer.setLayoutX(calcularPosX(vuelosHora, viewer.getVuelo().getExecutionDateTime(airPortLocation)));
            switch (viewer.getVuelo().getEstado()) {
                case 0:
                    viewer.getStyleClass().add("vuelo-programado");
                    break;
                case 1:
                    viewer.getStyleClass().add("vuelo-enVuelo");
                    break;
                default:
                    viewer.getStyleClass().add("vuelo-finalizado");
                    break;
            }
        });
        this.getChildren().addAll(vuelosDelDía);
    }

    private int calcularPosX(int vuelosHora, LocalDateTime executionTime) {
        int result = 0;
        result += 505 * executionTime.getHour() + 1;                       //250 es el resultado de dividir 6000px en 24 partes.
        result += (505 / 60) * executionTime.getMinute();                  //'250/60' (si 250 se refiere a horas '250/60' se refiere a minutos).
        result -= (505 / vuelosHora) / 2;                                  //Centra el recuadro                      
        result -= executionTime.getHour() * 3;                             //Los viewers se desalineaban con cada hora que pasaba
        return result;
    }

    public List<SingleFlightViewer> getVuelosDelDía() {
        return vuelosDelDía;
    }

    public boolean isValiadCoordenade(double x) {
        if (x > 0d && x < 12000d) {
            for (SingleFlightViewer viewer : vuelosDelDía) {
                double margen = viewer.getWidth() / 4;
                return (viewer.getLayoutX() + margen) > x || (viewer.getLayoutX() + margen * 3) < x;
            }
            return true;
        }
        return false;
    }
}
