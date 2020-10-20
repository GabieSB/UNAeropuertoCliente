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
//    private Date aperturaOficina;
//    private Date cierreOficina;
    String nombreRepresentante;
//    private Date aperturaOficina;
//    private Date cierreOficina;
    private Timestamp aperturaOficina;
    private Timestamp cierreOficina;

    public ParamSistemaDto() {
    }

//    public ParamSistemaDto(Integer id, Short vuelosHora, Integer tiempoInactividad, String telefonoAeropuerto, String emailAeropuerto, String nombreRepresentante, Date aperturaOficina, Date cierreOficina) {
//        this.id = id;
//        this.vuelosHora = vuelosHora;
//        this.tiempoInactividad = tiempoInactividad;
//        this.telefonoAeropuerto = telefonoAeropuerto;
//        this.emailAeropuerto = emailAeropuerto;
//        this.nombreRepresentante = nombreRepresentante;
//        this.aperturaOficina = aperturaOficina;
//        this.cierreOficina = cierreOficina;
//    }

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
//
//    public Date getAperturaOficina() {
//        return aperturaOficina;
//    }
//
//    public void setAperturaOficina(Date aperturaOficina) {
//        this.aperturaOficina = aperturaOficina;
//    }
//
//    public Date getCierreOficina() {
//        return cierreOficina;
//    }
//
//    public void setCierreOficina(Date cierreOficina) {
//        this.cierreOficina = cierreOficina;
//    }

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
    

}
