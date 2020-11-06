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

    Gson gson = new GsonBuilder().setDateFormat("yyy-MM-dd'T'HH:mm:ss.SSSX").create();

    public Respuesta create(NotificacionDto notificacion) {
        try {
            RequestHTTP requestHTTP = new RequestHTTP();
            HttpResponse respuesta = requestHTTP.post("notificaciones/create", gson.toJson(notificacion));
            if (requestHTTP.getStatus() != 200) {
                if (respuesta.statusCode() == 500) {
                    return new Respuesta(false, "Parece que un error interno ha causado que tu petición no se registre, considera reportar este error.", String.valueOf(requestHTTP.getStatus()));
                }
                return new Respuesta(false, "Parece que algo ha salido mal. Si el problema persiste solicita ayuda del encargado del sistema.", String.valueOf(requestHTTP.getStatus()));
            }
            NotificacionDto servicioMantenimientoDto = gson.fromJson(respuesta.body().toString(), NotificacionDto.class);
            return new Respuesta(true, "", "", "data", servicioMantenimientoDto);
        } catch (Exception ex) {
            Logger.getLogger(UsuarioService.class.getName()).log(Level.SEVERE, " logIn() ->", ex);
            return new Respuesta(false, "Ha ocurrido un error al establecer comunicación con el servidor.", ex.getMessage());
        }
    }

    public Respuesta getNoficaciones(int idArea) {
        try {
            String url = "notificaciones/findByAreasId/" + idArea;
            RequestHTTP requestHTTP = new RequestHTTP();
            HttpResponse respuesta = requestHTTP.get(url);
            if (requestHTTP.getStatus() != 200) {
                if (respuesta.statusCode() == 500) {
                    return new Respuesta(false, "Parece que has introducido mal tus credenciales de acceso.", String.valueOf(requestHTTP.getStatus()));
                }
                return new Respuesta(false, "Parece que algo ha salido mal. Si el problema persiste solicita ayuda del encargado del sistema.", String.valueOf(requestHTTP.getStatus()));
            }
            List<NotificacionDto> usuarioDto = new Gson().fromJson(respuesta.body().toString(), new TypeToken<List<NotificacionDto>>() {
            }.getType());
            return new Respuesta(true, "", "", "data", usuarioDto);
        } catch (Exception ex) {
            Logger.getLogger(UsuarioService.class.getName()).log(Level.SEVERE, " logIn() ->", ex);
            return new Respuesta(false, "Ha ocurrido un error al establecer comunicación con el servidor.", ex.getMessage());
        }
    }

    public Respuesta getIDandEstado(long idArea, boolean estado) {
        try {
            String url = "notificaciones/buscarIdandEstado/" + idArea + "/" + estado;
            RequestHTTP requestHTTP = new RequestHTTP();
            HttpResponse respuesta = requestHTTP.get(url);
            if (requestHTTP.getStatus() != 200) {
                if (respuesta.statusCode() == 500) {
                    return new Respuesta(false, "Parece que has introducido mal tus credenciales de acceso.", String.valueOf(requestHTTP.getStatus()));
                }
                return new Respuesta(false, "Parece que algo ha salido mal. Si el problema persiste solicita ayuda del encargado del sistema.", String.valueOf(requestHTTP.getStatus()));
            }
            List<NotificacionDto> usuarioDto = new Gson().fromJson(respuesta.body().toString(), new TypeToken<List<NotificacionDto>>() {
            }.getType());
            return new Respuesta(true, "", "", "data", usuarioDto);
        } catch (Exception ex) {
            Logger.getLogger(UsuarioService.class.getName()).log(Level.SEVERE, " logIn() ->", ex);
            return new Respuesta(false, "Ha ocurrido un error al establecer comunicación con el servidor.", ex.getMessage());
        }
    }

    public Respuesta update(NotificacionDto notif) {
        try {
            RequestHTTP requestHTTP = new RequestHTTP();
            HttpResponse respuesta = requestHTTP.put("notificaciones/update", gson.toJson(notif));
            if (requestHTTP.getStatus() != 200) {
                if (respuesta.statusCode() == 500) {
                    return new Respuesta(false, "Un error interno ha impedido modificar esta "
                            + "notificación, sin embargo si intentabas inhabilitar algún dato "
                            + "es muy probable que este se halla inabilitado corectamente. Solicita ayuda "
                            + "técnica para revisar este error.", "");
                }
                return new Respuesta(false, "Parece que algo ha salido mal. Si el problema persiste solicita ayuda del encargado del sistema.", String.valueOf(requestHTTP.getStatus()));
            }
            NotificacionDto notificacionDto = gson.fromJson(respuesta.body().toString(), NotificacionDto.class);
            return new Respuesta(true, "", "", "data", notificacionDto);
        } catch (Exception ex) {
            Logger.getLogger(UsuarioService.class.getName()).log(Level.SEVERE, " notificacion() ->", ex);
            return new Respuesta(false, "Ha ocurrido un error al establecer comunicación con el servidor.", ex.getMessage());
        }
    }
}
