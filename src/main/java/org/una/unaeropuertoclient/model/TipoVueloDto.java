/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.una.unaeropuertoclient.model;

import java.util.List;

/**
 *
 * @author roberth :)
 */
public class TipoVueloDto {

    private Long id;
    private String nombre;
    private Boolean activo;
    private List<VueloDto> vueloList;

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

    public List<VueloDto> getVueloList() {
        return vueloList;
    }

    public void setVueloList(List<VueloDto> vueloList) {
        this.vueloList = vueloList;
    }

}
