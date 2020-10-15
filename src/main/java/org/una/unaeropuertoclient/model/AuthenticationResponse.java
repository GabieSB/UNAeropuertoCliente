/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.una.unaeropuertoclient.model;

import java.util.List;

/**
 *
 * @author LordLalo
 */

public class AuthenticationResponse {
   private String jwt; 
   private UsuarioDto usuario;
   private List<RolUsuarioDto> rolUsuario;

   public AuthenticationResponse(String jwt, UsuarioDto usuario, List<RolUsuarioDto> rolUsuario) {
      this.jwt = jwt;
      this.usuario = usuario;
      this.rolUsuario = rolUsuario;
   }

   public String getJwt() {
      return jwt;
   }

   public void setJwt(String jwt) {
      this.jwt = jwt;
   }

   public UsuarioDto getUsuario() {
      return usuario;
   }

   public void setUsuario(UsuarioDto usuario) {
      this.usuario = usuario;
   }

   public List<RolUsuarioDto> getRolUsuario() {
      return rolUsuario;
   }

   public void setRolUsuario(List<RolUsuarioDto> rolUsuario) {
      this.rolUsuario = rolUsuario;
   }
}
