package org.una.unaeropuertoclient.model;

import java.util.Date;

public class NotificacionDto {
    long id;
    boolean activo;
    int idObjeto;
    Date fechaRegistro;
    AreaDto areasId;

    public NotificacionDto( boolean activo, int idObjeto, Date fechaRegistro, AreaDto areasId) {
        this.activo = activo;
        this.idObjeto = idObjeto;
        this.fechaRegistro = fechaRegistro;
        this.areasId = areasId;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }

    public int getIdObjeto() {
        return idObjeto;
    }

    public void setIdObjeto(int idObjeto) {
        this.idObjeto = idObjeto;
    }

    public Date getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(Date fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }

    public AreaDto getAreasId() {
        return areasId;
    }

    public void setAreasId(AreaDto areasId) {
        this.areasId = areasId;
    }
}
