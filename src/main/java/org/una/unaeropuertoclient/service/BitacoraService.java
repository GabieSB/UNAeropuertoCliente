package org.una.unaeropuertoclient.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import org.una.unaeropuertoclient.model.AuthenticationResponse;
import org.una.unaeropuertoclient.model.BitacoraDto;
import org.una.unaeropuertoclient.model.BitacoraDto;
import org.una.unaeropuertoclient.utils.AppContext;
import org.una.unaeropuertoclient.utils.RequestHTTP;
import org.una.unaeropuertoclient.utils.Respuesta;
import java.net.http.HttpResponse;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.una.unaeropuertoclient.utils.RequesUtils;

public class BitacoraService {

    Gson gson = new GsonBuilder().setDateFormat("yyy-MM-dd'T'HH:mm:ss.SSSX").create();

    public void create(String descripcion) {
        AuthenticationResponse authentication = (AuthenticationResponse) AppContext.getInstance().get("token");
        BitacoraDto bitacoraDto = new BitacoraDto(new Timestamp(System.currentTimeMillis()), descripcion, authentication.getUsuario());
        RequestHTTP requestHTTP = new RequestHTTP();
        HttpResponse response = requestHTTP.post("biatacoras/create", gson.toJson(bitacoraDto));
    }

    public Respuesta buscarEntreFechas(LocalDate fehaInicio, LocalDate fechaFinal) {
        try {
            RequestHTTP requestHTTP = new RequestHTTP();
            fehaInicio = (fehaInicio != null) ? fehaInicio : LocalDate.of(1500, 1, 1);
            fechaFinal = (fechaFinal != null) ? fechaFinal : LocalDate.of(2500, 1, 1);
            HttpResponse respuesta = requestHTTP.get("biatacoras/findBetweenFechas/" + fehaInicio + "/" + fechaFinal);
            if (RequesUtils.isError(respuesta.statusCode())) {
                return new Respuesta(false, "Parece que has introducido mal tus credenciales de acceso.", String.valueOf(requestHTTP.getStatus()));
            }
            if (RequesUtils.isEmptyResult(respuesta.statusCode())) {
                return new Respuesta(false, "No hubieron resultados para tu búsqueda.", String.valueOf(requestHTTP.getStatus()));
            }
            List<BitacoraDto> bitacoraDto = new Gson().fromJson(respuesta.body().toString(), new TypeToken<List<BitacoraDto>>() {
            }.getType());
            return new Respuesta(true, "", "", "data", bitacoraDto);
        } catch (Exception ex) {
            Logger.getLogger(UsuarioService.class.getName()).log(Level.SEVERE, " logIn() ->", ex);
            return new Respuesta(false, "Ha ocurrido un error al establecer comunicación con el servidor.", ex.getMessage());
        }
    }

    public Respuesta buscarPorTipoCambio(String tipoCambio) {
        try {
            RequestHTTP requestHTTP = new RequestHTTP();
            HttpResponse respuesta = requestHTTP.get("biatacoras/getByTipoCambio/" + tipoCambio);
            System.out.println(respuesta.body().toString());
            if (requestHTTP.getStatus() != 200) {
                return new Respuesta(false, "Parece que algo ha salido mal. Si el problema persiste solicita ayuda del encargado del sistema.", String.valueOf(requestHTTP.getStatus()));
            }
            if (respuesta.statusCode() == 500) {
                return new Respuesta(false, "Parece que has introducido mal tus credenciales de acceso.", String.valueOf(requestHTTP.getStatus()));
            }
            List<BitacoraDto> bitacoraDto = new Gson().fromJson(respuesta.body().toString(), new TypeToken<List<BitacoraDto>>() {
            }.getType());
            return new Respuesta(true, "", "", "data", bitacoraDto);
        } catch (Exception ex) {
            Logger.getLogger(UsuarioService.class.getName()).log(Level.SEVERE, " logIn() ->", ex);
            System.out.println("ha ocurrido un error");
            return new Respuesta(false, "Ha ocurrido un error al establecer comunicación con el servidor.", ex.getMessage());
        }
    }

    public Respuesta buscarPorUserCedula(String cedula) {
        try {
            RequestHTTP requestHTTP = new RequestHTTP();
            HttpResponse respuesta = requestHTTP.get("biatacoras/getByUserCedula/" + cedula);
            if (RequesUtils.isError(respuesta.statusCode())) {
                return new Respuesta(false, "Parece que has introducido mal tus credenciales de acceso.", String.valueOf(requestHTTP.getStatus()));
            }
            if (RequesUtils.isEmptyResult(respuesta.statusCode())) {
                return new Respuesta(false, "No hubieron resultados para tu búsqueda.", String.valueOf(requestHTTP.getStatus()));
            }
            List<BitacoraDto> bitacoraDto = new Gson().fromJson(respuesta.body().toString(), new TypeToken<List<BitacoraDto>>() {
            }.getType());
            return new Respuesta(true, "", "", "data", bitacoraDto);
        } catch (Exception ex) {
            Logger.getLogger(UsuarioService.class.getName()).log(Level.SEVERE, " logIn() ->", ex);
            return new Respuesta(false, "Ha ocurrido un error al establecer comunicación con el servidor.", ex.getMessage());
        }
    }

    public Respuesta buscarPorID(long id) {
        try {
            RequestHTTP requestHTTP = new RequestHTTP();
            HttpResponse respuesta = requestHTTP.get("biatacoras/" + id);
            if (RequesUtils.isError(respuesta.statusCode())) {
                if (RequesUtils.isEmptyResult(respuesta.statusCode())) {
                    return new Respuesta(false, "No hubieron resultados para tu búsqueda.", String.valueOf(requestHTTP.getStatus()));
                }
                return new Respuesta(false, "Parece que has introducido mal tus credenciales de acceso.", String.valueOf(requestHTTP.getStatus()));
            }
            BitacoraDto bitacoraDto = gson.fromJson(respuesta.body().toString(), BitacoraDto.class);
            return new Respuesta(true, "", "", "data", bitacoraDto);
        } catch (Exception ex) {
            Logger.getLogger(UsuarioService.class.getName()).log(Level.SEVERE, " logIn() ->", ex);
            return new Respuesta(false, "Ha ocurrido un error al establecer comunicación con el servidor.", ex.getMessage());
        }
    }
}
