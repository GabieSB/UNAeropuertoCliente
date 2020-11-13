/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.una.unaeropuertoclient.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.net.http.HttpResponse;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.una.unaeropuertoclient.model.ParamSistemaDto;
import org.una.unaeropuertoclient.utils.RequestHTTP;
import org.una.unaeropuertoclient.utils.Respuesta;

/**
 *
 * @author LordLalo
 */
public class ParamSistemaServicio {

    public Respuesta getById() {
        Gson gson = new GsonBuilder()
                .setDateFormat("HH:mm:ss").create();
        try {
            RequestHTTP requestHTTP = new RequestHTTP();
            HttpResponse respuesta = requestHTTP.get("param_sistema/1");
            if (requestHTTP.getStatus() != 200) {
                if (respuesta.statusCode() == 500) {
                    return new Respuesta(false, "Problemas al solicitar datos de parametros", String.valueOf(requestHTTP.getStatus()));
                }
                return new Respuesta(false, "Parece que algo ha salido mal. Si el problema persiste solicita ayuda del encargado del sistema.", String.valueOf(requestHTTP.getStatus()));
            }
            ParamSistemaDto paramSistemaDto = gson.fromJson(respuesta.body().toString(), ParamSistemaDto.class);

            return new Respuesta(true, "", "", "data", paramSistemaDto);

        } catch (Exception ex) {
            Logger.getLogger(UsuarioService.class.getName()).log(Level.SEVERE, " obtnerParametros() ->", ex);
            return new Respuesta(false, "Ha ocurrido un error al establecer comunicación con el servidor.", ex.getMessage());
        }
    }

    public Respuesta update(ParamSistemaDto paramSistemaDto) {
        Gson gsonn = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSX").create();
        try {
            RequestHTTP requestHTTP = new RequestHTTP();
            HttpResponse respuesta = requestHTTP.post("param_sistema/create", gsonn.toJson(paramSistemaDto));
            if (requestHTTP.getStatus() != 200) {
                if (respuesta.statusCode() == 500) {
                    return new Respuesta(false, "Un error interno ha impedido modificar los parámetros del sistema.", String.valueOf(requestHTTP.getStatus()));
                }
                return new Respuesta(false, "Parece que algo ha salido mal. Si el problema persiste solicita ayuda del encargado del sistema.", String.valueOf(requestHTTP.getStatus()));
            }
            registrarNuevaBitacora("Los parámetros de sistema ha sido cambiados");
            ParamSistemaDto paramSistemaDt = gsonn.fromJson(respuesta.body().toString(), ParamSistemaDto.class);
            return new Respuesta(true, "", "", "data", paramSistemaDt);

        } catch (Exception ex) {
            Logger.getLogger(UsuarioService.class.getName()).log(Level.SEVERE, " logIn() ->", ex);
            System.out.println("ha ocurrido un error");
            return new Respuesta(false, "Ha ocurrido un error al establecer comunicación con el servidor.", ex.getMessage());
        }
    }

    private void registrarNuevaBitacora(String descripcion) {
        Thread th = new Thread(() -> {
            new BitacoraService().create(descripcion);
        });
        th.start();
    }
}
