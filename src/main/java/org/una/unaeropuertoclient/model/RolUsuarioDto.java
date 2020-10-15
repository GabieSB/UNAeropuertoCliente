/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.una.unaeropuertoclient.model;

import java.util.Date;

public class RolUsuarioDto {

    private Long id;
    private Date fechaRegistro;
    private Date fechaModificacion;
    private Boolean activo;
    private RolDto rolesId;
    private UsuarioDto usuariosId;

    public Long getId() {
        return id;
    }

    @Override
    public String toString() {
        return "RolUsuarioDto{" +
                "id=" + id +
                ", fechaRegistro=" + fechaRegistro +
                ", fechaModificacion=" + fechaModificacion +
                ", activo=" + activo +
                ", rolesId=" + rolesId.toString() +
                ", usuariosId=" + usuariosId +
                '}';
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(Date fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }

    public Date getFechaModificacion() {
        return fechaModificacion;
    }

    public void setFechaModificacion(Date fechaModificacion) {
        this.fechaModificacion = fechaModificacion;
    }

    public Boolean getActivo() {
        return activo;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }

    public RolDto getRolesId() {
        return rolesId;
    }

    public void setRolesId(RolDto rolesId) {
        this.rolesId = rolesId;
    }

    public UsuarioDto getUsuariosId() {
        return usuariosId;
    }

    public void setUsuariosId(UsuarioDto usuariosId) {
        this.usuariosId = usuariosId;
    }
}
