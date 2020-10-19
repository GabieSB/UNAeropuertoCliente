/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.una.unaeropuertoclient.service;

import java.net.http.HttpResponse;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import java.sql.Timestamp;
import java.util.List;
import org.una.unaeropuertoclient.model.UsuarioDto;
import org.una.unaeropuertoclient.utils.RequestHTTP;
import org.una.unaeropuertoclient.model.AuthenticationRequest;
import org.una.unaeropuertoclient.model.AuthenticationResponse;
import org.una.unaeropuertoclient.model.RolUsuarioDto;
import org.una.unaeropuertoclient.utils.AppContext;
import org.una.unaeropuertoclient.utils.Respuesta;

/**
 *
 * @author LordLalo
 */
public class UsuarioService {

    Gson g = new GsonBuilder()
            .setDateFormat("yyy-MM-dd'T'HH:mm:ss.SSSX").create();

    public Respuesta logIn(String cedula, String password) {

        AuthenticationRequest autRiq = new AuthenticationRequest(cedula, password);
        try {
            RequestHTTP requestHTTP = new RequestHTTP();
            HttpResponse respuesta = requestHTTP.post("autenticacion/login", g.toJson(autRiq));
            if (requestHTTP.getStatus() != 200) {
                if (respuesta.statusCode() == 500) {
                    return new Respuesta(false, "Parece que has introducido mal tus credenciales de acceso.", String.valueOf(requestHTTP.getStatus()));
                }
                return new Respuesta(false, "Parece que algo ha salido mal. Si el problema persiste solicita ayuda del encargado del sistema.", String.valueOf(requestHTTP.getStatus()));
            }
            System.out.println(respuesta.body().toString());
            //List<AuthenticationResponse> users = new Gson().fromJson(respuesta.body().toString(), new TypeToken<>() {}.getType());
            AuthenticationResponse authenticationResponse = g.fromJson(respuesta.body().toString(), AuthenticationResponse.class);

            for (RolUsuarioDto usuarioDTO : authenticationResponse.getRolUsuario()) {
                System.out.println(usuarioDTO.toString());
            }

            AppContext.getInstance().set("token", authenticationResponse);

            return new Respuesta(true, "", "", "data", authenticationResponse);

        } catch (Exception ex) {
            Logger.getLogger(UsuarioService.class.getName()).log(Level.SEVERE, " logIn() ->", ex);
            return new Respuesta(false, "Ha ocurrido un error al establecer comunicación con el servidor.", ex.getMessage());
        }

    }

    public void create(UsuarioDto usuario) {

        Gson gson = new GsonBuilder()
                .setDateFormat("yyy-MM-dd'T'HH:mm:ss.SSSX").create();

        AuthenticationResponse authentication = (AuthenticationResponse) AppContext.getInstance().get("token");

        //System.out.println(bitacoraDto.getFechaModificacion().getTime());
        RequestHTTP requestHTTP = new RequestHTTP();

        HttpResponse response = requestHTTP.post("usuarios/create", gson.toJson(usuario));

    }

    public Respuesta getById(String nombr, String apellido, String cedula, String fechaInicio, String fechaFinal,boolean activo, boolean boolFechas) {
        try {
//            String b;
//            if(activo){
//                b="true";
//            }else{
//            b="false";
//            }
            String url = "usuarios/busquedaCompleta/" + nombr + "/" + apellido + "/" + cedula+"/"+activo;

            if (boolFechas) {
                url = "usuarios/busquedaCompletaConFechas/" + nombr + "/" + apellido + "/" + cedula+"/"+activo + "/"+fechaInicio + "/" +fechaFinal;
            }
            RequestHTTP requestHTTP = new RequestHTTP();
            HttpResponse respuesta = requestHTTP.get(url);
            if (requestHTTP.getStatus() != 200) {
                if (respuesta.statusCode() == 500) {
                    return new Respuesta(false, "Parece que has introducido mal tus credenciales de acceso.", String.valueOf(requestHTTP.getStatus()));
                }
                return new Respuesta(false, "Parece que algo ha salido mal. Si el problema persiste solicita ayuda del encargado del sistema.", String.valueOf(requestHTTP.getStatus()));
            }
            System.out.println(respuesta.body().toString());

            //UsuarioDto usuarioDto = g.fromJson(respuesta.body().toString(), UsuarioDto.class);
            List<UsuarioDto> usuarioDto = new Gson().fromJson(respuesta.body().toString(), new TypeToken<List<UsuarioDto>>() {
            }.getType());
            return new Respuesta(true, "", "", "data", usuarioDto);

        } catch (Exception ex) {
            Logger.getLogger(UsuarioService.class.getName()).log(Level.SEVERE, " logIn() ->", ex);
            return new Respuesta(false, "Ha ocurrido un error al establecer comunicación con el servidor.", ex.getMessage());
        }
    }
}
