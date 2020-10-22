/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.una.unaeropuertoclient.service;

import java.net.http.HttpResponse;
import java.time.LocalDate;
import org.una.unaeropuertoclient.model.PistaDto;
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

    public Respuesta filter(String aerolinea, String nombreVuelo, String matriculaAvion, String llegada, String salida, LocalDate desde, LocalDate hasta) {
        try {
            RequestHTTP requestHTTP = new RequestHTTP();
            aerolinea = (aerolinea.isBlank()) ? "none" : aerolinea.trim();
            nombreVuelo = (nombreVuelo.isBlank()) ? "none" : nombreVuelo.trim();
            matriculaAvion = (matriculaAvion.isBlank()) ? "none" : matriculaAvion.trim();
            llegada = (llegada.isBlank()) ? "none" : llegada.trim();
            salida = (salida.isBlank()) ? "none" : salida.trim();
            desde = desde != null ? desde : LocalDate.now().minusYears(1);
            hasta = desde != null ? hasta : LocalDate.now().plusYears(1);
            HttpResponse respuesta = requestHTTP.get("vuelos/filter/" + aerolinea + "/" + nombreVuelo + "/" + matriculaAvion + "/" + llegada + "/" + salida + "/" + desde + "/" + hasta);
            if (isError(respuesta.statusCode())) {
                return new Respuesta(false, "Error interno al consultar vuelos, considera reportar esta falla.", "");
            }
            if (isEmptyResult(respuesta.statusCode())) {
                return new Respuesta(false, "No hay vuelos que coincidan con lo que buscas.", "");
            }
            return new Respuesta(true, "", "", "data", RequesUtils.<PistaDto>asList(respuesta, PistaDto.class));
        } catch (Exception ex) {
            return new Respuesta(false, "Ha fallado la conexión con el servidor. Verifica que el servicio de internet se encuntre activo.", "");
        }
    }

    public Respuesta update(VueloDto vuelo) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public Respuesta create(VueloDto vuelo) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
