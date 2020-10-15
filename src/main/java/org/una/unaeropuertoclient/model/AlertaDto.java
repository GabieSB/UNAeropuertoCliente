/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.una.unaeropuertoclient.model;



import java.util.List;

/**
 *
 * @author Roberth :)
 */


public class AlertaDto {

    private Long id;
    private Byte tipo;
    private String titulo;
    private String cuerpo;
    private String emisor;
    private Boolean activa;
    private List<VueloDto> vueloList;
}
