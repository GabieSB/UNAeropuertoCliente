/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.una.unaeropuertoclient.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import org.una.unaeropuertoclient.utils.VuelosUtilis;

public class VueloDto {

    private Long id;
    private String nombreVuelo;
    private Date horaSalida;
    private Date horaLlegada;
    private Byte estado;
    private AvionDto avionesId;
    private PistaDto pistasId;
    private LugarDto lugarSalida;
    private LugarDto lugarLlegada;
    private AlertaDto alerta;
    private TipoVueloDto tipoVuelo;

    public String getSitioYFechaLLegada() {
        return lugarLlegada.getNombre() + " el " + toCompresionHumana(horaLlegada);
    }

    public String getSitioYFechaSalida() {
        return lugarSalida.getNombre() + " el " + toCompresionHumana(horaSalida);
    }

    public String getStateAsWord() {
        switch (estado) {
            case 0:
                return "Programado";
            case 1:
                return "En vuelo";
            case 2:
                return "Finalizado";
            default:
                return "Cancelado";
        }
    }

    public void setStateAsWord(String state) {
        switch (state) {
            case "Programado":
                estado = 0;
                break;
            case "En vuelo":
                estado = 1;
                break;
            case "Finalizado":
                estado = 2;
                break;
            default:
                estado = 3;
                break;
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombreVuelo() {
        return nombreVuelo;
    }

    public void setNombreVuelo(String nombreVuelo) {
        this.nombreVuelo = nombreVuelo;
    }

    public Date getHoraSalida() {
        return horaSalida;
    }

    public void setHoraSalida(Date horaSalida) {
        this.horaSalida = horaSalida;
    }

    public Date getHoraLlegada() {
        return horaLlegada;
    }

    public void setHoraLlegada(Date horaLlegada) {
        this.horaLlegada = horaLlegada;
    }

    public Byte getEstado() {
        return estado;
    }

    public void setEstado(Byte estado) {
        this.estado = estado;
    }

    public AvionDto getAvionesId() {
        return avionesId;
    }

    public void setAvionesId(AvionDto avionesId) {
        this.avionesId = avionesId;
    }

    public PistaDto getPistasId() {
        return pistasId;
    }

    public void setPistasId(PistaDto pistasId) {
        this.pistasId = pistasId;
    }

    public LugarDto getLugarSalida() {
        return lugarSalida;
    }

    public void setLugarSalida(LugarDto lugarSalida) {
        this.lugarSalida = lugarSalida;
    }

    public LugarDto getLugarLlegada() {
        return lugarLlegada;
    }

    public void setLugarLlegada(LugarDto lugarLlegada) {
        this.lugarLlegada = lugarLlegada;
    }

    public AlertaDto getAlerta() {
        return alerta;
    }

    public void setAlerta(AlertaDto alerta) {
        this.alerta = alerta;
    }

    private String toCompresionHumana(Date date) {
        DateTimeFormatter formatters = DateTimeFormatter.ofPattern("dd-MM-uuuu");
        LocalDateTime ldt = LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault());
        LocalDate dia = ldt.toLocalDate();
        String diasText = dia.format(formatters);
        String ret = diasText + " a la" + (ldt.getHour() != 1 ? "s" : "");
        ret += " " + ldt.toLocalTime().toString();
        return ret;
    }

    public TipoVueloDto getTipoVuelo() {
        return tipoVuelo;
    }

    public void setTipoVuelo(TipoVueloDto tipoVuelo) {
        this.tipoVuelo = tipoVuelo;
    }

    public LocalDateTime getExecutionDateTime(LugarDto ubicacionAeroP) {
        if (this.lugarLlegada.equals(ubicacionAeroP)) {
            return VuelosUtilis.toLocalDateTime(horaLlegada);
        } else {
            return VuelosUtilis.toLocalDateTime(horaSalida);
        }
    }

}
