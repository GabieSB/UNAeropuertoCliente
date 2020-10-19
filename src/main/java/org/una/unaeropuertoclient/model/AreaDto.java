/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.una.unaeropuertoclient.model;

import java.util.Date;
import java.util.List;

public class AreaDto {

    private Long id;
    private String nombre;
    private String descripcion;
    private Date fechaRegistro;
    private Date fechaModificacion;
    private Boolean activo;
   
    private List<UsuarioDto> usuarioList;
   
    private List<GastoReparacionDto> gastoReparacionList;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Date getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(Date fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }

    public Date getFechaModificacion() {
        return fechaModificacion;
    }

    public void setFechaModificacion(Date fechaModificacion) {
        this.fechaModificacion = fechaModificacion;
    }

    public Boolean getActivo() {
        return activo;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }

    public List<UsuarioDto> getUsuarioList() {
        return usuarioList;
    }

    public void setUsuarioList(List<UsuarioDto> usuarioList) {
        this.usuarioList = usuarioList;
    }

    public List<GastoReparacionDto> getGastoReparacionList() {
        return gastoReparacionList;
    }

    public void setGastoReparacionList(List<GastoReparacionDto> gastoReparacionList) {
        this.gastoReparacionList = gastoReparacionList;
    }
    
}
