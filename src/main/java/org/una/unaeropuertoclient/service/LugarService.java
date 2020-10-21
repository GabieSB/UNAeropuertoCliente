/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.una.unaeropuertoclient.service;

import com.google.gson.Gson;
import java.net.http.HttpResponse;
import org.una.unaeropuertoclient.model.LugarDto;
import org.una.unaeropuertoclient.model.PistaDto;
import org.una.unaeropuertoclient.utils.RequesUtils;
import static org.una.unaeropuertoclient.utils.RequesUtils.isEmptyResult;
import static org.una.unaeropuertoclient.utils.RequesUtils.isError;
import org.una.unaeropuertoclient.utils.RequestHTTP;
import org.una.unaeropuertoclient.utils.Respuesta;

/**
 *
 * @author Roberth :)
 */
public class LugarService {

    private final Gson jsonConv = new Gson();

    public Respuesta findByNombre(String nombre) {
        try {
            RequestHTTP requestHTTP = new RequestHTTP();
            nombre = nombre.isBlank() ? "none" : nombre;
            HttpResponse respuesta = requestHTTP.get("lugares/findByNombre/" + nombre);
            if (isError(respuesta.statusCode())) {
                return new Respuesta(false, "Error interno al consultar luagares, considera reportar esta falla.", "");
            }
            if (isEmptyResult(respuesta.statusCode())) {
                return new Respuesta(false, "No hay lugares que coisidan con lo que buscas.", "");
            }
            return new Respuesta(true, "", "", "data", RequesUtils.<LugarDto>asList(respuesta, LugarDto.class));
        } catch (Exception ex) {
            return new Respuesta(false, "Ha fallado la conexión con el servidor. Verifica que el servicio de internet se encuntre activo.", "");
        }
    }

    public Respuesta update(LugarDto lugar) {
        HttpResponse resp = new RequestHTTP().put("lugares/update", jsonConv.toJson(lugar));
        if (isError(resp.statusCode())) {
            return new Respuesta(false, "Error al crear modificar datos, , considera reportar este problema", "");
        }
        if (isEmptyResult(resp.statusCode())) {
            return new Respuesta(false, "No ha sido posible hallarel lugar que se desea modificar", "");
        }
        return new Respuesta(true, "", "", "data", RequesUtils.<LugarDto>toObject(resp, LugarDto.class));
    }

    public Respuesta create(LugarDto lugar) {
        lugar.setActivo(true);
        HttpResponse resp = new RequestHTTP().post("lugares/create", jsonConv.toJson(lugar));
        if (isError(resp.statusCode())) {
            return new Respuesta(false, "Error al registrar nuevo lugar en el sistema, considera reportar este problema", "");
        }
        return new Respuesta(true, "", "", "data", RequesUtils.<LugarDto>toObject(resp, LugarDto.class));
    }
}
