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
}
