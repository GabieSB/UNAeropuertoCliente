/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.una.unaeropuertoclient.model;

import java.util.Date;

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

    public String getSitioYFechaLLegada() {
        return lugarLlegada.getNombre() + " " + horaLlegada;
    }

    public String getSitioYFechaSalida() {
        return lugarSalida.getNombre() + " " + horaSalida;
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
            case "En vuelo":
                estado = 1;
            case "Finalizado":
                estado = 2;
            default:
                estado = 3;
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
    
    

}
