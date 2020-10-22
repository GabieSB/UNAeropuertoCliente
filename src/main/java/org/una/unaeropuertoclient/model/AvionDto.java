/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.una.unaeropuertoclient.model;

import java.util.List;

public class AvionDto {

    private Long id;
    private String matricula;
    private Boolean activo;
    private List<ServicioMantenimientoDto> servicioMantenimientoList;
    private AerolineaDto aerolineasId;
    private List<VueloDto> vueloList;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMatricula() {
        return matricula;
    }

    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }

    public Boolean getActivo() {
        return activo;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }

    public List<ServicioMantenimientoDto> getServicioMantenimientoList() {
        return servicioMantenimientoList;
    }

    public void setServicioMantenimientoList(List<ServicioMantenimientoDto> servicioMantenimientoList) {
        this.servicioMantenimientoList = servicioMantenimientoList;
    }

    public AerolineaDto getAerolineasId() {
        return aerolineasId;
    }

    public void setAerolineasId(AerolineaDto aerolineasId) {
        this.aerolineasId = aerolineasId;
    }

    public List<VueloDto> getVueloList() {
        return vueloList;
    }

    public void setVueloList(List<VueloDto> vueloList) {
        this.vueloList = vueloList;
    }

    @Override
    public String toString() {
        return this.matricula;
    }

}
