/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.una.unaeropuertoclient.service;

import com.google.gson.Gson;
import java.net.http.HttpResponse;
import java.util.List;
import org.una.unaeropuertoclient.model.PistaDto;
import org.una.unaeropuertoclient.utils.RequesUtils;
import org.una.unaeropuertoclient.utils.RequestHTTP;
import org.una.unaeropuertoclient.utils.Respuesta;
import static org.una.unaeropuertoclient.utils.RequesUtils.*;

/**
 *
 * @author Roberth :)
 */
public class PistaService {

    private final Gson jsonConv = new Gson();

    public Respuesta findAll() {
        try {
            RequestHTTP requestHTTP = new RequestHTTP();
            HttpResponse respuesta = requestHTTP.get("pistas/findAll");
            if (isError(respuesta.statusCode())) {
                return new Respuesta(false, "Error al consultar", String.valueOf(requestHTTP.getStatus()));
            }
            if (isEmptyResult(respuesta.statusCode())) {
                return new Respuesta(false, "No hay pistas", "");
            }
            return new Respuesta(true, "", "", "data", RequesUtils.<PistaDto>asList(respuesta, PistaDto.class));
        } catch (Exception ex) {
            return new Respuesta(false, "Error de conexión", "");
        }
    }

    public Respuesta findAll_devMode() {
        return null;
    }

    public Respuesta create() {
        return null;
    }

    public Respuesta update() {
        return null;
    }
}
