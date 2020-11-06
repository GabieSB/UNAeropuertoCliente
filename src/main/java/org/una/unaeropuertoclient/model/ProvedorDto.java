/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.una.unaeropuertoclient.model;

import java.util.List;

public class ProvedorDto {

    private Long id;
    private String nombre;
    private Boolean activo;
    private List<GastoReparacionDto> gastoReparList;

    public ProvedorDto(String text) {
        this.nombre = text;
        this.activo = true;
        this.gastoReparList = null;
    }

    public String getNombre() {
        return nombre;
    }

    public Long getId() {
        return id;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }



    public ProvedorDto(Long id, String nombre) {
        this.id = id;
        this.nombre = nombre;
        this.activo = true;
        this.gastoReparList = null;
    }

    public void setActivo(boolean b) {
        this.activo = b;
    }
}
