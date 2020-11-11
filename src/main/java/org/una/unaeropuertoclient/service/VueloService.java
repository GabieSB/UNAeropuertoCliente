/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.una.unaeropuertoclient.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.net.http.HttpResponse;
import java.time.LocalDate;
import java.util.List;
import org.una.unaeropuertoclient.model.AerolineaDto;
import org.una.unaeropuertoclient.model.VueloDto;
import org.una.unaeropuertoclient.utils.RequesUtils;
import static org.una.unaeropuertoclient.utils.RequesUtils.isEmptyResult;
import static org.una.unaeropuertoclient.utils.RequesUtils.*;
import org.una.unaeropuertoclient.utils.RequestHTTP;
import org.una.unaeropuertoclient.utils.Respuesta;

/**
 *
 * @author Roberth :)
 */
public class VueloService {

    private final Gson jsonConv = new GsonBuilder().setDateFormat("yyy-MM-dd'T'HH:mm:ss.SSSX").create();

    public Respuesta filter(String aerolinea, String nombreVuelo, String matriculaAvion, String llegada, String salida, LocalDate desde, LocalDate hasta) {
        try {
            RequestHTTP requestHTTP = new RequestHTTP();
            aerolinea = (aerolinea.isBlank()) ? "none" : aerolinea.trim().replace(" ", "-");
            nombreVuelo = (nombreVuelo.isBlank()) ? "none" : nombreVuelo.trim().replace(" ", "-");
            matriculaAvion = (matriculaAvion.isBlank()) ? "none" : matriculaAvion.trim().replace(" ", "-");
            llegada = (llegada.isBlank()) ? "none" : llegada.trim().replace(" ", "-");
            salida = (salida.isBlank()) ? "none" : salida.trim().replace(" ", "-");
            desde = desde != null ? desde : LocalDate.of(2500, 1, 1);
            hasta = hasta != null ? hasta : LocalDate.of(2500, 1, 1);
            HttpResponse respuesta = requestHTTP.get("vuelos/filter/" + aerolinea + "/" + nombreVuelo + "/" + matriculaAvion + "/" + llegada + "/" + salida + "/" + desde + "/" + hasta);
            if (isError(respuesta.statusCode())) {
                return new Respuesta(false, "Error interno al consultar vuelos, considera reportar esta falla.", "");
            }
            if (isEmptyResult(respuesta.statusCode())) {
                return new Respuesta(false, "No hay vuelos que coincidan con lo que buscas.", "");
            }
            return new Respuesta(true, "", "", "data", RequesUtils.<VueloDto>asList(respuesta, VueloDto.class));
        } catch (Exception ex) {
            return new Respuesta(false, "Ha fallado la conexión con el servidor. Verifica que el servicio de internet se encuntre activo.", "");
        }
    }

    public Respuesta findVuelosDelDia() {
        try {
            RequestHTTP requestHTTP = new RequestHTTP();
            LocalDate desde = LocalDate.now().minusDays(1);
            LocalDate hasta = LocalDate.now().plusDays(1);
            HttpResponse respuesta = requestHTTP.get("vuelos/filter/none/none/none/none/none/" + desde + "/" + hasta);
            if (isError(respuesta.statusCode())) {
                return new Respuesta(false, "Error interno al consultar vuelos, considera reportar esta falla.", "");
            }
            if (isEmptyResult(respuesta.statusCode())) {
                return new Respuesta(false, "No hay vuelos que coincidan con lo que buscas.", "");
            }
            return new Respuesta(true, "", "", "data", RequesUtils.<VueloDto>asList(respuesta, VueloDto.class));
        } catch (Exception ex) {
            return new Respuesta(false, "Ha fallado la conexión con el servidor. Verifica que el servicio de internet se encuntre activo.", "");
        }
    }

    public Respuesta update(VueloDto vuelo) {
        try {
            HttpResponse resp = new RequestHTTP().put("vuelos/update", jsonConv.toJson(vuelo));
            if (isError(resp.statusCode())) {
                return new Respuesta(false, "Error al crear modificar datos, considera reportar este problema", "");
            }
            if (isEmptyResult(resp.statusCode())) {
                return new Respuesta(false, "No ha sido posible hallar el vuelo que se desea modificar", "");
            }
            return new Respuesta(true, "", "", "data", RequesUtils.<VueloDto>asObject(resp, VueloDto.class));
        } catch (Exception ex) {
            return new Respuesta(false, "Ha fallado la conexión con el servidor. Verifica que el servicio de internet se encuntre activo.", "");
        }
    }

    public Respuesta create(VueloDto vuelo) {
        try {
            vuelo.setEstado((byte) 0);
            HttpResponse resp = new RequestHTTP().post("vuelos/create", jsonConv.toJson(vuelo));
            if (isError(resp.statusCode())) {
                return new Respuesta(false, "Error al registrar nuevo vuelo en el sistema, considera reportar este problema", "");
            }
            return new Respuesta(true, "", "", "data", RequesUtils.<VueloDto>asObject(resp, VueloDto.class));
        } catch (Exception ex) {
            return new Respuesta(false, "Ha fallado la conexión con el servidor. Verifica que el servicio de internet se encuntre activo.", "");
        }
    }

    public Respuesta countVuelosByAerolinea(AerolineaDto aerolinea) {
        try {
            HttpResponse resp = new RequestHTTP().get("aerolineas/countVuelos/" + aerolinea.getId());
            if (isError(resp.statusCode())) {
                return new Respuesta(false, "Error al consultar nombre vuelo, considera reportar este problema", "");
            }
            return new Respuesta(true, "", "", "data", RequesUtils.<Long>asObject(resp, Long.class));
        } catch (Exception ex) {
            return new Respuesta(false, "Error en conexión", "");
        }
    }

    public Respuesta findByIdUsingIdParam(List<Long> idList) {
        try {
            RequestHTTP requestHTTP = new RequestHTTP();
            HttpResponse respuesta = requestHTTP.put("vuelos/findByIdUsingListParam/", jsonConv.toJson(idList));
            if (requestHTTP.getStatus() != 200) {
                if (respuesta.statusCode() == 204) {
                    return new Respuesta(false, "Parece que no hay resultados en la búsqueda", String.valueOf(requestHTTP.getStatus()));
                }
                return new Respuesta(false, "Parece que algo ha salido mal. Si el problema persiste solicita ayuda del encargado del sistema.", String.valueOf(requestHTTP.getStatus()));
            }
            return new Respuesta(true, "", "", "data", RequesUtils.<VueloDto>asList(respuesta, VueloDto.class));
        } catch (Exception ex) {
            return new Respuesta(false, "Ha ocurrido un error al establecer comunicación con el servidor.", ex.getMessage());
        }
    }

}
