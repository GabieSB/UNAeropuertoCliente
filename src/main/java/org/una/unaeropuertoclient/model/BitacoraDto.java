/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.una.unaeropuertoclient.model;

import java.sql.Timestamp;
import java.util.Date;

public class BitacoraDto {
    
    private Long id;
    private Timestamp fechaModificacion;
    private String tipoCambio;
    private UsuarioDto usuariosId;

    public BitacoraDto(Timestamp fechaModificacion, String tipoCambio, UsuarioDto usuariosId) {
        this.fechaModificacion = fechaModificacion;
        this.tipoCambio = tipoCambio;
        this.usuariosId = usuariosId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Timestamp getFechaModificacion() {
        return fechaModificacion;
    }

    public void setFechaModificacion(Timestamp fechaModificacion) {
        this.fechaModificacion = fechaModificacion;
    }

    public String getTipoCambio() {
        return tipoCambio;
    }

    public void setTipoCambio(String tipoCambio) {
        this.tipoCambio = tipoCambio;
    }

    public UsuarioDto getUsuariosId() {
        return usuariosId;
    }

    public void setUsuariosId(UsuarioDto usuariosId) {
        this.usuariosId = usuariosId;
    }
}
