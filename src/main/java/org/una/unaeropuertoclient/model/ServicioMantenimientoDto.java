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
import java.util.ArrayList;
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

    public String getFechaServicioFormateada() {
        DateFormat dateFormat = new SimpleDateFormat("dd MMMM yyyy");
        String strDate = dateFormat.format(fechaServicio);
        return strDate;
    }

    public Long getNumeroFactura() {
        return numeroFactura;
    }


    public Boolean getEstadoPago() {
        return estadoPago;
    }

    public String getEstadoPagoPalabra(){
        return  (estadoPago)?"Pagado":"Pendiente";
    }

    public Boolean getEstaFinalizacion() {
        return estaFinalizacion;
    }

    public String getEstadoFinalizacionPalabra(){
        return  (estaFinalizacion)?"Finalizado":"Pendiente";
    }


    public Boolean getActivo() {
        return activo;
    }

    public String getActivoPalabra(){
        return  (activo)?"Activo":"Inactivo";
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }

    public AvionDto getAvionesId() {
        return avionesId;
    }

    public TipoServicioDto getTiposServiciosId() {
        return tiposServiciosId;
    }

    public List<CobroDto> getCobroList() {
        return cobroList;
    }

    public String getCobro(){
        if(cobroList!=null && !cobroList.isEmpty()){
            return cobroList.get(0).getMonto().toString();
        }
        return "No registrado";
    }

    public ServicioMantenimientoDto(LocalDate fechaServicio, Long numeroFactura, Boolean estadoPago, Boolean estaFinalizacion, AvionDto avionesId, TipoServicioDto tiposServiciosId) {
        this.fechaServicio = Date.from(fechaServicio.atStartOfDay(ZoneId.systemDefault()).toInstant());
        this.numeroFactura = numeroFactura;
        this.estadoPago = estadoPago;
        this.estaFinalizacion = estaFinalizacion;
        this.activo = true;
        this.avionesId = avionesId;
        this.tiposServiciosId = tiposServiciosId;
        this.cobroList = null;
    }

    public String getFechaEnFormatoHumano() {
        DateTimeFormatter formatters = DateTimeFormatter.ofPattern("dd-MM-uuuu");
        LocalDateTime ldt = LocalDateTime.ofInstant(this.fechaServicio.toInstant(), ZoneId.systemDefault());
        LocalDate dia = ldt.toLocalDate();
        String diasText = dia.format(formatters);
        String ret = diasText + " a la" + (ldt.getHour() != 1 ? "s" : "");
        ret += " " + ldt.toLocalTime().toString();
        return ret;
    }
}
