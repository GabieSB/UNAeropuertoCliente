/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.una.unaeropuertoclient.model;

import java.util.List;
import java.util.Objects;

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

    @Override
    public String toString() {
        return this.nombre;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj != null) {
            if (obj instanceof TipoVueloDto) {
                TipoVueloDto tipoV = (TipoVueloDto) obj;
                return tipoV.getNombre().equals(nombre) && Objects.equals(tipoV.getId(), id);
            }
        }
        return false;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 97 * hash + Objects.hashCode(this.id);
        hash = 97 * hash + Objects.hashCode(this.nombre);
        return hash;
    }

}
