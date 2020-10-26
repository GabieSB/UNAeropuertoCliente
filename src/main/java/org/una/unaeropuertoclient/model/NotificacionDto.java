package org.una.unaeropuertoclient.model;

import java.sql.Timestamp;
public class NotificacionDto {

    Long id;
    Boolean activo;
    Long idObjeto;
    Timestamp fechaRegistro;
    AreaDto areasId;

    public NotificacionDto() {
    }

    public NotificacionDto(boolean activo, Long idObjeto, Timestamp fechaRegistro, AreaDto areasId) {
        this.activo = activo;
        this.idObjeto = idObjeto;
        this.fechaRegistro = fechaRegistro;
        this.areasId = areasId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }

    public Long getIdObjeto() {
        return idObjeto;
    }

    public void setIdObjeto(Long idObjeto) {
        this.idObjeto = idObjeto;
    }

    public Timestamp getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(Timestamp fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }

    public AreaDto getAreasId() {
        return areasId;
    }

    public void setAreasId(AreaDto areasId) {
        this.areasId = areasId;
    }

}
