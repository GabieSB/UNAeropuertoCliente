package org.una.unaeropuertoclient.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import org.una.unaeropuertoclient.model.TipoReparacionDto;
import org.una.unaeropuertoclient.model.TipoReparacionDto;
import org.una.unaeropuertoclient.model.TipoReparacionDto;
import org.una.unaeropuertoclient.utils.RequesUtils;
import org.una.unaeropuertoclient.utils.RequestHTTP;
import org.una.unaeropuertoclient.utils.Respuesta;

import java.net.http.HttpResponse;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import static org.una.unaeropuertoclient.utils.RequesUtils.isEmptyResult;
import static org.una.unaeropuertoclient.utils.RequesUtils.isError;

public class TipoReparacionService {

    Gson gson = new GsonBuilder()
            .setDateFormat("yyy-MM-dd'T'HH:mm:ss.SSSX").create();

    public Respuesta create(TipoReparacionDto tipoReparacion) {
        try {
            RequestHTTP requestHTTP = new RequestHTTP();
            HttpResponse respuesta = requestHTTP.post("tiposReparaciones/create", gson.toJson(tipoReparacion));
            System.out.println(respuesta.body().toString());
            if (requestHTTP.getStatus() != 200) {
                if (respuesta.statusCode() == 500) {
                    return new Respuesta(false, "Parece que has introducido mal tus credenciales de acceso.", String.valueOf(requestHTTP.getStatus()));
                }
                return new Respuesta(false, "Parece que algo ha salido mal. Si el problema persiste solicita ayuda del encargado del sistema.", String.valueOf(requestHTTP.getStatus()));
            }

            TipoReparacionDto tipoReparacionDto = gson.fromJson(respuesta.body().toString(), TipoReparacionDto.class);

            return new Respuesta(true, "", "", "data", tipoReparacionDto);

        } catch (Exception ex) {
            Logger.getLogger(UsuarioService.class.getName()).log(Level.SEVERE, " logIn() ->", ex);
            return new Respuesta(false, "Ha ocurrido un error al establecer comunicación con el servidor.", ex.getMessage());
        }
    }

    public Respuesta getActivos() {

        try {
            RequestHTTP requestHTTP = new RequestHTTP();
            HttpResponse respuesta = requestHTTP.get("tiposReparaciones/findByEstado/true");
            if (requestHTTP.getStatus() != 200) {
                if (respuesta.statusCode() == 500) {
                    return new Respuesta(false, "Parece que has introducido mal tus credenciales de acceso.", String.valueOf(requestHTTP.getStatus()));
                }
                return new Respuesta(false, "Parece que algo ha salido mal. Si el problema persiste solicita ayuda del encargado del sistema.", String.valueOf(requestHTTP.getStatus()));
            }
            List<TipoReparacionDto> tipoReparacionDtos = new Gson().fromJson(respuesta.body().toString(), new TypeToken<List<TipoReparacionDto>>() {
            }.getType());

            for (TipoReparacionDto usuarioDTO : tipoReparacionDtos) {
                System.out.println(usuarioDTO.toString());
            }

            return new Respuesta(true, "", "", "data", tipoReparacionDtos);

        } catch (Exception ex) {
            Logger.getLogger(UsuarioService.class.getName()).log(Level.SEVERE, " logIn() ->", ex);
            return new Respuesta(false, "Ha ocurrido un error al establecer comunicación con el servidor.", ex.getMessage());
        }

    }

    public Respuesta update(TipoReparacionDto tipoReparacion) {
        HttpResponse resp = new RequestHTTP().put("tiposReparaciones/update", gson.toJson(tipoReparacion));
        System.out.println((resp.body().toString()));
        if (isError(resp.statusCode())) {
            return new Respuesta(false, "Error al modificar Tipo de Reparacion , considera reportar esta falla.", "");
        }
        if (isEmptyResult(resp.statusCode())) {
            return new Respuesta(false, "No ha sido posible hallar la tipoReparacion que se desea modificar", "");
        }
        return new Respuesta(true, "", "", "data", RequesUtils.asObject(resp, TipoReparacionDto.class));
    }

    public Respuesta findByNombre(String nombre) {
        try {
            RequestHTTP requestHTTP = new RequestHTTP();
            HttpResponse respuesta = requestHTTP.get("tiposReparaciones/findByNomb/" + nombre);
            if (isError(respuesta.statusCode())) {
                return new Respuesta(false, "Error al consultar", String.valueOf(requestHTTP.getStatus()));
            }
            if (isEmptyResult(respuesta.statusCode())) {
                return new Respuesta(false, "No hay Tipos de Reparaciones registrados en el servidor", "");
            }
            return new Respuesta(true, "", "", "data", RequesUtils.<TipoReparacionDto>asList(respuesta, TipoReparacionDto.class));
        } catch (Exception ex) {
            return new Respuesta(false, "Error de conexión", "");
        }

    }

      public Respuesta getAll() {

        try {
            RequestHTTP requestHTTP = new RequestHTTP();
            HttpResponse respuesta = requestHTTP.get("tiposReparaciones/reparacion");
            if (requestHTTP.getStatus() != 200) {
                if (respuesta.statusCode() == 500) {
                    return new Respuesta(false, "Parece que has introducido mal tus credenciales de acceso.", String.valueOf(requestHTTP.getStatus()));
                }
                return new Respuesta(false, "Parece que algo ha salido mal. Si el problema persiste solicita ayuda del encargado del sistema.", String.valueOf(requestHTTP.getStatus()));
            }
            List<TipoReparacionDto> tipoReparacionDtos = new Gson().fromJson(respuesta.body().toString(), new TypeToken<List<TipoReparacionDto>>() {
            }.getType());

            for (TipoReparacionDto usuarioDTO : tipoReparacionDtos) {
                System.out.println(usuarioDTO.toString());
            }

            return new Respuesta(true, "", "", "data", tipoReparacionDtos);

        } catch (Exception ex) {
            Logger.getLogger(UsuarioService.class.getName()).log(Level.SEVERE, " tipoReparaciones() ->", ex);
            return new Respuesta(false, "Ha ocurrido un error al establecer comunicación con el servidor.", ex.getMessage());
        }

    }

}
