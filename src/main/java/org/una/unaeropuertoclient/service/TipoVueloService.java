/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.una.unaeropuertoclient.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.net.http.HttpResponse;
import org.una.unaeropuertoclient.model.TipoVueloDto;
import org.una.unaeropuertoclient.utils.RequesUtils;
import static org.una.unaeropuertoclient.utils.RequesUtils.*;
import org.una.unaeropuertoclient.utils.RequestHTTP;
import org.una.unaeropuertoclient.utils.Respuesta;

/**
 *
 * @author roberth :)
 */
public class TipoVueloService {

    private final Gson jsonConv = new GsonBuilder().setDateFormat("yyy-MM-dd'T'HH:mm:ss.SSSX").create();

    public Respuesta findByEstado(boolean estado) {
        try {
            RequestHTTP requestHTTP = new RequestHTTP();
            HttpResponse respuesta = requestHTTP.get("tipos_vuelos/findByEstado/" + estado);
            if (isError(respuesta.statusCode())) {
                return new Respuesta(false, "Error al consultar", "");
            }
            if (isEmptyResult(respuesta.statusCode())) {
                return new Respuesta(false, "No hay tipos de vuelos", "");
            }
            return new Respuesta(true, "", "", "data", RequesUtils.<TipoVueloDto>asList(respuesta, TipoVueloDto.class));
        } catch (Exception ex) {
            return new Respuesta(false, "Error de conexión", "");
        }
    }
}
