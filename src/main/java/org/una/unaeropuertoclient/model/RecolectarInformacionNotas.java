/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.una.unaeropuertoclient.model;

/**
 *
 * @author LordLalo
 */
public class RecolectarInformacionNotas {

    private Long idNota;
    private String matricula;
    private String fecha;
    private String idArea;
    private String fechaServico;
    private String numeroFactura;
    private String tipoServicio;
    private Boolean estado;
 
    public RecolectarInformacionNotas() {
    }

    public RecolectarInformacionNotas(Long idNota, String matricula, String fecha, String idArea, String fechaServico, String numeroFeactura, String tipoServicio,boolean estado) {
        this.idNota = idNota;
        this.matricula = matricula;
        this.fecha = fecha;
        this.idArea = idArea;
        this.fechaServico = fechaServico;
        this.numeroFactura = numeroFeactura;
        this.tipoServicio = tipoServicio;
        this.estado=estado;

    }

    public String getTipoServicio() {
        return tipoServicio;
    }

    public boolean isEstado() {
        return estado;
    }

    public void setEstado(boolean estado) {
        this.estado = estado;
    }

    

    public String getNumeroFactura() {
        return numeroFactura;
    }

    public String getFecha() {
        return fecha;
    }

    public String getIdArea() {
        return idArea;
    }

    public Long getIdNota() {
        return idNota;
    }

    public String getMatricula() {
        return matricula;
    }

    public String getFechaServico() {
        return fechaServico;
    }

    public String getNumeroFeactura() {
        return numeroFactura;
    }

}
