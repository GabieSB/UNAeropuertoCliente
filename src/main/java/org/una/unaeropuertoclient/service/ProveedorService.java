package org.una.unaeropuertoclient.service;

import com.google.gson.Gson;
import org.una.unaeropuertoclient.model.ProvedorDto;
import org.una.unaeropuertoclient.model.TipoReparacionDto;
import org.una.unaeropuertoclient.utils.RequesUtils;
import org.una.unaeropuertoclient.utils.RequestHTTP;
import org.una.unaeropuertoclient.utils.Respuesta;

import java.net.http.HttpResponse;

import static org.una.unaeropuertoclient.utils.RequesUtils.isEmptyResult;
import static org.una.unaeropuertoclient.utils.RequesUtils.isError;

public class ProveedorService {
    private final Gson jsonConv = new Gson();
    public Respuesta findActivos() {
        try {
            RequestHTTP requestHTTP = new RequestHTTP();
            HttpResponse respuesta = requestHTTP.get("provedores/findByEstado/true");
            if (isError(respuesta.statusCode())) {
                return new Respuesta(false, "Error al consultar", String.valueOf(requestHTTP.getStatus()));
            }
            if (isEmptyResult(respuesta.statusCode())) {
                return new Respuesta(false, "No hay provedors", "");
            }
            return new Respuesta(true, "", "", "data", RequesUtils.<ProvedorDto>asList(respuesta, ProvedorDto.class));
        } catch (Exception ex) {
            return new Respuesta(false, "Error de conexión", "");
        }
    }

    public Respuesta findByNombre(String nombre) {
        try {
            RequestHTTP requestHTTP = new RequestHTTP();
            HttpResponse respuesta = requestHTTP.get("provedores/getByNombre/"+nombre);
            if (isError(respuesta.statusCode())) {
                return new Respuesta(false, "Error al consultar", String.valueOf(requestHTTP.getStatus()));
            }
            if (isEmptyResult(respuesta.statusCode())) {
                return new Respuesta(false, "No hay proveedores registrados en el servidor", "");
            }
            return new Respuesta(true, "", "", "data", RequesUtils.<ProvedorDto>asList(respuesta, ProvedorDto.class));
        } catch (Exception ex) {
            return new Respuesta(false, "Error de conexión", "");
        }


    }

    public Respuesta create(ProvedorDto provedor) {
        provedor.setActivo(true);
        HttpResponse resp = new RequestHTTP().post("provedores/create", jsonConv.toJson(provedor));
        if (isError(resp.statusCode())) {
            return new Respuesta(false, "Error al crear un nuevo proveedor.", "");
        }
        return new Respuesta(true, "", "", "data", RequesUtils.<ProvedorDto>asObject(resp, ProvedorDto.class));
    }

    public Respuesta update(ProvedorDto provedor) {
        HttpResponse resp = new RequestHTTP().put("provedores/update", jsonConv.toJson(provedor));
        if (isError(resp.statusCode())) {
            return new Respuesta(false, "Error al modificar provedor de aterrizaje, considera reportar esta falla.", "");
        }
        if (isEmptyResult(resp.statusCode())) {
            return new Respuesta(false, "No ha sido posible hallar la proveedor que se desea modificar", "");
        }
        return new Respuesta(true, "", "", "data", RequesUtils.<ProvedorDto>asObject(resp, ProvedorDto.class));
    }
}
