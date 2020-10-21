/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.una.unaeropuertoclient.model;

import java.util.List;

public class LugarDto {

    private Long id;
    private String nombre;
    private Boolean activo;
    private List<VueloDto> vuelosSalidaList;
    private List<VueloDto> vuelosLlegadaList;

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

    public List<VueloDto> getVuelosSalidaList() {
        return vuelosSalidaList;
    }

    public void setVuelosSalidaList(List<VueloDto> vuelosSalidaList) {
        this.vuelosSalidaList = vuelosSalidaList;
    }

    public List<VueloDto> getVuelosLlegadaList() {
        return vuelosLlegadaList;
    }

    public void setVuelosLlegadaList(List<VueloDto> vuelosLlegadaList) {
        this.vuelosLlegadaList = vuelosLlegadaList;
    }

    @Override
    public String toString() {
        return this.getNombre();
    }
    
    
}
