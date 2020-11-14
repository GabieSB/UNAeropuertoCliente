/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.una.unaeropuertoclient.service;

import com.google.gson.Gson;
import java.net.http.HttpResponse;
import org.una.unaeropuertoclient.model.LugarDto;
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
            nombre = nombre.replace(" ", "-");
            HttpResponse respuesta = requestHTTP.get("lugares/findByNombre/" + nombre);
            if (isError(respuesta.statusCode())) {
                return new Respuesta(false, "Error interno al consultar luagares, considera reportar esta falla.", "");
            }
            if (isEmptyResult(respuesta.statusCode())) {
                return new Respuesta(false, "No hay lugares que coincidan con lo que buscas.", "");
            }
            return new Respuesta(true, "", "", "data", RequesUtils.<LugarDto>asList(respuesta, LugarDto.class));
        } catch (Exception ex) {
            return new Respuesta(false, "Ha fallado la conexión con el servidor. Verifica que el servicio de internet se encuntre activo.", "");
        }
    }

    public Respuesta findByEstado(boolean estado) {
        try {
            RequestHTTP requestHTTP = new RequestHTTP();
            HttpResponse respuesta = requestHTTP.get("lugares/findByEstado/" + estado);
            if (isError(respuesta.statusCode())) {
                return new Respuesta(false, "Error al consultar", "");
            }
            if (isEmptyResult(respuesta.statusCode())) {
                return new Respuesta(false, "No hay lugares", "");
            }
            return new Respuesta(true, "", "", "data", RequesUtils.<LugarDto>asList(respuesta, LugarDto.class));
        } catch (Exception ex) {
            return new Respuesta(false, "Error de conexión", "");
        }
    }

    public Respuesta update(LugarDto lugar) {
        if (lugar.getId() != 1) {
            HttpResponse resp = new RequestHTTP().put("lugares/update", jsonConv.toJson(lugar));
            if (isError(resp.statusCode())) {
                return new Respuesta(false, "Error al crear modificar datos, considera reportar este problema", "");
            }
            if (isEmptyResult(resp.statusCode())) {
                return new Respuesta(false, "No ha sido posible hallar el lugar que se desea modificar", "");
            }
            registrarNuevaBitacora("Modificó un lugar, Id de lugar: " + lugar.getId());
            return new Respuesta(true, "", "", "data", RequesUtils.<LugarDto>asObject(resp, LugarDto.class));
        }
        return new Respuesta(false, "No está permitido modificar este lugar.", "");
    }

    public Respuesta create(LugarDto lugar) {
        lugar.setActivo(true);
        HttpResponse resp = new RequestHTTP().post("lugares/create", jsonConv.toJson(lugar));
        if (isError(resp.statusCode())) {
            return new Respuesta(false, "Error al registrar nuevo lugar en el sistema, considera reportar este problema", "");
        }
        LugarDto lug = RequesUtils.<LugarDto>asObject(resp, LugarDto.class);
        registrarNuevaBitacora("Registró un nuevo lugar con id " + lug.getId());
        return new Respuesta(true, "", "", "data", lug);
    }

    private void registrarNuevaBitacora(String descripcion) {
        Thread th = new Thread(() -> {
            new BitacoraService().create(descripcion);
        });
        th.start();
    }
}
