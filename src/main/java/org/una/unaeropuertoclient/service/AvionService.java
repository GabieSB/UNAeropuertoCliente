package org.una.unaeropuertoclient.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import org.una.unaeropuertoclient.model.AvionDto;
import org.una.unaeropuertoclient.utils.RequestHTTP;
import org.una.unaeropuertoclient.utils.Respuesta;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.una.unaeropuertoclient.model.LugarDto;
import org.una.unaeropuertoclient.utils.RequesUtils;
import static org.una.unaeropuertoclient.utils.RequesUtils.isEmptyResult;
import static org.una.unaeropuertoclient.utils.RequesUtils.isError;

public class AvionService {

    Gson g = new GsonBuilder()
            .setDateFormat("yyy-MM-dd'T'HH:mm:ss.SSSX").create();

    public Respuesta getByMatricula(String id) {
        try {
            RequestHTTP requestHTTP = new RequestHTTP();
            HttpResponse respuesta = requestHTTP.get("aviones/getByMatricula/" + id);
            if (requestHTTP.getStatus() != 200) {
                if (respuesta.statusCode() == 500) {
                    return new Respuesta(false, "Parece que has introducido mal tus credenciales de acceso.", String.valueOf(requestHTTP.getStatus()));
                }
                return new Respuesta(false, "Parece que algo ha salido mal. Si el problema persiste solicita ayuda del encargado del sistema.", String.valueOf(requestHTTP.getStatus()));
            }
            System.out.println(respuesta.body().toString());
            AvionDto avion = g.fromJson(respuesta.body().toString(), AvionDto.class);
            return new Respuesta(true, "", "", "data", avion);
        } catch (Exception ex) {
            Logger.getLogger(UsuarioService.class.getName()).log(Level.SEVERE, " logIn() ->", ex);
            return new Respuesta(false, "Ha ocurrido un error al establecer comunicación con el servidor.", ex.getMessage());
        }
    }

    public Respuesta getById(String id) {
        try {
            RequestHTTP requestHTTP = new RequestHTTP();
            HttpResponse respuesta = requestHTTP.get("aviones/" + id);
            if (requestHTTP.getStatus() != 200) {
                if (respuesta.statusCode() == 500) {
                    return new Respuesta(false, "Parece que has introducido mal tus credenciales de acceso.", String.valueOf(requestHTTP.getStatus()));
                }
                return new Respuesta(false, "Parece que algo ha salido mal. Si el problema persiste solicita ayuda del encargado del sistema.", String.valueOf(requestHTTP.getStatus()));
            }
            System.out.println(respuesta.body().toString());
            AvionDto avion = g.fromJson(respuesta.body().toString(), AvionDto.class);
            return new Respuesta(true, "", "", "data", avion);
        } catch (Exception ex) {
            Logger.getLogger(UsuarioService.class.getName()).log(Level.SEVERE, " logIn() ->", ex);
            return new Respuesta(false, "Ha ocurrido un error al establecer comunicación con el servidor.", ex.getMessage());
        }
    }

    public Respuesta getByMatriculaLike(String id) {
        try {
            RequestHTTP requestHTTP = new RequestHTTP();
            HttpResponse respuesta = requestHTTP.get("aviones/findByMatriculaLike/" + id);
            if (requestHTTP.getStatus() != 200) {
                if (respuesta.statusCode() == 500) {
                    return new Respuesta(false, "Parece que has introducido mal tus credenciales de acceso.", String.valueOf(requestHTTP.getStatus()));
                }
                return new Respuesta(false, "Parece que algo ha salido mal. Si el problema persiste solicita ayuda del encargado del sistema.", String.valueOf(requestHTTP.getStatus()));
            }
            System.out.println(respuesta.body().toString());
            List<AvionDto> avion = new Gson().fromJson(respuesta.body().toString(), new TypeToken<List<AvionDto>>() {
            }.getType());
            return new Respuesta(true, "", "", "data", avion);
        } catch (Exception ex) {
            Logger.getLogger(UsuarioService.class.getName()).log(Level.SEVERE, " logIn() ->", ex);
            return new Respuesta(false, "Ha ocurrido un error al establecer comunicación con el servidor.", ex.getMessage());
        }
    }

    public Respuesta getByAerolinaNombre(String id) {
        try {
            System.out.println(id);
            RequestHTTP requestHTTP = new RequestHTTP();
            HttpResponse respuesta = requestHTTP.get("aviones/findByAerolineaNombre/" + id);
            System.out.println(respuesta.body().toString());
            if (requestHTTP.getStatus() != 200) {
                if (respuesta.statusCode() == 500) {
                    return new Respuesta(false, "Parece que has introducido mal tus credenciales de acceso.", String.valueOf(requestHTTP.getStatus()));
                }
                return new Respuesta(false, "Parece que algo ha salido mal. Si el problema persiste solicita ayuda del encargado del sistema.", String.valueOf(requestHTTP.getStatus()));
            }
            List<AvionDto> avion = new Gson().fromJson(respuesta.body().toString(), new TypeToken<List<AvionDto>>() {
            }.getType());
            return new Respuesta(true, "", "", "data", avion);
        } catch (Exception ex) {
            Logger.getLogger(UsuarioService.class.getName()).log(Level.SEVERE, " logIn() ->", ex);
            return new Respuesta(false, "Ha ocurrido un error al establecer comunicación con el servidor.", ex.getMessage());
        }
    }

    public Respuesta filter(String matricula, String aerolinea) {
        try {
            RequestHTTP requestHTTP = new RequestHTTP();
            matricula = (matricula.isBlank()) ? "none" : matricula.trim();
            aerolinea = (aerolinea.isBlank()) ? "none" : aerolinea.trim();
            HttpResponse respuesta = requestHTTP.get("aviones/filter/" + matricula + "/" + aerolinea);
            if (isError(respuesta.statusCode())) {
                return new Respuesta(false, "Error interno al consultar aviones, considera reportar esta falla.", "");
            }
            if (isEmptyResult(respuesta.statusCode())) {
                return new Respuesta(false, "No hay aviones que coincidan con lo que buscas.", "");
            }
            return new Respuesta(true, "", "", "data", RequesUtils.<AvionDto>asList(respuesta, AvionDto.class));
        } catch (Exception ex) {
            return new Respuesta(false, "Ha fallado la conexión con el servidor. Verifica que el servicio de internet se encuntre activo.", "");
        }
    }

    public Respuesta update(AvionDto avion) {
        HttpResponse resp = new RequestHTTP().put("aviones/update", g.toJson(avion));
        if (isError(resp.statusCode())) {
            return new Respuesta(false, "Error al crear modificar datos, considera reportar este problema", "");
        }
        if (isEmptyResult(resp.statusCode())) {
            return new Respuesta(false, "No ha sido posible hallar el avión que se desea modificar", "");
        }
        return new Respuesta(true, "", "", "data", RequesUtils.<AvionDto>asObject(resp, AvionDto.class));
    }

    public Respuesta create(AvionDto avion) {
        avion.setActivo(true);
        HttpResponse resp = new RequestHTTP().post("aviones/create", g.toJson(avion));
        if (isError(resp.statusCode())) {
            return new Respuesta(false, "Error al registrar nuevo avión en el sistema, considera reportar este problema", "");
        }
        return new Respuesta(true, "", "", "data", RequesUtils.<AvionDto>asObject(resp, AvionDto.class));
    }

}
