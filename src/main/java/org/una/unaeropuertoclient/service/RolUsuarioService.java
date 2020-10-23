/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.una.unaeropuertoclient.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.net.http.HttpResponse;
import org.una.unaeropuertoclient.model.RolUsuarioDto;
import org.una.unaeropuertoclient.utils.RequesUtils;
import static org.una.unaeropuertoclient.utils.RequesUtils.isError;
import org.una.unaeropuertoclient.utils.RequestHTTP;
import org.una.unaeropuertoclient.utils.Respuesta;

/**
 *
 * @author LordLalo
 */
public class RolUsuarioService {
    Gson gson = new GsonBuilder()
            .setDateFormat("yyy-MM-dd'T'HH:mm:ss.SSSX").create();

    public Respuesta create( RolUsuarioDto rolUsuarioDto) {
        
        HttpResponse resp = new RequestHTTP().post("rolesUsuarios/create", gson.toJson(rolUsuarioDto));
        if (isError(resp.statusCode())) {
            return new Respuesta(false, "Error al crear un usuario con rol", "");
        }
        return new Respuesta(true, "", "", "data", RequesUtils.<RolUsuarioDto>asObject(resp, RolUsuarioDto.class));
    }
}
