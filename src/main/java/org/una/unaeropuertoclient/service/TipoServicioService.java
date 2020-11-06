package org.una.unaeropuertoclient.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import org.una.unaeropuertoclient.model.*;
import org.una.unaeropuertoclient.utils.AppContext;
import org.una.unaeropuertoclient.utils.RequestHTTP;
import org.una.unaeropuertoclient.utils.Respuesta;

import java.net.http.HttpResponse;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TipoServicioService {
    Gson g = new GsonBuilder()
            .setDateFormat("yyy-MM-dd'T'HH:mm:ss.SSSX").create();


    public Respuesta getAll() {

        try {
            RequestHTTP requestHTTP = new RequestHTTP();
            HttpResponse respuesta = requestHTTP.get("tipos_servicios");
            if (requestHTTP.getStatus()!=200) {
                if (respuesta.statusCode() == 500) {
                    return new Respuesta(false, "Parece que has introducido mal tus credenciales de acceso.", String.valueOf(requestHTTP.getStatus()));
                }
                return new Respuesta(false, "Parece que algo ha salido mal. Si el problema persiste solicita ayuda del encargado del sistema." ,String.valueOf(requestHTTP.getStatus()));
            }
            System.out.println(respuesta.body().toString());
            List<TipoServicioDto> tipoServicioDtos = new Gson().fromJson(respuesta.body().toString(), new TypeToken<List<TipoServicioDto>>() {}.getType());

            for(TipoServicioDto usuarioDTO: tipoServicioDtos){
                System.out.println(usuarioDTO.toString());
            }

            return new Respuesta(true, "", "", "data", tipoServicioDtos);

        } catch (Exception ex) {
            Logger.getLogger(UsuarioService.class.getName()).log(Level.SEVERE, " logIn() ->", ex);
            return new Respuesta(false, "Ha ocurrido un error al establecer comunicación con el servidor.", ex.getMessage());
        }

    }

    public Respuesta create(TipoServicioDto tipoServicio) {
        try {
            RequestHTTP requestHTTP = new RequestHTTP();
            HttpResponse respuesta = requestHTTP.post("tipos_servicios/create", g.toJson(tipoServicio));
            System.out.println(respuesta.body().toString());
            if (requestHTTP.getStatus()!=200) {
                if (respuesta.statusCode() == 500) {
                    return new Respuesta(false, "Parece que has introducido mal los datos.", String.valueOf(requestHTTP.getStatus()));
                }
                return new Respuesta(false, "Parece que algo ha salido mal. Si el problema persiste solicita ayuda del encargado del sistema.", String.valueOf(requestHTTP.getStatus()));
            }

            TipoServicioDto tipoServicioDto = g.fromJson(respuesta.body().toString(), TipoServicioDto.class);

            return new Respuesta(true, "", "", "data", tipoServicioDto);

        } catch (Exception ex) {
            Logger.getLogger(UsuarioService.class.getName()).log(Level.SEVERE, " logIn() ->", ex);
            return new Respuesta(false, "Ha ocurrido un error al establecer comunicación con el servidor.", ex.getMessage());
        }
    }

    public Respuesta update(TipoServicioDto tipoServicio) {
        try {
            RequestHTTP requestHTTP = new RequestHTTP();
            HttpResponse respuesta = requestHTTP.put("tipos_servicios/update", g.toJson(tipoServicio));
            System.out.println(respuesta.body().toString());
            if (requestHTTP.getStatus()!=200) {
                if (respuesta.statusCode() == 500) {
                    return new Respuesta(false, "Parece que has introducido mal los datos.", String.valueOf(requestHTTP.getStatus()));
                }
                return new Respuesta(false, "Parece que algo ha salido mal. Si el problema persiste solicita ayuda del encargado del sistema.", String.valueOf(requestHTTP.getStatus()));
            }

            TipoServicioDto tipoServicioDto = g.fromJson(respuesta.body().toString(), TipoServicioDto.class);

            return new Respuesta(true, "", "", "data", tipoServicioDto);

        } catch (Exception ex) {
            Logger.getLogger(UsuarioService.class.getName()).log(Level.SEVERE, " logIn() ->", ex);
            return new Respuesta(false, "Ha ocurrido un error al establecer comunicación con el servidor.", ex.getMessage());
        }
    }
}
