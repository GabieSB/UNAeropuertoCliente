/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.una.unaeropuertoclient.model;

import java.util.List;
import java.util.Objects;

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

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 23 * hash + Objects.hashCode(this.id);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj != null) {
            if (obj instanceof AvionDto) {
                AvionDto avi = (AvionDto) obj;
                return this.matricula.equals(avi.getMatricula()) && Objects.equals(avi.getId(), id);
            }
        }
        return false;
    }

}
