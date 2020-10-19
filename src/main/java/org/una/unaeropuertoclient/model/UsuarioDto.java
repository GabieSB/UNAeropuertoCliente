/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.una.unaeropuertoclient.model;



import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

public class UsuarioDto {

    private Long id;
    private String cedula;
    private String nombre;
    private String apellidos;
    private String contrasenna;
    private Timestamp fechaNacimiento;
    private Timestamp fechaIngreso;
    private Timestamp fechaModificacion;
    private Boolean activo;
    private List<BitacoraDto> bitacoraList;
    private List<RolUsuarioDto> rolUsuarioList;
    private AreaDto areasId;

    public UsuarioDto(String cedula, String nombre, String apellidos, String contrasenna,Timestamp fechaNacimiento,Timestamp fechaIngreso, Timestamp fechaModificacion, Boolean activo,AreaDto areaDto) {
        this.cedula = cedula;
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.contrasenna = contrasenna;
        this.fechaNacimiento = fechaNacimiento;
        this.fechaIngreso = fechaIngreso;
        this.fechaModificacion = fechaModificacion;
        this.activo = activo;
         areasId=areaDto;
    }

  
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(Timestamp fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public List<BitacoraDto> getBitacoraList() {
        return bitacoraList;
    }

    public void setBitacoraList(List<BitacoraDto> bitacoraList) {
        this.bitacoraList = bitacoraList;
    }

    public List<RolUsuarioDto> getRolUsuarioList() {
        return rolUsuarioList;
    }

    public void setRolUsuarioList(List<RolUsuarioDto> rolUsuarioList) {
        this.rolUsuarioList = rolUsuarioList;
    }

    public AreaDto getAreasId() {
        return areasId;
    }

    public void setAreasId(AreaDto areasId) {
        this.areasId = areasId;
    }

    public String getCedula() {
        return cedula;
    }

    public void setCedula(String cedula) {
        this.cedula = cedula;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getContrasenna() {
        return contrasenna;
    }

    public void setContrasenna(String contrasenna) {
        this.contrasenna = contrasenna;
    }

    public Date getFechaIngreso() {
        return fechaIngreso;
    }

    public void setFechaIngreso(Timestamp fechaIngreso) {
        this.fechaIngreso = fechaIngreso;
    }

    public Date getFechaModificacion() {
        return fechaModificacion;
    }

    public void setFechaModificacion(Timestamp fechaModificacion) {
        this.fechaModificacion = fechaModificacion;
    }

    public Boolean getActivo() {
        return activo;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }
    
}
