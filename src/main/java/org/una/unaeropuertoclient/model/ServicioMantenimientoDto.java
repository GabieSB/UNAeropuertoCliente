/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


package org.una.unaeropuertoclient.model;

import java.util.Date;
import java.util.List;

public class ServicioMantenimientoDto {

    private Long id;
    private Date fechaServicio;
    private Long numeroFactura;
    private Boolean estadoPago;
    private Boolean estaFinalizacion;
    private Boolean activo;
    private AvionDto avionesId;
    private TipoServicioDto tiposServiciosId;
    private List<CobroDto> cobroList;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getFechaServicio() {
        return fechaServicio;
    }

    public void setFechaServicio(Date fechaServicio) {
        this.fechaServicio = fechaServicio;
    }

    public Long getNumeroFactura() {
        return numeroFactura;
    }

    public void setNumeroFactura(Long numeroFactura) {
        this.numeroFactura = numeroFactura;
    }

    public Boolean getEstadoPago() {
        return estadoPago;
    }

    public void setEstadoPago(Boolean estadoPago) {
        this.estadoPago = estadoPago;
    }

    public Boolean getEstaFinalizacion() {
        return estaFinalizacion;
    }

    public void setEstaFinalizacion(Boolean estaFinalizacion) {
        this.estaFinalizacion = estaFinalizacion;
    }

    public Boolean getActivo() {
        return activo;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }

    public AvionDto getAvionesId() {
        return avionesId;
    }

    public void setAvionesId(AvionDto avionesId) {
        this.avionesId = avionesId;
    }

    public TipoServicioDto getTiposServiciosId() {
        return tiposServiciosId;
    }

    public void setTiposServiciosId(TipoServicioDto tiposServiciosId) {
        this.tiposServiciosId = tiposServiciosId;
    }

    public List<CobroDto> getCobroList() {
        return cobroList;
    }

    public void setCobroList(List<CobroDto> cobroList) {
        this.cobroList = cobroList;
    }

    public ServicioMantenimientoDto( Long numeroFactura, Boolean estadoPago, Boolean estaFinalizacion, AvionDto avionesId, TipoServicioDto tiposServiciosId) {
        this.fechaServicio = new Date();
        this.numeroFactura = numeroFactura;
        this.estadoPago = estadoPago;
        this.estaFinalizacion = estaFinalizacion;
        this.activo = true;
        this.avionesId = avionesId;
        this.tiposServiciosId = tiposServiciosId;
        this.cobroList = null;
    }
}
