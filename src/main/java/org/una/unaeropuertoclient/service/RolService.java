/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.una.unaeropuertoclient.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.una.unaeropuertoclient.model.PistaDto;
import org.una.unaeropuertoclient.model.RolDto;
import org.una.unaeropuertoclient.utils.RequesUtils;
import org.una.unaeropuertoclient.utils.RequestHTTP;
import org.una.unaeropuertoclient.utils.Respuesta;

import static org.una.unaeropuertoclient.utils.RequesUtils.isEmptyResult;
import static org.una.unaeropuertoclient.utils.RequesUtils.isError;


/**
 *
 * @author LordLalo
 */
public class RolService {

     Gson g = new GsonBuilder()
            .setDateFormat("yyy-MM-dd'T'HH:mm:ss.SSSX").create();

    public Respuesta getAll() {
        try {
            RequestHTTP requestHTTP = new RequestHTTP();
            HttpResponse respuesta = requestHTTP.get("roles");
            if (isError(respuesta.statusCode())) {
                return new Respuesta(false, "Error al consultar", String.valueOf(requestHTTP.getStatus()));
            }
            if (isEmptyResult(respuesta.statusCode())) {
                return new Respuesta(false, "No hay roles", "");
            }
            return new Respuesta(true, "", "", "data", RequesUtils.asList(respuesta, RolDto.class));
        } catch (Exception ex) {
            return new Respuesta(false, "Error de conexión", "");
        }
    }
}
