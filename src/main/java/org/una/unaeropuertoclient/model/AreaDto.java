/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.una.unaeropuertoclient.model;

import java.util.Date;
import java.util.List;

public class AreaDto {

    private Long id;
    private String nombre;
    private String descripcion;
    private Date fechaRegistro;
    private Date fechaModificacion;
    private Boolean activo;
   
    private List<UsuarioDto> usuarioList;
   
    private List<GastoReparacionDto> gastoReparacionList;
    
}
