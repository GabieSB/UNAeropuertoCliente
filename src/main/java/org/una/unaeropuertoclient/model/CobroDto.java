/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.una.unaeropuertoclient.model;

public class CobroDto {

    private Long id;
    private Float monto;
    private String detalleCobro;
    private Boolean activo;
    private ServicioMantenimientoDto serviciosMantenimientoId;

    public CobroDto(Float monto, String detalleCobro) {
        this.monto = monto;
        this.detalleCobro = detalleCobro;
        this.activo = true;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Float getMonto() {
        return monto;
    }

    public void setMonto(Float monto) {
        this.monto = monto;
    }

    public String getDetalleCobro() {
        return detalleCobro;
    }

    public void setDetalleCobro(String detalleCobro) {
        this.detalleCobro = detalleCobro;
    }

    public Boolean getActivo() {
        return activo;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }

    public ServicioMantenimientoDto getServiciosMantenimientoId() {
        return serviciosMantenimientoId;
    }

    public void setServiciosMantenimientoId(ServicioMantenimientoDto serviciosMantenimientoId) {
        this.serviciosMantenimientoId = serviciosMantenimientoId;
    }
}
