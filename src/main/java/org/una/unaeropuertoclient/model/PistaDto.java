/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.una.unaeropuertoclient.model;

import java.util.List;
import java.util.Objects;

public class PistaDto {

    private Long id;
    private String numeroPista;
    private Float longitud;
    private Boolean activo;
    private List<VueloDto> vueloList;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNumeroPista() {
        return numeroPista;
    }

    public void setNumeroPista(String numeroPista) {
        this.numeroPista = numeroPista;
    }

    public Float getLongitud() {
        return longitud;
    }

    public void setLongitud(Float longitud) {
        this.longitud = longitud;
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
        return numeroPista + " - " + longitud.toString() + "m";
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 59 * hash + Objects.hashCode(this.id);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj != null) {
            if (obj instanceof PistaDto) {
                PistaDto pist = (PistaDto) obj;
                return pist.getNumeroPista().equals(pist.getNumeroPista()) && Objects.equals(pist.getId(), id);
            }
        }
        return false;
    }

}
