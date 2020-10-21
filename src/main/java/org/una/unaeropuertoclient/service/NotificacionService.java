package org.una.unaeropuertoclient.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import org.una.unaeropuertoclient.model.NotificacionDto;
import org.una.unaeropuertoclient.utils.RequestHTTP;
import org.una.unaeropuertoclient.utils.Respuesta;

import java.net.http.HttpResponse;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class NotificacionService {

    Gson gson = new GsonBuilder()
            .setDateFormat("yyy-MM-dd'T'HH:mm:ss.SSSX").create();

    public Respuesta create(NotificacionDto notificacion) {
        try {
            RequestHTTP requestHTTP = new RequestHTTP();
            HttpResponse respuesta = requestHTTP.post("notificaciones/create", gson.toJson(notificacion));
            System.out.println(respuesta.body().toString());
            if (requestHTTP.getStatus()!=200) {
                if (respuesta.statusCode() == 500) {
                    return new Respuesta(false, "Parece que has introducido mal tus credenciales de acceso.", String.valueOf(requestHTTP.getStatus()));
                }
                return new Respuesta(false, "Parece que algo ha salido mal. Si el problema persiste solicita ayuda del encargado del sistema.", String.valueOf(requestHTTP.getStatus()));
            }

            //List<AuthenticationResponse> users = new Gson().fromJson(respuesta.body().toString(), new TypeToken<>() {}.getType());
            NotificacionDto servicioMantenimientoDto = gson.fromJson(respuesta.body().toString(), NotificacionDto.class);

            return new Respuesta(true, "", "", "data", servicioMantenimientoDto);

        } catch (Exception ex) {
            Logger.getLogger(UsuarioService.class.getName()).log(Level.SEVERE, " logIn() ->", ex);
            return new Respuesta(false, "Ha ocurrido un error al establecer comunicación con el servidor.", ex.getMessage());
        }
    }
        public Respuesta getNoficaciones(int idArea) {
        try {         
            String url = "notificaciones/findByAreasId/"+idArea;
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
            List<NotificacionDto> usuarioDto = new Gson().fromJson(respuesta.body().toString(), new TypeToken<List<NotificacionDto>>() {
            }.getType());
            return new Respuesta(true, "", "", "data", usuarioDto);

        } catch (Exception ex) {
            Logger.getLogger(UsuarioService.class.getName()).log(Level.SEVERE, " logIn() ->", ex);
            return new Respuesta(false, "Ha ocurrido un error al establecer comunicación con el servidor.", ex.getMessage());
        }
    }
}
