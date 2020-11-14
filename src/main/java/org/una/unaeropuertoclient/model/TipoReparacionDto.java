/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


package org.una.unaeropuertoclient.model;

import java.util.List;

public class TipoReparacionDto {

    private Long id;
    private String nombre;
    private Boolean activo;
    private List<GastoReparacionDto> gastoReparList;

    public TipoReparacionDto(String nombre, Boolean activo, List<GastoReparacionDto> gastoReparList) {
        this.nombre = nombre;
        this.activo = activo;
        this.gastoReparList = gastoReparList;
    }

    public TipoReparacionDto(Long id, String text) {
        this.id = id;
        this.nombre = text;
        this.activo = true;
        gastoReparList = null;
    }

    public TipoReparacionDto(String text) {
        this.nombre = text;
        this.activo = true;
        this.gastoReparList = null;
    }

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

    public Boolean getActivo() {
        return activo;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }

    public List<GastoReparacionDto> getGastoReparList() {
        return gastoReparList;
    }

    public void setGastoReparList(List<GastoReparacionDto> gastoReparList) {
        this.gastoReparList = gastoReparList;
    }
}
