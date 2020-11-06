/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.una.unaeropuertoclient.model;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class GastoReparacionDto {

    private Long id;
    private Date fechaRegistro;
    private Boolean estadoPago;
    private Long numeroContrato;
    private Integer duracion;
    private Integer periodicidad;
    private String observaciones;
    private Boolean activo;
    private AreaDto areasId;
    private TipoReparacionDto tiposId;
    private ProvedorDto provedoresId;
    private Float monto;

    public GastoReparacionDto() {

    }

    public GastoReparacionDto(LocalDate fechaRegistro, Boolean estadoPago, Long numeroContrato, Integer duracion, Integer periodicidad, String observaciones, AreaDto areasId, TipoReparacionDto tiposId, ProvedorDto proveedor, Float monto) {
        this.fechaRegistro = Date.from(fechaRegistro.atStartOfDay(ZoneId.systemDefault()).toInstant());
        this.estadoPago = estadoPago;
        this.numeroContrato = numeroContrato;
        this.duracion = duracion;
        this.periodicidad = periodicidad;
        this.observaciones = observaciones;
        this.areasId = areasId;
        this.tiposId = tiposId;
        this.provedoresId  = proveedor;
        this.activo = true;
        this.monto = monto;
    }

    public ProvedorDto getProvedoresId() {
        return provedoresId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getFechaRegistro() {
        return fechaRegistro;
    }

    public String getFechaServicioFormateada() {
        DateFormat dateFormat = new SimpleDateFormat("dd MMMM yyyy");
        String strDate = dateFormat.format(fechaRegistro);
        return strDate;
    }

    public Boolean getEstadoPago() {
        return estadoPago;
    }

    public void setEstadoPago(Boolean estadoPago) {
        this.estadoPago = estadoPago;
    }

    public Long getNumeroContrato() {
        return numeroContrato;
    }


    public Integer getDuracion() {
        return duracion;
    }

    public void setDuracion(Integer duracion) {
        this.duracion = duracion;
    }

    public Integer getPeriodicidad() {
        return periodicidad;
    }

    public void setPeriodicidad(Integer periodicidad) {
        this.periodicidad = periodicidad;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public Boolean getActivo() {
        return activo;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }

    public AreaDto getAreasId() {
        return areasId;
    }

    public void setAreasId(AreaDto areasId) {
        this.areasId = areasId;
    }

    public TipoReparacionDto getTiposId() {
        return tiposId;
    }

    public void setTiposId(TipoReparacionDto tiposId) {
        this.tiposId = tiposId;
    }

    public Float getMonto() {
        return  monto;
    }

    public String toCompresionHumana() {
        DateTimeFormatter formatters = DateTimeFormatter.ofPattern("dd-MM-uuuu");
        LocalDateTime ldt = LocalDateTime.ofInstant(fechaRegistro.toInstant(), ZoneId.systemDefault());
        LocalDate dia = ldt.toLocalDate();
        String diasText = dia.format(formatters);
        String ret = diasText + " a la" + (ldt.getHour() != 1 ? "s" : "");
        ret += " " + ldt.toLocalTime().toString();
        return ret;
    }
}
