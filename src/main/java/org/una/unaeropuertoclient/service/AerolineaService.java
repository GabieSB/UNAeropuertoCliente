/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.una.unaeropuertoclient.service;

import com.google.gson.Gson;
import java.net.http.HttpResponse;
import org.una.unaeropuertoclient.model.AerolineaDto;
import org.una.unaeropuertoclient.utils.RequesUtils;
import static org.una.unaeropuertoclient.utils.RequesUtils.isEmptyResult;
import static org.una.unaeropuertoclient.utils.RequesUtils.isError;
import org.una.unaeropuertoclient.utils.RequestHTTP;
import org.una.unaeropuertoclient.utils.Respuesta;

/**
 *
 * @author Roberth :)
 */
public class AerolineaService {

    private final Gson jsonConv = new Gson();

    public Respuesta findByNombre(String nombre) {
        try {
            RequestHTTP requestHTTP = new RequestHTTP();
            nombre = nombre.isBlank() ? "none" : nombre;
            nombre = nombre.replace(" ", "-");
            HttpResponse respuesta = requestHTTP.get("aerolineas/findByNomb/" + nombre);
            if (isError(respuesta.statusCode())) {
                return new Respuesta(false, "Error interno al consultar aerolineas, considera reportar esta falla.", "");
            }
            if (isEmptyResult(respuesta.statusCode())) {
                return new Respuesta(false, "No hay aerolineas que coincidan con lo que buscas.", "");
            }
            return new Respuesta(true, "", "", "data", RequesUtils.<AerolineaDto>asList(respuesta, AerolineaDto.class));
        } catch (Exception ex) {
            return new Respuesta(false, "Ha fallado la conexión con el servidor. Verifica que el servicio de internet se encuntre activo.", "");
        }
    }

    public Respuesta findByEstado(boolean estado) {
        try {
            RequestHTTP requestHTTP = new RequestHTTP();
            HttpResponse respuesta = requestHTTP.get("aerolineas/findByEstado/" + estado);
            if (isError(respuesta.statusCode())) {
                return new Respuesta(false, "Error al consultar", "");
            }
            if (isEmptyResult(respuesta.statusCode())) {
                return new Respuesta(false, "No hay aerolineas", "");
            }
            return new Respuesta(true, "", "", "data", RequesUtils.<AerolineaDto>asList(respuesta, AerolineaDto.class));
        } catch (Exception ex) {
            return new Respuesta(false, "Error de conexión", "");
        }
    }

    public Respuesta update(AerolineaDto aerolinea) {
        HttpResponse resp = new RequestHTTP().put("aerolineas/update", jsonConv.toJson(aerolinea));
        if (isError(resp.statusCode())) {
            return new Respuesta(false, "Error al crear modificar datos, , considera reportar este problema", "");
        }
        if (isEmptyResult(resp.statusCode())) {
            return new Respuesta(false, "No ha sido posible hallar la aerolinea que se desea modificar", "");
        }
        return new Respuesta(true, "", "", "data", RequesUtils.<AerolineaDto>asObject(resp, AerolineaDto.class));
    }

    public Respuesta create(AerolineaDto aerolinea) {
        aerolinea.setActiov(true);
        HttpResponse resp = new RequestHTTP().post("aerolineas/create", jsonConv.toJson(aerolinea));
        if (isError(resp.statusCode())) {
            return new Respuesta(false, "Error al registrar nueva aerolinea en el sistema, considera reportar este problema", "");
        }
        return new Respuesta(true, "", "", "data", RequesUtils.<AerolineaDto>asObject(resp, AerolineaDto.class));
    }

}
