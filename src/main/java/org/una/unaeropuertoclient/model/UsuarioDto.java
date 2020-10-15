/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.una.unaeropuertoclient.model;



import java.util.Date;
import java.util.List;

public class UsuarioDto {

    private Long id;
    private String cedula;
    private String nombre;
    private String apellidos;
    private String contrasenna;
    private Date fechaNacimiento;
    private Date fechaIngreso;
    private Date fechaModificacion;
    private Boolean activo;
    private List<BitacoraDto> bitacoraList;
    private List<RolUsuarioDto> rolUsuarioList;
    private AreaDto areasId;
}
