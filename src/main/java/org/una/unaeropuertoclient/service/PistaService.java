/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.una.unaeropuertoclient.service;

import com.google.gson.Gson;
import java.net.http.HttpResponse;
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

    public Respuesta create(PistaDto pista) {
        pista.setActivo(true);
        HttpResponse resp = new RequestHTTP().post("pistas/create", jsonConv.toJson(pista));
        if (isError(resp.statusCode())) {
            return new Respuesta(false, "Error al crear nueva pista de aterrizaje", "");
        }
        return new Respuesta(true, "", "", "data", RequesUtils.<PistaDto>asObject(resp, PistaDto.class));
    }

    public Respuesta update(PistaDto pista) {
        HttpResponse resp = new RequestHTTP().put("pistas/update", jsonConv.toJson(pista));
        if (isError(resp.statusCode())) {
            return new Respuesta(false, "Error al modificar pista de aterrizaje, considera reportar esta falla.", "");
        }
        if (isEmptyResult(resp.statusCode())) {
            return new Respuesta(false, "No ha sido posible hallar la pista que se desea modificar", "");
        }
        return new Respuesta(true, "", "", "data", RequesUtils.<PistaDto>asObject(resp, PistaDto.class));
    }

    public Respuesta filter(String numeroPista, String longitudPista) {
        try {
            RequestHTTP requestHTTP = new RequestHTTP();
            numeroPista = (numeroPista.isBlank()) ? "none" : numeroPista.trim();
            longitudPista = (longitudPista.isBlank()) ? "none" : longitudPista.trim();
            numeroPista = numeroPista.replace(" ", "_");
            HttpResponse respuesta = requestHTTP.get("pistas/filter/" + numeroPista + "/" + longitudPista);
            if (isError(respuesta.statusCode())) {
                return new Respuesta(false, "Error interno al consultar pistas, considera reportar esta falla.", "");
            }
            if (isEmptyResult(respuesta.statusCode())) {
                return new Respuesta(false, "No hay pistas que coincidan con lo que buscas.", "");
            }
            return new Respuesta(true, "", "", "data", RequesUtils.<PistaDto>asList(respuesta, PistaDto.class));
        } catch (Exception ex) {
            return new Respuesta(false, "Ha fallado la conexión con el servidor. Verifica que el servicio de internet se encuntre activo.", "");
        }


    }
}
