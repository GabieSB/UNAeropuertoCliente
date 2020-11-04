/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.una.unaeropuertoclient.model;

import java.util.List;

/**
 *
 * @author LordLalo
 */
public class RecolectorInfoNotas {

    private Long idNota;
    private String matricula;
    private String fecha;
    private String idArea;
    private String fechaServico;
    private String numeroFactura;
    private String tipoServicio;
    private boolean estado;

    public RecolectorInfoNotas() {
    }

    public RecolectorInfoNotas(VueloDto vuelo, List<NotificacionDto> notifList) {
        notifList.forEach(notif -> {
            if (notif.getIdObjeto().equals(vuelo.getId())) {
                this.idNota = notif.getId();
                this.matricula = vuelo.getNombreVuelo();
                this.fecha = notif.getFechaEnFormatoHumano();
                this.idArea = notif.getAreasId().toString();
                this.fechaServico = vuelo.getSitioYFechaLLegada();
                this.numeroFactura = vuelo.getAvionesId().getMatricula();
                this.tipoServicio = vuelo.getSitioYFechaSalida();
                this.estado = notif.isActivo();
            }
        });
    }

    public RecolectorInfoNotas(GastoReparacionDto gastoRepar, List<NotificacionDto> notifList) {
        notifList.forEach(notif -> {
            if (notif.getIdObjeto().equals(gastoRepar.getId())) {
                this.idNota = notif.getId();
                this.matricula = gastoRepar.getNumeroContrato().toString();
                this.fecha = notif.getFechaEnFormatoHumano();
                this.idArea = notif.getAreasId().toString();
                this.fechaServico = gastoRepar.getFechaServicioFormateada();
                this.numeroFactura = gastoRepar.getTiposId().getNombre();
                this.tipoServicio = gastoRepar.getObservaciones();
                this.estado = notif.isActivo();
            }
        });
    }

    public RecolectorInfoNotas(ServicioMantenimientoDto servicioM, List<NotificacionDto> notifList) {
        notifList.forEach(notif -> {
            if (notif.getIdObjeto().equals(servicioM.getId())) {
                this.idNota = notif.getId();
                this.matricula = servicioM.getAvionesId().getMatricula();
                this.fecha = notif.getFechaEnFormatoHumano();
                this.idArea = notif.getAreasId().toString();
                this.fechaServico = servicioM.getFechaEnFormatoHumano();
                this.numeroFactura = servicioM.getNumeroFactura().toString();
                this.tipoServicio = servicioM.getTiposServiciosId().getNombre();
                this.estado = notif.isActivo();
            }
        });
    }

    public String getTipoServicio() {
        return tipoServicio;
    }

    public boolean isEstado() {
        return estado;
    }

    public void setEstado(boolean estado) {
        this.estado = estado;
    }

    public String getNumeroFactura() {
        return numeroFactura;
    }

    public String getFecha() {
        return fecha;
    }

    public String getIdArea() {
        return idArea;
    }

    public Long getIdNota() {
        return idNota;
    }

    public String getMatricula() {
        return matricula;
    }

    public String getFechaServico() {
        return fechaServico;
    }

    public String getNumeroFeactura() {
        return numeroFactura;
    }

    public String getEstadoEnFormatoHumano() {
        return estado ? "Pendiente" : "Atendida";
    }

}
