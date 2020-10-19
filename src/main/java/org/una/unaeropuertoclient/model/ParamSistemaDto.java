/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.una.unaeropuertoclient.model;

import java.util.Date;

public class ParamSistemaDto {

    private Long id;
    private Short vuelosHora;
    private Integer tiempoInactividad;
    private String telefonoAeropuerto;
    private String emailAeropuerto;
    private String nombreRepresentante;
    private Date aperturaOficina;
    private Date cierreOficina;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
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

    public Date getAperturaOficina() {
        return aperturaOficina;
    }

    public void setAperturaOficina(Date aperturaOficina) {
        this.aperturaOficina = aperturaOficina;
    }

    public Date getCierreOficina() {
        return cierreOficina;
    }

    public void setCierreOficina(Date cierreOficina) {
        this.cierreOficina = cierreOficina;
    }
    
}
