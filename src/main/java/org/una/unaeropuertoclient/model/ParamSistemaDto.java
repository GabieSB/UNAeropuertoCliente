/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.una.unaeropuertoclient.model;

import java.sql.Timestamp;

public class ParamSistemaDto {

    private Integer id;
    private Short vuelosHora;
    private Integer tiempoInactividad;
    private String telefonoAeropuerto;
    private String emailAeropuerto;
    String nombreRepresentante;
    private Timestamp aperturaOficina;
    private Timestamp cierreOficina;
    private LugarDto ubicacion;

    public ParamSistemaDto() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Short getVuelosHora() {
        return vuelosHora;
    }

    public void setVuelosHora(Short vuelosHora) {
        this.vuelosHora = vuelosHora;
    }

    public Integer getTiempoInactividad() {
        return tiempoInactividad;
    }

    public void setTiempoInactividad(Integer tiempoInactividad) {
        this.tiempoInactividad = tiempoInactividad;
    }

    public String getTelefonoAeropuerto() {
        return telefonoAeropuerto;
    }

    public void setTelefonoAeropuerto(String telefonoAeropuerto) {
        this.telefonoAeropuerto = telefonoAeropuerto;
    }

    public String getEmailAeropuerto() {
        return emailAeropuerto;
    }

    public void setEmailAeropuerto(String emailAeropuerto) {
        this.emailAeropuerto = emailAeropuerto;
    }

    public String getNombreRepresentante() {
        return nombreRepresentante;
    }

    public void setNombreRepresentante(String nombreRepresentante) {
        this.nombreRepresentante = nombreRepresentante;
    }

    public Timestamp getAperturaOficina() {
        return aperturaOficina;
    }

    public void setAperturaOficina(Timestamp aperturaOficina) {
        this.aperturaOficina = aperturaOficina;
    }

    public Timestamp getCierreOficina() {
        return cierreOficina;
    }

    public void setCierreOficina(Timestamp cierreOficina) {
        this.cierreOficina = cierreOficina;
    }

    public LugarDto getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(LugarDto ubicacion) {
        this.ubicacion = ubicacion;
    }

}
