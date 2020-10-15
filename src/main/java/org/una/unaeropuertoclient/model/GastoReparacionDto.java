/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.una.unaeropuertoclient.model;

import java.util.Date;
import java.util.List;

public class GastoReparacionDto {

    private Long id;
    private Date fechaRegistro;
    private Boolean estadoPago;
    private Long numeroContrato;
    private Integer duracion;
    private Integer periodicidad;
    private String observaciones;
    private Boolean activo;
    private List<DetalleServicioDto> detalleServicioList;
    private AreaDto areasId;
    private TipoReparacionDto tiposId;
    private ProvedorDto provedoresId;

}
