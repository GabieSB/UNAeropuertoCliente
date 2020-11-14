package org.una.unaeropuertoclient.model;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class NotificacionDto {

    private Long id;
    private Boolean activo;
    private Long idObjeto;
    private Timestamp fechaRegistro;
    private AreaDto areasId;

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

    public String getFechaEnFormatoHumano() {
        LocalDateTime ldt = fechaRegistro.toLocalDateTime();
        DateTimeFormatter formatters = DateTimeFormatter.ofPattern("dd-MM-uuuu");
        LocalDate dia = ldt.toLocalDate();
        String diasText = dia.format(formatters);
        String ret = diasText + " a la" + (ldt.getHour() != 1 ? "s" : "");
        ret += " " + ldt.toLocalTime().toString();
        return ret;
    }

   

}
