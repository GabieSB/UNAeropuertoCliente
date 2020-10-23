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
 * @author Roberth :)
 */
public class AerolineaDto {

    private Long id;
    private String nombre;
    private Boolean actiov;
    private List<AvionDto> avionList;

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

    public Boolean getActiov() {
        return actiov;
    }

    public void setActiov(Boolean actiov) {
        this.actiov = actiov;
    }

    public List<AvionDto> getAvionList() {
        return avionList;
    }

    public void setAvionList(List<AvionDto> avionList) {
        this.avionList = avionList;
    }

    @Override
    public String toString() {
        return this.nombre;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj != null) {
            if (obj instanceof AerolineaDto) {
                AerolineaDto aero = (AerolineaDto) obj;
                return aero.getNombre().equals(nombre) && Objects.equals(aero.getId(), id);
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
